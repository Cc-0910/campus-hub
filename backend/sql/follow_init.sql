-- 创建关注关系表
CREATE TABLE IF NOT EXISTS campus_hub_follow (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  follower_id BIGINT NOT NULL COMMENT '关注者ID',
  followed_id BIGINT NOT NULL COMMENT '被关注者ID',
  create_time BIGINT NOT NULL COMMENT '关注时间戳',
  INDEX idx_follower (follower_id),
  INDEX idx_followed (followed_id),
  UNIQUE KEY uk_follow (follower_id, followed_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户关注关系表';

-- 插入示例关注关系
INSERT INTO campus_hub_follow (follower_id, followed_id, create_time) VALUES 
(1, 2, UNIX_TIMESTAMP() * 1000),
(1, 3, UNIX_TIMESTAMP() * 1000),
(2, 1, UNIX_TIMESTAMP() * 1000),
(3, 1, UNIX_TIMESTAMP() * 1000);