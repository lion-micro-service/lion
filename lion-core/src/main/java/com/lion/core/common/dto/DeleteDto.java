package com.lion.core.common.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @author Mr.Liu
 * @Description:
 * @date 2021/3/23上午11:37
 */
@Data
@ApiModel
public class DeleteDto {
    @ApiModelProperty("id")
    @NotNull(message = "id不能未空")
    private Long id;
}
