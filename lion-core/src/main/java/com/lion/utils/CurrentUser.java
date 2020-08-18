package com.lion.utils;

import com.lion.constant.DubboConstant;
import com.lion.core.ICurrentUser;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

/**
 * @description: 获取当前登陆用户信息
 * @author: Mr.Liu
 * @create: 2020-02-20 20:03
 */
@Component
public class CurrentUser implements ICurrentUser {

    @DubboReference(cluster = DubboConstant.CLUSTER_FAILOVER, retries = DubboConstant.RETRIES)
    private ICurrentUser iCurrentUser;


    @Override
    public Object findUser(String username) {
        return iCurrentUser.findUser(username);
    }
}