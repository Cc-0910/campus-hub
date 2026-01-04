-- 创建文章表
CREATE TABLE IF NOT EXISTS `sys_article` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '文章ID',
  `user_id` bigint(20) NOT NULL COMMENT '作者ID',
  `title` varchar(255) NOT NULL COMMENT '标题',
  `content` longtext COMMENT '内容',
  `summary` varchar(500) DEFAULT NULL COMMENT '摘要',
  `view_count` int(11) DEFAULT 0 COMMENT '浏览数',
  `like_count` int(11) DEFAULT 0 COMMENT '点赞数',
  `comment_count` int(11) DEFAULT 0 COMMENT '评论数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`),
  KEY `idx_user_id` (`user_id`),
  KEY `idx_create_time` (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文章表';

-- 插入一些测试文章数据
INSERT INTO `sys_article` (`user_id`, `title`, `content`, `summary`, `view_count`, `like_count`, `comment_count`) VALUES
(1, 'Vue.js入门指南', '这是一篇关于Vue.js入门指南的文章，详细介绍了Vue.js的基础知识和核心概念...', 'Vue.js入门指南', 125, 25, 5),
(1, 'Spring Boot整合MyBatis-Plus实践', '本文主要介绍了如何在Spring Boot项目中集成MyBatis-Plus，以及如何进行基本的CRUD操作...', 'Spring Boot整合MyBatis-Plus实践', 80, 15, 3),
(2, 'Java并发编程核心概念', '本文介绍了Java并发编程的核心概念，包括线程、锁、同步等...', 'Java并发编程核心概念', 200, 50, 10),
(2, '前端优化策略总结', '本文总结了几种前端性能优化的策略，包括资源压缩、缓存策略等...', '前端优化策略总结', 150, 30, 8),
(3, 'Docker容器化应用部署', '本文介绍了如何使用Docker对应用进行容器化，以及如何部署到生产环境...', 'Docker容器化应用部署', 300, 75, 20),
(3, '设计模式在项目中的应用', '本文通过实例讲解了几种常见设计模式在实际项目中的应用...', '设计模式在项目中的应用', 100, 20, 4),
(1, 'Vue 3组合式API详解', '本文详细介绍了Vue 3中的组合式API，包括ref、reactive、computed等的使用...', 'Vue 3组合式API详解', 180, 40, 12),
(2, 'Spring Security安全框架入门', '本文介绍了Spring Security的基本概念和配置方法...', 'Spring Security安全框架入门', 220, 60, 18),
(3, '微服务架构设计原则', '本文讲解了微服务架构的设计原则和注意事项...', '微服务架构设计原则', 350, 80, 25),
(1, 'MySQL性能优化实践', '本文分享了MySQL数据库的性能优化经验和技巧...', 'MySQL性能优化实践', 160, 35, 9);