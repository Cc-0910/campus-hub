-- 创建关注表（如果不存在）
CREATE TABLE IF NOT EXISTS `follow` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `follower_id` BIGINT NOT NULL COMMENT '关注者ID',
    `following_id` BIGINT NOT NULL COMMENT '被关注者ID',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '关注时间',
    INDEX `idx_follower` (`follower_id`),
    INDEX `idx_following` (`following_id`),
    UNIQUE KEY `uk_follow` (`follower_id`, `following_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='关注关系表';

-- 插入一些测试数据（可选）
-- INSERT INTO follow (follower_id, following_id) VALUES (1, 2), (2, 1);