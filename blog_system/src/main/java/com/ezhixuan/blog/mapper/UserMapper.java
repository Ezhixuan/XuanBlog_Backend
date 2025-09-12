package com.ezhixuan.blog.mapper;

import com.ezhixuan.blog.domain.entity.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Select;

/**
* @author ezhixuan
* @description 针对表【sys_user(用户表)】的数据库操作Mapper
* @createDate 2025-09-12 17:10:08
* @Entity com.ezhixuan.blog.domain.entity.User
*/
public interface UserMapper extends BaseMapper<User> {

    @Select("select account from sys_user where id = #{userId}")
    String selectAccountById(long userId);
}




