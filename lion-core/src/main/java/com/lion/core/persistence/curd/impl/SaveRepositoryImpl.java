package com.lion.core.persistence.curd.impl;

import com.lion.core.persistence.curd.RepositoryParameter;
import com.lion.core.persistence.curd.SaveRepository;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.Map;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 * @param <T>
 */
public class SaveRepositoryImpl<T>  implements SaveRepository<T> {

	private EntityManager entityManager;
	
	public SaveRepositoryImpl(EntityManager entityManager) {
		super();
		this.entityManager = entityManager;
	}
	
	@Override
	public int save(String jpql,Map<String, Object> parameter) {
		Query query = entityManager.createQuery(jpql);
		query = RepositoryParameter.setParameter(query, parameter);
		return query.executeUpdate(); 
	}

	@Override
	public int save(String jpql) {
		return save(jpql, null);
	}

}
