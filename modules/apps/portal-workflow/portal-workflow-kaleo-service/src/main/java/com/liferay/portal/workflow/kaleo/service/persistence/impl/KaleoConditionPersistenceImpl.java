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

package com.liferay.portal.workflow.kaleo.service.persistence.impl;

import com.liferay.petra.string.StringBundler;
import com.liferay.portal.kernel.change.tracking.CTColumnResolutionType;
import com.liferay.portal.kernel.configuration.Configuration;
import com.liferay.portal.kernel.dao.orm.EntityCache;
import com.liferay.portal.kernel.dao.orm.FinderCache;
import com.liferay.portal.kernel.dao.orm.FinderPath;
import com.liferay.portal.kernel.dao.orm.Query;
import com.liferay.portal.kernel.dao.orm.QueryPos;
import com.liferay.portal.kernel.dao.orm.QueryUtil;
import com.liferay.portal.kernel.dao.orm.Session;
import com.liferay.portal.kernel.dao.orm.SessionFactory;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.security.auth.CompanyThreadLocal;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.persistence.BasePersistence;
import com.liferay.portal.kernel.service.persistence.change.tracking.helper.CTPersistenceHelper;
import com.liferay.portal.kernel.service.persistence.impl.BasePersistenceImpl;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.OrderByComparator;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.kernel.util.PropsUtil;
import com.liferay.portal.kernel.util.ProxyUtil;
import com.liferay.portal.kernel.util.StringUtil;
import com.liferay.portal.workflow.kaleo.exception.NoSuchConditionException;
import com.liferay.portal.workflow.kaleo.model.KaleoCondition;
import com.liferay.portal.workflow.kaleo.model.KaleoConditionTable;
import com.liferay.portal.workflow.kaleo.model.impl.KaleoConditionImpl;
import com.liferay.portal.workflow.kaleo.model.impl.KaleoConditionModelImpl;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoConditionPersistence;
import com.liferay.portal.workflow.kaleo.service.persistence.KaleoConditionUtil;
import com.liferay.portal.workflow.kaleo.service.persistence.impl.constants.KaleoPersistenceConstants;

import java.io.Serializable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.sql.DataSource;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The persistence implementation for the kaleo condition service.
 *
 * <p>
 * Caching information and settings can be found in <code>portal.properties</code>
 * </p>
 *
 * @author Brian Wing Shun Chan
 * @generated
 */
