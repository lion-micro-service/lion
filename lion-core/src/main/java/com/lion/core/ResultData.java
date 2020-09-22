package com.lion.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lion.constant.ResultDataConstant;
import com.lion.core.common.enums.ResultDataState;
import com.lion.utils.BeanToMapUtil;
import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 返回给客户端的结果集
 *
 * @author mrliu
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(Include.NON_NULL)
@Data
public class ResultData implements Serializable, IResultData {

	/**
	 *
	 */
	private static final long serialVersionUID = 981792735336739260L;


	/**
	 * 消息
	 */
	private String message = ResultDataConstant.SUCCEED_MESSAGE;

	/**
	 * 异常信息
	 */
	private String exceptionMessage;

	/**
	 * 状态编码
	 */
	private Integer status = ResultDataState.SUCCESS.getKey();

	/**
	 * 结果集
	 */
	private Map<String, Object> data = new HashMap<String, Object>();


	public IResultData setData(Map<String, Object> data) {
		this.data.putAll(data);
		return this;
	}

	/**
	 * @param javaBean
	 */
	public IResultData setData(Object javaBean) {
		if (Objects.nonNull(javaBean)) {
			BeanToMapUtil.transBeanToMap(data, javaBean);
		}
		return this;
	}

	public IResultData setData(String key, Object object) {
		data.put(key, object);
		return this;
	}

	public IResultData setData(String key, Collection<Object> collection) {
		data.put(key, collection);
		return this;
	}

	public IResultData succeed(String message){
		if (StringUtils.hasText(message)){
			this.message = message;
		}
		return this;
	}

	public IResultData failed(String message){
		this.status = ResultDataState.ERROR.getKey();
		if (StringUtils.hasText(message)){
			this.message = message;
		}
		return this;
	}

	public IResultData failed(){
		return failed(ResultDataConstant.FAILED_MESSAGE);
	}

	public static ResultData instance(){
		return new ResultData();
	}

}
