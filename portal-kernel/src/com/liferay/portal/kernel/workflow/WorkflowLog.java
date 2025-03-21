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

package com.liferay.portal.kernel.workflow;

import java.util.Date;
import java.util.Locale;

/**
 * @author Micha Kiener
 * @author Shuyang Zhou
 * @author Brian Wing Shun Chan
 * @author Marcellus Tavares
 */
public interface WorkflowLog extends WorkflowModel {

	public static final int NODE_ENTRY = 4;

	public static final int TASK_ASSIGN = 1;

	public static final int TASK_COMPLETION = 3;

	public static final int TASK_UPDATE = 2;

	public static final int TRANSITION = 0;

	public long getAuditUserId();

	public String getComment();

	public Date getCreateDate();

	public String getCurrentWorkflowNodeLabel(Locale locale);

	public String getCurrentWorkflowNodeName();

	public long getPreviousRoleId();

	public long getPreviousUserId();

	public String getPreviousWorkflowNodeLabel(Locale locale);

	public String getPreviousWorkflowNodeName();

	public long getRoleId();

	public int getType();

	public long getUserId();

	public long getWorkflowLogId();

	public long getWorkflowTaskId();

}