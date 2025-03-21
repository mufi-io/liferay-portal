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

package com.liferay.portal.cluster.multiple.internal;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.cluster.Address;
import com.liferay.portal.kernel.cluster.ClusterEvent;
import com.liferay.portal.kernel.cluster.ClusterEventListener;
import com.liferay.portal.kernel.cluster.ClusterEventType;
import com.liferay.portal.kernel.cluster.ClusterMasterExecutor;
import com.liferay.portal.kernel.cluster.ClusterMasterTokenTransitionListener;
import com.liferay.portal.kernel.cluster.ClusterNode;
import com.liferay.portal.kernel.cluster.ClusterNodeResponse;
import com.liferay.portal.kernel.cluster.ClusterNodeResponses;
import com.liferay.portal.kernel.cluster.ClusterRequest;
import com.liferay.portal.kernel.concurrent.DefaultNoticeableFuture;
import com.liferay.portal.kernel.concurrent.NoticeableFuture;
import com.liferay.portal.kernel.concurrent.NoticeableFutureConverter;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.MethodHandler;
import com.liferay.portal.kernel.util.Validator;

import java.util.HashSet;
import java.util.Set;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * @author Michael C. Han
 */
@Component(
	enabled = false, immediate = true, service = ClusterMasterExecutor.class
)
public class ClusterMasterExecutorImpl implements ClusterMasterExecutor {

	@Override
	public void addClusterMasterTokenTransitionListener(
		ClusterMasterTokenTransitionListener
			clusterMasterTokenTransitionListener) {

		_clusterMasterTokenTransitionListeners.add(
			clusterMasterTokenTransitionListener);
	}

	@Override
	public <T> NoticeableFuture<T> executeOnMaster(
		MethodHandler methodHandler) {

		if (!_enabled) {
			if (_log.isWarnEnabled()) {
				_log.warn(
					"Executing on the local node because the cluster master " +
						"executor is disabled");
			}

			DefaultNoticeableFuture<T> defaultNoticeableFuture =
				new DefaultNoticeableFuture<>();

			try {
				defaultNoticeableFuture.set((T)methodHandler.invoke());

				return defaultNoticeableFuture;
			}
			catch (Exception exception) {
				throw new SystemException(exception);
			}
		}

		final String masterClusterNodeId = getMasterClusterNodeId(true);

		ClusterRequest clusterRequest = ClusterRequest.createUnicastRequest(
			methodHandler, masterClusterNodeId);

		try {
			return new NoticeableFutureConverter<T, ClusterNodeResponses>(
				_clusterExecutorImpl.execute(clusterRequest)) {

				@Override
				protected T convert(ClusterNodeResponses clusterNodeResponses)
					throws Exception {

					ClusterNodeResponse clusterNodeResponse =
						clusterNodeResponses.getClusterResponse(
							masterClusterNodeId);

					return (T)clusterNodeResponse.getResult();
				}

			};
		}
		catch (Exception exception) {
			throw new SystemException(
				"Unable to execute on master " + masterClusterNodeId,
				exception);
		}
	}

	@Override
	public boolean isEnabled() {
		return _enabled;
	}

	@Override
	public boolean isMaster() {
		if (isEnabled()) {
			return _master;
		}

		return true;
	}

	@Override
	public void removeClusterMasterTokenTransitionListener(
		ClusterMasterTokenTransitionListener
			clusterMasterTokenTransitionListener) {

		_clusterMasterTokenTransitionListeners.remove(
			clusterMasterTokenTransitionListener);
	}

	@Activate
	protected synchronized void activate() {
		if (!_clusterExecutorImpl.isEnabled()) {
			return;
		}

		_clusterEventListener = new ClusterMasterTokenClusterEventListener();

		_clusterExecutorImpl.addClusterEventListener(_clusterEventListener);

		ClusterNode localClusterNode =
			_clusterExecutorImpl.getLocalClusterNode();

		_localClusterNodeId = localClusterNode.getClusterNodeId();

		_enabled = true;

		getMasterClusterNodeId(false);
	}

	@Deactivate
	protected void deactivate() {
		if (_clusterEventListener != null) {
			_clusterExecutorImpl.removeClusterEventListener(
				_clusterEventListener);
		}

		_clusterEventListener = null;
		_enabled = false;
		_localClusterNodeId = null;
	}

	protected String getMasterClusterNodeId(boolean notify) {
		boolean master = false;
		String masterClusterNodeId = null;

		while (true) {
			ClusterChannel clusterChannel =
				_clusterExecutorImpl.getClusterChannel();

			ClusterReceiver clusterReceiver =
				clusterChannel.getClusterReceiver();

			Address coordinatorAddress =
				clusterReceiver.getCoordinatorAddress();

			master = coordinatorAddress.equals(
				clusterChannel.getLocalAddress());

			if (master) {
				masterClusterNodeId = _localClusterNodeId;
			}
			else {
				masterClusterNodeId = _clusterExecutorImpl.getClusterNodeId(
					coordinatorAddress);
			}

			if (Validator.isNotNull(masterClusterNodeId)) {
				break;
			}

			if (_log.isInfoEnabled()) {
				_log.info(
					StringBundler.concat(
						"Unable to get cluster node information for ",
						"coordinator address ", coordinatorAddress,
						". Trying again."));
			}
		}

		if (master == _master) {
			return masterClusterNodeId;
		}

		_master = master;

		if (notify) {
			notifyMasterTokenTransitionListeners(master);
		}

		return masterClusterNodeId;
	}

	protected void notifyMasterTokenTransitionListeners(
		boolean masterTokenAcquired) {

		for (ClusterMasterTokenTransitionListener
				clusterMasterTokenTransitionListener :
					_clusterMasterTokenTransitionListeners) {

			if (masterTokenAcquired) {
				clusterMasterTokenTransitionListener.masterTokenAcquired();
			}
			else {
				clusterMasterTokenTransitionListener.masterTokenReleased();
			}
		}
	}

	protected void setClusterMasterTokenTransitionListeners(
		Set<ClusterMasterTokenTransitionListener>
			clusterMasterTokenTransitionListeners) {

		_clusterMasterTokenTransitionListeners.addAll(
			clusterMasterTokenTransitionListeners);
	}

	private static final Log _log = LogFactoryUtil.getLog(
		ClusterMasterExecutorImpl.class);

	private static volatile boolean _master;

	private ClusterEventListener _clusterEventListener;

	@Reference
	private ClusterExecutorImpl _clusterExecutorImpl;

	private final Set<ClusterMasterTokenTransitionListener>
		_clusterMasterTokenTransitionListeners = new HashSet<>();
	private boolean _enabled;
	private volatile String _localClusterNodeId;

	private class ClusterMasterTokenClusterEventListener
		implements ClusterEventListener {

		@Override
		public void processClusterEvent(ClusterEvent clusterEvent) {
			ClusterEventType clusterEventType =
				clusterEvent.getClusterEventType();

			if (clusterEventType ==
					ClusterEventType.COORDINATOR_ADDRESS_UPDATE) {

				getMasterClusterNodeId(true);
			}
		}

	}

}