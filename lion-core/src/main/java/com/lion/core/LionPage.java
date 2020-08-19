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
        this(1,30, Sort.unsorted());
    }

    public LionPage(int page, int size, Sort sort){
        super(page,size,sort);
    }

    public JpqlParameter getJpqlParameter() {
        return jpqlParameter;
    }

    public void setJpqlParameter(JpqlParameter jpqlParameter) {
        this.jpqlParameter = jpqlParameter;
    }
}
