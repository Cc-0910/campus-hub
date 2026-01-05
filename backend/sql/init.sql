-- 创建数据库（如果不存在）
CREATE DATABASE IF NOT EXISTS campus_hub CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 选择数据库
USE campus_hub;

-- 创建 sys_user 表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    nickname VARCHAR(50) COMMENT '昵称',
    avatar VARCHAR(255) COMMENT '头像URL',
    email VARCHAR(100) COMMENT '邮箱',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    INDEX idx_username (username)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 创建 sys_topic 表
CREATE TABLE IF NOT EXISTS sys_topic (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '话题ID',
    name VARCHAR(100) NOT NULL COMMENT '话题名称',
    description TEXT COMMENT '话题描述',
    cover_image VARCHAR(255) COMMENT '话题封面图',
    follower_count INT DEFAULT 0 COMMENT '关注数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_name (name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='话题表';

-- 创建 sys_post 表
CREATE TABLE IF NOT EXISTS sys_post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '帖子ID',
    type VARCHAR(20) NOT NULL COMMENT '类型: question(问答) | article(文章)',
    title VARCHAR(255) NOT NULL COMMENT '标题',
    content TEXT COMMENT '内容',
    summary VARCHAR(500) COMMENT '摘要',
    author_id BIGINT NOT NULL COMMENT '作者ID',
    topic_id BIGINT COMMENT '话题ID',
    view_count INT DEFAULT 0 COMMENT '浏览量',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_author_id (author_id),
    INDEX idx_topic_id (topic_id),
    INDEX idx_type (type),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='帖子表';

-- 创建 sys_comment 表
CREATE TABLE IF NOT EXISTS sys_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    post_id BIGINT NOT NULL COMMENT '帖子ID',
    parent_id BIGINT COMMENT '父评论ID',
    content TEXT NOT NULL COMMENT '评论内容',
    author_id BIGINT NOT NULL COMMENT '作者ID',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_post_id (post_id),
    INDEX idx_author_id (author_id),
    INDEX idx_parent_id (parent_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- 插入默认管理员账号 (密码是 123456 的MD5加密值: e10adc3949ba59abbe56e057f20f883e)
INSERT INTO sys_user (username, password, nickname, email, avatar) 
VALUES ('admin', 'e10adc3949ba59abbe56e057f20f883e', '超级管理员', 'admin@csu.edu.cn', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png');

-- 插入测试用户
INSERT INTO sys_user (username, password, nickname, email, avatar) 
VALUES 
('user1', 'e10adc3949ba59abbe56e057f20f883e', '张三', 'zhangsan@csu.edu.cn', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'),
('user2', 'e10adc3949ba59abbe56e057f20f883e', '李四', 'lisi@csu.edu.cn', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'),
('user3', 'e10adc3949ba59abbe56e057f20f883e', '王五', 'wangwu@csu.edu.cn', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png');

-- 插入测试话题
INSERT INTO sys_topic (name, description) 
VALUES 
('前端开发', '关于前端开发技术讨论'),
('后端开发', '关于后端开发技术讨论'),
('校园生活', '校园生活相关话题');

-- 插入测试帖子
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count) 
VALUES 
('question', '如何在Vue 3项目中实现国际化？', '<p>我在开发一个Vue 3项目，需要支持多语言功能。请问如何在Vue 3中实现国际化？有没有推荐的库或最佳实践？</p>', 'Vue 3国际化实现方法', 2, 1, 189, 3, 15),
('article', 'Vue 3 Composition API 最佳实践', '<p>Vue 3 的 Composition API 为我们提供了一种更灵活、更强大的方式来组织组件逻辑...</p>', 'Vue 3 Composition API使用指南', 3, 1, 245, 2, 25),
('question', 'Spring Boot 如何配置多数据源？', '<p>在项目中需要连接多个数据库，请问如何在Spring Boot中配置多数据源？</p>', 'Spring Boot多数据源配置', 4, 2, 156, 1, 8);

-- 插入测试评论
INSERT INTO sys_comment (post_id, author_id, parent_id, content, like_count) 
VALUES 
(1, 3, NULL, '可以使用vue-i18n库来实现国际化，配置相对简单。', 3),
(1, 4, NULL, '除了vue-i18n，也可以考虑使用@intlify/unplugin-vue-i18n插件。', 2),
(1, 2, 1, '谢谢，我试试看这个方法。', 1),
(2, 2, NULL, '写得很详细，学到了很多新知识！', 5),
(2, 4, NULL, '感谢分享，对Composition API有了更深的理解。', 2),
(3, 2, NULL, '可以参考官方文档的多数据源配置章节。', 4);