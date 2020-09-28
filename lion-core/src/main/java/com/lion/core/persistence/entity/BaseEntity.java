package com.lion.core.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.lion.core.common.enums.Delete;
import com.lion.core.common.enums.DeleteConverter;
import com.lion.core.common.enums.StateConverter;
import com.lion.core.persistence.Validator;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @description:
 * @author: Mr.Liu
 * @create: 2020-01-04 10:49
 */
@MappedSuperclass
@Data
@EntityListeners(AuditingEntityListener.class)
@JsonIgnoreProperties(ignoreUnknown = true,value = {"createDateTime","updateDateTime","createUserId","updateUserId"})
public abstract class BaseEntity implements Serializable {

    private static final long serialVersionUID = -90000050L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "snow_flake_id")
    @GenericGenerator(name = "snow_flake_id", strategy = "com.lion.utils.id.LionIdGenerator")
    @Column(name = "id")
    @NotNull(message="ID不能为空",groups= {Validator.Update.class, Validator.Delete.class})
    private Long id;

//    @Column(name = "is_delete", nullable = false,  columnDefinition = " bit(1) default b'0' comment '是否删除（逻辑删除标记）'")
//    @Convert(converter = DeleteConverter.class)
//    private Delete isDelete;

    @CreatedDate
    @Column(name = "create_date_time", updatable = false)
    private LocalDateTime createDateTime;

    @LastModifiedDate
    @Column(name = "update_date_time", insertable = false)
    private LocalDateTime updateDateTime;

    @CreatedBy
    @Column(name = "create_user_id", updatable = false)
    private Long createUserId;

    @LastModifiedBy
    @Column(name = "update_user_id", insertable = false)
    private Long updateUserId;

    @Column(name = "version",nullable = false,columnDefinition = "BIGINT(18) default 1 comment '版本号'")
    @NotNull(message="版本号不能为空",groups= {Validator.Update.class, Validator.Delete.class})
    private Long version;
}
