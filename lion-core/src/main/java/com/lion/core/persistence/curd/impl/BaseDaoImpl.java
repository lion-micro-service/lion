package com.lion.core.persistence.curd.impl;

import com.lion.core.persistence.curd.LionSimpleJpaRepository;
import org.hibernate.jdbc.ReturningWork;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.repository.NoRepositoryBean;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.sql.SQLException;

/**
 *	为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 */
@NoRepositoryBean
public class BaseDaoImpl<T> extends LionSimpleJpaRepository<T> {
	
	public BaseDaoImpl(JpaEntityInformation<T, Serializable> entityInformation, EntityManager entityManager) {
		super(entityInformation, entityManager);
	}

	
	/**
	 * 执行JDBC操作（DEMO） --非特殊情况不要使用JDBC直接操作方式，避免兼容性问题
	 */
	protected Object executeJdbc() {
		Object result =getSession().doReturningWork(
		new ReturningWork<T>() {
			@Override
			public T execute(java.sql.Connection connection) throws SQLException {
				connection.setAutoCommit(true);
				return null;
			}
		});
		return result;
	}


}
