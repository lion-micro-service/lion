package com.lion.core.persistence.curd;

import com.lion.constant.SearchConstant;
import com.lion.core.IEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.*;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * @author mrliu
 *
 */
public class PredicateBuilder {

	private static Logger log = LoggerFactory.getLogger(PredicateBuilder.class);
	/**
	 * 构建查询条件
	 * @param root
	 * @param criteriaBuilder
	 * @param searchParameter
	 * @return
	 */
	public static List<Predicate> builder(final Root<?> root,
                                          final CriteriaBuilder criteriaBuilder, final Map<String, Object> searchParameter) {
		List<Predicate> predicates = new ArrayList<>();
		Iterator<Entry<String, Object>> iterator = searchParameter.entrySet().iterator();
		Entry<String, Object> entry;
		while (iterator.hasNext()) {
			entry = iterator.next();
			String key = entry.getKey();
			Object value = entry.getValue();
			if (StringUtils.hasText(key)) {
				
				String searchStr[] = key.split("_");
				if (searchStr.length < 2) {
					continue;
				}
				String searchType = searchStr[0];
				String field = searchStr[1];
				
				List<Path<?>> path = new ArrayList<Path<?>>();
				try{
					for(String str : field.split("&")) {
						path.add(root.get(str));
					}
					
				}catch (Exception e) {
					e.printStackTrace();
				}
				Predicate predicate = criteriaBuilder(path, searchType, value, criteriaBuilder);
				if (predicate != null) {
					predicates.add(predicate);
				}
			}
		}
		return predicates;
	}

	@SuppressWarnings("unchecked")
	private static Predicate criteriaBuilder(final List<Path<?>> path, final String searchType,
                                             final Object value, final CriteriaBuilder criteriaBuilder) {
		if (path == null) {
			return null;
		}
		Predicate predicate = null;
		switch (searchType.toUpperCase()) {
		case SearchConstant.EQUAL:
			if (path.get(0).getJavaType().isEnum()) {
				predicate = predicateEnum(path.get(0),path.get(0).getJavaType(),value,criteriaBuilder);
			}else{
				predicate = criteriaBuilder.equal(path.get(0).as(path.get(0).getJavaType()), value);
			}
			break;
		case SearchConstant.NOT_EQUAL:
			if (path.get(0).getJavaType().isEnum()) {
				predicate = predicateEnum(path.get(0),path.get(0).getJavaType(),value,criteriaBuilder);
			}else{
				predicate = criteriaBuilder.notEqual(path.get(0).as(path.get(0).getJavaType()), value);
			}
			break;
		case SearchConstant.IS_NULL:
			predicate = criteriaBuilder.isNull(path.get(0).as(path.get(0).getJavaType()));
			break;
		case SearchConstant.IS_NOT_NULL:
			predicate = criteriaBuilder.isNotNull(path.get(0).as(path.get(0).getJavaType()));
			break;
		case SearchConstant.NOT_IN:
			CriteriaBuilder.In<Object> notIn = predicateIn(path.get(0),path.get(0).getJavaType(),value,criteriaBuilder);
			predicate = criteriaBuilder.not(notIn);
			break;
		case SearchConstant.IN:
			predicate = predicateIn(path.get(0),path.get(0).getJavaType(),value,criteriaBuilder);
			break;
		case SearchConstant.LIKE:
			predicate = criteriaBuilder.like(path.get(0).as(String.class), "%" + value + "%");
			break;
		case SearchConstant.NOT_LIKE:
			predicate = criteriaBuilder.notLike(path.get(0).as(String.class), "%" + value + "%");
			break;
		case SearchConstant.GE:
			predicate = predicateNumber(path.get(0), searchType, value, criteriaBuilder);
			break;
		case SearchConstant.GT:
			predicate = predicateNumber(path.get(0), searchType, value, criteriaBuilder);
			break;
		case SearchConstant.LE:
			predicate = predicateNumber(path.get(0), searchType, value, criteriaBuilder);
			break;
		case SearchConstant.LT:
			predicate = predicateNumber(path.get(0), searchType, value, criteriaBuilder);
			break;
		case SearchConstant.GREATER_THAN:
			predicate = criteriaBuilder.greaterThan((Expression<Date>)path.get(0),(Date)value);
			break;
		case SearchConstant.GREATER_THAN_OR_EQUAL_TO:
			predicate = criteriaBuilder.greaterThanOrEqualTo((Expression<Date>)path.get(0),(Date)value);
			break;
		case SearchConstant.LESS_THAN:
			predicate = criteriaBuilder.lessThan((Expression<Date>)path.get(0),(Date)value);
			break;
		case SearchConstant.LESS_THAN_OR_EQUAL_TO:
			predicate = criteriaBuilder.lessThanOrEqualTo((Expression<Date>)path.get(0),(Date)value);
			break;
		case SearchConstant.OR:
			List<Predicate> or = new ArrayList<Predicate>();
			for(Path<?> obj : path) {
				or.add( criteriaBuilder.or(criteriaBuilder.equal(obj.as(obj.getJavaType()), value)));
			}
			predicate = criteriaBuilder.or(or.toArray(new Predicate[or.size()]));
			break;
		case SearchConstant.OR_LIKE:
			List<Predicate> orLike = new ArrayList<Predicate>();
			if(value==null||!StringUtils.hasText(value.toString())) {
				break;
			}
			for(Path<?> obj : path) {
				orLike.add( criteriaBuilder.or(criteriaBuilder.like(obj.as(String.class), "%" + value + "%")));
			}
			predicate = criteriaBuilder.or(orLike.toArray(new Predicate[orLike.size()]));
			break;
		case SearchConstant.IS_FALSE:
			predicate = criteriaBuilder.isFalse((Expression<Boolean>)path.get(0));
			break;
		case SearchConstant.IS_TRUE:
			predicate = criteriaBuilder.isTrue((Expression<Boolean>)path.get(0));
			break;
		default:
			break;
		}
		return predicate;
	}
	
