package com.edu.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.example.backend.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper extends BaseMapper<User> {
    User findByUsername(@Param("username") String username);
}