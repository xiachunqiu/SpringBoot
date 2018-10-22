package com.x.y.repository;

import com.x.y.common.DaoCommon;
import com.x.y.dto.Pager;
import com.x.y.utils.StringUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.transaction.Transactional;
import java.io.Serializable;
import java.util.List;

@Repository
public class CommonRepository {
    @PersistenceContext
    private EntityManager entityManager;

    @Transactional
    public void add(Object entity) throws DataAccessException {
        entityManager.persist(entity);
    }

    @Transactional
    public void merge(Object entity) throws DataAccessException {
        entityManager.merge(entity);
    }

    @Transactional
    public void delete(Object entity) throws DataAccessException {
        entityManager.remove(entity);
    }

    public <T> T findById(Serializable id, Class<T> objectClass) throws DataAccessException {
        return entityManager.find(objectClass, id);
    }

    @SuppressWarnings("unchecked")
    public <T> T findByUniqueKey(Class<T> objectClass, String fieldName, String fieldValue) {
        Query query = entityManager.createQuery("from " + objectClass.getSimpleName() + " where " + fieldName + " =:" + fieldName);
        query.setParameter(fieldName, fieldValue);
        return (T) query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findListByObj(Object object, Pager pager, String sqlString) throws DataAccessException {
        StringBuffer queryBuffer = new StringBuffer("from " + object.getClass().getName() + " where 1=1 ");
        DaoCommon.setQueryBufferForAccurateSearch(queryBuffer, object);
        if (StringUtils.isNotNull(sqlString)) {
            queryBuffer.append(" ").append(sqlString);
        }
        Query query = entityManager.createQuery(queryBuffer.toString());
        DaoCommon.setQueryValueForAccurateSearch(query, object);
        DaoCommon.setQueryPager(query, pager);
        return query.getResultList();
    }

    public Integer findCountByObj(Object object, String sqlString) throws DataAccessException {
        StringBuffer queryBuffer = new StringBuffer("select count(*) from " + object.getClass().getName() + " where 1=1 ");
        DaoCommon.setQueryBufferForAccurateSearch(queryBuffer, object);
        if (StringUtils.isNotNull(sqlString)) {
            queryBuffer.append(" ").append(sqlString);
        }
        Query query = entityManager.createQuery(queryBuffer.toString());
        DaoCommon.setQueryValueForAccurateSearch(query, object);
        return Integer.parseInt(query.getSingleResult().toString());
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findListForSearch(Object object, Pager pager, String sqlString) throws DataAccessException {
        StringBuffer queryBuffer = new StringBuffer("from " + object.getClass().getName() + " where 1=1 ");
        DaoCommon.setQueryBufferForStringSearch(queryBuffer, object);
        if (StringUtils.isNotNull(sqlString)) {
            queryBuffer.append(" ").append(sqlString);
        }
        Query query = entityManager.createQuery(queryBuffer.toString());
        DaoCommon.setQueryValueForAccurateSearch(query, object);
        DaoCommon.setQueryPager(query, pager);
        return query.getResultList();
    }

    public Integer findCountForSearch(Object object, String sqlString) throws DataAccessException {
        StringBuffer queryBuffer = new StringBuffer("select count(*) from " + object.getClass().getName() + " where 1=1 ");
        DaoCommon.setQueryBufferForStringSearch(queryBuffer, object);
        if (StringUtils.isNotNull(sqlString)) {
            queryBuffer.append(" ").append(sqlString);
        }
        Query query = entityManager.createQuery(queryBuffer.toString());
        DaoCommon.setQueryValueForAccurateSearch(query, object);
        return Integer.parseInt(query.getSingleResult().toString());
    }
}