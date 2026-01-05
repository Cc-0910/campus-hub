package com.edu.example.backend.service;

import com.edu.example.backend.dto.AuthorDTO;
import com.edu.example.backend.dto.PostItemDTO;
import com.edu.example.backend.dto.TopicDTO;
import com.edu.example.backend.entity.Post;
import com.edu.example.backend.entity.User;
import com.edu.example.backend.entity.Topic;
import com.edu.example.backend.mapper.PostMapper;
import com.edu.example.backend.mapper.UserMapper;
import com.edu.example.backend.mapper.TopicMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Service
public class MockDataService {

    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private TopicMapper topicMapper;

    // 校园生活和学习相关的标题
    private static final String[] QUESTION_TITLES = {
        "如何在大学里平衡学习和社团活动？",
        "大学宿舍生活有什么需要注意的？",
        "如何选择适合自己的专业选修课？",
        "大学图书馆的座位怎么预约？",
        "如何准备期末考试更有效率？",
        "大学里如何建立良好的人际关系？",
        "如何在大学期间找到合适的实习？",
        "大学食堂的饭菜怎么样？有什么推荐？",
        "如何在大学里培养良好的学习习惯？",
        "大学社团有哪些值得参加的？"
    };
    
    private static final String[] ARTICLE_TITLES = {
        "大学生活指南：如何规划四年学习生涯",
        "校园美食探索：校内校外美食推荐",
        "学习方法分享：高效记忆技巧",
        "宿舍生活小贴士：与室友和谐相处",
        "选课攻略：如何选到心仪的课程",
        "社团活动参与指南：丰富课余生活",
        "实习经验分享：如何找到好实习",
        "考试复习策略：期末冲刺指南",
        "大学人际交往：建立有益人脉",
        "校园安全须知：保护自身安全"
    };
    
    private static final String[] SUMMARIES = {
        "大学生活是人生中重要的阶段，需要在学习、生活、社交等方面做好平衡。",
        "学习方法对于大学生来说至关重要，找到适合自己的学习方式能事半功倍。",
        "校园生活丰富多彩，参与社团活动和人际交往是大学生活的重要组成部分。",
        "合理规划时间，平衡学习与娱乐，是大学生活的关键。",
        "选课、考试、实习等都是大学期间需要面对的重要环节。",
        "宿舍生活是大学生活的重要体验，与室友和谐相处很重要。",
        "校园美食和周边环境也是大学生活体验的重要部分。",
        "大学期间培养良好的学习和生活习惯对未来职业发展很有帮助。",
        "大学是个人成长的重要阶段，需要做好学业规划和职业规划。",
        "校园安全和健康生活是大学生活的基础保障。"
    };
    
    private static final String[] CONTENTS = {
        "大学生活是人生中一个重要的转折点，它不仅是学习专业知识的地方，更是培养个人能力、建立人际关系的重要阶段。在大学里，我们需要学会独立生活，规划自己的学习和生活，同时也要积极参与各种活动，丰富自己的经历。",
        "学习是大学生活的核心，但学习方法和效率同样重要。高效的学习方法不仅能帮助我们更好地掌握知识，还能节省时间用于其他活动。建议制定学习计划，合理分配时间，找到适合自己的学习方法。",
        "校园生活是大学体验的重要组成部分。宿舍生活、社团活动、同学交往等都是大学生活的精彩部分。与室友和谐相处，积极参与社团活动，能够帮助我们建立良好的人际关系，丰富大学生活。",
        "时间管理是大学生活中的重要技能。面对繁重的学习任务和丰富的课外活动，合理安排时间显得尤为重要。建议制定日程表，优先完成重要任务，保持良好的作息习惯。",
        "大学期间的选课、考试、实习等环节都需要我们认真对待。选课时要考虑自己的兴趣和职业规划，考试前要制定复习计划，实习则要积极寻找机会，提升实践能力。",
        "宿舍生活是许多大学生第一次离开家庭独立生活的重要体验。与室友和谐相处需要相互尊重、沟通理解，共同维护宿舍环境，处理好生活中的小摩擦。",
        "校园及其周边往往有丰富的美食资源，品尝不同美食也是大学生活的一种体验。可以尝试不同食堂的菜品，探索周边的小吃店，与朋友一起分享美食。",
        "大学是个人成长和能力培养的关键时期。通过参与各种活动，培养独立思考能力、团队协作能力和解决问题的能力，为未来的职业发展打下基础。",
        "大学期间不仅要专注于学业，也要关注个人健康和安全。保持良好的生活习惯，注意校园安全，学会保护自己，是大学生活的基本要求。",
        "大学是连接学校与社会的桥梁，通过实习、兼职等方式接触社会，了解职场环境，为未来的职业生涯做好准备是非常重要的。"
    };
    
