-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS campus_hub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE campus_hub;

-- 创建用户表 - 与User实体类匹配
CREATE TABLE campus_hub_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    email VARCHAR(100) COMMENT '邮箱',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    signature VARCHAR(500) COMMENT '个人签名',
    bio VARCHAR(1000) COMMENT '个人简介',
    school VARCHAR(100) COMMENT '学校',
    location VARCHAR(100) COMMENT '所在地',
    interests VARCHAR(500) COMMENT '兴趣爱好',
    role VARCHAR(20) DEFAULT 'user' COMMENT '用户角色',
    create_time BIGINT COMMENT '创建时间戳',
    update_time BIGINT COMMENT '更新时间戳',
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 创建关注表
CREATE TABLE follow (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'ID',
    follower_id BIGINT NOT NULL COMMENT '关注者ID',
    followed_id BIGINT NOT NULL COMMENT '被关注者ID',
    create_time BIGINT COMMENT '创建时间戳',
    UNIQUE KEY uk_follow (follower_id, followed_id),
    INDEX idx_follower (follower_id),
    INDEX idx_followed (followed_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关注表';

-- 创建文章表
CREATE TABLE article (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文章ID',
    title VARCHAR(200) NOT NULL COMMENT '标题',
    content TEXT COMMENT '内容',
    author_id BIGINT NOT NULL COMMENT '作者ID',
    topic_id BIGINT COMMENT '话题ID',
    view_count INT DEFAULT 0 COMMENT '浏览次数',
    like_count INT DEFAULT 0 COMMENT '点赞次数',
    comment_count INT DEFAULT 0 COMMENT '评论次数',
    status VARCHAR(20) DEFAULT 'published' COMMENT '状态',
    create_time BIGINT COMMENT '创建时间戳',
    update_time BIGINT COMMENT '更新时间戳',
    INDEX idx_author (author_id),
    INDEX idx_topic (topic_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章表';

-- 创建话题表
CREATE TABLE topic (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '话题ID',
    name VARCHAR(100) NOT NULL UNIQUE COMMENT '话题名称',
    description VARCHAR(500) COMMENT '话题描述',
    icon VARCHAR(255) COMMENT '话题图标',
    color VARCHAR(20) COMMENT '主题色',
    article_count INT DEFAULT 0 COMMENT '文章数量',
    follow_count INT DEFAULT 0 COMMENT '关注数量',
    create_time BIGINT COMMENT '创建时间戳',
    update_time BIGINT COMMENT '更新时间戳',
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='话题表';

-- 插入默认管理员账号 (密码是 123456 的MD5加密值: e10adc3949ba59abbe56e057f20f883e)
INSERT INTO campus_hub_user (username, password, email, nickname, signature, role, create_time, update_time) 
VALUES (
    'admin', 
    'e10adc3949ba59abbe56e057f20f883e', 
    'admin@campus-hub.edu.cn', 
    '超级管理员', 
    '欢迎使用校园Hub！', 
    'admin',
    UNIX_TIMESTAMP(NOW()) * 1000,
    UNIX_TIMESTAMP(NOW()) * 1000
);

-- 插入一些默认话题
INSERT INTO topic (name, description, icon, color, create_time, update_time) VALUES
('校园生活', '分享校园生活中的点点滴滴', '/images/topics/校园生活.png', '#FF6B6B', UNIX_TIMESTAMP(NOW()) * 1000, UNIX_TIMESTAMP(NOW()) * 1000),
('学术交流', '学术讨论、论文分享、学习心得', '/images/topics/学术交流.png', '#4ECDC4', UNIX_TIMESTAMP(NOW()) * 1000, UNIX_TIMESTAMP(NOW()) * 1000),
('二手交易', '校园二手物品交易', '/images/topics/二手交易.png', '#45B7D1', UNIX_TIMESTAMP(NOW()) * 1000, UNIX_TIMESTAMP(NOW()) * 1000),
('求职招聘', '实习、兼职、全职招聘信息', '/images/topics/求职招聘.png', '#96CEB4', UNIX_TIMESTAMP(NOW()) * 1000, UNIX_TIMESTAMP(NOW()) * 1000),
('美食分享', '推荐校园美食、分享烹饪心得', '/images/topics/美食分享.png', '#FFEAA7', UNIX_TIMESTAMP(NOW()) * 1000, UNIX_TIMESTAMP(NOW()) * 1000);

-- 检查表是否创建成功
SHOW TABLES;