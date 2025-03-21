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

import ClayButton, {ClayButtonWithIcon} from '@clayui/button';
import PropTypes from 'prop-types';
import React, {useContext, useState} from 'react';

import {DEFAULT_LANGUAGE} from '../../../../../source-builder/constants';
import {DiagramBuilderContext} from '../../../../DiagramBuilderContext';
import SidebarPanel from '../../SidebarPanel';
import BaseActionsInfo from '../shared-components/BaseActionsInfo';

const scriptLanguageOptions = [
	{
		label: Liferay.Language.get('groovy'),
		value: 'groovy',
	},
	{
		label: Liferay.Language.get('java'),
		value: 'java',
	},
];

const ActionsInfo = ({
	identifier,
	index,
	executionTypeInput = () => {
		'';
	},
	sectionsLength,
	setSections,
}) => {
	const {selectedItem, setSelectedItem} = useContext(DiagramBuilderContext);
	const {actions} = selectedItem.data;

	const [script, setScript] = useState(actions?.script?.[index] || '');

	const [description, setDescription] = useState(
		actions?.description?.[index] || ''
	);

	const [executionTypeOptions, setExecutionTypeOptions] = useState([
		{
			label: Liferay.Language.get('on-entry'),
			value: 'onEntry',
		},
		{
			label: Liferay.Language.get('on-exit'),
			value: 'onExit',
		},
	]);

	const [scriptLanguage, setScriptLanguage] = useState(
		actions?.scriptLanguage?.[index] || DEFAULT_LANGUAGE
	);

	if (
		selectedItem.type === 'task' &&
		executionTypeOptions &&
		executionTypeOptions[0].value !== 'onAssignment'
	) {
		executionTypeOptions.unshift({
			label: Liferay.Language.get('on-assignment'),
			value: 'onAssignment',
		});
	}

	const [executionType, setExecutionType] = useState(
		actions?.executionType?.[index] ?? executionTypeOptions[0].value
	);
	const [name, setName] = useState(actions?.name?.[index] || '');
	const [priority, setPriority] = useState(actions?.priority?.[index] || 1);

	const deleteSection = () => {
		setSections((prevSections) => {
			const newSections = prevSections.filter(
				(prevSection) => prevSection.identifier !== identifier
			);

			updateSelectedItem(newSections);

			return newSections;
		});
	};

	const updateActionInfo = (item) => {
		if (item.name && item.script && item.executionType) {
			setSections((prev) => {
				prev[index] = {
					...prev[index],
					...item,
				};

				updateSelectedItem(prev);

				return prev;
			});
		}
	};

	const updateSelectedItem = (values) => {
		setSelectedItem((previousItem) => ({
			...previousItem,
			data: {
				...previousItem.data,
				actions: {
					description: values.map(({description}) => description),
					executionType: values.map(
						({executionType}) => executionType
					),
					name: values.map(({name}) => name),
					priority: values.map(({priority}) => priority),
					script: values.map(({script}) => script),
					scriptLanguage: values.map(
						({scriptLanguage}) => scriptLanguage
					),
					sectionsData: values.map((values) => values),
				},
			},
		}));
	};

	return (
		<SidebarPanel panelTitle={Liferay.Language.get('information')}>
			<BaseActionsInfo
				description={description}
				executionType={executionType}
				executionTypeInput={executionTypeInput}
				executionTypeOptions={executionTypeOptions}
				index={index}
				name={name}
				placeholderName={Liferay.Language.get('my-action')}
				placeholderScript="${userName} sent you a ${entryType} for review in the workflow."
				priority={priority}
				script={script}
				scriptLanguage={scriptLanguage}
				scriptLanguageOptions={scriptLanguageOptions}
				selectedItem={selectedItem}
				setDescription={setDescription}
				setExecutionType={setExecutionType}
				setExecutionTypeOptions={setExecutionTypeOptions}
				setName={setName}
				setPriority={setPriority}
				setScript={setScript}
				setScriptLanguage={setScriptLanguage}
				updateActionInfo={updateActionInfo}
			/>

			<div className="section-buttons-area">
				<ClayButton
					className="mr-3"
					disabled={actions?.name === '' || script === ''}
					displayType="secondary"
					onClick={() =>
						setSections((prev) => {
							return [
								...prev,
								{identifier: `${Date.now()}-${prev.length}`},
							];
						})
					}
				>
					{Liferay.Language.get('new-action')}
				</ClayButton>

				{sectionsLength > 1 && (
					<ClayButtonWithIcon
						className="delete-button"
						displayType="unstyled"
						onClick={deleteSection}
						symbol="trash"
					/>
				)}
			</div>
		</SidebarPanel>
	);
};

ActionsInfo.propTypes = {
	executionTypeInput: PropTypes.func.isRequired,
	identifier: PropTypes.string.isRequired,
	index: PropTypes.number.isRequired,
	sectionsLength: PropTypes.number.isRequired,
	setSections: PropTypes.func.isRequired,
};

export default ActionsInfo;