@Component(service = {KaleoConditionPersistence.class, BasePersistence.class})
public class KaleoConditionPersistenceImpl
	extends BasePersistenceImpl<KaleoCondition>
	implements KaleoConditionPersistence {

	/*
	 * NOTE FOR DEVELOPERS:
	 *
	 * Never modify or reference this class directly. Always use <code>KaleoConditionUtil</code> to access the kaleo condition persistence. Modify <code>service.xml</code> and rerun ServiceBuilder to regenerate this class.
	 */
	public static final String FINDER_CLASS_NAME_ENTITY =
		KaleoConditionImpl.class.getName();

	public static final String FINDER_CLASS_NAME_LIST_WITH_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List1";

	public static final String FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION =
		FINDER_CLASS_NAME_ENTITY + ".List2";

	private FinderPath _finderPathWithPaginationFindAll;
	private FinderPath _finderPathWithoutPaginationFindAll;
	private FinderPath _finderPathCountAll;
	private FinderPath _finderPathWithPaginationFindByCompanyId;
	private FinderPath _finderPathWithoutPaginationFindByCompanyId;
	private FinderPath _finderPathCountByCompanyId;

	/**
	 * Returns all the kaleo conditions where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the matching kaleo conditions
	 */
	@Override
	public List<KaleoCondition> findByCompanyId(long companyId) {
		return findByCompanyId(
			companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the kaleo conditions where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoConditionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of kaleo conditions
	 * @param end the upper bound of the range of kaleo conditions (not inclusive)
	 * @return the range of matching kaleo conditions
	 */
	@Override
	public List<KaleoCondition> findByCompanyId(
		long companyId, int start, int end) {

		return findByCompanyId(companyId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo conditions where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoConditionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of kaleo conditions
	 * @param end the upper bound of the range of kaleo conditions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching kaleo conditions
	 */
	@Override
	public List<KaleoCondition> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<KaleoCondition> orderByComparator) {

		return findByCompanyId(companyId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the kaleo conditions where companyId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoConditionModelImpl</code>.
	 * </p>
	 *
	 * @param companyId the company ID
	 * @param start the lower bound of the range of kaleo conditions
	 * @param end the upper bound of the range of kaleo conditions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching kaleo conditions
	 */
	@Override
	public List<KaleoCondition> findByCompanyId(
		long companyId, int start, int end,
		OrderByComparator<KaleoCondition> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoCondition.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindByCompanyId;
				finderArgs = new Object[] {companyId};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindByCompanyId;
			finderArgs = new Object[] {
				companyId, start, end, orderByComparator
			};
		}

		List<KaleoCondition> list = null;

		if (useFinderCache && productionMode) {
			list = (List<KaleoCondition>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (KaleoCondition kaleoCondition : list) {
					if (companyId != kaleoCondition.getCompanyId()) {
						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_KALEOCONDITION_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(KaleoConditionModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				list = (List<KaleoCondition>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first kaleo condition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo condition
	 * @throws NoSuchConditionException if a matching kaleo condition could not be found
	 */
	@Override
	public KaleoCondition findByCompanyId_First(
			long companyId, OrderByComparator<KaleoCondition> orderByComparator)
		throws NoSuchConditionException {

		KaleoCondition kaleoCondition = fetchByCompanyId_First(
			companyId, orderByComparator);

		if (kaleoCondition != null) {
			return kaleoCondition;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchConditionException(sb.toString());
	}

	/**
	 * Returns the first kaleo condition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo condition, or <code>null</code> if a matching kaleo condition could not be found
	 */
	@Override
	public KaleoCondition fetchByCompanyId_First(
		long companyId, OrderByComparator<KaleoCondition> orderByComparator) {

		List<KaleoCondition> list = findByCompanyId(
			companyId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last kaleo condition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo condition
	 * @throws NoSuchConditionException if a matching kaleo condition could not be found
	 */
	@Override
	public KaleoCondition findByCompanyId_Last(
			long companyId, OrderByComparator<KaleoCondition> orderByComparator)
		throws NoSuchConditionException {

		KaleoCondition kaleoCondition = fetchByCompanyId_Last(
			companyId, orderByComparator);

		if (kaleoCondition != null) {
			return kaleoCondition;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("companyId=");
		sb.append(companyId);

		sb.append("}");

		throw new NoSuchConditionException(sb.toString());
	}

	/**
	 * Returns the last kaleo condition in the ordered set where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo condition, or <code>null</code> if a matching kaleo condition could not be found
	 */
	@Override
	public KaleoCondition fetchByCompanyId_Last(
		long companyId, OrderByComparator<KaleoCondition> orderByComparator) {

		int count = countByCompanyId(companyId);

		if (count == 0) {
			return null;
		}

		List<KaleoCondition> list = findByCompanyId(
			companyId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the kaleo conditions before and after the current kaleo condition in the ordered set where companyId = &#63;.
	 *
	 * @param kaleoConditionId the primary key of the current kaleo condition
	 * @param companyId the company ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next kaleo condition
	 * @throws NoSuchConditionException if a kaleo condition with the primary key could not be found
	 */
	@Override
	public KaleoCondition[] findByCompanyId_PrevAndNext(
			long kaleoConditionId, long companyId,
			OrderByComparator<KaleoCondition> orderByComparator)
		throws NoSuchConditionException {

		KaleoCondition kaleoCondition = findByPrimaryKey(kaleoConditionId);

		Session session = null;

		try {
			session = openSession();

			KaleoCondition[] array = new KaleoConditionImpl[3];

			array[0] = getByCompanyId_PrevAndNext(
				session, kaleoCondition, companyId, orderByComparator, true);

			array[1] = kaleoCondition;

			array[2] = getByCompanyId_PrevAndNext(
				session, kaleoCondition, companyId, orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected KaleoCondition getByCompanyId_PrevAndNext(
		Session session, KaleoCondition kaleoCondition, long companyId,
		OrderByComparator<KaleoCondition> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_KALEOCONDITION_WHERE);

		sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(KaleoConditionModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(companyId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						kaleoCondition)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<KaleoCondition> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the kaleo conditions where companyId = &#63; from the database.
	 *
	 * @param companyId the company ID
	 */
	@Override
	public void removeByCompanyId(long companyId) {
		for (KaleoCondition kaleoCondition :
				findByCompanyId(
					companyId, QueryUtil.ALL_POS, QueryUtil.ALL_POS, null)) {

			remove(kaleoCondition);
		}
	}

	/**
	 * Returns the number of kaleo conditions where companyId = &#63;.
	 *
	 * @param companyId the company ID
	 * @return the number of matching kaleo conditions
	 */
	@Override
	public int countByCompanyId(long companyId) {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoCondition.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByCompanyId;

			finderArgs = new Object[] {companyId};

			count = (Long)finderCache.getResult(finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_KALEOCONDITION_WHERE);

			sb.append(_FINDER_COLUMN_COMPANYID_COMPANYID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(companyId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_COMPANYID_COMPANYID_2 =
		"kaleoCondition.companyId = ?";

	private FinderPath _finderPathWithPaginationFindByKaleoDefinitionVersionId;
	private FinderPath
		_finderPathWithoutPaginationFindByKaleoDefinitionVersionId;
	private FinderPath _finderPathCountByKaleoDefinitionVersionId;

	/**
	 * Returns all the kaleo conditions where kaleoDefinitionVersionId = &#63;.
	 *
	 * @param kaleoDefinitionVersionId the kaleo definition version ID
	 * @return the matching kaleo conditions
	 */
	@Override
	public List<KaleoCondition> findByKaleoDefinitionVersionId(
		long kaleoDefinitionVersionId) {

		return findByKaleoDefinitionVersionId(
			kaleoDefinitionVersionId, QueryUtil.ALL_POS, QueryUtil.ALL_POS,
			null);
	}

	/**
	 * Returns a range of all the kaleo conditions where kaleoDefinitionVersionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoConditionModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoDefinitionVersionId the kaleo definition version ID
	 * @param start the lower bound of the range of kaleo conditions
	 * @param end the upper bound of the range of kaleo conditions (not inclusive)
	 * @return the range of matching kaleo conditions
	 */
	@Override
	public List<KaleoCondition> findByKaleoDefinitionVersionId(
		long kaleoDefinitionVersionId, int start, int end) {

		return findByKaleoDefinitionVersionId(
			kaleoDefinitionVersionId, start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo conditions where kaleoDefinitionVersionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoConditionModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoDefinitionVersionId the kaleo definition version ID
	 * @param start the lower bound of the range of kaleo conditions
	 * @param end the upper bound of the range of kaleo conditions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of matching kaleo conditions
	 */
	@Override
	public List<KaleoCondition> findByKaleoDefinitionVersionId(
		long kaleoDefinitionVersionId, int start, int end,
		OrderByComparator<KaleoCondition> orderByComparator) {

		return findByKaleoDefinitionVersionId(
			kaleoDefinitionVersionId, start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the kaleo conditions where kaleoDefinitionVersionId = &#63;.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoConditionModelImpl</code>.
	 * </p>
	 *
	 * @param kaleoDefinitionVersionId the kaleo definition version ID
	 * @param start the lower bound of the range of kaleo conditions
	 * @param end the upper bound of the range of kaleo conditions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of matching kaleo conditions
	 */
	@Override
	public List<KaleoCondition> findByKaleoDefinitionVersionId(
		long kaleoDefinitionVersionId, int start, int end,
		OrderByComparator<KaleoCondition> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoCondition.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath =
					_finderPathWithoutPaginationFindByKaleoDefinitionVersionId;
				finderArgs = new Object[] {kaleoDefinitionVersionId};
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath =
				_finderPathWithPaginationFindByKaleoDefinitionVersionId;
			finderArgs = new Object[] {
				kaleoDefinitionVersionId, start, end, orderByComparator
			};
		}

		List<KaleoCondition> list = null;

		if (useFinderCache && productionMode) {
			list = (List<KaleoCondition>)finderCache.getResult(
				finderPath, finderArgs, this);

			if ((list != null) && !list.isEmpty()) {
				for (KaleoCondition kaleoCondition : list) {
					if (kaleoDefinitionVersionId !=
							kaleoCondition.getKaleoDefinitionVersionId()) {

						list = null;

						break;
					}
				}
			}
		}

		if (list == null) {
			StringBundler sb = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					3 + (orderByComparator.getOrderByFields().length * 2));
			}
			else {
				sb = new StringBundler(3);
			}

			sb.append(_SQL_SELECT_KALEOCONDITION_WHERE);

			sb.append(
				_FINDER_COLUMN_KALEODEFINITIONVERSIONID_KALEODEFINITIONVERSIONID_2);

			if (orderByComparator != null) {
				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);
			}
			else {
				sb.append(KaleoConditionModelImpl.ORDER_BY_JPQL);
			}

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoDefinitionVersionId);

				list = (List<KaleoCondition>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Returns the first kaleo condition in the ordered set where kaleoDefinitionVersionId = &#63;.
	 *
	 * @param kaleoDefinitionVersionId the kaleo definition version ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo condition
	 * @throws NoSuchConditionException if a matching kaleo condition could not be found
	 */
	@Override
	public KaleoCondition findByKaleoDefinitionVersionId_First(
			long kaleoDefinitionVersionId,
			OrderByComparator<KaleoCondition> orderByComparator)
		throws NoSuchConditionException {

		KaleoCondition kaleoCondition = fetchByKaleoDefinitionVersionId_First(
			kaleoDefinitionVersionId, orderByComparator);

		if (kaleoCondition != null) {
			return kaleoCondition;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoDefinitionVersionId=");
		sb.append(kaleoDefinitionVersionId);

		sb.append("}");

		throw new NoSuchConditionException(sb.toString());
	}

	/**
	 * Returns the first kaleo condition in the ordered set where kaleoDefinitionVersionId = &#63;.
	 *
	 * @param kaleoDefinitionVersionId the kaleo definition version ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the first matching kaleo condition, or <code>null</code> if a matching kaleo condition could not be found
	 */
	@Override
	public KaleoCondition fetchByKaleoDefinitionVersionId_First(
		long kaleoDefinitionVersionId,
		OrderByComparator<KaleoCondition> orderByComparator) {

		List<KaleoCondition> list = findByKaleoDefinitionVersionId(
			kaleoDefinitionVersionId, 0, 1, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the last kaleo condition in the ordered set where kaleoDefinitionVersionId = &#63;.
	 *
	 * @param kaleoDefinitionVersionId the kaleo definition version ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo condition
	 * @throws NoSuchConditionException if a matching kaleo condition could not be found
	 */
	@Override
	public KaleoCondition findByKaleoDefinitionVersionId_Last(
			long kaleoDefinitionVersionId,
			OrderByComparator<KaleoCondition> orderByComparator)
		throws NoSuchConditionException {

		KaleoCondition kaleoCondition = fetchByKaleoDefinitionVersionId_Last(
			kaleoDefinitionVersionId, orderByComparator);

		if (kaleoCondition != null) {
			return kaleoCondition;
		}

		StringBundler sb = new StringBundler(4);

		sb.append(_NO_SUCH_ENTITY_WITH_KEY);

		sb.append("kaleoDefinitionVersionId=");
		sb.append(kaleoDefinitionVersionId);

		sb.append("}");

		throw new NoSuchConditionException(sb.toString());
	}

	/**
	 * Returns the last kaleo condition in the ordered set where kaleoDefinitionVersionId = &#63;.
	 *
	 * @param kaleoDefinitionVersionId the kaleo definition version ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the last matching kaleo condition, or <code>null</code> if a matching kaleo condition could not be found
	 */
	@Override
	public KaleoCondition fetchByKaleoDefinitionVersionId_Last(
		long kaleoDefinitionVersionId,
		OrderByComparator<KaleoCondition> orderByComparator) {

		int count = countByKaleoDefinitionVersionId(kaleoDefinitionVersionId);

		if (count == 0) {
			return null;
		}

		List<KaleoCondition> list = findByKaleoDefinitionVersionId(
			kaleoDefinitionVersionId, count - 1, count, orderByComparator);

		if (!list.isEmpty()) {
			return list.get(0);
		}

		return null;
	}

	/**
	 * Returns the kaleo conditions before and after the current kaleo condition in the ordered set where kaleoDefinitionVersionId = &#63;.
	 *
	 * @param kaleoConditionId the primary key of the current kaleo condition
	 * @param kaleoDefinitionVersionId the kaleo definition version ID
	 * @param orderByComparator the comparator to order the set by (optionally <code>null</code>)
	 * @return the previous, current, and next kaleo condition
	 * @throws NoSuchConditionException if a kaleo condition with the primary key could not be found
	 */
	@Override
	public KaleoCondition[] findByKaleoDefinitionVersionId_PrevAndNext(
			long kaleoConditionId, long kaleoDefinitionVersionId,
			OrderByComparator<KaleoCondition> orderByComparator)
		throws NoSuchConditionException {

		KaleoCondition kaleoCondition = findByPrimaryKey(kaleoConditionId);

		Session session = null;

		try {
			session = openSession();

			KaleoCondition[] array = new KaleoConditionImpl[3];

			array[0] = getByKaleoDefinitionVersionId_PrevAndNext(
				session, kaleoCondition, kaleoDefinitionVersionId,
				orderByComparator, true);

			array[1] = kaleoCondition;

			array[2] = getByKaleoDefinitionVersionId_PrevAndNext(
				session, kaleoCondition, kaleoDefinitionVersionId,
				orderByComparator, false);

			return array;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	protected KaleoCondition getByKaleoDefinitionVersionId_PrevAndNext(
		Session session, KaleoCondition kaleoCondition,
		long kaleoDefinitionVersionId,
		OrderByComparator<KaleoCondition> orderByComparator, boolean previous) {

		StringBundler sb = null;

		if (orderByComparator != null) {
			sb = new StringBundler(
				4 + (orderByComparator.getOrderByConditionFields().length * 3) +
					(orderByComparator.getOrderByFields().length * 3));
		}
		else {
			sb = new StringBundler(3);
		}

		sb.append(_SQL_SELECT_KALEOCONDITION_WHERE);

		sb.append(
			_FINDER_COLUMN_KALEODEFINITIONVERSIONID_KALEODEFINITIONVERSIONID_2);

		if (orderByComparator != null) {
			String[] orderByConditionFields =
				orderByComparator.getOrderByConditionFields();

			if (orderByConditionFields.length > 0) {
				sb.append(WHERE_AND);
			}

			for (int i = 0; i < orderByConditionFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByConditionFields[i]);

				if ((i + 1) < orderByConditionFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN_HAS_NEXT);
					}
					else {
						sb.append(WHERE_LESSER_THAN_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(WHERE_GREATER_THAN);
					}
					else {
						sb.append(WHERE_LESSER_THAN);
					}
				}
			}

			sb.append(ORDER_BY_CLAUSE);

			String[] orderByFields = orderByComparator.getOrderByFields();

			for (int i = 0; i < orderByFields.length; i++) {
				sb.append(_ORDER_BY_ENTITY_ALIAS);
				sb.append(orderByFields[i]);

				if ((i + 1) < orderByFields.length) {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC_HAS_NEXT);
					}
					else {
						sb.append(ORDER_BY_DESC_HAS_NEXT);
					}
				}
				else {
					if (orderByComparator.isAscending() ^ previous) {
						sb.append(ORDER_BY_ASC);
					}
					else {
						sb.append(ORDER_BY_DESC);
					}
				}
			}
		}
		else {
			sb.append(KaleoConditionModelImpl.ORDER_BY_JPQL);
		}

		String sql = sb.toString();

		Query query = session.createQuery(sql);

		query.setFirstResult(0);
		query.setMaxResults(2);

		QueryPos queryPos = QueryPos.getInstance(query);

		queryPos.add(kaleoDefinitionVersionId);

		if (orderByComparator != null) {
			for (Object orderByConditionValue :
					orderByComparator.getOrderByConditionValues(
						kaleoCondition)) {

				queryPos.add(orderByConditionValue);
			}
		}

		List<KaleoCondition> list = query.list();

		if (list.size() == 2) {
			return list.get(1);
		}
		else {
			return null;
		}
	}

	/**
	 * Removes all the kaleo conditions where kaleoDefinitionVersionId = &#63; from the database.
	 *
	 * @param kaleoDefinitionVersionId the kaleo definition version ID
	 */
	@Override
	public void removeByKaleoDefinitionVersionId(
		long kaleoDefinitionVersionId) {

		for (KaleoCondition kaleoCondition :
				findByKaleoDefinitionVersionId(
					kaleoDefinitionVersionId, QueryUtil.ALL_POS,
					QueryUtil.ALL_POS, null)) {

			remove(kaleoCondition);
		}
	}

	/**
	 * Returns the number of kaleo conditions where kaleoDefinitionVersionId = &#63;.
	 *
	 * @param kaleoDefinitionVersionId the kaleo definition version ID
	 * @return the number of matching kaleo conditions
	 */
	@Override
	public int countByKaleoDefinitionVersionId(long kaleoDefinitionVersionId) {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoCondition.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByKaleoDefinitionVersionId;

			finderArgs = new Object[] {kaleoDefinitionVersionId};

			count = (Long)finderCache.getResult(finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_KALEOCONDITION_WHERE);

			sb.append(
				_FINDER_COLUMN_KALEODEFINITIONVERSIONID_KALEODEFINITIONVERSIONID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoDefinitionVersionId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String
		_FINDER_COLUMN_KALEODEFINITIONVERSIONID_KALEODEFINITIONVERSIONID_2 =
			"kaleoCondition.kaleoDefinitionVersionId = ?";

	private FinderPath _finderPathFetchByKaleoNodeId;
	private FinderPath _finderPathCountByKaleoNodeId;

	/**
	 * Returns the kaleo condition where kaleoNodeId = &#63; or throws a <code>NoSuchConditionException</code> if it could not be found.
	 *
	 * @param kaleoNodeId the kaleo node ID
	 * @return the matching kaleo condition
	 * @throws NoSuchConditionException if a matching kaleo condition could not be found
	 */
	@Override
	public KaleoCondition findByKaleoNodeId(long kaleoNodeId)
		throws NoSuchConditionException {

		KaleoCondition kaleoCondition = fetchByKaleoNodeId(kaleoNodeId);

		if (kaleoCondition == null) {
			StringBundler sb = new StringBundler(4);

			sb.append(_NO_SUCH_ENTITY_WITH_KEY);

			sb.append("kaleoNodeId=");
			sb.append(kaleoNodeId);

			sb.append("}");

			if (_log.isDebugEnabled()) {
				_log.debug(sb.toString());
			}

			throw new NoSuchConditionException(sb.toString());
		}

		return kaleoCondition;
	}

	/**
	 * Returns the kaleo condition where kaleoNodeId = &#63; or returns <code>null</code> if it could not be found. Uses the finder cache.
	 *
	 * @param kaleoNodeId the kaleo node ID
	 * @return the matching kaleo condition, or <code>null</code> if a matching kaleo condition could not be found
	 */
	@Override
	public KaleoCondition fetchByKaleoNodeId(long kaleoNodeId) {
		return fetchByKaleoNodeId(kaleoNodeId, true);
	}

	/**
	 * Returns the kaleo condition where kaleoNodeId = &#63; or returns <code>null</code> if it could not be found, optionally using the finder cache.
	 *
	 * @param kaleoNodeId the kaleo node ID
	 * @param useFinderCache whether to use the finder cache
	 * @return the matching kaleo condition, or <code>null</code> if a matching kaleo condition could not be found
	 */
	@Override
	public KaleoCondition fetchByKaleoNodeId(
		long kaleoNodeId, boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoCondition.class);

		Object[] finderArgs = null;

		if (useFinderCache && productionMode) {
			finderArgs = new Object[] {kaleoNodeId};
		}

		Object result = null;

		if (useFinderCache && productionMode) {
			result = finderCache.getResult(
				_finderPathFetchByKaleoNodeId, finderArgs, this);
		}

		if (result instanceof KaleoCondition) {
			KaleoCondition kaleoCondition = (KaleoCondition)result;

			if (kaleoNodeId != kaleoCondition.getKaleoNodeId()) {
				result = null;
			}
		}

		if (result == null) {
			StringBundler sb = new StringBundler(3);

			sb.append(_SQL_SELECT_KALEOCONDITION_WHERE);

			sb.append(_FINDER_COLUMN_KALEONODEID_KALEONODEID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoNodeId);

				List<KaleoCondition> list = query.list();

				if (list.isEmpty()) {
					if (useFinderCache && productionMode) {
						finderCache.putResult(
							_finderPathFetchByKaleoNodeId, finderArgs, list);
					}
				}
				else {
					if (list.size() > 1) {
						Collections.sort(list, Collections.reverseOrder());

						if (_log.isWarnEnabled()) {
							if (!productionMode || !useFinderCache) {
								finderArgs = new Object[] {kaleoNodeId};
							}

							_log.warn(
								"KaleoConditionPersistenceImpl.fetchByKaleoNodeId(long, boolean) with parameters (" +
									StringUtil.merge(finderArgs) +
										") yields a result set with more than 1 result. This violates the logical unique restriction. There is no order guarantee on which result is returned by this finder.");
						}
					}

					KaleoCondition kaleoCondition = list.get(0);

					result = kaleoCondition;

					cacheResult(kaleoCondition);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		if (result instanceof List<?>) {
			return null;
		}
		else {
			return (KaleoCondition)result;
		}
	}

	/**
	 * Removes the kaleo condition where kaleoNodeId = &#63; from the database.
	 *
	 * @param kaleoNodeId the kaleo node ID
	 * @return the kaleo condition that was removed
	 */
	@Override
	public KaleoCondition removeByKaleoNodeId(long kaleoNodeId)
		throws NoSuchConditionException {

		KaleoCondition kaleoCondition = findByKaleoNodeId(kaleoNodeId);

		return remove(kaleoCondition);
	}

	/**
	 * Returns the number of kaleo conditions where kaleoNodeId = &#63;.
	 *
	 * @param kaleoNodeId the kaleo node ID
	 * @return the number of matching kaleo conditions
	 */
	@Override
	public int countByKaleoNodeId(long kaleoNodeId) {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoCondition.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		Long count = null;

		if (productionMode) {
			finderPath = _finderPathCountByKaleoNodeId;

			finderArgs = new Object[] {kaleoNodeId};

			count = (Long)finderCache.getResult(finderPath, finderArgs, this);
		}

		if (count == null) {
			StringBundler sb = new StringBundler(2);

			sb.append(_SQL_COUNT_KALEOCONDITION_WHERE);

			sb.append(_FINDER_COLUMN_KALEONODEID_KALEONODEID_2);

			String sql = sb.toString();

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				QueryPos queryPos = QueryPos.getInstance(query);

				queryPos.add(kaleoNodeId);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(finderPath, finderArgs, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	private static final String _FINDER_COLUMN_KALEONODEID_KALEONODEID_2 =
		"kaleoCondition.kaleoNodeId = ?";

	public KaleoConditionPersistenceImpl() {
		setModelClass(KaleoCondition.class);

		setModelImplClass(KaleoConditionImpl.class);
		setModelPKClass(long.class);

		setTable(KaleoConditionTable.INSTANCE);
	}

	/**
	 * Caches the kaleo condition in the entity cache if it is enabled.
	 *
	 * @param kaleoCondition the kaleo condition
	 */
	@Override
	public void cacheResult(KaleoCondition kaleoCondition) {
		if (kaleoCondition.getCtCollectionId() != 0) {
			return;
		}

		entityCache.putResult(
			KaleoConditionImpl.class, kaleoCondition.getPrimaryKey(),
			kaleoCondition);

		finderCache.putResult(
			_finderPathFetchByKaleoNodeId,
			new Object[] {kaleoCondition.getKaleoNodeId()}, kaleoCondition);
	}

	private int _valueObjectFinderCacheListThreshold;

	/**
	 * Caches the kaleo conditions in the entity cache if it is enabled.
	 *
	 * @param kaleoConditions the kaleo conditions
	 */
	@Override
	public void cacheResult(List<KaleoCondition> kaleoConditions) {
		if ((_valueObjectFinderCacheListThreshold == 0) ||
			((_valueObjectFinderCacheListThreshold > 0) &&
			 (kaleoConditions.size() > _valueObjectFinderCacheListThreshold))) {

			return;
		}

		for (KaleoCondition kaleoCondition : kaleoConditions) {
			if (kaleoCondition.getCtCollectionId() != 0) {
				continue;
			}

			if (entityCache.getResult(
					KaleoConditionImpl.class, kaleoCondition.getPrimaryKey()) ==
						null) {

				cacheResult(kaleoCondition);
			}
		}
	}

	/**
	 * Clears the cache for all kaleo conditions.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache() {
		entityCache.clearCache(KaleoConditionImpl.class);

		finderCache.clearCache(KaleoConditionImpl.class);
	}

	/**
	 * Clears the cache for the kaleo condition.
	 *
	 * <p>
	 * The <code>EntityCache</code> and <code>FinderCache</code> are both cleared by this method.
	 * </p>
	 */
	@Override
	public void clearCache(KaleoCondition kaleoCondition) {
		entityCache.removeResult(KaleoConditionImpl.class, kaleoCondition);
	}

	@Override
	public void clearCache(List<KaleoCondition> kaleoConditions) {
		for (KaleoCondition kaleoCondition : kaleoConditions) {
			entityCache.removeResult(KaleoConditionImpl.class, kaleoCondition);
		}
	}

	@Override
	public void clearCache(Set<Serializable> primaryKeys) {
		finderCache.clearCache(KaleoConditionImpl.class);

		for (Serializable primaryKey : primaryKeys) {
			entityCache.removeResult(KaleoConditionImpl.class, primaryKey);
		}
	}

	protected void cacheUniqueFindersCache(
		KaleoConditionModelImpl kaleoConditionModelImpl) {

		Object[] args = new Object[] {kaleoConditionModelImpl.getKaleoNodeId()};

		finderCache.putResult(
			_finderPathCountByKaleoNodeId, args, Long.valueOf(1));
		finderCache.putResult(
			_finderPathFetchByKaleoNodeId, args, kaleoConditionModelImpl);
	}

	/**
	 * Creates a new kaleo condition with the primary key. Does not add the kaleo condition to the database.
	 *
	 * @param kaleoConditionId the primary key for the new kaleo condition
	 * @return the new kaleo condition
	 */
	@Override
	public KaleoCondition create(long kaleoConditionId) {
		KaleoCondition kaleoCondition = new KaleoConditionImpl();

		kaleoCondition.setNew(true);
		kaleoCondition.setPrimaryKey(kaleoConditionId);

		kaleoCondition.setCompanyId(CompanyThreadLocal.getCompanyId());

		return kaleoCondition;
	}

	/**
	 * Removes the kaleo condition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param kaleoConditionId the primary key of the kaleo condition
	 * @return the kaleo condition that was removed
	 * @throws NoSuchConditionException if a kaleo condition with the primary key could not be found
	 */
	@Override
	public KaleoCondition remove(long kaleoConditionId)
		throws NoSuchConditionException {

		return remove((Serializable)kaleoConditionId);
	}

	/**
	 * Removes the kaleo condition with the primary key from the database. Also notifies the appropriate model listeners.
	 *
	 * @param primaryKey the primary key of the kaleo condition
	 * @return the kaleo condition that was removed
	 * @throws NoSuchConditionException if a kaleo condition with the primary key could not be found
	 */
	@Override
	public KaleoCondition remove(Serializable primaryKey)
		throws NoSuchConditionException {

		Session session = null;

		try {
			session = openSession();

			KaleoCondition kaleoCondition = (KaleoCondition)session.get(
				KaleoConditionImpl.class, primaryKey);

			if (kaleoCondition == null) {
				if (_log.isDebugEnabled()) {
					_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
				}

				throw new NoSuchConditionException(
					_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			return remove(kaleoCondition);
		}
		catch (NoSuchConditionException noSuchEntityException) {
			throw noSuchEntityException;
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}
	}

	@Override
	protected KaleoCondition removeImpl(KaleoCondition kaleoCondition) {
		Session session = null;

		try {
			session = openSession();

			if (!session.contains(kaleoCondition)) {
				kaleoCondition = (KaleoCondition)session.get(
					KaleoConditionImpl.class,
					kaleoCondition.getPrimaryKeyObj());
			}

			if ((kaleoCondition != null) &&
				ctPersistenceHelper.isRemove(kaleoCondition)) {

				session.delete(kaleoCondition);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (kaleoCondition != null) {
			clearCache(kaleoCondition);
		}

		return kaleoCondition;
	}

	@Override
	public KaleoCondition updateImpl(KaleoCondition kaleoCondition) {
		boolean isNew = kaleoCondition.isNew();

		if (!(kaleoCondition instanceof KaleoConditionModelImpl)) {
			InvocationHandler invocationHandler = null;

			if (ProxyUtil.isProxyClass(kaleoCondition.getClass())) {
				invocationHandler = ProxyUtil.getInvocationHandler(
					kaleoCondition);

				throw new IllegalArgumentException(
					"Implement ModelWrapper in kaleoCondition proxy " +
						invocationHandler.getClass());
			}

			throw new IllegalArgumentException(
				"Implement ModelWrapper in custom KaleoCondition implementation " +
					kaleoCondition.getClass());
		}

		KaleoConditionModelImpl kaleoConditionModelImpl =
			(KaleoConditionModelImpl)kaleoCondition;

		ServiceContext serviceContext =
			ServiceContextThreadLocal.getServiceContext();

		Date date = new Date();

		if (isNew && (kaleoCondition.getCreateDate() == null)) {
			if (serviceContext == null) {
				kaleoCondition.setCreateDate(date);
			}
			else {
				kaleoCondition.setCreateDate(
					serviceContext.getCreateDate(date));
			}
		}

		if (!kaleoConditionModelImpl.hasSetModifiedDate()) {
			if (serviceContext == null) {
				kaleoCondition.setModifiedDate(date);
			}
			else {
				kaleoCondition.setModifiedDate(
					serviceContext.getModifiedDate(date));
			}
		}

		Session session = null;

		try {
			session = openSession();

			if (ctPersistenceHelper.isInsert(kaleoCondition)) {
				if (!isNew) {
					session.evict(
						KaleoConditionImpl.class,
						kaleoCondition.getPrimaryKeyObj());
				}

				session.save(kaleoCondition);
			}
			else {
				kaleoCondition = (KaleoCondition)session.merge(kaleoCondition);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		if (kaleoCondition.getCtCollectionId() != 0) {
			if (isNew) {
				kaleoCondition.setNew(false);
			}

			kaleoCondition.resetOriginalValues();

			return kaleoCondition;
		}

		entityCache.putResult(
			KaleoConditionImpl.class, kaleoConditionModelImpl, false, true);

		cacheUniqueFindersCache(kaleoConditionModelImpl);

		if (isNew) {
			kaleoCondition.setNew(false);
		}

		kaleoCondition.resetOriginalValues();

		return kaleoCondition;
	}

	/**
	 * Returns the kaleo condition with the primary key or throws a <code>com.liferay.portal.kernel.exception.NoSuchModelException</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the kaleo condition
	 * @return the kaleo condition
	 * @throws NoSuchConditionException if a kaleo condition with the primary key could not be found
	 */
	@Override
	public KaleoCondition findByPrimaryKey(Serializable primaryKey)
		throws NoSuchConditionException {

		KaleoCondition kaleoCondition = fetchByPrimaryKey(primaryKey);

		if (kaleoCondition == null) {
			if (_log.isDebugEnabled()) {
				_log.debug(_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
			}

			throw new NoSuchConditionException(
				_NO_SUCH_ENTITY_WITH_PRIMARY_KEY + primaryKey);
		}

		return kaleoCondition;
	}

	/**
	 * Returns the kaleo condition with the primary key or throws a <code>NoSuchConditionException</code> if it could not be found.
	 *
	 * @param kaleoConditionId the primary key of the kaleo condition
	 * @return the kaleo condition
	 * @throws NoSuchConditionException if a kaleo condition with the primary key could not be found
	 */
	@Override
	public KaleoCondition findByPrimaryKey(long kaleoConditionId)
		throws NoSuchConditionException {

		return findByPrimaryKey((Serializable)kaleoConditionId);
	}

	/**
	 * Returns the kaleo condition with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param primaryKey the primary key of the kaleo condition
	 * @return the kaleo condition, or <code>null</code> if a kaleo condition with the primary key could not be found
	 */
	@Override
	public KaleoCondition fetchByPrimaryKey(Serializable primaryKey) {
		if (ctPersistenceHelper.isProductionMode(
				KaleoCondition.class, primaryKey)) {

			return super.fetchByPrimaryKey(primaryKey);
		}

		KaleoCondition kaleoCondition = null;

		Session session = null;

		try {
			session = openSession();

			kaleoCondition = (KaleoCondition)session.get(
				KaleoConditionImpl.class, primaryKey);

			if (kaleoCondition != null) {
				cacheResult(kaleoCondition);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return kaleoCondition;
	}

	/**
	 * Returns the kaleo condition with the primary key or returns <code>null</code> if it could not be found.
	 *
	 * @param kaleoConditionId the primary key of the kaleo condition
	 * @return the kaleo condition, or <code>null</code> if a kaleo condition with the primary key could not be found
	 */
	@Override
	public KaleoCondition fetchByPrimaryKey(long kaleoConditionId) {
		return fetchByPrimaryKey((Serializable)kaleoConditionId);
	}

	@Override
	public Map<Serializable, KaleoCondition> fetchByPrimaryKeys(
		Set<Serializable> primaryKeys) {

		if (ctPersistenceHelper.isProductionMode(KaleoCondition.class)) {
			return super.fetchByPrimaryKeys(primaryKeys);
		}

		if (primaryKeys.isEmpty()) {
			return Collections.emptyMap();
		}

		Map<Serializable, KaleoCondition> map =
			new HashMap<Serializable, KaleoCondition>();

		if (primaryKeys.size() == 1) {
			Iterator<Serializable> iterator = primaryKeys.iterator();

			Serializable primaryKey = iterator.next();

			KaleoCondition kaleoCondition = fetchByPrimaryKey(primaryKey);

			if (kaleoCondition != null) {
				map.put(primaryKey, kaleoCondition);
			}

			return map;
		}

		if ((databaseInMaxParameters > 0) &&
			(primaryKeys.size() > databaseInMaxParameters)) {

			Iterator<Serializable> iterator = primaryKeys.iterator();

			while (iterator.hasNext()) {
				Set<Serializable> page = new HashSet<>();

				for (int i = 0;
					 (i < databaseInMaxParameters) && iterator.hasNext(); i++) {

					page.add(iterator.next());
				}

				map.putAll(fetchByPrimaryKeys(page));
			}

			return map;
		}

		StringBundler sb = new StringBundler((primaryKeys.size() * 2) + 1);

		sb.append(getSelectSQL());
		sb.append(" WHERE ");
		sb.append(getPKDBName());
		sb.append(" IN (");

		for (Serializable primaryKey : primaryKeys) {
			sb.append((long)primaryKey);

			sb.append(",");
		}

		sb.setIndex(sb.index() - 1);

		sb.append(")");

		String sql = sb.toString();

		Session session = null;

		try {
			session = openSession();

			Query query = session.createQuery(sql);

			for (KaleoCondition kaleoCondition :
					(List<KaleoCondition>)query.list()) {

				map.put(kaleoCondition.getPrimaryKeyObj(), kaleoCondition);

				cacheResult(kaleoCondition);
			}
		}
		catch (Exception exception) {
			throw processException(exception);
		}
		finally {
			closeSession(session);
		}

		return map;
	}

	/**
	 * Returns all the kaleo conditions.
	 *
	 * @return the kaleo conditions
	 */
	@Override
	public List<KaleoCondition> findAll() {
		return findAll(QueryUtil.ALL_POS, QueryUtil.ALL_POS, null);
	}

	/**
	 * Returns a range of all the kaleo conditions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoConditionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo conditions
	 * @param end the upper bound of the range of kaleo conditions (not inclusive)
	 * @return the range of kaleo conditions
	 */
	@Override
	public List<KaleoCondition> findAll(int start, int end) {
		return findAll(start, end, null);
	}

	/**
	 * Returns an ordered range of all the kaleo conditions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoConditionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo conditions
	 * @param end the upper bound of the range of kaleo conditions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @return the ordered range of kaleo conditions
	 */
	@Override
	public List<KaleoCondition> findAll(
		int start, int end,
		OrderByComparator<KaleoCondition> orderByComparator) {

		return findAll(start, end, orderByComparator, true);
	}

	/**
	 * Returns an ordered range of all the kaleo conditions.
	 *
	 * <p>
	 * Useful when paginating results. Returns a maximum of <code>end - start</code> instances. <code>start</code> and <code>end</code> are not primary keys, they are indexes in the result set. Thus, <code>0</code> refers to the first result in the set. Setting both <code>start</code> and <code>end</code> to <code>QueryUtil#ALL_POS</code> will return the full result set. If <code>orderByComparator</code> is specified, then the query will include the given ORDER BY logic. If <code>orderByComparator</code> is absent, then the query will include the default ORDER BY logic from <code>KaleoConditionModelImpl</code>.
	 * </p>
	 *
	 * @param start the lower bound of the range of kaleo conditions
	 * @param end the upper bound of the range of kaleo conditions (not inclusive)
	 * @param orderByComparator the comparator to order the results by (optionally <code>null</code>)
	 * @param useFinderCache whether to use the finder cache
	 * @return the ordered range of kaleo conditions
	 */
	@Override
	public List<KaleoCondition> findAll(
		int start, int end, OrderByComparator<KaleoCondition> orderByComparator,
		boolean useFinderCache) {

		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoCondition.class);

		FinderPath finderPath = null;
		Object[] finderArgs = null;

		if ((start == QueryUtil.ALL_POS) && (end == QueryUtil.ALL_POS) &&
			(orderByComparator == null)) {

			if (useFinderCache && productionMode) {
				finderPath = _finderPathWithoutPaginationFindAll;
				finderArgs = FINDER_ARGS_EMPTY;
			}
		}
		else if (useFinderCache && productionMode) {
			finderPath = _finderPathWithPaginationFindAll;
			finderArgs = new Object[] {start, end, orderByComparator};
		}

		List<KaleoCondition> list = null;

		if (useFinderCache && productionMode) {
			list = (List<KaleoCondition>)finderCache.getResult(
				finderPath, finderArgs, this);
		}

		if (list == null) {
			StringBundler sb = null;
			String sql = null;

			if (orderByComparator != null) {
				sb = new StringBundler(
					2 + (orderByComparator.getOrderByFields().length * 2));

				sb.append(_SQL_SELECT_KALEOCONDITION);

				appendOrderByComparator(
					sb, _ORDER_BY_ENTITY_ALIAS, orderByComparator);

				sql = sb.toString();
			}
			else {
				sql = _SQL_SELECT_KALEOCONDITION;

				sql = sql.concat(KaleoConditionModelImpl.ORDER_BY_JPQL);
			}

			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(sql);

				list = (List<KaleoCondition>)QueryUtil.list(
					query, getDialect(), start, end);

				cacheResult(list);

				if (useFinderCache && productionMode) {
					finderCache.putResult(finderPath, finderArgs, list);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return list;
	}

	/**
	 * Removes all the kaleo conditions from the database.
	 *
	 */
	@Override
	public void removeAll() {
		for (KaleoCondition kaleoCondition : findAll()) {
			remove(kaleoCondition);
		}
	}

	/**
	 * Returns the number of kaleo conditions.
	 *
	 * @return the number of kaleo conditions
	 */
	@Override
	public int countAll() {
		boolean productionMode = ctPersistenceHelper.isProductionMode(
			KaleoCondition.class);

		Long count = null;

		if (productionMode) {
			count = (Long)finderCache.getResult(
				_finderPathCountAll, FINDER_ARGS_EMPTY, this);
		}

		if (count == null) {
			Session session = null;

			try {
				session = openSession();

				Query query = session.createQuery(_SQL_COUNT_KALEOCONDITION);

				count = (Long)query.uniqueResult();

				if (productionMode) {
					finderCache.putResult(
						_finderPathCountAll, FINDER_ARGS_EMPTY, count);
				}
			}
			catch (Exception exception) {
				throw processException(exception);
			}
			finally {
				closeSession(session);
			}
		}

		return count.intValue();
	}

	@Override
	protected EntityCache getEntityCache() {
		return entityCache;
	}

	@Override
	protected String getPKDBName() {
		return "kaleoConditionId";
	}

	@Override
	protected String getSelectSQL() {
		return _SQL_SELECT_KALEOCONDITION;
	}

	@Override
	public Set<String> getCTColumnNames(
		CTColumnResolutionType ctColumnResolutionType) {

		return _ctColumnNamesMap.getOrDefault(
			ctColumnResolutionType, Collections.emptySet());
	}

	@Override
	public List<String> getMappingTableNames() {
		return _mappingTableNames;
	}

	@Override
	public Map<String, Integer> getTableColumnsMap() {
		return KaleoConditionModelImpl.TABLE_COLUMNS_MAP;
	}

	@Override
	public String getTableName() {
		return "KaleoCondition";
	}

	@Override
	public List<String[]> getUniqueIndexColumnNames() {
		return _uniqueIndexColumnNames;
	}

	private static final Map<CTColumnResolutionType, Set<String>>
		_ctColumnNamesMap = new EnumMap<CTColumnResolutionType, Set<String>>(
			CTColumnResolutionType.class);
	private static final List<String> _mappingTableNames =
		new ArrayList<String>();
	private static final List<String[]> _uniqueIndexColumnNames =
		new ArrayList<String[]>();

	static {
		Set<String> ctControlColumnNames = new HashSet<String>();
		Set<String> ctIgnoreColumnNames = new HashSet<String>();
		Set<String> ctStrictColumnNames = new HashSet<String>();

		ctControlColumnNames.add("mvccVersion");
		ctControlColumnNames.add("ctCollectionId");
		ctStrictColumnNames.add("groupId");
		ctStrictColumnNames.add("companyId");
		ctStrictColumnNames.add("userId");
		ctStrictColumnNames.add("userName");
		ctStrictColumnNames.add("createDate");
		ctIgnoreColumnNames.add("modifiedDate");
		ctStrictColumnNames.add("kaleoDefinitionId");
		ctStrictColumnNames.add("kaleoDefinitionVersionId");
		ctStrictColumnNames.add("kaleoNodeId");
		ctStrictColumnNames.add("script");
		ctStrictColumnNames.add("scriptLanguage");
		ctStrictColumnNames.add("scriptRequiredContexts");

		_ctColumnNamesMap.put(
			CTColumnResolutionType.CONTROL, ctControlColumnNames);
		_ctColumnNamesMap.put(
			CTColumnResolutionType.IGNORE, ctIgnoreColumnNames);
		_ctColumnNamesMap.put(
			CTColumnResolutionType.PK,
			Collections.singleton("kaleoConditionId"));
		_ctColumnNamesMap.put(
			CTColumnResolutionType.STRICT, ctStrictColumnNames);
	}

	/**
	 * Initializes the kaleo condition persistence.
	 */
	@Activate
	public void activate() {
		_valueObjectFinderCacheListThreshold = GetterUtil.getInteger(
			PropsUtil.get(PropsKeys.VALUE_OBJECT_FINDER_CACHE_LIST_THRESHOLD));

		_finderPathWithPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathWithoutPaginationFindAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findAll", new String[0],
			new String[0], true);

		_finderPathCountAll = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countAll",
			new String[0], new String[0], false);

		_finderPathWithPaginationFindByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITH_PAGINATION, "findByCompanyId",
			new String[] {
				Long.class.getName(), Integer.class.getName(),
				Integer.class.getName(), OrderByComparator.class.getName()
			},
			new String[] {"companyId"}, true);

		_finderPathWithoutPaginationFindByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "findByCompanyId",
			new String[] {Long.class.getName()}, new String[] {"companyId"},
			true);

		_finderPathCountByCompanyId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByCompanyId",
			new String[] {Long.class.getName()}, new String[] {"companyId"},
			false);

		_finderPathWithPaginationFindByKaleoDefinitionVersionId =
			new FinderPath(
				FINDER_CLASS_NAME_LIST_WITH_PAGINATION,
				"findByKaleoDefinitionVersionId",
				new String[] {
					Long.class.getName(), Integer.class.getName(),
					Integer.class.getName(), OrderByComparator.class.getName()
				},
				new String[] {"kaleoDefinitionVersionId"}, true);

		_finderPathWithoutPaginationFindByKaleoDefinitionVersionId =
			new FinderPath(
				FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
				"findByKaleoDefinitionVersionId",
				new String[] {Long.class.getName()},
				new String[] {"kaleoDefinitionVersionId"}, true);

		_finderPathCountByKaleoDefinitionVersionId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION,
			"countByKaleoDefinitionVersionId",
			new String[] {Long.class.getName()},
			new String[] {"kaleoDefinitionVersionId"}, false);

		_finderPathFetchByKaleoNodeId = new FinderPath(
			FINDER_CLASS_NAME_ENTITY, "fetchByKaleoNodeId",
			new String[] {Long.class.getName()}, new String[] {"kaleoNodeId"},
			true);

		_finderPathCountByKaleoNodeId = new FinderPath(
			FINDER_CLASS_NAME_LIST_WITHOUT_PAGINATION, "countByKaleoNodeId",
			new String[] {Long.class.getName()}, new String[] {"kaleoNodeId"},
			false);

		_setKaleoConditionUtilPersistence(this);
	}

	@Deactivate
	public void deactivate() {
		_setKaleoConditionUtilPersistence(null);

		entityCache.removeCache(KaleoConditionImpl.class.getName());
	}

	private void _setKaleoConditionUtilPersistence(
		KaleoConditionPersistence kaleoConditionPersistence) {

		try {
			Field field = KaleoConditionUtil.class.getDeclaredField(
				"_persistence");

			field.setAccessible(true);

			field.set(null, kaleoConditionPersistence);
		}
		catch (ReflectiveOperationException reflectiveOperationException) {
			throw new RuntimeException(reflectiveOperationException);
		}
	}

	@Override
	@Reference(
		target = KaleoPersistenceConstants.SERVICE_CONFIGURATION_FILTER,
		unbind = "-"
	)
	public void setConfiguration(Configuration configuration) {
	}

	@Override
	@Reference(
		target = KaleoPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
	}

	@Override
	@Reference(
		target = KaleoPersistenceConstants.ORIGIN_BUNDLE_SYMBOLIC_NAME_FILTER,
		unbind = "-"
	)
	public void setSessionFactory(SessionFactory sessionFactory) {
		super.setSessionFactory(sessionFactory);
	}

	@Reference
	protected CTPersistenceHelper ctPersistenceHelper;

	@Reference
	protected EntityCache entityCache;

	@Reference
	protected FinderCache finderCache;

	private static final String _SQL_SELECT_KALEOCONDITION =
		"SELECT kaleoCondition FROM KaleoCondition kaleoCondition";

	private static final String _SQL_SELECT_KALEOCONDITION_WHERE =
		"SELECT kaleoCondition FROM KaleoCondition kaleoCondition WHERE ";

	private static final String _SQL_COUNT_KALEOCONDITION =
		"SELECT COUNT(kaleoCondition) FROM KaleoCondition kaleoCondition";

	private static final String _SQL_COUNT_KALEOCONDITION_WHERE =
		"SELECT COUNT(kaleoCondition) FROM KaleoCondition kaleoCondition WHERE ";

	private static final String _ORDER_BY_ENTITY_ALIAS = "kaleoCondition.";

	private static final String _NO_SUCH_ENTITY_WITH_PRIMARY_KEY =
		"No KaleoCondition exists with the primary key ";

	private static final String _NO_SUCH_ENTITY_WITH_KEY =
		"No KaleoCondition exists with the key {";

	private static final Log _log = LogFactoryUtil.getLog(
		KaleoConditionPersistenceImpl.class);

	@Override
	protected FinderCache getFinderCache() {
		return finderCache;
	}

}