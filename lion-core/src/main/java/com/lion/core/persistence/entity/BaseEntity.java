package com.lion.core.persistence.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lion.core.persistence.Validator;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-01-04 10:49
 */
@MappedSuperclass
@Data

@DynamicInsert
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true,value = {"createDateTime","updateDateTime","createUserId","updateUserId"})
public abstract class BaseEntity implements Serializable {

    protected static final long serialVersionUID = -90000050L;

    @Id()
//    @GeneratedValue(generator = "snow_flake_id")
//    @GenericGenerator(name = "snow_flake_id", strategy = "com.lion.utils.id.LionIdGenerator")
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GenericGenerator(name="assigned", strategy="assigned")
    @GeneratedValue(generator="assigned")
    @Column(name = "id")
    @NotNull(message="ID不能为空",groups= {Validator.Update.class, Validator.Delete.class})
    @Schema(description = "ID")
    private Long id;

//    @Column(name = "is_delete", nullable = false,  columnDefinition = " bit(1) default b'0' comment '是否删除（逻辑删除标记）'")
//    @Convert(converter = DeleteConverter.class)
//    private Delete isDelete;

    @CreatedDate
    @Column(name = "create_date_time", updatable = false)
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime createDateTime;

    @LastModifiedDate
    @Column(name = "update_date_time", insertable = false)
    @JsonFormat(pattern = "YYYY-MM-dd HH:mm:ss")
    private LocalDateTime updateDateTime;

    @CreatedBy
    @Column(name = "create_user_id", updatable = false)
    private Long createUserId;

    @LastModifiedBy
    @Column(name = "update_user_id", insertable = false)
    private Long updateUserId;

    @Column(name = "version")
    @NotNull(message="版本号不能为空",groups= {Validator.Update.class})
    @Schema(description = "版本号（修改需要传version,新增不需要传）")
    @Version
    private Long version;
}
