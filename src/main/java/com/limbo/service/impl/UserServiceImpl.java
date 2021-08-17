package com.limbo.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.limbo.mapper.UserMapper;
import com.limbo.modal.User;
import com.limbo.service.UserService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 用户相关操作
 *
 * @author limbo
 **/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {


    @Override
    public User queryByUsername(String username) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUsername,username);
        return this.baseMapper.selectOne(queryWrapper);
    }
}
