package com.lion.core.controller.impl;

import com.lion.core.controller.BaseController;
import com.lion.core.persistence.entity.BaseEntity;
import com.lion.utils.CurrentUserUtil;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-01-28 10:45
 */
public abstract class BaseControllerImpl implements BaseController {

    /**
     * 获取当前登陆用户
     * @return
     */
    public Object getCurrentUser(){
        return CurrentUserUtil.getCurrentUser();
    }

}
