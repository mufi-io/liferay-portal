/**
 * Copyright (c) 2000-present Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

import ClayButton from '@clayui/button';
import ClayIcon from '@clayui/icon';
import PropTypes from 'prop-types';
import React from 'react';

import editFragmentEntryComment from '../../../app/actions/editFragmentEntryLinkComment';
import {useSelectItem} from '../../../app/contexts/ControlsContext';
import {useDispatch, useSelector} from '../../../app/contexts/StoreContext';
import SidebarPanelHeader from '../../../common/components/SidebarPanelHeader';
import AddCommentForm from './AddCommentForm';
import FragmentComment from './FragmentComment';
import ResolvedCommentsToggle from './ResolvedCommentsToggle';

export default function FragmentComments({fragmentEntryLink}) {
	const {comments = [], fragmentEntryLinkId, name} = fragmentEntryLink;

	const selectItem = useSelectItem();

	const dispatch = useDispatch();
	const showResolvedComments = useSelector(
		(state) => state.showResolvedComments
	);

	const fragmentEntryLinkComments = showResolvedComments
		? comments
		: comments.filter(({resolved}) => !resolved);

	return (
		<>
			<div className="flex-shrink-0">
				<SidebarPanelHeader
					iconLeft={
						<ClayButton
							aria-label={Liferay.Language.get('back')}
							borderless
							className="mr-3 p-0 text-dark"
							displayType="secondary"
							onClick={() => selectItem(null)}
							small
						>
							<ClayIcon symbol="angle-left" />
						</ClayButton>
					}
				>
					{name}
				</SidebarPanelHeader>

				<ResolvedCommentsToggle />

				<AddCommentForm fragmentEntryLinkId={fragmentEntryLinkId} />
			</div>

			<div className="overflow-auto">
				{fragmentEntryLinkComments.map((_, i) => {
					const comment =
						fragmentEntryLinkComments[
							fragmentEntryLinkComments.length - 1 - i
						];

					return (
						<FragmentComment
							comment={comment}
							fragmentEntryLinkId={fragmentEntryLinkId}
							key={comment.commentId}
							onEdit={(fragmentEntryLinkComment) =>
								dispatch(
									editFragmentEntryComment({
										fragmentEntryLinkComment,
										fragmentEntryLinkId,
									})
								)
							}
						/>
					);
				})}
			</div>
		</>
	);
}

FragmentComments.propTypes = {
	fragmentEntryLink: PropTypes.object.isRequired,
};
