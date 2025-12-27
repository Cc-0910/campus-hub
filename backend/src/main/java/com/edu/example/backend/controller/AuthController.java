package com.edu.example.backend.controller;

import cn.hutool.core.util.IdUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.edu.example.backend.common.Result;
import com.edu.example.backend.dto.LoginDTO;
import com.edu.example.backend.dto.RegisterDTO;
import com.edu.example.backend.entity.User;
import com.edu.example.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<Map<String, Object>> register(@Valid @RequestBody RegisterDTO registerDTO, BindingResult bindingResult) {
        // 参数验证
        if (bindingResult.hasErrors()) {
            return Result.error(400, bindingResult.getFieldError().getDefaultMessage());
        }
        
        // 密码长度验证
        if (registerDTO.getPassword().length() < 6) {
            return Result.error(400, "密码长度至少6位");
        }
        
        boolean success = userService.register(
                registerDTO.getUsername(),
                registerDTO.getPassword(),
                registerDTO.getEmail()
        );
        
        if (success) {
            // 注册成功后自动登录，返回用户信息
            User user = userService.login(registerDTO.getUsername(), registerDTO.getPassword());
            
            if (user != null) {
                // 生成token
                String token = IdUtil.simpleUUID();
                
                // 返回用户信息 - 符合CSUer Hub公约
                Map<String, Object> data = new HashMap<>();
                data.put("token", token);
                
                // 用户信息对象
                Map<String, Object> userInfo = new HashMap<>();
                userInfo.put("id", user.getId());
                userInfo.put("nickname", user.getNickname());
                userInfo.put("avatar", user.getAvatar());
                userInfo.put("role", "user");
                
                data.put("userInfo", userInfo);
                
                return Result.success(data);
            } else {
                return Result.error(500, "注册成功但自动登录失败");
            }
        } else {
            return Result.error(400, "注册失败，用户名可能已存在");
        }
    }

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Valid @RequestBody LoginDTO loginDTO, BindingResult bindingResult) {
        // 参数验证
        if (bindingResult.hasErrors()) {
            return Result.error(400, bindingResult.getFieldError().getDefaultMessage());
        }
        
        User user = userService.login(loginDTO.getUsername(), loginDTO.getPassword());
        
        if (user != null) {
            // 生成token
            String token = IdUtil.simpleUUID();
            
            // 返回用户信息 - 符合CSUer Hub公约
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            
            // 用户信息对象
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", user.getId());
            userInfo.put("nickname", user.getNickname());
            userInfo.put("avatar", user.getAvatar());
            userInfo.put("role", "user"); // 默认角色
            
            data.put("userInfo", userInfo);
            
            return Result.success(data);
        } else {
            return Result.error(401, "用户名或密码错误");
        }
    }
}