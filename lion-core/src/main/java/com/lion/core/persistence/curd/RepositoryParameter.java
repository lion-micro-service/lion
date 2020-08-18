package com.lion.core.persistence.curd;

import javax.persistence.Query;
import java.util.Map;


/**
 * 
 * @author mrliu
 *
 */
public class RepositoryParameter {
	
	/**
	 * 设置参数
	 * @param query
	 * @param para
	 */
	public static void setParameter(final Query query, final Map<String, Object> parameter) {
		if(parameter==null || query==null){
			return;
		}
		for (String key : parameter.keySet()) {
			query.setParameter(key, parameter.get(key));
		}
	}
	
	

}
