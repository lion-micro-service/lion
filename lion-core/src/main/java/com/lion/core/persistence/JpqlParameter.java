package com.lion.core.persistence;

import com.lion.constant.GlobalConstant;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * 构建查询参数
 * @author mrliu
 *
 */
public class JpqlParameter implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3482359007036882853L;

	private Map<String, Object> searchParameter = new HashMap<String, Object>();

	private Map<String, Object> sortParameter = new HashMap<String, Object>();

	public Map<String, Object> getSearchParameter() {
		return searchParameter;
	}

	public Map<String, Object> getSortParameter() {
		return sortParameter;
	}

	public void setSearchParameter(Map<String, Object> searchParameter) {
		this.searchParameter = searchParameter;
	}

	public void setSortParameter(Map<String, Object> sortParameter) {
		this.sortParameter = sortParameter;
	}
	
	public void setSearchParameter(String key, Object value) {
		this.searchParameter.put(key, value);
	}

	public void setSortParameter(String key, Direction value) {
		this.sortParameter.put(key, value.name());
	}
	
	
}
