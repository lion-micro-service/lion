package com.lion.core.persistence.curd.impl;

import com.lion.core.IEnum;
import com.lion.core.LionPage;
import com.lion.core.PageResultData;
import com.lion.core.persistence.curd.PredicateBuilder;
import com.lion.core.persistence.curd.RepositoryParameter;
import com.lion.core.persistence.curd.SelectRepository;
import com.lion.core.persistence.curd.SortBuilder;
import org.hibernate.Session;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.hql.internal.ast.QueryTranslatorImpl;
import org.hibernate.param.NamedParameterSpecification;
import org.hibernate.param.ParameterSpecification;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * 
 * @author mrliu
 *
 * @param <T>
 */
public class SelectRepositoryImpl<T> implements SelectRepository<T> {

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
	public List<?> find(String jpql) {
		return find(jpql, null);
	}

	@Override
	public Object findOne(String jpql) {
		List<?> list = find(jpql);
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return null;
	}

	@Override
	public List<?> find(String jpql,Map<String, Object> searchParameter) {
		Query query = entityManager.createQuery(jpql);
		RepositoryParameter.setParameter(query, searchParameter);
		return query.getResultList();
	}

	@Override
	public PageResultData<?> findNavigator(LionPage lionPage, String jpql) {
		return findNavigator(lionPage, jpql, null);
	}

	@Override
	public PageResultData<?> findNavigator(LionPage lionPage, String jpql, Map<String, Object> searchParameter) {
		List<T> list = (List<T>) executeJpql(lionPage, jpql, searchParameter);
		return convertPageResultData(list, lionPage,getCount(jpql, searchParameter));
	}

	@Override
	public PageResultData<T> findNavigator(LionPage lionPage) {
		return (PageResultData<T>) find(lionPage.getPageSize(), lionPage.getPageNumber(), lionPage.getJpqlParameter().getSearchParameter(),
				lionPage.getJpqlParameter().getSortParameter());
	}

	/**
	 * 分页查询
	 * 
	 * @param lionPage
	 * @param jpql
	 * @param searchParameter
	 * @return
	 */
	private List<?> executeJpql(LionPage lionPage, String jpql, Map<String, Object> searchParameter) {
		Query query = entityManager.createQuery(jpql);
		RepositoryParameter.setParameter(query, searchParameter);
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
	private Long getCount(final String jpql, final Map<String, Object> searchParameter) {
		Session session = entityManager.unwrap(Session.class);
		SessionFactoryImplementor sessionFactoryImplementor = (SessionFactoryImplementor)session.getSessionFactory();
		QueryTranslatorImpl queryTranslator=new QueryTranslatorImpl(jpql,jpql,searchParameter==null?Collections.EMPTY_MAP:searchParameter,sessionFactoryImplementor);
		queryTranslator.compile(searchParameter==null?Collections.EMPTY_MAP:searchParameter, false);
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
		StringBuffer countSql = new StringBuffer();
		countSql.append(" select count(1) from (");
		countSql.append(sql);
		countSql.append(") tb");
		Query query = entityManager.createNativeQuery(countSql.toString());
		for(int i =1; i<=parameter.size(); i++){
			String namedParameter = ((NamedParameterSpecification)parameter.get(i-1)).getName();
			Object object = searchParameter.get(namedParameter);
			if(object.getClass().isEnum() ) {
				query.setParameter(namedParameter, ((IEnum)object).getKey());
			}else {
				query.setParameter(namedParameter,object);
			}

		}
		return Long.valueOf(query.getSingleResult().toString());
	}

	/**
	 * 拼接sql中的参数
	 * @param sb
	 * @param parameter
	 * @param i
	 * @return
	 */
	private StringBuilder spliceSql(StringBuilder sb,List<ParameterSpecification> parameter ,Integer i){
		if (!(parameter.size()>0)){
			return sb;
		}
		String tmp = sb.toString().trim();
		if (Objects.equals(tmp.substring((tmp.length()-1)<0?0:tmp.length()-1),"=")){
			String namedParameter = ((NamedParameterSpecification)parameter.get(i)).getName();
			sb.append(":").append(namedParameter);
		}
		return sb;
	}

	@Override
	public List<T> find(Map<String, Object> searchParameter) {
		return find(searchParameter, null);
	}

	@Override
	public List<T> find(Map<String, Object> searchParameter, Map<String, Object> sortParameter) {
		return (List<T>) find(Integer.MAX_VALUE, 1, searchParameter, sortParameter).getContent();
	}

	private PageResultData<?> convertPageResultData(List<?> list,Pageable pageable,Long total){
		PageResultData<T> pageResultData = new PageResultData(list,pageable,total);
		return pageResultData;
	}

	private PageResultData<?> find(Integer pageSize, Integer pageNumber, Map<String, Object> searchParameter,
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
		return convertPageResultData(page.getContent(),pageable,page.getTotalElements());
	}

}
