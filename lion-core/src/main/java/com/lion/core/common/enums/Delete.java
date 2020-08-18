package com.lion.core.common.enums;

import com.lion.core.IEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author mr.liu
 * @title: Delete
 * @description: 是否删除标识（逻辑删除）
 * @date 2020/8/14上午9:43
 */
public enum Delete implements IEnum {

    FALSE(0, "禁用"), TRUE(1, "正常");

    private final int key;

    private final String desc;

    private Delete(int key, String desc) {
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
