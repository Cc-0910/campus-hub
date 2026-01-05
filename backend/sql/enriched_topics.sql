-- 丰富话题内容，添加更多校园相关话题
-- 这些话题将补充现有的基础话题

-- 插入更多校园生活相关的话题
INSERT INTO sys_topic (name, description, cover_image, follower_count, create_time) VALUES
('校园新闻', '关注校园最新动态，了解学校重要通知和新闻', '/images/topics/校园新闻.png', 0, NOW()),
('校园生活', '分享校园生活的点点滴滴，记录美好时光', '/images/topics/校园生活.png', 0, NOW()),
('学术交流', '学术讨论、论文分享、研究心得交流', '/images/topics/学术交流.png', 0, NOW()),
('考研交流', '考研经验分享、复习计划、院校选择', '/images/topics/考研交流.png', 0, NOW()),
('求职招聘', '实习信息、招聘信息、求职经验分享', '/images/topics/求职招聘.png', 0, NOW()),
('美食分享', '食堂美食、周边探店、美食推荐', '/images/topics/美食分享.png', 0, NOW()),
('社团活动', '社团招新、活动通知、社团生活', '/images/topics/社团活动.png', 0, NOW()),
('二手交易', '二手物品交易、闲置物品转让', '/images/topics/二手交易.png', 0, NOW()),
('心理健康', '心理健康咨询、压力释放、情感交流', '/images/topics/心理健康.png', 0, NOW()),
('体育健身', '运动健身、体育赛事、健康生活', '/images/topics/体育健身.png', 0, NOW()),
('编程技术', '编程学习、技术分享、代码交流', '/images/topics/编程技术.png', 0, NOW()),
('留学申请', '留学经验、申请攻略、海外生活', '/images/topics/留学申请.png', 0, NOW());

-- 为这些话题添加一些初始帖子内容

-- 校园新闻相关帖子
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count, create_time) VALUES
('article', '新学期开学典礼圆满举行', '<p>新学期开学典礼于今日上午在学校大礼堂隆重举行，校长发表了重要讲话，鼓励同学们在新学期里努力学习，全面发展。</p>', '新学期开学典礼圆满举行，校长发表重要讲话', 2, (SELECT id FROM sys_topic WHERE name = '校园新闻' LIMIT 1), 256, 8, 32, DATE_SUB(NOW(), INTERVAL 5 DAY)),
('article', '学校图书馆新增电子资源', '<p>为满足师生学习研究需求，学校图书馆新增了多个电子数据库，包括知网、万方、IEEE等，欢迎同学们使用。</p>', '学校图书馆新增多个电子数据库资源', 3, (SELECT id FROM sys_topic WHERE name = '校园新闻' LIMIT 1), 189, 5, 18, DATE_SUB(NOW(), INTERVAL 3 DAY));

-- 校园生活相关帖子
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count, create_time) VALUES
('article', '我的大学生活初体验', '<p>作为一名大一新生，我想分享一下我的大学生活初体验。大学和高中真的很不一样，有更多的自由，但也需要更多的自律。</p><p>我参加了几个社团，认识了很多新朋友，也学到了很多新技能。虽然有时候会感到压力，但总体来说，大学生活还是很充实的。</p>', '大一新生分享大学生活初体验', 2, (SELECT id FROM sys_topic WHERE name = '校园生活' LIMIT 1), 324, 12, 45, DATE_SUB(NOW(), INTERVAL 7 DAY)),
('question', '如何平衡学习和社团活动？', '<p>我参加了几个社团，但是发现时间不够用，学习和社团活动总是冲突。想请教一下学长学姐们，如何平衡学习和社团活动？</p>', '如何平衡学习和社团活动的时间', 3, (SELECT id FROM sys_topic WHERE name = '校园生活' LIMIT 1), 198, 6, 22, DATE_SUB(NOW(), INTERVAL 4 DAY));

