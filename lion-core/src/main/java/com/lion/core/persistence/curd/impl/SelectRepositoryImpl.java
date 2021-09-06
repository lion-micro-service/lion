package com.lion.core.persistence.curd.impl;

import com.lion.core.IEnum;
import com.lion.core.LionPage;
import com.lion.core.PageResultData;
import com.lion.core.persistence.curd.PredicateBuilder;
import com.lion.core.persistence.curd.RepositoryParameter;
import com.lion.core.persistence.curd.SelectRepository;
import com.lion.core.persistence.curd.SortBuilder;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.param.NamedParameterSpecification;
import org.hibernate.param.ParameterSpecification;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.*;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * 
 * @author mrliu
 *
 * @param <T>
 */
public class SelectRepositoryImpl<T> implements SelectRepository<T> {

	private final Integer maxResults = 50000;

	private EntityManager entityManager;

	private Class<T> clazz;

	private SimpleJpaRepository<T, Serializable> simpleJpaRepository;

	public SelectRepositoryImpl(EntityManager entityManager, Class<T> clazz,
                                SimpleJpaRepository<T, Serializable> simpleJpaRepository) {
		super();
		this.entityManager = entityManager;
		this.clazz = clazz;
		this.simpleJpaRepository = simpleJpaRepository;
	}

	@Override
	public List<?> findAll(String jpql) {
		return findAll(jpql, null);
	}

	@Override
	public Object findOne(String jpql) {
		Pageable pageable = PageRequest.of(1,1);
		Page<?> page = findNavigator(pageable,jpql);
		List<?> list = page.getContent();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<?> findAll(String jpql,Map<String, Object> searchParameter) {
		Query query = entityManager.createQuery(jpql);
		query.setMaxResults(maxResults);
		query = RepositoryParameter.setParameter(query, searchParameter);
		return query.getResultList();
	}

	@Override
	public Page<?> findNavigator(Pageable pageable, String jpql) {
		return findNavigator(pageable, jpql, null);
	}

	@Override
	public Page<?> findNavigator(Pageable pageable, String jpql, Map<String, Object> searchParameter) {
		searchParameter = (Objects.isNull(searchParameter) || searchParameter.isEmpty())?null:searchParameter;
		List<?> list = (List<?>) executeJpql(pageable, jpql, searchParameter);
		Long total = getCount(jpql, searchParameter);
		Page page = new PageImpl(list,pageable,total);
		return page;
	}

	@Override
	public Page<?> findNavigatorByNativeSql(Pageable pageable, String sql) {
		return findNavigatorByNativeSql(pageable,sql,null,null);
	}

	@Override
	public Page<?> findNavigatorByNativeSql(Pageable pageable, String sql, Map<String, Object> searchParameter, Class<?> returnType) {
		searchParameter = (Objects.isNull(searchParameter) || searchParameter.isEmpty())?null:searchParameter;
		List<?> list = (List<?>) executeSql(pageable, sql, searchParameter,returnType);
		Long total = getCountByNativeSql(sql, searchParameter);
		Page page = new PageImpl(list,pageable,total);
		return page;
	}

	@Override
	public Page<T> findNavigator(LionPage lionPage) {
		return (Page<T>) find(lionPage.getPageSize(), lionPage.getPageNumber(), lionPage.getJpqlParameter().getSearchParameter(),
				lionPage.getJpqlParameter().getSortParameter());
	}

	@Override
	public Page<T> findNavigator(LionPage LionPage, Map<String, Object> searchParameter) {
		return findNavigator(LionPage,searchParameter,null);
	}

	@Override
	public Page<T> findNavigator(LionPage LionPage, Map<String, Object> searchParameter, Map<String, Object> sortParameter) {
		if (Objects.nonNull(searchParameter) && searchParameter.size()>0) {
			LionPage.getJpqlParameter().setSearchParameter(searchParameter);
		}
		if (Objects.nonNull(sortParameter) && sortParameter.size()>0) {
			LionPage.getJpqlParameter().setSortParameter(sortParameter);
		}
		return findNavigator(LionPage);
	}

	/**
	 * 分页查询
	 * 
	 * @param lionPage
	 * @param jpql
	 * @param searchParameter
	 * @return
	 */
	private List<?> executeJpql(Pageable lionPage, String jpql, Map<String, Object> searchParameter) {
		Query query = entityManager.createQuery(jpql);
		query = RepositoryParameter.setParameter(query, searchParameter);
		query.setFirstResult(lionPage.getPageNumber() * lionPage.getPageSize());
		query.setMaxResults(lionPage.getPageSize());
		return query.getResultList();
	}

	private List<?> executeSql(Pageable lionPage, String sql, Map<String, Object> searchParameter,Class<?> returnType) {
		Query query = null;
		if (Objects.nonNull(returnType) && (!Objects.equals(returnType,Map.class) || !Objects.equals(returnType,HashMap.class))) {
			query = entityManager.createNativeQuery(sql, returnType);
		}else {
			query = entityManager.createNativeQuery(sql);
		}
		if (Objects.isNull(returnType) || Objects.equals(returnType,Map.class) || Objects.equals(returnType,HashMap.class)){
			query = query.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.ALIAS_TO_ENTITY_MAP);
		}
		query = RepositoryParameter.setParameter(query, searchParameter);
		query.setFirstResult(lionPage.getPageNumber() * lionPage.getPageSize());
		query.setMaxResults(lionPage.getPageSize());
		return query.getResultList();
	}

