/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * The contents of this file are subject to the terms of the Liferay Enterprise
 * Subscription License ("License"). You may not use this file except in
 * compliance with the License. You can obtain a copy of the License by
 * contacting Liferay, Inc. See the License for the specific language governing
 * permissions and limitations under the License, including but not limited to
 * distribution rights of the Software.
 */

import ClayAlert from '@clayui/alert';
import ClayButton from '@clayui/button';
import ClayEmptyState from '@clayui/empty-state';
import ClayIcon from '@clayui/icon';
import ClayLayout from '@clayui/layout';
import ClayToolbar from '@clayui/toolbar';
import getCN from 'classnames';
import {fetch, navigate} from 'frontend-js-web';
import {PropTypes} from 'prop-types';
import React, {
	useCallback,
	useContext,
	useEffect,
	useRef,
	useState,
} from 'react';

import sxpElementSchema from '../../schemas/sxp-query-element.schema.json';
import useDebounceCallback from '../hooks/useDebounceCallback';
import useShouldConfirmBeforeNavigate from '../hooks/useShouldConfirmBeforeNavigate';
import CodeMirrorEditor from '../shared/CodeMirrorEditor';
import ErrorBoundary from '../shared/ErrorBoundary';
import JSONSXPElement from '../shared/JSONSXPElement';
import LearnMessage from '../shared/LearnMessage';
import PageToolbar from '../shared/PageToolbar';
import PreviewModal from '../shared/PreviewModal';
import SearchInput from '../shared/SearchInput';
import Sidebar from '../shared/Sidebar';
import SubmitWarningModal from '../shared/SubmitWarningModal';
import ThemeContext from '../shared/ThemeContext';
import SXPElement from '../shared/sxp_element/index';
import {CONFIG_PREFIX, DEFAULT_ERROR} from '../utils/constants';
import {
	formatLocaleWithDashes,
	formatLocaleWithUnderscores,
	renameKeys,
	sub,
} from '../utils/language';
import {openErrorToast, setInitialSuccessToast} from '../utils/toasts';
import {getUIConfigurationValues, isCustomJSONSXPElement} from '../utils/utils';
import SidebarPanel from './SidebarPanel';

/**
 * Checks that property of object is a valid non-Array object. Prevents errors
 * in displaying title and description for the preview and toolbar.
 * @param {*} sxpElementJSONObject The SXP element as an object
 * @param {*} property Property to check (`title_i18n` or `description_i18n`)
 */
const isPropertyValid = (sxpElementJSONObject, property) =>
	typeof sxpElementJSONObject[property] === 'object' &&
	!Array.isArray(sxpElementJSONObject[property]);

/**
 * Gets current value of the CodeMirror editor.
 *
 * The state `elementJSONEditorValue` might not be accurate due to
 * setElementJSONEditorValue being asynchronous and when immediately navigating
 * away or submitting, `elementJSONEditorValue` could not be what's exactly in
 * the editor since it hasn't updated yet.
 */
const getCodeMirrorValue = (codeMirrorRef) => {
	const doc = codeMirrorRef?.current?.getDoc();

	return doc?.getValue();
};

/**
 * Reassigns the values of `title_i18n` and `description_i18n` in current
 * JSON object after editing them through the modal. Maintains current order
 * of properties and uses the expected locale format.
 * @param {*} sxpElementJSONObject
 * @param {*} title_i18n
 * @param {*} description_i18n
 * @returns
 */
const reassignTitleAndDescription = (
	sxpElementJSONObject,
	title_i18n,
	description_i18n
) => {
	sxpElementJSONObject.description_i18n = renameKeys(
		description_i18n,
		formatLocaleWithUnderscores
	);
	sxpElementJSONObject.title_i18n = renameKeys(
		title_i18n,
		formatLocaleWithUnderscores
	);

	return sxpElementJSONObject;
};

/**
 * Checks if all `${configuration.___}` template strings are defined in the
 * `uiConfiguration`.
 * @param {object} configurationJSONObject The configuration object to check.
 * @param {object} uiConfigurationJSONObject The definition of configuration
 * 	variables.
 */