-- 学术交流相关帖子
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count, create_time) VALUES
('article', '如何写好学术论文', '<p>学术论文写作是大学生必须掌握的技能之一。本文将从选题、文献综述、研究方法、数据分析等方面，分享一些写作经验和技巧。</p>', '学术论文写作经验分享', 4, (SELECT id FROM sys_topic WHERE name = '学术交流' LIMIT 1), 278, 9, 38, DATE_SUB(NOW(), INTERVAL 6 DAY)),
('question', '如何查找和阅读英文文献？', '<p>老师要求我们阅读英文文献，但是我英语不太好，查找文献也很困难。请问有什么好的方法和工具推荐吗？</p>', '如何查找和阅读英文文献的方法', 2, (SELECT id FROM sys_topic WHERE name = '学术交流' LIMIT 1), 156, 4, 15, DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 考研交流相关帖子
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count, create_time) VALUES
('article', '考研复习时间规划', '<p>考研是一个长期的过程，需要合理的规划。本文分享一个详细的复习时间表，包括基础阶段、强化阶段、冲刺阶段的具体安排。</p>', '考研复习时间规划经验分享', 3, (SELECT id FROM sys_topic WHERE name = '考研交流' LIMIT 1), 445, 15, 67, DATE_SUB(NOW(), INTERVAL 8 DAY)),
('question', '考研数学应该怎么复习？', '<p>我数学基础不太好，现在开始准备考研，不知道数学应该怎么复习。有没有推荐的教材和复习方法？</p>', '考研数学复习方法和教材推荐', 4, (SELECT id FROM sys_topic WHERE name = '考研交流' LIMIT 1), 267, 8, 29, DATE_SUB(NOW(), INTERVAL 5 DAY));

-- 求职招聘相关帖子
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count, create_time) VALUES
('article', '简历制作技巧分享', '<p>一份好的简历是求职成功的第一步。本文分享一些简历制作的技巧，包括格式、内容、关键词等方面的建议。</p>', '简历制作技巧和经验分享', 2, (SELECT id FROM sys_topic WHERE name = '求职招聘' LIMIT 1), 389, 11, 52, DATE_SUB(NOW(), INTERVAL 9 DAY)),
('question', '大四学生如何找实习？', '<p>我是大四学生，想找一份实习，但是不知道从哪里开始。学校有相关的资源吗？还是需要自己投简历？</p>', '大四学生找实习的方法和渠道', 3, (SELECT id FROM sys_topic WHERE name = '求职招聘' LIMIT 1), 223, 6, 19, DATE_SUB(NOW(), INTERVAL 3 DAY));

-- 美食分享相关帖子
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count, create_time) VALUES
('article', '学校周边美食推荐', '<p>作为资深吃货，我整理了一份学校周边美食清单，包括各种价位和口味的餐厅，希望能帮助到大家。</p>', '学校周边美食推荐清单', 4, (SELECT id FROM sys_topic WHERE name = '美食分享' LIMIT 1), 512, 18, 89, DATE_SUB(NOW(), INTERVAL 10 DAY)),
('question', '一食堂哪个窗口最好吃？', '<p>刚来学校，对食堂还不熟悉，想问问大家一食堂哪个窗口的菜最好吃？有什么推荐的吗？</p>', '一食堂美食推荐', 2, (SELECT id FROM sys_topic WHERE name = '美食分享' LIMIT 1), 178, 5, 14, DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 社团活动相关帖子
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count, create_time) VALUES
('article', '摄影社团招新啦！', '<p>摄影社团开始招新了！无论你是摄影小白还是资深玩家，都欢迎加入我们。我们会定期组织外拍活动，分享摄影技巧。</p>', '摄影社团招新通知', 3, (SELECT id FROM sys_topic WHERE name = '社团活动' LIMIT 1), 234, 7, 28, DATE_SUB(NOW(), INTERVAL 4 DAY)),
('question', '有哪些值得加入的社团？', '<p>大一新生，想参加一些社团，但是不知道有哪些社团，也不知道哪些比较有意思。求推荐！</p>', '值得加入的社团推荐', 4, (SELECT id FROM sys_topic WHERE name = '社团活动' LIMIT 1), 167, 4, 12, DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 二手交易相关帖子
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count, create_time) VALUES
('article', '毕业季二手物品交易指南', '<p>毕业季到了，很多同学需要处理闲置物品。本文分享一些二手交易的注意事项和技巧，帮助大家更好地进行交易。</p>', '毕业季二手物品交易指南', 2, (SELECT id FROM sys_topic WHERE name = '二手交易' LIMIT 1), 298, 9, 35, DATE_SUB(NOW(), INTERVAL 6 DAY)),
('question', '想买一辆二手自行车，有什么建议？', '<p>想买一辆二手自行车代步，但是不知道应该注意什么，价格大概多少合适？有没有推荐的渠道？</p>', '购买二手自行车的建议', 3, (SELECT id FROM sys_topic WHERE name = '二手交易' LIMIT 1), 145, 3, 8, DATE_SUB(NOW(), INTERVAL 1 DAY));

