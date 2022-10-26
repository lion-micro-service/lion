package com.lion.security;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.util.StringUtils;

import java.util.*;

/**
 * @description: 此类只为解决dubbo序列化问题
 * @author: Mr.Liu
 * @create: 2020-01-24 18:55
 */
public class LionUserDetails extends User {

    private static final long serialVersionUID = -883666601L;

    private Map<String,Object> extended = new HashMap<>();

    public LionUserDetails() {
        this(UUID.randomUUID().toString(),UUID.randomUUID().toString(), getGrantedAuthority());
    }

    public LionUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(StringUtils.hasText(username)?username:UUID.randomUUID().toString(), StringUtils.hasText(password)?password:UUID.randomUUID().toString(), Objects.isNull(authorities)?getGrantedAuthority():authorities);
    }

    public LionUserDetails(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    private static List<GrantedAuthority> getGrantedAuthority(){
        LionSimpleGrantedAuthority grantedAuthority = new LionSimpleGrantedAuthority();
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        list.add(grantedAuthority);
        return list;
    }

    public Map<String, Object> getExtended() {
        return extended;
    }

    public Optional<Object> getExtended(String key) {
        return (extended.containsKey(key) && Objects.nonNull(extended.get(key)))? Optional.of(extended.get(key)) : Optional.empty();
    }

    public void setExtended(Map<String, Object> extended) {
        this.extended = extended;
    }
}