	@SuppressWarnings("unchecked")
	private static CriteriaBuilder.In<Object> predicateIn(final Path<?> path, final Class<?> classz, final Object value, final CriteriaBuilder criteriaBuilder){
		CriteriaBuilder.In<Object> in = criteriaBuilder.in(path.as(classz));
		if (value instanceof String) {
			for (String v : String.valueOf(value).split(",")) {
				in.value(v);
			}
		} else if (value instanceof Collection) {
			for (Object v : (Collection<Object>) value) {
				in.value(v);
			}
		}else if (value instanceof Object[]) {
			for (Object v : (Object[])value) {
				in.value(v);
			}
		}
		return in;
	}
	
	private static Predicate predicateEnum(final Path<?> path, final Class<?> classz, final Object value, final CriteriaBuilder criteriaBuilder){
		Predicate predicate = null;
		if (classz.isEnum()) {
			if(value.getClass().isEnum()){
				predicate = criteriaBuilder.equal(path.as(classz),value);
			}else if(value instanceof String){
				try {
					Method method = classz.getMethod("values");
					IEnum inter[] = (IEnum[]) method.invoke(null);
					for (IEnum ienum : inter) {
						if(ienum.toString().equals(value)){
							predicate = criteriaBuilder.equal(path.as(classz),ienum.getKey());
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}else{
			predicate = criteriaBuilder.equal(path.as(classz), value);
		}
		return predicate;
	}

	@SuppressWarnings("unchecked")
	private static Predicate predicateNumber(final Path<?> path, final String searchType, final Object value,
                                             final CriteriaBuilder criteriaBuilder) {
		Predicate predicate = null;
		if (value instanceof Long) {
			path.as(Long.class);
		} else if (value instanceof Integer) {
			path.as(Integer.class);
		} else if (value instanceof Double) {
			path.as(Double.class);
		} else if (value instanceof Float) {
			path.as(Float.class);
		} else if (value instanceof BigDecimal) {
			path.as(BigDecimal.class);
		}else if (value instanceof Date) {
			path.as(Date.class);
		}
		switch (searchType.toUpperCase()) {
		case SearchConstant.GE://大于等于
			predicate = criteriaBuilder.ge((Expression<Number>) path,(Number)value);
			break;
		case SearchConstant.GT://大于
			predicate = criteriaBuilder.gt((Expression<Number>) path, (Number)value);
			break;
		case SearchConstant.LE://小于
			predicate = criteriaBuilder.le((Expression<Number>) path, (Number)value);
			break;
		case SearchConstant.LT://小于等于
			predicate = criteriaBuilder.lt((Expression<Number>) path, (Number)value);
			break;
		default:
			break;
		}
		return predicate;
	}

}