-- 心理健康相关帖子
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count, create_time) VALUES
('article', '如何应对考试焦虑', '<p>考试焦虑是很多同学都会遇到的问题。本文分享一些应对考试焦虑的方法，包括心理调节、时间管理、复习策略等。</p>', '如何应对考试焦虑的方法', 4, (SELECT id FROM sys_topic WHERE name = '心理健康' LIMIT 1), 356, 12, 48, DATE_SUB(NOW(), INTERVAL 7 DAY)),
('question', '最近压力很大，感觉很焦虑，怎么办？', '<p>最近学习压力很大，总是感觉很焦虑，晚上也睡不好。想问问大家有没有什么好的缓解方法？</p>', '如何缓解学习压力和焦虑', 2, (SELECT id FROM sys_topic WHERE name = '心理健康' LIMIT 1), 189, 6, 21, DATE_SUB(NOW(), INTERVAL 3 DAY));

-- 体育健身相关帖子
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count, create_time) VALUES
('article', '校园跑步路线推荐', '<p>跑步是很好的运动方式，本文推荐几条校园内的跑步路线，包括距离、难度、风景等方面的介绍。</p>', '校园跑步路线推荐', 3, (SELECT id FROM sys_topic WHERE name = '体育健身' LIMIT 1), 267, 8, 31, DATE_SUB(NOW(), INTERVAL 5 DAY)),
('question', '学校健身房什么时候开放？', '<p>想健身，但是不知道学校健身房什么时候开放，需要办卡吗？费用是多少？</p>', '学校健身房开放时间和费用', 4, (SELECT id FROM sys_topic WHERE name = '体育健身' LIMIT 1), 134, 3, 9, DATE_SUB(NOW(), INTERVAL 2 DAY));

-- 编程技术相关帖子
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count, create_time) VALUES
('article', 'Python学习路线推荐', '<p>Python是一门很实用的编程语言，本文分享一个Python学习路线，从基础语法到项目实战，适合初学者。</p>', 'Python学习路线推荐', 2, (SELECT id FROM sys_topic WHERE name = '编程技术' LIMIT 1), 423, 14, 58, DATE_SUB(NOW(), INTERVAL 8 DAY)),
('question', '如何学习前端开发？', '<p>对前端开发很感兴趣，但是不知道从哪里开始。需要学习哪些技术？有什么好的学习资源推荐吗？</p>', '前端开发学习方法和资源推荐', 3, (SELECT id FROM sys_topic WHERE name = '编程技术' LIMIT 1), 278, 9, 36, DATE_SUB(NOW(), INTERVAL 4 DAY));

-- 留学申请相关帖子
INSERT INTO sys_post (type, title, content, summary, author_id, topic_id, view_count, comment_count, like_count, create_time) VALUES
('article', '留学申请时间规划', '<p>留学申请是一个复杂的过程，需要提前规划。本文分享一个详细的申请时间表，包括语言考试、材料准备、申请提交等各个环节。</p>', '留学申请时间规划经验分享', 4, (SELECT id FROM sys_topic WHERE name = '留学申请' LIMIT 1), 312, 10, 42, DATE_SUB(NOW(), INTERVAL 6 DAY)),
('question', '托福和雅思应该考哪个？', '<p>准备出国留学，但是不知道应该考托福还是雅思。这两个考试有什么区别？哪个更容易？</p>', '托福和雅思的选择建议', 2, (SELECT id FROM sys_topic WHERE name = '留学申请' LIMIT 1), 201, 5, 17, DATE_SUB(NOW(), INTERVAL 3 DAY));

