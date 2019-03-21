package com.x.y.service;

import com.x.y.dto.PagerDTO;
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

    public void insert(Object entity) {
        commonRepository.insert(entity);
    }

    public void update(Object entity) {
        commonRepository.update(entity);
    }

    public void delete(Object entity) {
        commonRepository.delete(entity);
    }

    public <T> T getEntityById(Serializable id, Class<T> objectClass) {
        return commonRepository.getEntityById(id, objectClass);
    }

    public <T> T getEntityByUniqueKey(Class<T> objectClass, String fieldName, String fieldValue) {
        return commonRepository.getEntityByUniqueKey(objectClass, fieldName, fieldValue);
    }

    public <T> List<T> getEntityList(Object object, PagerDTO pagerDTO) {
        return commonRepository.getEntityList(object, pagerDTO, null);
    }

    public <T> List<T> getEntityList(Object object, PagerDTO pagerDTO, String sqlString) {
        return commonRepository.getEntityList(object, pagerDTO, sqlString);
    }

    public Integer getEntityCount(Object object) {
        return commonRepository.getEntityCount(object, null);
    }

    public Integer getEntityCount(Object object, String sqlString) {
        return commonRepository.getEntityCount(object, sqlString);
    }

    public <T> List<T> getEntityListForSearch(Object object, PagerDTO pagerDTO) {
        return commonRepository.getEntityListForSearch(object, pagerDTO, null);
    }

    public <T> List<T> getEntityListForSearch(Object object, PagerDTO pagerDTO, String sqlString) {
        return commonRepository.getEntityListForSearch(object, pagerDTO, sqlString);
    }

    public Integer getEntityCountForSearch(Object object) {
        return commonRepository.getEntityCountForSearch(object, null);
    }

    public Integer getEntityCountForSearch(Object object, String sqlString) {
        return commonRepository.getEntityCountForSearch(object, sqlString);
    }
}