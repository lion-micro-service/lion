package com.lion.core.persistence.curd;

import com.lion.core.LionPage;
import com.lion.core.PageResultData;
import com.lion.core.persistence.curd.impl.DeleteRepositoryImpl;
import com.lion.core.persistence.curd.impl.SaveRepositoryImpl;
import com.lion.core.persistence.curd.impl.SelectRepositoryImpl;
import com.lion.core.persistence.curd.impl.UpdateRepositoryImpl;
import org.hibernate.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.JpaEntityInformation;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * 
 * @author mrliu
 *
 */
@NoRepositoryBean
public abstract class LionSimpleJpaRepository<T> extends SimpleJpaRepository<T, Serializable> implements BaseDao<T> {

	private SelectRepository<T> selectRepository;

	private UpdateRepository<T> updateRepository;

	private SaveRepository<T> saveRepository;

	private DeleteRepository<T> deleteRepository;

	protected EntityManager entityManager;

	protected JpaEntityInformation<T, Serializable> entityInformation;

	// T的具体类
	protected Class<T> clazz;

	public LionSimpleJpaRepository(JpaEntityInformation<T, Serializable> entityInformation,
								   EntityManager entityManager) {
		super(entityInformation, entityManager);
		this.entityManager = entityManager;
		this.entityInformation = entityInformation;
		this.clazz = entityInformation.getJavaType();
		updateRepository = new UpdateRepositoryImpl<T>(entityManager, entityInformation, this);
		selectRepository = new SelectRepositoryImpl<T>(entityManager, clazz, this);
		saveRepository = new SaveRepositoryImpl<T>(entityManager);
		deleteRepository = new DeleteRepositoryImpl<T>(entityManager);
	}


	@Override
	public Session getSession() {
		return entityManager.unwrap(Session.class);
	}

	@Override
	@Transactional(readOnly=true)
	public List<T> find(Map<String, Object> searchParameter) {
		return selectRepository.find(searchParameter);
	}

	@Override
	@Transactional(readOnly=true)
	public List<T> find(Map<String, Object> searchParameter, Map<String, Object> sortParameter) {
		return selectRepository.find(searchParameter, sortParameter);
	}

	@Override
	@Transactional(readOnly=true)
	public List<?> find(String jpql) {
		return selectRepository.find(jpql);
	}

	@Override
	@Transactional(readOnly=true)
	public Object findOne(String jpql) {
		return selectRepository.findOne(jpql);
	}

	@Override
	@Transactional(readOnly=true)
	public List<?> find(String jpql, Map<String, Object> parameter) {
		return selectRepository.find(jpql, parameter);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<?> findNavigator(Pageable pageable, String jpql) {
		return selectRepository.findNavigator(pageable, jpql);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<?> findNavigator(Pageable pageable, String jpql, Map<String, Object> parameter) {
		return selectRepository.findNavigator(pageable, jpql, parameter);
	}

	@Override
	@Transactional(readOnly=true)
	public Page<T> findNavigator(LionPage lionPage) {
		return selectRepository.findNavigator(lionPage);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED)
	public int update(String jpql) {
		return updateRepository.update(jpql);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED)
	public int update(String jpql, Map<String, Object> parameter) {
		return updateRepository.update(jpql, parameter);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED)
	public void update(T entity) {
		updateRepository.update(entity);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED)
	public int save(String jpql) {
		return saveRepository.save(jpql);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED)
	public int save(String jpql, Map<String, Object> parameter) {
		return saveRepository.save(jpql, parameter);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED)
	public int delete(String jpql) {
		return deleteRepository.delete(jpql);
	}

	@Override
	@Transactional(propagation= Propagation.REQUIRED)
	public int delete(String jpql, Map<String, Object> parameter) {
		return deleteRepository.delete(jpql, parameter);
	}
}
