package com.edu.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.example.backend.entity.User;

public interface UserService extends IService<User> {
    User findByUsername(String username);
    
    boolean register(String username, String password, String email);
    
    User login(String username, String password);
}