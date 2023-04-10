package com.lion.core;

import java.util.List;
import java.util.Map;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-02-20 20:03
 */
public interface ICurrentUser<T> {

    /**
     * 根据用户登陆账号查找用户
     * @param username
     * @return
     */
    Map<String,Object> findUserToMap(String username);

    /**
     * 获取数据权限
     * @param userId
     * @return
     */
    List<Long> getDataAuthority(Long userId);
}
