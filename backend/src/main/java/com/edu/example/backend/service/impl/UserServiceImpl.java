package com.edu.example.backend.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.example.backend.entity.User;
import com.edu.example.backend.mapper.UserMapper;
import com.edu.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean register(String username, String password, String email) {
        try {
            // 检查用户名是否已存在
            if (findByUsername(username) != null) {
                return false;
            }
            
            // 创建新用户
            User user = new User();
            user.setUsername(username);
            user.setPassword(DigestUtil.md5Hex(password));
            user.setEmail(email);
            user.setNickname(username); // 默认昵称为用户名
            user.setAvatar(null); // 默认头像为空
            
            int result = userMapper.insert(user);
            return result > 0;
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            System.err.println("注册失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User login(String username, String password) {
        User user = findByUsername(username);
        if (user != null && user.getPassword().equals(DigestUtil.md5Hex(password))) {
            return user;
        }
        return null;
    }
}