package com.lion.core;

import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Map;

/**
 * 枚举接口
 * @author mrliu
 *
 */
public interface IEnum {

	default String code(){
		return "";
	}

	Integer getKey();

	String getName();

	String getDesc();

	@JsonValue
	Object jsonValue();

}
