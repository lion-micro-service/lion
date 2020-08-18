package com.lion.core;

import com.lion.constant.GlobalConstant;
import com.lion.core.persistence.JpqlParameter;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import javax.servlet.http.HttpServletRequest;
import java.util.Objects;

/**
 * @description: 分页查询
 * @author: Mr.Liu
 * @create: 2020-01-13 16:18
 */
public class LionPage extends PageRequest  {

    private static final long serialVersionUID = -4541509938956089563L;

    private JpqlParameter jpqlParameter;

    public LionPage() {
        this(getPage(),getSize(), Sort.unsorted());
        jpqlParameter = new JpqlParameter();
    }

    public LionPage(int page, int size, Sort sort){
        super(page,size,sort);
    }

    /**
     * 获取当前页码
     * @return
     */
    private synchronized static Integer getPage(){
        HttpServletRequest request = getHttpServletRequest();
        if (Objects.isNull(request)){
            return 0;
        }
        String pageNumber = request.getParameter(GlobalConstant.PAGE_NUMBER);
        if (NumberUtils.isDigits(pageNumber)) {
            return Integer.valueOf(pageNumber)-1;
        }
        return 0;
    }

    /**
     * 获取分页大小
     * @return
     */
    private synchronized static Integer getSize(){
        HttpServletRequest request = getHttpServletRequest();
        if (Objects.isNull(request)){
            return 30;
        }
        String pageSize = request.getParameter(GlobalConstant.PAGE_SIZE);
        if (NumberUtils.isDigits(pageSize)) {
            return Integer.valueOf(pageSize);
        }
        return 30;
    }

    private synchronized static HttpServletRequest getHttpServletRequest(){
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if(requestAttributes == null){
            return null;
        }
        HttpServletRequest request = ((ServletRequestAttributes)requestAttributes).getRequest();
        return request;
    }

    public JpqlParameter getJpqlParameter() {
        return jpqlParameter;
    }

    public void setJpqlParameter(JpqlParameter jpqlParameter) {
        this.jpqlParameter = jpqlParameter;
    }
}
