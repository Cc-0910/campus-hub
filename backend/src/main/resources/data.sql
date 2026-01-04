-- 创建用户表
DROP TABLE IF EXISTS campus_hub_user;
CREATE TABLE campus_hub_user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  username VARCHAR(50) NOT NULL UNIQUE,
  password VARCHAR(100) NOT NULL,
  nickname VARCHAR(50),
  avatar VARCHAR(200),
  bio VARCHAR(200),
  school VARCHAR(100),
  location VARCHAR(100),
  interests VARCHAR(200),
  create_time BIGINT NOT NULL,
  update_time BIGINT NOT NULL
);

-- 创建关注关系表
DROP TABLE IF EXISTS follow;
CREATE TABLE follow (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  follower_id BIGINT NOT NULL,
  following_id BIGINT NOT NULL,
  created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT uk_follow UNIQUE (follower_id, following_id)
);

-- 创建文章表
CREATE TABLE IF NOT EXISTS campus_hub_article (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  user_id BIGINT NOT NULL,
  title VARCHAR(200) NOT NULL,
  content TEXT NOT NULL,
  type VARCHAR(20) NOT NULL,
  create_time BIGINT NOT NULL,
  update_time BIGINT NOT NULL
);

-- 插入默认管理员用户 (使用MD5哈希 "123456")
INSERT INTO campus_hub_user (username, password, nickname, avatar, bio, school, location, interests, create_time, update_time)
VALUES ('admin', 'e99a18c428cb38d5f260853678922e03', '管理员', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', '这个人很懒，什么都没有留下', 'XX大学', '北京市', '技术、摄影', 1700000000000, 1700000000000);

-- 插入测试用户 (使用MD5哈希 "123456")
INSERT INTO campus_hub_user (username, password, nickname, avatar, bio, school, location, interests, create_time, update_time)
VALUES ('user1', 'e99a18c428cb38d5f260853678922e03', '张三', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', '今天天气真好！', 'XX大学', '上海市', '文学、运动', 1700000001000, 1700000001000);

INSERT INTO campus_hub_user (username, password, nickname, avatar, bio, school, location, interests, create_time, update_time)
VALUES ('user2', 'e99a18c428cb38d5f260853678922e03', '李四', 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png', '大学生活真美好！', 'XX大学', '广州市', '编程、游戏', 1700000002000, 1700000002000);

-- 插入测试关注关系
INSERT INTO follow (follower_id, following_id, created_at)
VALUES (1, 2, CURRENT_TIMESTAMP);

INSERT INTO follow (follower_id, following_id, created_at)
VALUES (2, 1, CURRENT_TIMESTAMP);

-- 插入测试文章
INSERT INTO campus_hub_article (user_id, title, content, type, create_time, update_time)
VALUES (1, '我的第一篇文章', '这是我的第一篇文章内容，欢迎大家阅读！', 'article', 1700000005000, 1700000005000);

INSERT INTO campus_hub_article (user_id, title, content, type, create_time, update_time)
VALUES (2, '大学生活分享', '分享一下我在大学的一些美好时光，希望对新生有帮助！', 'article', 1700000006000, 1700000006000);

INSERT INTO campus_hub_article (user_id, title, content, type, create_time, update_time)
VALUES (1, '技术学习笔记', '最近在学习Spring Boot框架，记录一些学习心得！', 'article', 1700000007000, 1700000007000);