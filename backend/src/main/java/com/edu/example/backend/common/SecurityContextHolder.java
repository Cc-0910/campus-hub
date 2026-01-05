package com.edu.example.backend.common;

import com.edu.example.backend.entity.User;

/**
 * 安全上下文持有者
 */
public class SecurityContextHolder {
    
    private static final ThreadLocal<User> CONTEXT = new ThreadLocal<>();
    
    public static void setUser(User user) {
        CONTEXT.set(user);
    }
    
    public static User getUser() {
        return CONTEXT.get();
    }
    
    public static void clear() {
        CONTEXT.remove();
    }
    
    public static Long getUserId() {
        User user = getUser();
        return user != null ? user.getId() : null;
    }
}