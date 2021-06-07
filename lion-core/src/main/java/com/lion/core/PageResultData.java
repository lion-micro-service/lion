package com.lion.core;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSetter;
import com.lion.constant.ResultDataConstant;
import com.lion.core.common.enums.ResultDataState;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import springfox.documentation.annotations.ApiIgnore;

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
@ApiModel()
public class PageResultData<T> extends PageImpl<T> implements IPageResultData<T>, Serializable {

    private static final long serialVersionUID = 8078379219201834984L;

    @ApiModelProperty(value = "返回消息", dataType="string")
    private String message = ResultDataConstant.SUCCEED_MESSAGE;

    @ApiModelProperty(value = "异常信息", dataType="string")
    private String exceptionMessage;

    @ApiModelProperty(value = "状态编码", dataType="integer")
    private Integer status = ResultDataState.SUCCESS.getKey();

    @ApiModelProperty(value = "结果集", dataType="object")
    private T data;

    public PageResultData(List content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageResultData(List content) {
        super(content);
    }

    public PageResultData(){
        super(Collections.EMPTY_LIST, new LionPage(),10L);
    }

    @JsonGetter
    public T getData() {
        return (T) this.getContent();
    }

    public PageResultData<T> setData(T data) {
        this.data = data;
        return this;
    }

    public Integer getPageNumber(){
        return this.getNumber()+1;
    }

    public Integer getPageSize(){
        return this.getSize();
    }


}
