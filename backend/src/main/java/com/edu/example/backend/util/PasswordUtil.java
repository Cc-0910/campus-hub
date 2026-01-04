package com.edu.example.backend.util;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static void main(String[] args) {
        // 生成密码"123456"的BCrypt哈希
        String password = "123456";
        String hash = BCrypt.hashpw(password, BCrypt.gensalt());
        System.out.println("原始密码: " + password);
        System.out.println("BCrypt哈希: " + hash);
    }
}