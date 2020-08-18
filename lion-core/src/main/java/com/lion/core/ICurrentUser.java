package com.lion.core;

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
    T findUser(String username);
}
