package com.lion.core.persistence.curd.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.lion.core.persistence.curd.RepositoryParameter;
import com.lion.core.persistence.curd.UpdateRepository;
import org.hibernate.Session;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.io.Serializable;
import java.util.Map;
import java.util.Optional;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 * @param <T>
 */
public class UpdateRepositoryImpl<T>  implements UpdateRepository<T> {

	private EntityManager entityManager;
	
	private JpaEntityInformation<T, Serializable> entityInformation;
	
	private SimpleJpaRepository<T, Serializable> simpleJpaRepository;

	public UpdateRepositoryImpl(EntityManager entityManager, JpaEntityInformation<T, Serializable> entityInformation, SimpleJpaRepository<T, Serializable> simpleJpaRepository) {
		super();
		this.entityManager = entityManager;
		this.entityInformation = entityInformation;
		this.simpleJpaRepository = simpleJpaRepository;
	}

	private Session getSession() {
		return entityManager.unwrap(Session.class);
	}
	
	@Override
	public int update(String jpql) {
		return update(jpql, null);
	}

	@Override
	public int update(String jpql,Map<String, Object> parameter) {
		Query query = entityManager.createQuery(jpql);
		RepositoryParameter.setParameter(query, parameter);
		return query.executeUpdate(); 
	}

	@Override
	public void update(T entity) {
		Serializable id = entityInformation.getId(entity);
		Optional<T> optional = simpleJpaRepository.findById(id);
		if(!optional.isPresent()) {
			return;
		}
		T oldEntity = optional.get();
		BeanUtil.copyProperties(entity,oldEntity,CopyOptions.create().setIgnoreNullValue(true).setIgnoreError(true));
		getSession().update(oldEntity);
	}

}
