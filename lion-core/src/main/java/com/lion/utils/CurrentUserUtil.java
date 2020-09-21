package com.lion.utils;

import com.lion.constant.DubboConstant;
import com.lion.core.ICurrentUser;
import com.lion.exception.AuthorizationException;
import org.apache.dubbo.rpc.Invocation;
import org.apache.dubbo.rpc.RpcContext;
import org.apache.dubbo.rpc.protocol.dubbo.DecodeableRpcInvocation;
import org.springframework.boot.web.embedded.tomcat.TomcatEmbeddedWebappClassLoader;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.Objects;

/**
 * @description: 获取当前登陆用户
 * @author: Mr.Liu
 * @create: 2020-02-17 21:48
 */
public class CurrentUserUtil {

    private static volatile ICurrentUser iCurrentUser;

    public static Object getCurrentUser(){
        return getCurrentUser(true);
    }
    /**
     * 获取当前登陆用户
     * @return
     */
    public static Map<String,Object> getCurrentUser(Boolean isMustLogin){
        Map<String,Object> user = null;
        if(isHttpWebRequest()){
            String username = getUsername();
            if(StringUtils.hasText(username)) {
                user = getICurrentUser().findUserToMap(username);
            }
        }else if (isDubooRequest()){
            RpcContext rpcContext = RpcContext.getContext();
            Object username = rpcContext.get(DubboConstant.USERNAME);
            if( Objects.nonNull(username) ) {
                user = getICurrentUser().findUserToMap(String.valueOf(username));
            }
            return user;
        }
        if(Objects.isNull(user) && isMustLogin){
            AuthorizationException.throwException("登陆异常，请重新登陆");
        }
        return user;
    }

    /**
     * 获取当前用户姓名
     * @return
     */
    private static String getCurrentUserName(){
        Map<String,Object> currentUser = getCurrentUserToMap();
        if(currentUser.containsKey("name")){
            return String.valueOf(currentUser.get("name"));
        }
        return "";
    }

    /**
     * 获取当前登陆用户登陆账号
     * @return
     */
    public static String getCurrentUserUsername(){
        if(isHttpWebRequest()){
            return getUsername();
        }else if (isDubooRequest()){
            String username = String.valueOf(RpcContext.getContext().get(DubboConstant.USERNAME));
            if(StringUtils.hasText(username)){
                return username;
            }
        }
        return "";
    }

    /**
     * 获取token username
     * @return
     */
    private static String getUsername(){
        String username = null;
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(Objects.nonNull(authentication)){
            Object principal = authentication.getPrincipal();
            if(Objects.nonNull(principal)){
                username = String.valueOf(principal);
            }
        }
        return username;
    }

    /**
     * 获取当前用户ID
     * @return
     */
    public static Long getCurrentUserId(){
        Map<String,Object> currentUser = getCurrentUserToMap();
        if(currentUser.containsKey("id")){
            return Long.valueOf(String.valueOf(currentUser.get("id")));
        }
        return null;
    }

    /**
     * 获取当前用户
     * @return
     */
    private static Map<String,Object> getCurrentUserToMap(){
        Object currentUser = getCurrentUser();
        return BeanToMapUtil.transBeanToMap(currentUser);
    }

    /**
     * 判断是不是http请求
     * @return
     */
    private static boolean isHttpWebRequest(){
        Thread thread = Thread.currentThread();
        if (thread.getContextClassLoader() instanceof TomcatEmbeddedWebappClassLoader){
            return true;
        }
        return false;
    }

    /**
     * 判断是不是dubbo调用
     * @return
     */
    private static boolean isDubooRequest(){
        RpcContext rpcContext = RpcContext.getContext();
        Invocation invocation = rpcContext.getInvocation();
        if(Objects.nonNull(invocation) && invocation instanceof DecodeableRpcInvocation){
            return true;
        }
        return false;
    }

    /**
     *
     * @return
     */
    private static ICurrentUser getICurrentUser(){
        if(Objects.isNull(iCurrentUser)){
            synchronized(CurrentUserUtil.class){
                if(Objects.isNull(iCurrentUser)){
                    iCurrentUser = (ICurrentUser) SpringContextUtil.getBean("currentUser");
                }
            }
        }
        return iCurrentUser;
    }

}
