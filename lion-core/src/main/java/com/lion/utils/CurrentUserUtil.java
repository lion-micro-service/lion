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
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;

/**
 * @description: 获取当前登陆用户
 * @author: Mr.Liu
 * @create: 2020-02-17 21:48
 */
public class CurrentUserUtil {

    private static volatile ICurrentUser iCurrentUser;

    public static Map<String,Object> getCurrentUser(){
        return getCurrentUser(true);
    }
    /**
     * 获取当前登陆用户
     * @return
     */
    public static Map<String,Object> getCurrentUser(Boolean isMustLogin){
        Map<String,Object> user = null;
        String username = null;
        if(isHttpWebRequest()){
            username = getUsername();
        }else {
            RpcContext rpcContext = RpcContext.getServiceContext();
            username = String.valueOf(rpcContext.getAttachment(DubboConstant.USERNAME));
        }
        if(StringUtils.hasText(username)) {
            user = getICurrentUser().findUserToMap(username);
        }
        if((Objects.isNull(user) || user.isEmpty() || user.size() ==0 || !user.containsKey("id")) && isMustLogin){
            AuthorizationException.throwException("登陆异常，请重新登陆");
        }
        return user;
    }

    /**
     * 获取当前用户姓名
     * @return
     */
    private static String getCurrentUserName(){
        Map<String,Object> currentUser = getCurrentUser();
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
        }else{
            Object obj = RpcContext.getServiceContext().getAttachment(DubboConstant.USERNAME);
            if (Objects.nonNull(obj)) {
                String username = String.valueOf(obj);
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
            if(Objects.nonNull(principal) && principal instanceof String){
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
        Map<String,Object> currentUser = getCurrentUser();
        if(currentUser.containsKey("id")){
            return Long.valueOf(String.valueOf(currentUser.get("id")));
        }
        return null;
    }

    /**
     * 判断是不是http请求
     * @return
     */
    private static boolean isHttpWebRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (Objects.nonNull(requestAttributes)) {
            HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();
            if (Objects.nonNull(request)) {
                return true;
            }
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
