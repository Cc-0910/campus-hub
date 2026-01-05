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
('question', '新校区图书馆闭馆时间', '如题，新校区图书馆晚上几点闭馆？有人知道吗？', '新校区图书馆晚上几点闭馆？', 1, 1, 5230, 12, 5),
('article', '校园生活指南', '这里是完整的校园生活指南内容，包含各种实用信息。', '校园生活指南摘要', 1, 2, 1200, 8, 15);