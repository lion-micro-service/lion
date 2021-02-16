package com.lion.core.service.impl;

import com.lion.core.LionPage;
import com.lion.core.PageResultData;
import com.lion.core.persistence.curd.BaseDao;
import com.lion.core.persistence.entity.BaseEntity;
import com.lion.core.service.BaseService;
import com.lion.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * @param <T>
 * @author mrliu
 */
public abstract class BaseServiceImpl<T> implements BaseService<T> {


    @Autowired
    private BaseDao<T> baseDao;

    @Override
    public List<T> findAll() {
        return baseDao.findAll();
    }

    @Override
    public List<T> findAll(Sort sort) {
        return baseDao.findAll(sort);
    }

    @Override
    public List<T> findAllById(Iterable<Serializable> ids) {
        return baseDao.findAllById(ids);
    }

    @Override
    public <S extends T> List<S> saveAll(Iterable<S> entities) {
        return baseDao.saveAll(entities);
    }

    @Override
    public <S extends T> S saveAndFlush(S entity) {
        return baseDao.saveAndFlush(entity);
    }

    @Override
    public void update(T entity) {
        baseDao.update(entity);
    }

    @Override
    public void deleteInBatch(Iterable<T> entities) {
        baseDao.deleteInBatch(entities);
    }

    @Override
    public void deleteAllInBatch() {
        baseDao.deleteAllInBatch();
    }

    @Override
    public T getOne(Serializable id) {
        return baseDao.getOne(id);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example) {
        return baseDao.findAll(example);
    }

    @Override
    public <S extends T> List<S> findAll(Example<S> example, Sort sort) {
        return baseDao.findAll(example, sort);
    }

    @Override
    public List<T> find(Map<String, Object> searchParameter) {
        return baseDao.find(searchParameter);
    }

    @Override
    public List<T> find(Map<String, Object> searchParameter, Map<String, Object> sortParameter) {
        return baseDao.find(searchParameter, sortParameter);
    }

    @Override
    public Page<T> findAll(Pageable pageable) {
        Page<T> page = baseDao.findAll(pageable);
        return page;
    }

    @Override
    public <S extends T> S save(S entity) {
        if (Objects.isNull(entity)){
            return entity;
        }
        if (Objects.nonNull( ((BaseEntity)entity).getId())){
            this.update(entity);
            return entity;
        }
        return baseDao.save(entity);
    }

    @Override
    public T findById(Serializable id) {
        try {
            if (id != null) {
                Optional<T> optional = baseDao.findById(id);
                if (optional.isPresent()) {
                    return optional.get();
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            BusinessException.throwException("findById Exception");
        }
        return null;
    }

    @Override
    public boolean existsById(Serializable id) {
        return baseDao.existsById(id);
    }

    @Override
    public long count() {
        return baseDao.count();
    }

    @Override
    public void deleteById(Serializable id) {
        baseDao.deleteById(id);
    }

    @Override
    public void delete(T entity) {
        baseDao.delete(entity);
    }

    @Override
    public void deleteAll(Iterable<? extends T> entities) {
        baseDao.deleteAll(entities);
    }

    @Override
    public void deleteAll() {
        baseDao.deleteAll();
    }

    @Override
    public <S extends T> Optional<S> findOne(Example<S> example) {
        return baseDao.findOne(example);
    }

    @Override
    public <S extends T> Page<S> findAll(Example<S> example, Pageable pageable) {
        Page<S> page = (Page<S>) baseDao.findAll((Example<T>) example,pageable);
        return page;
    }

    @Override
    public <S extends T> long count(Example<S> example) {
        return baseDao.count(example);
    }

    @Override
    public <S extends T> boolean exists(Example<S> example) {
        return baseDao.exists(example);
    }

    @Override
    public Page<T> findNavigator(LionPage lionPage) {
        return baseDao.findNavigator(lionPage);
    }

}
