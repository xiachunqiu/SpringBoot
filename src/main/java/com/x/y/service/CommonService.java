package com.x.y.service;

import com.x.y.dto.Pager;
import com.x.y.repository.CommonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;

@Service
public class CommonService {
    private final CommonRepository commonRepository;

    @Autowired
    public CommonService(CommonRepository commonRepository) {
        this.commonRepository = commonRepository;
    }

    public void add(Object entity) {
        commonRepository.add(entity);
    }

    public void merge(Object entity) {
        commonRepository.merge(entity);
    }

    public void delete(Object entity) {
        commonRepository.delete(entity);
    }

    public <T> T findById(Serializable id, Class<T> objectClass) {
        return commonRepository.findById(id, objectClass);
    }

    public <T> T findByUniqueKey(Class<T> objectClass, String fieldName, String fieldValue) {
        return commonRepository.findByUniqueKey(objectClass, fieldName, fieldValue);
    }

    public <T> List<T> findListByObj(Object object, Pager pager) {
        return commonRepository.findListByObj(object, pager, null);
    }

    public <T> List<T> findListByObj(Object object, Pager pager, String sqlString) {
        return commonRepository.findListByObj(object, pager, sqlString);
    }

    public Integer findCountByObj(Object object) {
        return commonRepository.findCountByObj(object, null);
    }

    public Integer findCountByObj(Object object, String sqlString) {
        return commonRepository.findCountByObj(object, sqlString);
    }

    public <T> List<T> findListForSearch(Object object, Pager pager) {
        return commonRepository.findListForSearch(object, pager, null);
    }

    public <T> List<T> findListForSearch(Object object, Pager pager, String sqlString) {
        return commonRepository.findListForSearch(object, pager, sqlString);
    }

    public Integer findCountForSearch(Object object) {
        return commonRepository.findCountForSearch(object, null);
    }

    public Integer findCountForSearch(Object object, String sqlString) {
        return commonRepository.findCountForSearch(object, sqlString);
    }
}