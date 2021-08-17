package com.limbo.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.limbo.modal.User;

import java.util.List;

/**
 * 用户相关操作
 *
 * @author limbo
 **/
public interface UserService extends IService<User> {


    /**
     * 用户名获取用户
     *
     * @param username 用户名
     * @return User
     */
    User queryByUsername(String username);

}
