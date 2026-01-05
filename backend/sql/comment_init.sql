-- 创建 sys_comment 表 (评论表)
CREATE TABLE sys_comment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '评论ID',
    post_id BIGINT NOT NULL COMMENT '关联的文章ID',
    user_id BIGINT NOT NULL COMMENT '评论者ID',
    parent_id BIGINT COMMENT '父评论ID（用于楼中楼回复）',
    reply_to_user_id BIGINT COMMENT '回复的用户ID',
    content TEXT NOT NULL COMMENT '评论内容',
    like_count INT DEFAULT 0 COMMENT '点赞数',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    is_deleted TINYINT DEFAULT 0 COMMENT '是否删除：0-未删除，1-已删除',
    INDEX idx_post_id (post_id),
    INDEX idx_user_id (user_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_create_time (create_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='评论表';

-- 插入测试数据
INSERT INTO sys_comment (post_id, user_id, content, like_count) 
VALUES 
(1, 2, '图书馆晚上10点闭馆哦', 2),
(1, 3, '周末会延长开放时间', 1),
(2, 2, '感谢分享，很有用', 3);