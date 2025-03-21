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

package com.liferay.social.activities.web.internal.portlet.action;

import com.liferay.petra.string.StringPool;
import com.liferay.portal.kernel.dao.search.SearchContainer;
import com.liferay.portal.kernel.model.Group;
import com.liferay.portal.kernel.portlet.LiferayPortletResponse;
import com.liferay.portal.kernel.portlet.bridges.mvc.BaseRSSMVCResourceCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCResourceCommand;
import com.liferay.portal.kernel.service.GroupLocalService;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextFactory;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.HtmlParser;
import com.liferay.portal.kernel.util.ParamUtil;
import com.liferay.portal.kernel.util.Portal;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.rss.export.RSSExporter;
import com.liferay.rss.model.SyndContent;
import com.liferay.rss.model.SyndEntry;
import com.liferay.rss.model.SyndFeed;
import com.liferay.rss.model.SyndLink;
import com.liferay.rss.model.SyndModelFactory;
import com.liferay.rss.util.RSSUtil;
import com.liferay.social.activities.constants.SocialActivitiesPortletKeys;
import com.liferay.social.activities.web.internal.helper.SocialActivitiesQueryHelper;
import com.liferay.social.kernel.model.SocialActivityFeedEntry;
import com.liferay.social.kernel.model.SocialActivitySet;
import com.liferay.social.kernel.service.SocialActivityInterpreterLocalService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.portlet.PortletPreferences;
import javax.portlet.ResourceRequest;
import javax.portlet.ResourceResponse;
import javax.portlet.ResourceURL;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Brian Wing Shun Chan
 * @author Vilmos Papp
 * @author Eduardo García
 * @author Raymond Augé
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + SocialActivitiesPortletKeys.SOCIAL_ACTIVITIES,
		"mvc.command.name=/social_activities/rss"
	},
	service = MVCResourceCommand.class
)
public class RSSMVCResourceCommand extends BaseRSSMVCResourceCommand {

	@Override
	protected byte[] getRSS(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		String tabs1 = ParamUtil.getString(resourceRequest, "tabs1", "all");

		String feedTitle = ParamUtil.getString(resourceRequest, "feedTitle");
		String format = ParamUtil.getString(
			resourceRequest, "type", RSSUtil.FORMAT_DEFAULT);
		double version = ParamUtil.getDouble(
			resourceRequest, "version", RSSUtil.VERSION_DEFAULT);
		String displayStyle = ParamUtil.getString(
			resourceRequest, "displayStyle", RSSUtil.DISPLAY_STYLE_DEFAULT);
		int max = ParamUtil.getInteger(
			resourceRequest, "max", SearchContainer.DEFAULT_DELTA);

		Group group = _groupLocalService.getGroup(
			themeDisplay.getScopeGroupId());

		SocialActivitiesQueryHelper.Scope scope =
			SocialActivitiesQueryHelper.Scope.fromValue(tabs1);

		List<SocialActivitySet> socialActivitySets =
			_socialActivitiesQueryHelper.getSocialActivitySets(
				group, themeDisplay.getLayout(), scope, 0, max);

		ServiceContext serviceContext = ServiceContextFactory.getInstance(
			resourceRequest);

		String rss = _exportToRSS(
			resourceRequest, resourceResponse, feedTitle, null, format, version,
			displayStyle, socialActivitySets, serviceContext);

		return rss.getBytes(StringPool.UTF8);
	}

	@Override
	protected boolean isRSSFeedsEnabled(ResourceRequest resourceRequest) {
		if (!super.isRSSFeedsEnabled(resourceRequest)) {
			return false;
		}

		PortletPreferences portletPreferences =
			resourceRequest.getPreferences();

		return GetterUtil.getBoolean(
			portletPreferences.getValue("enableRss", null), true);
	}

	private String _exportToRSS(
			ResourceRequest resourceRequest, ResourceResponse resourceResponse,
			String title, String description, String format, double version,
			String displayStyle, List<SocialActivitySet> socialActivitySets,
			ServiceContext serviceContext)
		throws Exception {

		ThemeDisplay themeDisplay = (ThemeDisplay)resourceRequest.getAttribute(
			WebKeys.THEME_DISPLAY);

		SyndFeed syndFeed = _syndModelFactory.createSyndFeed();

		syndFeed.setDescription(GetterUtil.getString(description, title));

		List<SyndEntry> syndEntries = new ArrayList<>();

		syndFeed.setEntries(syndEntries);

		for (SocialActivitySet socialActivitySet : socialActivitySets) {
			SocialActivityFeedEntry socialActivityFeedEntry =
				_socialActivityInterpreterLocalService.interpret(
					StringPool.BLANK, socialActivitySet, serviceContext);

			if (socialActivityFeedEntry == null) {
				continue;
			}

			SyndEntry syndEntry = _syndModelFactory.createSyndEntry();

			SyndContent syndContent = _syndModelFactory.createSyndContent();

			syndContent.setType(RSSUtil.ENTRY_TYPE_DEFAULT);

			String value = null;

			if (displayStyle.equals(RSSUtil.DISPLAY_STYLE_TITLE)) {
				value = StringPool.BLANK;
			}
			else {
				value = socialActivityFeedEntry.getBody();
			}

			syndContent.setValue(value);

			syndEntry.setDescription(syndContent);

			if (Validator.isNotNull(socialActivityFeedEntry.getLink())) {
				syndEntry.setLink(socialActivityFeedEntry.getLink());
			}

			syndEntry.setPublishedDate(
				new Date(socialActivitySet.getCreateDate()));
			syndEntry.setTitle(
				_htmlParser.extractText(socialActivityFeedEntry.getTitle()));
			syndEntry.setUri(socialActivityFeedEntry.getLink());

			syndEntries.add(syndEntry);
		}

		syndFeed.setFeedType(RSSUtil.getFeedType(format, version));

		List<SyndLink> syndLinks = new ArrayList<>();

		syndFeed.setLinks(syndLinks);

		SyndLink selfSyndLink = _syndModelFactory.createSyndLink();

		syndLinks.add(selfSyndLink);

		LiferayPortletResponse liferayPortletResponse =
			_portal.getLiferayPortletResponse(resourceResponse);

		ResourceURL rssURL = liferayPortletResponse.createResourceURL();

		rssURL.setParameter("feedTitle", title);
		rssURL.setResourceID("/social_activities/rss");

		selfSyndLink.setHref(rssURL.toString());

		selfSyndLink.setRel("self");

		SyndLink alternateSyndLink = _syndModelFactory.createSyndLink();

		syndLinks.add(alternateSyndLink);

		alternateSyndLink.setHref(_portal.getLayoutFullURL(themeDisplay));
		alternateSyndLink.setRel("alternate");

		syndFeed.setPublishedDate(new Date());
		syndFeed.setTitle(title);
		syndFeed.setUri(rssURL.toString());

		return _rssExporter.export(syndFeed);
	}

	@Reference
	private GroupLocalService _groupLocalService;

	@Reference
	private HtmlParser _htmlParser;

	@Reference
	private Portal _portal;

	@Reference
	private RSSExporter _rssExporter;

	@Reference
	private SocialActivitiesQueryHelper _socialActivitiesQueryHelper;

	@Reference
	private SocialActivityInterpreterLocalService
		_socialActivityInterpreterLocalService;

	@Reference
	private SyndModelFactory _syndModelFactory;

}