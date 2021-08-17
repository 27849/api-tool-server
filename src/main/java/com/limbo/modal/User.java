package com.limbo.modal;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

/**
 * 用户实体类
 *
 * @author limbo
 **/
@Data
@TableName("user")
@EqualsAndHashCode(callSuper = false)
@Builder(toBuilder = true)
public class User implements UserDetails {
    private static final long serialVersionUID = 2471521101672779667L;

    @TableId(type = IdType.ID_WORKER)
    private Long id;

    private String username;

    private String password;

    /**
     * 用户类型：0-普通用户、1-超级管理员
     */
    private Integer type;

    /**
     * 业务线权限，多个逗号分隔
     */
    private String permissionBiz;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