const validateConfigKeys = (
	configurationJSONObject,
	uiConfigurationJSONObject
) => {
	const regex = new RegExp(`\\$\\{${CONFIG_PREFIX}.([\\w\\d_]+)\\}`, 'g');

	const elementKeys = [
		...JSON.stringify(configurationJSONObject).matchAll(regex),
	].map((item) => item[1]);

	const uiConfigKeys = uiConfigurationJSONObject?.fieldSets
		? uiConfigurationJSONObject.fieldSets.reduce((acc, curr) => {

				// Find names within each fields array

				const configKeys = curr.fields
					? curr.fields.map((item) => item.name)
					: [];

				return [...acc, ...configKeys];
		  }, [])
		: [];

	const missingKeys = elementKeys.filter(
		(item) => !uiConfigKeys.includes(item)
	);

	if (missingKeys.length === 1) {
		throw sub(
			Liferay.Language.get(
				'the-following-configuration-key-is-missing-x'
			),
			[missingKeys[0]]
		);
	}

	if (missingKeys.length > 1) {
		throw sub(
			Liferay.Language.get(
				'the-following-configuration-keys-are-missing-x'
			),
			[missingKeys.join(', ')]
		);
	}
};

function EditSXPElementForm({
	initialElementJSONEditorValue = {},
	predefinedVariables = [],
	readOnly,
	type,
	sxpElementId,
}) {
	const {defaultLocale, redirectURL} = useContext(ThemeContext);

	const formRef = useRef();
	const elementJSONEditorRef = useRef();

	const initialElementJSONEditorValueString = JSON.stringify(
		initialElementJSONEditorValue,
		null,
		'\t'
	);

	const [errors, setErrors] = useState([]);
	const [expandAllVariables, setExpandAllVariables] = useState(false);
	const [isSubmitting, setIsSubmitting] = useState(false);
	const [showInfoSidebar, setShowInfoSidebar] = useState(false);
	const [showSubmitWarningModal, setShowSubmitWarningModal] = useState(false);
	const [showVariablesSidebar, setShowVariablesSidebar] = useState(false);
	const [elementJSONEditorValue, setElementJSONEditorValue] = useState(
		initialElementJSONEditorValueString
	);

	/**
	 * When set to `true`, `isSXPElementJSONInvalid` prevents saving, rendering
	 * preview, and editing on the title/description modal.
	 */
	const [isSXPElementJSONInvalid, setIsSXPElementJSONInvalid] = useState(
		false
	);

	/**
	 * Saves the most recent valid version of sxpElement as an object.
	 * Contains `title_i18n`, `description_i18n`, and `elementDefinition`.
	 */
	const [sxpElementJSONObject, setSXPElementJSONObject] = useState(
		initialElementJSONEditorValue
	);

	const filteredCategories = {};

	predefinedVariables.map((variable) => {
		const category = variable.templateVariable.match(/\w+/g)[0];

		filteredCategories[category] = [
			...(filteredCategories[category] || []),
			variable,
		];
	});

	const [variables, setVariables] = useState(filteredCategories);

	const shouldConfirmBeforeNavigate = useCallback(() => {
		return (
			initialElementJSONEditorValueString !==
				getCodeMirrorValue(elementJSONEditorRef) &&
			!isSubmitting &&
			!readOnly
		);
	}, [
		initialElementJSONEditorValueString,
		elementJSONEditorRef,
		isSubmitting,
		readOnly,
	]);

	useShouldConfirmBeforeNavigate(shouldConfirmBeforeNavigate);

	/**
	 * Workaround to force a re-render so `elementJSONEditorRef` will be
	 * defined when calling `_handleVariableClick`
	 */
	useEffect(() => {
		if (!readOnly) {
			setShowVariablesSidebar(true);
		}
	}, [readOnly]);

	/**
	 * Validates `title_i18n` and `description_i18n` and updates
	 * `sxpElementJSONObject` if parsing succeeds.
	 *
	 * Returns `updatedState` to use in `_handleSubmit` to save the most
	 * up-to-date value.
	 */
	const _validateAndUpdateSXPElementJSONObject = (sxpElementString) => {
		const updatedState = {
			isInvalid: false,
			sxpElementJSONObjectNew: sxpElementJSONObject,
		};

		try {
			const sxpElement = JSON.parse(sxpElementString);

			if (!isPropertyValid(sxpElement, 'title_i18n')) {
				sxpElement.title_i18n = {};
			}

			if (!isPropertyValid(sxpElement, 'description_i18n')) {
				sxpElement.description_i18n = {};
			}

			setIsSXPElementJSONInvalid(false);
			updatedState.isInvalid = false;

			setSXPElementJSONObject(sxpElement);
			updatedState.sxpElementJSONObjectNew = sxpElement;
		}
		catch {
			setIsSXPElementJSONInvalid(true);

			updatedState.isInvalid = true;
		}

		return updatedState;
	};

	/**
	 * Parses CodeMirror text after user types into it or submits changes
	 * on title and description modal. Validates `title_i18n` and
	 * `description_i18n` and updates `sxpElementJSONObject` if parsing
	 * succeeds.
	 */
	const _handleJSONEditorValueChange = (value) => {
		setElementJSONEditorValue(value);

		_validateAndUpdateSXPElementJSONObject(value);
	};

	const [handleJSONEditorValueChangeDebounced] = useDebounceCallback(
		_handleJSONEditorValueChange,
		500
	);

	const _handleSearchFilter = useCallback(
		(value) => {
			const filteredCategories = {};

			predefinedVariables.map((variable) => {
				const category = variable.templateVariable.match(/\w+/g)[0];

				if (
					variable.description
						.toLowerCase()
						.includes(value.toLowerCase())
				) {
					filteredCategories[category] = [
						...(filteredCategories[category] || []),
						variable,
					];
				}
			});

			setVariables(filteredCategories);
			setExpandAllVariables(!!value);
		},
		[predefinedVariables]
	);

	const _handleSubmit = async (event) => {
		event.preventDefault();

		setIsSubmitting(true);

		// Since `_handleJSONEditorValueChange` is debounced while typing in the
		// CodeMirror editor, validate and update `sxpElementJSONObject` in the
		// case where a user types in the CodeMirror editor and very quickly
		// clicks save.

		const {
			isInvalid,
			sxpElementJSONObjectNew,
		} = _validateAndUpdateSXPElementJSONObject(
			getCodeMirrorValue(elementJSONEditorRef)
		);

		try {
			if (isInvalid) {
				throw sub(Liferay.Language.get('x-is-invalid'), [
					Liferay.Language.get('element-source-json'),
				]);
			}

			validateConfigKeys(
				sxpElementJSONObjectNew.elementDefinition?.configuration,
				sxpElementJSONObjectNew.elementDefinition?.uiConfiguration
			);

			if (!Object.keys(sxpElementJSONObjectNew.title_i18n).length) {
				throw Liferay.Language.get('error.title-empty');
			}

			if (!sxpElementJSONObjectNew.title_i18n[defaultLocale]) {
				throw Liferay.Language.get('error.default-locale-title-empty');
			}
		}
		catch (error) {
			openErrorToast({
				message: error,
			});

			setIsSubmitting(false);

			return;
		}

		try {

			/**
			 * If the warning modal is already open, assume the form was submitted
			 * using the "Continue To Save" action and should skip the schema
			 * validation step.
			 *
			 * TODO: Update this once a validation REST endpoint is decided
			 */

			if (!showSubmitWarningModal) {
				const validateErrors = {errors: []};

				/*
				const validateErrors = await fetch(
					'/o/search-experiences-rest/v1.0/sxp-elements/validate',
					{
						body: JSON.stringify({
							description_i18n: sxpElementJSONObjectNew.description_i18n,
							elementDefinition: sxpElementJSONObjectNew.elementDefinition,
							title_i18n: sxpElementJSONObjectNew.title_i18n,
							type,
						}),
						method: 'POST',
					}
				).then((response) => response.json());
				*/

				if (validateErrors.errors?.length) {
					setErrors(validateErrors.errors);
					setShowSubmitWarningModal(true);
					setIsSubmitting(false);

					return;
				}
			}

			const responseContent = await fetch(
				`/o/search-experiences-rest/v1.0/sxp-elements/${sxpElementId}`,
				{
					body: JSON.stringify({
						description_i18n:
							sxpElementJSONObjectNew.description_i18n,
						elementDefinition:
							sxpElementJSONObjectNew.elementDefinition,
						title_i18n: sxpElementJSONObjectNew.title_i18n,
						type,
					}),
					headers: new Headers({
						'Content-Type': 'application/json',
					}),
					method: 'PATCH',
				}
			).then((response) => {
				if (!response.ok) {
					setShowSubmitWarningModal(false);

					throw DEFAULT_ERROR;
				}

				return response.json();
			});

			if (
				Object.prototype.hasOwnProperty.call(responseContent, 'errors')
			) {
				responseContent.errors.forEach((message) =>
					openErrorToast({message})
				);

				setIsSubmitting(false);
			}
			else {
				setInitialSuccessToast(
					Liferay.Language.get('the-element-was-saved-successfully')
				);

				navigate(redirectURL);
			}
		}
		catch (error) {
			openErrorToast();

			setIsSubmitting(false);

			if (process.env.NODE_ENV === 'development') {
				console.error(error);
			}
		}
	};

	/**
	 * Called after clicking 'Done' in title and description edit modal.
	 * Updates `title_i18n`, `description_i18n` inside CodeMirror editor,
	 * which triggers `_handleJSONEditorValueChange`.
	 */
	const _handleTitleAndDescriptionChange = ({description, title}) => {
		if (!isSXPElementJSONInvalid) {
			const doc = elementJSONEditorRef.current.getDoc();

			doc.setValue(
				JSON.stringify(
					reassignTitleAndDescription(
						sxpElementJSONObject,
						title,
						description
					),
					null,
					'\t'
				)
			);
		}
	};

	const _handleVariableClick = (variable) => {
		const doc = elementJSONEditorRef.current.getDoc();
		const cursor = doc.getCursor();

		doc.replaceRange(variable, cursor);
	};

	const _renderPreviewBody = () => {
		if (!isSXPElementJSONInvalid) {
			const previewSXPElementJSON = {
				elementDefinition: {}, // Define elementDefinition to prevent error
				...sxpElementJSONObject,
			};

			const uiConfigurationValues = getUIConfigurationValues(
				previewSXPElementJSON
			);

			return (
				<div className="portlet-sxp-blueprint-admin">
					<ErrorBoundary>
						{isCustomJSONSXPElement(uiConfigurationValues) ? (
							<JSONSXPElement
								collapseAll={false}
								readOnly={true}
								sxpElement={previewSXPElementJSON}
								uiConfigurationValues={uiConfigurationValues}
							/>
						) : (
							<SXPElement
								collapseAll={false}
								sxpElement={previewSXPElementJSON}
								uiConfigurationValues={uiConfigurationValues}
							/>
						)}
					</ErrorBoundary>
				</div>
			);
		}
		else {
			return (
				<ClayEmptyState
					description={Liferay.Language.get(
						'json-may-be-incorrect-and-we-were-unable-to-load-a-preview-of-the-configuration'
					)}
					imgSrc="/o/admin-theme/images/states/empty_state.gif"
					title={Liferay.Language.get('unable-to-load-preview')}
				/>
			);
		}
	};

	return (
		<>
			<form ref={formRef}>
				<SubmitWarningModal
					errors={errors}
					isSubmitting={isSubmitting}
					message={Liferay.Language.get(
						'the-element-configuration-has-errors-that-may-cause-unexpected-results'
					)}
					onClose={() => setShowSubmitWarningModal(false)}
					onSubmit={_handleSubmit}
					visible={showSubmitWarningModal}
				/>

				<PageToolbar
					description={renameKeys(
						sxpElementJSONObject.description_i18n,
						formatLocaleWithDashes
					)}
					disableTitleAndDescriptionModal={isSXPElementJSONInvalid}
					isSubmitting={isSubmitting}
					onCancel={redirectURL}
					onSubmit={_handleSubmit}
					onTitleAndDescriptionChange={
						_handleTitleAndDescriptionChange
					}
					readOnly={readOnly}
					title={renameKeys(
						sxpElementJSONObject.title_i18n,
						formatLocaleWithDashes
					)}
				>
					{readOnly && (
						<ClayToolbar.Item>
							<ClayAlert
								className="m-0"
								displayType="info"
								title={Liferay.Language.get('read-only')}
								variant="feedback"
							/>
						</ClayToolbar.Item>
					)}

					<ClayToolbar.Item>
						<PreviewModal
							body={_renderPreviewBody()}
							size="lg"
							title={Liferay.Language.get(
								'preview-configuration'
							)}
						>
							<ClayButton
								borderless
								displayType="secondary"
								small
							>
								{Liferay.Language.get('preview')}
							</ClayButton>
						</PreviewModal>
					</ClayToolbar.Item>
				</PageToolbar>
			</form>

			<Sidebar
				className="info-sidebar"
				onClose={() => setShowInfoSidebar(false)}
				title={Liferay.Language.get('element-source')}
				visible={showInfoSidebar}
			>
				<div className="container-fluid">
					<span className="help-text text-secondary">
						{Liferay.Language.get('element-source-description')}
					</span>

					<LearnMessage resourceKey="element-source" />
				</div>

				{!readOnly && (
					<>
						<div className="sidebar-header">
							<h4 className="component-title">
								<span className="text-truncate-inline">
									<span className="text-truncate">
										{Liferay.Language.get(
											'json-autocomplete'
										)}
									</span>
								</span>
							</h4>
						</div>

						<div className="container-fluid">
							<div className="help-text text-secondary">
								{Liferay.Language.get(
									'begin-typing-inside-double-quotes-to-see-the-autocomplete-options'
								)}
							</div>

							<div className="help-text text-secondary">
								{Liferay.Language.get(
									'use-the-arrow-keys-or-the-mouse-to-navigate-the-menu'
								)}
							</div>

							<div className="help-text text-secondary">
								{Liferay.Language.get(
									'use-the-enter-key-to-select-a-menu-item'
								)}
							</div>
						</div>
					</>
				)}
			</Sidebar>

			<div
				className={getCN('sxp-element-row', {
					shifted: showInfoSidebar,
				})}
			>
				<ClayLayout.Row>
					<ClayLayout.Col size={12}>
						<div className="sxp-element-section">
							<div className="sxp-element-header">
								{!readOnly && (
									<ClayButton
										borderless
										className={getCN({
											active: showVariablesSidebar,
										})}
										disabled={false}
										displayType="secondary"
										monospaced
										onClick={() =>
											setShowVariablesSidebar(
												!showVariablesSidebar
											)
										}
										small
										title={Liferay.Language.get(
											'predefined-variables'
										)}
										type="submit"
									>
										<ClayIcon symbol="list-ul" />
									</ClayButton>
								)}

								<div className="expand-header">
									<div className="header-label">
										<label>
											{Liferay.Language.get(
												'element-source-json'
											)}
										</label>
									</div>
								</div>

								<ClayButton
									borderless
									className={getCN({active: showInfoSidebar})}
									displayType="secondary"
									monospaced
									onClick={() =>
										setShowInfoSidebar(!showInfoSidebar)
									}
									small
								>
									<ClayIcon symbol="info-circle-open" />
								</ClayButton>
							</div>

							<ClayLayout.Row>
								<ClayLayout.Col
									className={getCN('json-section', {
										hide: !showVariablesSidebar,
									})}
									size={3}
								>
									<div className="sidebar sidebar-light">
										<div className="sidebar-header">
											<span className="text-truncate-inline">
												<span className="text-truncate">
													{Liferay.Language.get(
														'predefined-variables'
													)}
												</span>
											</span>
										</div>

										<div className="container-fluid sidebar-input">
											<SearchInput
												onChange={_handleSearchFilter}
											/>
										</div>

										<div className="container-fluid">
											<dl className="sidebar-dl">
												{Object.keys(variables)
													.length ? (
													Object.keys(
														variables
													).map((category) => (
														<SidebarPanel
															categoryName={
																category
															}
															expand={
																expandAllVariables
															}
															key={category}
															onVariableClick={
																_handleVariableClick
															}
															parameterDefinitions={
																variables[
																	category
																]
															}
														/>
													))
												) : (
													<div className="empty-list-message">
														<ClayEmptyState
															description=""
															title={Liferay.Language.get(
																'no-predefined-variables-were-found'
															)}
														/>
													</div>
												)}
											</dl>
										</div>
									</div>
								</ClayLayout.Col>

								<ClayLayout.Col
									className="json-section"
									size={showVariablesSidebar ? 9 : 12}
								>
									<CodeMirrorEditor
										autocompleteSchema={sxpElementSchema}
										onChange={
											handleJSONEditorValueChangeDebounced
										}
										readOnly={readOnly}
										ref={elementJSONEditorRef}
										value={elementJSONEditorValue}
									/>
								</ClayLayout.Col>
							</ClayLayout.Row>
						</div>
					</ClayLayout.Col>
				</ClayLayout.Row>
			</div>
		</>
	);
}

EditSXPElementForm.propTypes = {
	initialElementJSONEditorValue: PropTypes.object,
	predefinedVariables: PropTypes.arrayOf(PropTypes.object),
	readOnly: PropTypes.bool,
	sxpElementId: PropTypes.string,
	type: PropTypes.number,
};

export default React.memo(EditSXPElementForm);
