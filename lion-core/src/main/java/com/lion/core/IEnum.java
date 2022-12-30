package com.lion.core;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 枚举接口
 * @author mrliu
 *
 */
public interface IEnum {

	Integer getKey();

	String getName();

	String getDesc();

	@JsonValue
	Object jsonValue();

	@JsonCreator
	static IEnum instance(Object value) {
		return null;
	}

}
