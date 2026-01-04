-- 创建 sys_user 表
CREATE TABLE sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    signature VARCHAR(255) COMMENT '个性签名',
    email VARCHAR(100) COMMENT '邮箱',
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间'
);

-- 创建索引
CREATE INDEX idx_username ON sys_user (username);

-- 插入默认管理员账号 (密码是 123456)
INSERT INTO sys_user (username, password, nickname, email, avatar, signature) 
VALUES ('admin', '123456', '超级管理员', 'admin@csu.edu.cn', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', '这个人很懒，什么都没有留下');
