package com.x.y.repository;

import com.x.y.common.DaoCommon;
import com.x.y.dto.Pager;
import com.x.y.utils.StringUtils;
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

    public void add(Object entity) {
        entityManager.persist(entity);
    }

    public void merge(Object entity) {
        entityManager.merge(entity);
    }

    public void delete(Object entity) {
        entityManager.remove(entity);
    }

    public <T> T findById(Serializable id, Class<T> objectClass) {
        return entityManager.find(objectClass, id);
    }

    @SuppressWarnings("unchecked")
    public <T> T findByUniqueKey(Class<T> objectClass, String fieldName, String fieldValue) {
        Query query = entityManager.createQuery("from " + objectClass.getSimpleName() + " where " + fieldName + " =:" + fieldName);
        query.setParameter(fieldName, fieldValue);
        return (T) query.getSingleResult();
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findListByObj(Object object, Pager pager, String sqlString) {
        StringBuffer queryBuffer = new StringBuffer("from " + object.getClass().getName() + " where 1=1 ");
        Query query = getFindByObjQuery(object, sqlString, queryBuffer);
        DaoCommon.setQueryPager(query, pager);
        return query.getResultList();
    }

    public Integer findCountByObj(Object object, String sqlString) {
        StringBuffer queryBuffer = new StringBuffer("select count(*) from " + object.getClass().getName() + " where 1=1 ");
        Query query = getFindByObjQuery(object, sqlString, queryBuffer);
        return Integer.parseInt(query.getSingleResult().toString());
    }

    private Query getFindByObjQuery(Object object, String sqlString, StringBuffer queryBuffer) {
        DaoCommon.setQueryBufferForAccurateSearch(queryBuffer, object);
        if (StringUtils.isNotNull(sqlString)) {
            queryBuffer.append(" ").append(sqlString);
        }
        Query query = entityManager.createQuery(queryBuffer.toString());
        DaoCommon.setQueryValueForAccurateSearch(query, object);
        return query;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> findListForSearch(Object object, Pager pager, String sqlString) {
        StringBuffer queryBuffer = new StringBuffer("from " + object.getClass().getName() + " where 1=1 ");
        Query query = getFindForSearchQuery(object, sqlString, queryBuffer);
        DaoCommon.setQueryPager(query, pager);
        return query.getResultList();
    }

    public Integer findCountForSearch(Object object, String sqlString) {
        StringBuffer queryBuffer = new StringBuffer("select count(*) from " + object.getClass().getName() + " where 1=1 ");
        Query query = getFindForSearchQuery(object, sqlString, queryBuffer);
        return Integer.parseInt(query.getSingleResult().toString());
    }

    private Query getFindForSearchQuery(Object object, String sqlString, StringBuffer queryBuffer) {
        DaoCommon.setQueryBufferForStringSearch(queryBuffer, object);
        if (StringUtils.isNotNull(sqlString)) {
            queryBuffer.append(" ").append(sqlString);
        }
        Query query = entityManager.createQuery(queryBuffer.toString());
        DaoCommon.setQueryValueForStringSearch(query, object);
        return query;
    }
}