    private static final String[] TOPIC_NAMES = {
        "学习方法", "校园生活", "社团活动", "宿舍生活", "考试复习", "实习就业", "选课指导", "人际交往", "校园安全", "心理健康"
    };

    public List<PostItemDTO> generateMockPosts(int count, String type) {
        List<PostItemDTO> mockPosts = new ArrayList<>();
        Random random = new Random();
        
        // 确保话题存在
        ensureTopicsExist();
        
        // 获取所有用户和话题
        List<User> users = userMapper.selectList(null);
        List<Topic> topics = topicMapper.selectList(null);
        
        if (users.isEmpty()) {
            // 如果没有用户，创建一些默认用户
            createDefaultUsers();
            users = userMapper.selectList(null);
        }
        
        if (topics.isEmpty()) {
            // 如果没有话题，创建默认话题
            createDefaultTopics();
            topics = topicMapper.selectList(null);
        }
        
        for (int i = 1; i <= count; i++) {
            PostItemDTO post = new PostItemDTO();
            
            // 设置ID
            post.setId((long) (getMaxPostId() + i)); // 使用当前最大ID+1作为新ID
            
            // 设置类型（问题或文章）
            post.setType(type);
            
            // 随机选择标题
            String[] titles = type.equals("question") ? QUESTION_TITLES : ARTICLE_TITLES;
            post.setTitle(titles[random.nextInt(titles.length)]);
            
            // 设置摘要
            post.setSummary(SUMMARIES[random.nextInt(SUMMARIES.length)]);
            
            // 随机选择一个用户作为作者
            if (!users.isEmpty()) {
                User user = users.get(random.nextInt(users.size()));
                // 使用AuthorDTO作为作者信息，符合API公约
                AuthorDTO authorDTO = new AuthorDTO();
                authorDTO.setId(user.getId());
                authorDTO.setNickname(user.getNickname());
                authorDTO.setAvatar(user.getAvatar());
                post.setAuthor(authorDTO);
            }
            
            // 设置统计信息
            post.setViewCount(random.nextInt(500));
            post.setCommentCount(random.nextInt(50));
            post.setCreateTime(LocalDateTime.now().minusDays(random.nextInt(30)));
            
            // 随机选择1-3个话题
            List<Topic> selectedTopics = new ArrayList<>();
            int topicCount = 1 + random.nextInt(3);
            for (int j = 0; j < topicCount && j < topics.size(); j++) {
                selectedTopics.add(topics.get(random.nextInt(topics.size())));
            }
            post.setTopics(selectedTopics.stream()
                .map(topic -> {
                    TopicDTO topicDTO = new TopicDTO();
                    topicDTO.setId(topic.getId());
                    topicDTO.setTitle(topic.getName());
                    return topicDTO;
                })
                .collect(Collectors.toList()));
            
            mockPosts.add(post);
        }
        
        return mockPosts;
    }
    
    private Long getMaxPostId() {
        // 获取当前数据库中最大的post ID，如果没有则返回0
        List<Post> allPosts = postMapper.selectList(null);
        if (allPosts.isEmpty()) {
            return 0L;
        }
        return allPosts.stream()
                .mapToLong(Post::getId)
                .max()
                .orElse(0L);
    }
    
    private void ensureTopicsExist() {
        List<Topic> existingTopics = topicMapper.selectList(null);
        if (existingTopics.isEmpty()) {
            createDefaultTopics();
        }
    }
    
    private void createDefaultTopics() {
        for (String topicName : TOPIC_NAMES) {
            Topic topic = new Topic();
            topic.setName(topicName);
            topic.setCreateTime(LocalDateTime.now());
            topic.setUpdateTime(LocalDateTime.now());
            topicMapper.insert(topic);
        }
    }
    
    private void createDefaultUsers() {
        // 创建一些默认用户用于测试
        for (int i = 1; i <= 5; i++) {
            User user = new User();
            user.setUsername("用户" + i);
            user.setPassword("123456"); // 这里应该使用加密后的密码，但为测试目的简化
            user.setNickname("昵称" + i);
            user.setEmail("user" + i + "@example.com");
            user.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
            user.setCreateTime(LocalDateTime.now());
            userMapper.insert(user);
        }
    }
}