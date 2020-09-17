package com.lion.core;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.lion.constant.ResultDataConstant;
import com.lion.core.common.enums.ResultDataState;
import lombok.Data;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @description: 分页返回结果集
 * @author: Mr.Liu
 * @create: 2020-01-14 11:04
 */
@JsonIgnoreProperties(ignoreUnknown = true,value = {"content","pageable","sort","numberOfElements","empty","number","size"})
@JsonInclude(JsonInclude.Include.NON_NULL)
@Data
public class PageResultData<T> extends PageImpl<T> implements IResultData, Serializable {


    private static final long serialVersionUID = 8078379219201834984L;

    /**
     * 消息
     */
    private String message = ResultDataConstant.MESSAGE;

    /**
     * 异常信息
     */
    private String exceptionMessage;

    /**
     * 状态编码
     */
    private Integer status = ResultDataState.SUCCESS.getKey();


    public PageResultData(List content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageResultData(List content) {
        super(content);
    }

    public PageResultData(){
        super(Collections.EMPTY_LIST);
    }


    @Override
    public Map<String, Object> getData() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("list",this.getContent());
        return map;
    }

    public Integer getPageNumber(){
        return this.getNumber()+1;
    }

    public Integer getPageSize(){
        return this.getSize();
    }
}