	/**
	 * 查询总行数
	 * 
	 * @param jpql
	 * @param searchParameter
	 * @return
	 */
	private Long getCount(String jpql, Map<String, Object> searchParameter) {
		Session session = entityManager.unwrap(Session.class);
		SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor)session.getSessionFactory();
		Map replacements = Objects.isNull(searchParameter)?Collections.EMPTY_MAP:searchParameter;
		QueryTranslatorImpl queryTranslator=new QueryTranslatorImpl(jpql,jpql,replacements,sessionFactoryImplementor);
		queryTranslator.compile(replacements, !(jpql.indexOf("order")>-1 || jpql.indexOf("ORDER")>-1));
		String sql = queryTranslator.getSQLString();
		List<ParameterSpecification> parameter = queryTranslator.getCollectedParameterSpecifications();
		for(ParameterSpecification parameterSpecification : parameter){
			StringBuilder sb = new StringBuilder();
			int index = sql.indexOf("?");
			sb.append(sql.substring(0,index));
			sb.append(":");
			sb.append(((NamedParameterSpecification)parameterSpecification).getName());
			sb.append(sql.substring(index+1));
			sql = sb.toString();
		}
		if (sql.indexOf("order")>-1) {
			sql = sql.substring(0, sql.indexOf("order"));
		}
		if (sql.indexOf("ORDER")>-1) {
			sql = sql.substring(0, sql.indexOf("ORDER"));
		}
		return getCountByNativeSql(sql,searchParameter);
	}

	private Long getCountByNativeSql(String sql,Map<String, Object> searchParameter) {
		StringBuffer countSql = new StringBuffer();
		countSql.append(" select count(1) from (");
		countSql.append(sql);
		countSql.append(") tb");
		Query query = entityManager.createNativeQuery(countSql.toString());
		query = RepositoryParameter.setParameter(query, searchParameter);
		return Long.valueOf(query.getSingleResult().toString());
	}

	@Override
	public List<T> find(Map<String, Object> searchParameter) {
		return find(searchParameter, null);
	}

	@Override
	public List<T> find(Map<String, Object> searchParameter, Map<String, Object> sortParameter) {
		return (List<T>) find(maxResults, 0, searchParameter, sortParameter).getContent();
	}

	private Page<?> find(Integer pageSize, Integer pageNumber, Map<String, Object> searchParameter,
						 Map<String, Object> sortParameter) {
		Specification<T> specification = new Specification<T>() {
			private static final long serialVersionUID = 1L;
			@Override
			public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				// 动态构建where条件
				List<Predicate> list = PredicateBuilder.builder(root, criteriaBuilder, searchParameter);
				return criteriaBuilder.and(list.toArray(new Predicate[list.size()]));
			}
		};
		// 构建排序
		Sort sort = SortBuilder.builder(sortParameter);
		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
		org.springframework.data.domain.Page<T> page = simpleJpaRepository.findAll(specification, pageable);
		return page;
	}

}
