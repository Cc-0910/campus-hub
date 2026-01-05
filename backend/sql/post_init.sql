-- 创建 sys_post 表 (文章/问答表)
CREATE TABLE sys_post (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '文章ID',
    type VARCHAR(20) NOT NULL COMMENT '类型: question(问答) | article(文章)',
    title VARCHAR(255) NOT NULL COMMENT '标题',
    content TEXT COMMENT '内容',
    summary VARCHAR(500) COMMENT '摘要',
    author_id BIGINT NOT NULL COMMENT '作者ID',
    topic_id BIGINT COMMENT '话题ID',
    view_count INT DEFAULT 0 COMMENT '浏览量',
    comment_count INT DEFAULT 0 COMMENT '评论数',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    INDEX idx_author_id (author_id),
    INDEX idx_topic_id (topic_id),
    INDEX idx_create_time (create_time),
    INDEX idx_type (type)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='文章/问答表';

-- 插入测试数据
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count) 
VALUES 
('question', '新校区图书馆几点闭馆？', '如题，新校区图书馆晚上几点闭馆？有人知道吗？', '如题，新校区图书馆晚上几点闭馆？有人知道吗？', 1, 3, 5230, 12, 5),
('question', '如何申请校园网账号？', '新生如何申请校园网账号？需要哪些材料？', '新生如何申请校园网账号？需要哪些材料？', 2, 3, 3200, 8, 3),
('question', '食堂哪个窗口的饭菜最好吃？', '求推荐食堂里最好吃的窗口和菜品！', '求推荐食堂里最好吃的窗口和菜品！', 3, 3, 4500, 15, 7),
('question', '宿舍可以自己装空调吗？', '夏天太热了，宿舍可以自己安装空调吗？', '夏天太热了，宿舍可以自己安装空调吗？', 4, 3, 2800, 6, 2),
('question', '选修课怎么选比较好？', '大一新生，选修课应该怎么选择？有什么建议？', '大一新生，选修课应该怎么选择？有什么建议？', 5, 3, 3900, 10, 4),
('article', '校园生活指南', '这里是完整的校园生活指南内容...', '这里是校园生活指南的摘要...', 1, 3, 1200, 8, 15),
('article', 'Vue.js学习路线', '详细的Vue.js学习路线和资源推荐', '详细的Vue.js学习路线和资源推荐', 2, 1, 2500, 12, 9),
('article', 'Spring Boot项目实战', 'Spring Boot项目开发实战经验分享', 'Spring Boot项目开发实战经验分享', 3, 2, 1800, 6, 5),
('article', '前端开发最佳实践', '前端开发中的最佳实践和代码规范', '前端开发中的最佳实践和代码规范', 4, 1, 2200, 9, 6),
('article', '数据库优化技巧', 'MySQL数据库性能优化技巧分享', 'MySQL数据库性能优化技巧分享', 5, 2, 1900, 7, 4);