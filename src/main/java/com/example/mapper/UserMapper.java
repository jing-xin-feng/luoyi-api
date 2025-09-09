package com.example.mapper;

import com.example.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {

    @Select("SELECT * FROM sys_user WHERE username = #{username} AND status = '0'")
    User findByUsername(String username);

    @Select("SELECT * FROM sys_user WHERE id = #{id}")
    User findById(Long id);
}