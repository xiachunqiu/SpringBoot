package com.x.y.repository;

import com.x.y.common.DaoCommon;
import com.x.y.dto.PagerDTO;
import com.x.y.util.StringUtil;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.List;

@Repository
public class CommonRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public void insert(Object entity) {
        entityManager.persist(entity);
    }

    public void update(Object entity) {
        entityManager.merge(entity);
    }

    public void delete(Object entity) {
        entityManager.remove(entity);
    }

    public <T> T getEntityById(Serializable id, Class<T> objectClass) {
        return entityManager.find(objectClass, id);
    }

    @SuppressWarnings("unchecked")
    public <T> T getEntityByUniqueKey(Class<T> objectClass, String fieldName, String fieldValue) {
        Query query = entityManager.createQuery("from " + objectClass.getSimpleName() + " where " + fieldName + " =:" + fieldName);
        query.setParameter(fieldName, fieldValue);
        return (T) query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getEntityList(Object object, PagerDTO pagerDTO, String sqlString) {
        StringBuffer queryBuffer = new StringBuffer("from " + object.getClass().getName() + " where 1=1 ");
        Query query = getEntityQuery(object, sqlString, queryBuffer);
        DaoCommon.setQueryPager(query, pagerDTO);
        return query.getResultList();
    }

    public Integer getEntityCount(Object object, String sqlString) {
        StringBuffer queryBuffer = new StringBuffer("select count(*) from " + object.getClass().getName() + " where 1=1 ");
        Query query = getEntityQuery(object, sqlString, queryBuffer);
        return Integer.parseInt(query.getSingleResult().toString());
    }

    private Query getEntityQuery(Object object, String sqlString, StringBuffer queryBuffer) {
        DaoCommon.setQueryBufferForAccurateSearch(queryBuffer, object);
        if (StringUtil.isNotNull(sqlString)) {
            queryBuffer.append(" ").append(sqlString);
        }
        Query query = entityManager.createQuery(queryBuffer.toString());
        DaoCommon.setQueryValueForAccurateSearch(query, object);
        return query;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getEntityListForSearch(Object object, PagerDTO pagerDTO, String sqlString) {
        StringBuffer queryBuffer = new StringBuffer("from " + object.getClass().getName() + " where 1=1 ");
        Query query = getEntityQueryForSearch(object, sqlString, queryBuffer);
        DaoCommon.setQueryPager(query, pagerDTO);
        return query.getResultList();
    }

    public Integer getEntityCountForSearch(Object object, String sqlString) {
        StringBuffer queryBuffer = new StringBuffer("select count(*) from " + object.getClass().getName() + " where 1=1 ");
        Query query = getEntityQueryForSearch(object, sqlString, queryBuffer);
        return Integer.parseInt(query.getSingleResult().toString());
    }

    private Query getEntityQueryForSearch(Object object, String sqlString, StringBuffer queryBuffer) {
        DaoCommon.setQueryBufferForStringSearch(queryBuffer, object);
        if (StringUtil.isNotNull(sqlString)) {
            queryBuffer.append(" ").append(sqlString);
        }
        Query query = entityManager.createQuery(queryBuffer.toString());
        DaoCommon.setQueryValueForStringSearch(query, object);
        return query;
    }
}