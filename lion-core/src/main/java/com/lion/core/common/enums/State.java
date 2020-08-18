package com.lion.core.common.enums;

import com.lion.core.IEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-04-23 16:48
 **/
public enum State implements IEnum {
    DISABLE(0, "禁用"), NORMAL(1, "正常");

    private final int key;

    private final String desc;

    private State(int key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    @Override
    public Integer getKey() {
        return key;
    }

    @Override
    public String getName() {
        return this.toString();
    }

    @Override
    public String getDesc(){
        return desc;
    }

    @Override
    public Map<String, Object> jsonValue() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("key", key);
        map.put("desc", desc);
        map.put("name", getName());
        return map;
    }
}
