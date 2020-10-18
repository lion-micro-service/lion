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
import org.springframework.data.domain.Pageable;
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

    /**
     * 消息
     */
    @ApiModelProperty(name = "返回消息",notes = "返回消息", dataType="string")
    private String message = ResultDataConstant.SUCCEED_MESSAGE;

    /**
     * 异常信息
     */
    @ApiModelProperty(name = "返回消息",notes = "返回消息", dataType="string")
    private String exceptionMessage;

    /**
     * 状态编码
     */
    @ApiModelProperty(name = "返回消息",notes = "返回消息", dataType="string")
    private Integer status = ResultDataState.SUCCESS.getKey();

    @ApiModelProperty(name = "结果集",notes = "结果集", dataType="object")
    private T data;

    public PageResultData(List content, Pageable pageable, long total) {
        super(content, pageable, total);
    }

    public PageResultData(List content) {
        super(content);
    }

    public PageResultData(){
        super(Collections.EMPTY_LIST);
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
