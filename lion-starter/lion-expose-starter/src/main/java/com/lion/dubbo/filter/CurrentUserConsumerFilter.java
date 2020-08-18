package com.lion.dubbo.filter;

import com.lion.constant.DubboConstant;
import com.lion.utils.CurrentUserUtil;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

/**
 * @description: dubbo传递当前登陆用户
 * @author: Mr.Liu
 * @create: 2020-02-17 19:08
 */
@Activate(group = {CommonConstants.CONSUMER},order = Integer.MIN_VALUE)
public class CurrentUserConsumerFilter implements Filter {

    @Override
    public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
        RpcContext rpcContext = RpcContext.getContext();
        if(!invocation.getAttachments().containsKey(DubboConstant.USERNAME)){
            String username = CurrentUserUtil.getCurrentUserUsername();
            invocation.setAttachmentIfAbsent(DubboConstant.USERNAME,username);
            rpcContext.set(DubboConstant.USERNAME,username);
        }
        Result result = invoker.invoke(invocation);
        rpcContext.remove(DubboConstant.USERNAME);
        return result;
    }

}