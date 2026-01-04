package com.edu.example.backend.common;

import com.edu.example.backend.entity.User;

/**
 * 安全上下文工具类
 */
public class SecurityContextHolder {
    
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();
    
    public static void setUser(User user) {
        currentUser.set(user);
    }
    
    public static User getUser() {
        return currentUser.get();
    }
    
    public static void clear() {
        currentUser.remove();
    }
}