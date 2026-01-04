package com.edu.example.backend.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.edu.example.backend.common.Result;
import com.edu.example.backend.common.JwtUtil;
import com.edu.example.backend.dto.LoginDTO;
import com.edu.example.backend.dto.RegisterDTO;
import com.edu.example.backend.entity.User;
import com.edu.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;
    
    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public Result<String> register(@RequestBody RegisterDTO registerDTO) {
        boolean success = userService.register(
                registerDTO.getUsername(),
                registerDTO.getPassword(),
                registerDTO.getEmail()
        );
        
        if (success) {
            return Result.success("注册成功");
        } else {
            return Result.error("注册失败，用户名可能已存在");
        }
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody LoginDTO loginDTO) {
        User user = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        
        if (user != null) {
            // 生成JWT token
            String token = jwtUtil.generateToken(user.getUsername(), user.getId());
            
            // 返回用户信息
            Map<String, Object> data = new HashMap<>();
            data.put("id", user.getId());
            data.put("username", user.getUsername());
            data.put("nickname", user.getNickname());
            data.put("avatar", user.getAvatar());
            data.put("token", token);
            
            return Result.success(data);
        } else {
            return Result.error("用户名或密码错误");
        }
    }
}