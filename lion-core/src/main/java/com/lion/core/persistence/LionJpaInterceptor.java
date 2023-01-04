package com.lion.core.persistence;

import com.lion.core.persistence.entity.BaseEntity;
import com.lion.utils.CurrentUserUtil;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.aspectj.ConfigurableObject;

import javax.persistence.PrePersist;
import java.util.Objects;

/**
 * Create On 2022/12/30
 *
 * @author Zvoon
 */
@Configurable
public class LionJpaInterceptor implements ConfigurableObject {

    @PrePersist
    public void setTenantId(BaseEntity baseEntity) {
        Long tenantId = CurrentUserUtil.getCurrentUserTenantId(false);
        if (Objects.nonNull(tenantId) ) {
            baseEntity.setTenantId(tenantId);
        }
    }
}
