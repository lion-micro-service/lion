package com.lion.dubbo.filter;

import com.lion.constant.DubboConstant;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @description: dubbo传递当前登陆用户
 * @author: Mr.Liu
 * @create: 2020-02-17 19:08
 */
@Activate(group = {CommonConstants.PROVIDER},order = Integer.MIN_VALUE)
public class CurrentUserProvideFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        if(invocation.getAttachments().containsKey(DubboConstant.USERNAME)){
            String username = invocation.getAttachments().get(DubboConstant.USERNAME);
            RpcContext.getContext().set(DubboConstant.USERNAME,username);
        }
        Result result = invoker.invoke(invocation);
        RpcContext.getContext().remove(DubboConstant.USERNAME);
        return result;
    }
}