-- 校园生活和大学学习相关的评论数据
-- 这些评论将添加到现有的帖子中

-- 注意：根据数据库表结构，评论表使用 author_id 字段

-- 校园生活相关评论
INSERT INTO sys_comment (post_id, author_id, parent_id, reply_to_user_id, content, like_count, create_time) VALUES
-- 帖子1的评论（假设是校园生活相关）
(1, 2, NULL, NULL, '图书馆确实是个好地方，我每天都会去那里自习，环境安静，学习效率很高！', 12, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(1, 3, NULL, NULL, '推荐去新校区的图书馆，那里有更多的座位和更安静的环境。', 8, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 4, (SELECT id FROM sys_comment WHERE post_id = 1 AND author_id = 2 LIMIT 1), 2, '同意！我也经常去，特别是考试周的时候。', 5, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(1, 2, (SELECT id FROM sys_comment WHERE post_id = 1 AND author_id = 3 LIMIT 1), 3, '新校区确实不错，就是离宿舍有点远。', 3, DATE_SUB(NOW(), INTERVAL 12 HOUR)),

-- 帖子2的评论（假设是学习相关）
(2, 3, NULL, NULL, '这个方法很实用，我已经在用了，效果不错！', 15, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(2, 4, NULL, NULL, '感谢分享，对我帮助很大，特别是时间管理那部分。', 9, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(2, 2, (SELECT id FROM sys_comment WHERE post_id = 2 AND author_id = 3 LIMIT 1), 3, '不客气，能帮到大家我很开心！', 4, DATE_SUB(NOW(), INTERVAL 1 DAY)),
(2, 3, (SELECT id FROM sys_comment WHERE post_id = 2 AND author_id = 4 LIMIT 1), 4, '时间管理真的很重要，我现在每天都会做计划。', 6, DATE_SUB(NOW(), INTERVAL 18 HOUR)),

-- 帖子3的评论（假设是课程相关）
(3, 4, NULL, NULL, '这门课确实比较难，建议提前预习，多做题。', 11, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(3, 2, NULL, NULL, '老师讲得挺好的，就是作业有点多，需要合理安排时间。', 7, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(3, 3, (SELECT id FROM sys_comment WHERE post_id = 3 AND author_id = 4 LIMIT 1), 4, '是的，我也有同感，不过学完收获还是很大的。', 5, DATE_SUB(NOW(), INTERVAL 2 DAY)),
(3, 4, (SELECT id FROM sys_comment WHERE post_id = 3 AND author_id = 2 LIMIT 1), 2, '作业确实多，但是能帮助我们更好地理解知识点。', 3, DATE_SUB(NOW(), INTERVAL 1 DAY)),

-- 帖子4的评论（假设是社团活动相关）
(4, 2, NULL, NULL, '这个活动很有意思，我已经报名了，期待和大家一起参加！', 14, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(4, 3, NULL, NULL, '我也报名了，希望能认识更多志同道合的朋友。', 8, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(4, 4, (SELECT id FROM sys_comment WHERE post_id = 4 AND author_id = 2 LIMIT 1), 2, '太好了，到时候我们可以一起组队！', 6, DATE_SUB(NOW(), INTERVAL 3 DAY)),
(4, 2, (SELECT id FROM sys_comment WHERE post_id = 4 AND author_id = 3 LIMIT 1), 3, '好的，我已经加你微信了，保持联系！', 2, DATE_SUB(NOW(), INTERVAL 2 DAY)),

-- 帖子5的评论（假设是食堂美食相关）
(5, 3, NULL, NULL, '一食堂的麻辣烫确实不错，我经常去吃，价格也实惠。', 18, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(5, 4, NULL, NULL, '推荐二食堂的盖浇饭，味道很好，分量也足。', 12, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(5, 2, (SELECT id FROM sys_comment WHERE post_id = 5 AND author_id = 3 LIMIT 1), 3, '麻辣烫我也喜欢，特别是冬天的时候，暖暖的。', 9, DATE_SUB(NOW(), INTERVAL 4 DAY)),
(5, 3, (SELECT id FROM sys_comment WHERE post_id = 5 AND author_id = 4 LIMIT 1), 4, '盖浇饭确实不错，我明天去试试！', 4, DATE_SUB(NOW(), INTERVAL 3 DAY)),

-- 帖子6的评论（假设是考试相关）
(6, 4, NULL, NULL, '考试周确实压力大，建议提前开始复习，不要临时抱佛脚。', 16, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(6, 2, NULL, NULL, '同意，我一般提前一个月开始复习，这样时间比较充裕。', 10, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(6, 3, (SELECT id FROM sys_comment WHERE post_id = 6 AND author_id = 4 LIMIT 1), 4, '提前复习是个好习惯，我也要向你学习。', 7, DATE_SUB(NOW(), INTERVAL 5 DAY)),
(6, 4, (SELECT id FROM sys_comment WHERE post_id = 6 AND author_id = 2 LIMIT 1), 2, '一起加油，相信我们都能取得好成绩！', 5, DATE_SUB(NOW(), INTERVAL 4 DAY)),

-- 帖子7的评论（假设是宿舍生活相关）
(7, 2, NULL, NULL, '宿舍生活确实需要相互理解，我们宿舍关系就很好。', 13, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(7, 3, NULL, NULL, '建议制定一些宿舍公约，比如作息时间、卫生值日等。', 9, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(7, 4, (SELECT id FROM sys_comment WHERE post_id = 7 AND author_id = 2 LIMIT 1), 2, '这个建议很好，我们宿舍就是这样做的，效果不错。', 6, DATE_SUB(NOW(), INTERVAL 6 DAY)),
(7, 2, (SELECT id FROM sys_comment WHERE post_id = 7 AND author_id = 3 LIMIT 1), 3, '是的，有规则大家都会遵守，相处起来更和谐。', 4, DATE_SUB(NOW(), INTERVAL 5 DAY)),

-- 帖子8的评论（假设是选课相关）
(8, 3, NULL, NULL, '选课确实要提前准备，热门课程很快就满了。', 15, DATE_SUB(NOW(), INTERVAL 9 DAY)),
(8, 4, NULL, NULL, '建议多选几门备选课程，以防第一选择选不上。', 11, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(8, 2, (SELECT id FROM sys_comment WHERE post_id = 8 AND author_id = 3 LIMIT 1), 3, '是的，我上次就是因为没有备选，最后只能选不太喜欢的课。', 8, DATE_SUB(NOW(), INTERVAL 7 DAY)),
(8, 3, (SELECT id FROM sys_comment WHERE post_id = 8 AND author_id = 4 LIMIT 1), 4, '选课系统确实需要优化，希望学校能改进一下。', 5, DATE_SUB(NOW(), INTERVAL 6 DAY)),

-- 帖子9的评论（假设是实习相关）
(9, 4, NULL, NULL, '实习经历确实很重要，建议大二大三就开始准备。', 17, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(9, 2, NULL, NULL, '可以通过学校就业指导中心找实习机会，他们有很多资源。', 12, DATE_SUB(NOW(), INTERVAL 9 DAY)),
(9, 3, (SELECT id FROM sys_comment WHERE post_id = 9 AND author_id = 4 LIMIT 1), 4, '就业指导中心确实不错，我就是在那里找到的实习。', 9, DATE_SUB(NOW(), INTERVAL 8 DAY)),
(9, 4, (SELECT id FROM sys_comment WHERE post_id = 9 AND author_id = 2 LIMIT 1), 2, '感谢分享，我明天就去咨询一下。', 4, DATE_SUB(NOW(), INTERVAL 7 DAY)),

-- 帖子10的评论（假设是考研相关）
(10, 2, NULL, NULL, '考研确实需要提前准备，建议大三下学期就开始。', 19, DATE_SUB(NOW(), INTERVAL 11 DAY)),
(10, 3, NULL, NULL, '数学和英语要早点开始，这两门需要长期积累。', 14, DATE_SUB(NOW(), INTERVAL 10 DAY)),
(10, 4, (SELECT id FROM sys_comment WHERE post_id = 10 AND author_id = 2 LIMIT 1), 2, '同意，我数学基础不好，现在每天都要花很多时间。', 10, DATE_SUB(NOW(), INTERVAL 9 DAY)),
(10, 2, (SELECT id FROM sys_comment WHERE post_id = 10 AND author_id = 3 LIMIT 1), 3, '坚持就是胜利，一起加油！', 6, DATE_SUB(NOW(), INTERVAL 8 DAY));

-- 更新帖子的评论数
UPDATE sys_post SET comment_count = (
    SELECT COUNT(*) FROM sys_comment 
    WHERE sys_comment.post_id = sys_post.id AND sys_comment.is_deleted = 0
) WHERE id IN (1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

