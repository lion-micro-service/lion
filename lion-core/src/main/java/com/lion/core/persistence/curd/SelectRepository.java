package com.lion.core.persistence.curd;

import com.lion.core.LionPage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

/**
 * 为兼容其它数据库所有操作均不提供本地sql封装，均采用jpql操作！
 * @author mrliu
 *
 */
public interface SelectRepository<T> {

	/**
	 * 使用jpql语句查询数据
	 * 
	 * @param jpql
	 *            jpql查询语句
	 * @return T
	 */
	List<?> find(String jpql);

	/**
	 * 使用jpql语句查询数据返回单个对象
	 * 
	 * @param jpql
	 *            jpql查询语句
	 * @return T
	 */
	Object findOne(String jpql);

	/**
	 * 使用jpql语句查询
	 * 
	 * @param jpql
	 *            jpql语句
	 * @return T
	 */
	List<?> find(String jpql, Map<String, Object> searchParameter);

	/**
	 * 分页查询(Page中的searchParameter&sortParameter将不生效)
	 * @param pageable
	 * @param jpql
	 * @return
	 */
	Page<?> findNavigator(Pageable pageable, String jpql);

	/**
	 * 分页查询(Page中的searchParameter&sortParameter将不生效)
	 * @param jpql
	 * @param pageable
	 * @param searchParameter
	 * @return
	 */
	Page<?> findNavigator(Pageable pageable, String jpql, Map<String, Object> searchParameter);

	/**
	 * 分页查询(Page中的searchParameter&sortParameter将不生效)
	 * @param pageable
	 * @param sql
	 * @return
	 */
	Page<?> findNavigatorByNativeSql(Pageable pageable, String sql);

	/**
	 *
	 * @param pageable
	 * @param sql
	 * @param searchParameter
	 * @param returnType
	 * @return
	 */
	Page<?> findNavigatorByNativeSql(Pageable pageable, String sql, Map<String, Object> searchParameter,Class<?> returnType);

	/**
	 * 分页查询
	 * @param LionPage
	 * @return
	 */
	Page<T> findNavigator(LionPage LionPage);
	/**
	 *
	 * @param searchParameter
	 * @return
	 */
	List<T> find(Map<String, Object> searchParameter);

	/**
	 *
	 * @param searchParameter
	 * @param sortParameter
	 * @return
	 */
	List<T> find(Map<String, Object> searchParameter, Map<String, Object> sortParameter);

}
