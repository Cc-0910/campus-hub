-- 创建 sys_user 表
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    email VARCHAR(100) COMMENT '邮箱',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';
-- 插入默认管理员账号 (密码是 123456)
INSERT INTO sys_user (username, password, nickname, email, avatar) 
VALUES ('admin', '123456', '超级管理员', 'admin@csu.edu.cn', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png');