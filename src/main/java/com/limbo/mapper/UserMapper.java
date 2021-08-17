package com.limbo.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.limbo.modal.User;
import org.apache.ibatis.annotations.Mapper;

/**
 * 用户mapper
 *
 * @author limbo
 **/
@Mapper
public interface UserMapper extends BaseMapper<User> {
}
