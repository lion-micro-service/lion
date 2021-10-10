package com.lion.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.lion.constant.ResultDataConstant;
import com.lion.core.common.enums.ResultDataState;
import com.lion.utils.BeanToMapUtil;
import io.swagger.v3.oas.annotations.media.Schema;

import lombok.Data;
import org.springframework.util.StringUtils;

import java.io.Serializable;
import java.util.Collection;
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
@Schema()
public class ResultData<T> implements Serializable, IResultData<T> {

	private static final long serialVersionUID = 981792735336739260L;

	@Schema(description = "返回消息", type="string")
	private String message = ResultDataConstant.SUCCEED_MESSAGE;

	@Schema(description = "异常消息", type="string")
	private String exceptionMessage;

	@Schema(description = "状态编码",type="integer")
	private Integer status = ResultDataState.SUCCESS.getKey();

	@Schema(description = "结果集", type="object")
	private T data;


	public IResultData<T> setData(T data) {
		this.data = data;
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
