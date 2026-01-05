package com.edu.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.example.backend.dto.AuthorDTO;
import com.edu.example.backend.dto.PostDetailDTO;
import com.edu.example.backend.dto.PostItemDTO;
import com.edu.example.backend.dto.TopicDTO;
import com.edu.example.backend.entity.Post;
import com.edu.example.backend.entity.Topic;
import com.edu.example.backend.entity.User;
import com.edu.example.backend.mapper.PostMapper;
import com.edu.example.backend.service.MockDataService;
import com.edu.example.backend.service.PostService;
import com.edu.example.backend.service.TopicService;
import com.edu.example.backend.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    
    // 模拟内容数组
    private static final String[] CONTENTS = {
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

    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TopicService topicService;
    
    @Autowired
    private MockDataService mockDataService;

    @Override
    public boolean createPost(String type, String title, String content, Long authorId, Long topicId) {
        try {
            Post post = new Post();
            post.setType(type);
            post.setTitle(title);
            post.setContent(content);
            
            // 生成摘要（取内容前100个字符）
            if (content != null && content.length() > 100) {
                post.setSummary(content.substring(0, 100) + "...");
            } else {
                post.setSummary(content);
            }
            
            post.setAuthorId(authorId);
            post.setTopicId(topicId);
            post.setCreateTime(LocalDateTime.now());
            post.setUpdateTime(LocalDateTime.now());
            
            return this.save(post);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public PageInfo<PostItemDTO> getPosts(Integer pageNum, Integer pageSize, Long topicId) {
        // 使用PageHelper进行分页
        Page<Post> page = new Page<>(pageNum, pageSize);
        
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0); // 未删除的
        
        // 如果指定了话题ID，则查询该话题下的内容
        if (topicId != null && topicId > 0) {
            queryWrapper.eq("topic_id", topicId);
        }
        
        queryWrapper.orderByDesc("create_time"); // 按创建时间倒序
        
        Page<Post> postPage = this.page(page, queryWrapper);
        
        // 转换为DTO
        List<PostItemDTO> postItemDTOList = new ArrayList<>();
        for (Post post : postPage.getRecords()) {
            PostItemDTO dto = new PostItemDTO();
            BeanUtils.copyProperties(post, dto);
            
            // 设置作者信息
            try {
                User author = userService.getById(post.getAuthorId());
                if (author != null) {
                    // 只保留需要的字段，符合API公约
                    AuthorDTO authorDTO = new AuthorDTO();
                    authorDTO.setId(author.getId());
                    authorDTO.setNickname(author.getNickname());
                    authorDTO.setAvatar(author.getAvatar());
                    dto.setAuthor(authorDTO);
                } else {
                    // 作者不存在，设置默认作者信息
                    AuthorDTO authorDTO = new AuthorDTO();
                    authorDTO.setId(post.getAuthorId());
                    authorDTO.setNickname("匿名用户");
                    authorDTO.setAvatar("/images/default-avatar.png");
                    dto.setAuthor(authorDTO);
                }
            } catch (Exception e) {
                // 作者信息设置失败，设置默认作者信息
                System.err.println("设置作者信息失败，作者ID: " + post.getAuthorId() + ", 错误: " + e.getMessage());
                AuthorDTO authorDTO = new AuthorDTO();
                authorDTO.setId(post.getAuthorId());
                authorDTO.setNickname("匿名用户");
                authorDTO.setAvatar("/images/default-avatar.png");
                dto.setAuthor(authorDTO);
            }
            
            // 设置话题信息
            if (post.getTopicId() != null && post.getTopicId() > 0) {
                try {
                    Topic topic = topicService.getById(post.getTopicId());
                    if (topic != null) {
                        List<TopicDTO> topics = new ArrayList<>();
                        TopicDTO topicDTO = new TopicDTO();
                        topicDTO.setId(topic.getId());
                        topicDTO.setTitle(topic.getName());
                        topics.add(topicDTO);
                        dto.setTopics(topics);
                    }
                } catch (Exception e) {
                    // 话题信息设置失败，但不影响整体查询
                    System.err.println("设置话题信息失败，话题ID: " + post.getTopicId() + ", 错误: " + e.getMessage());
                }
            }
            
            postItemDTOList.add(dto);
        }
        
        // 创建PageInfo并设置数据
        PageInfo<PostItemDTO> pageInfo = new PageInfo<>();
        pageInfo.setList(postItemDTOList);
        pageInfo.setTotal(postPage.getTotal());
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        
        return pageInfo;
    }

    @Override
    public PostDetailDTO getPostDetail(Long id, Long userId) {
        Post post = this.getById(id);
        
        // 如果找不到帖子，生成模拟数据
        if (post == null) {
            PostDetailDTO mockDetail = new PostDetailDTO();
            mockDetail.setId(id);
            
            // 根据ID的奇偶性决定是问题还是文章
            String type = (id % 2 == 0) ? "article" : "question";
            
            // 生成标题
            if (type.equals("question")) {
                mockDetail.setTitle("这是一个模拟问题标题" + id);
            } else {
                mockDetail.setTitle("这是一篇模拟文章标题" + id);
            }
            
            // 生成内容
            mockDetail.setContent(generateMockContent(type));
            
            // 设置基本信息
            mockDetail.setCreateTime(LocalDateTime.now().minusDays((int) (Math.random() * 30)));
            mockDetail.setViewCount((int) (Math.random() * 500));
            mockDetail.setLikeCount((int) (Math.random() * 100));
            
            // 设置作者信息
            AuthorDTO mockAuthor = new AuthorDTO();
            mockAuthor.setId(1L);
            mockAuthor.setNickname("模拟用户");
            mockAuthor.setAvatar("https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png");
            mockDetail.setAuthor(mockAuthor);
            
            // 设置话题信息
            List<TopicDTO> mockTopics = new ArrayList<>();
            TopicDTO mockTopic = new TopicDTO();
            mockTopic.setId(1L);
            mockTopic.setTitle("模拟话题");
            mockTopics.add(mockTopic);
            mockDetail.setTopics(mockTopics);
            
            // 设置交互状态
            mockDetail.setIsLiked(false);
            mockDetail.setIsStared(false);
            
            return mockDetail;
        }
        
        // 如果找到帖子，正常返回详情
        // 增加浏览量
        post.setViewCount(post.getViewCount() + 1);
        this.updateById(post);
        
        PostDetailDTO dto = new PostDetailDTO();
        BeanUtils.copyProperties(post, dto);
        
        // 设置作者信息
        User author = userService.getById(post.getAuthorId());
        if (author != null) {
            AuthorDTO authorDTO = new AuthorDTO();
            authorDTO.setId(author.getId());
            authorDTO.setNickname(author.getNickname());
            authorDTO.setAvatar(author.getAvatar());
            dto.setAuthor(authorDTO);
        }
        
        // 设置话题信息
        if (post.getTopicId() != null && post.getTopicId() > 0) {
            Topic topic = topicService.getById(post.getTopicId());
            if (topic != null) {
                List<TopicDTO> topics = new ArrayList<>();
                TopicDTO topicDTO = new TopicDTO();
                topicDTO.setId(topic.getId());
                topicDTO.setTitle(topic.getName());
                topics.add(topicDTO);
                dto.setTopics(topics);
            }
        }
        
        // 设置交互状态（简化处理，实际应根据用户ID查询是否点赞/收藏）
        dto.setIsLiked(false);
        dto.setIsStared(false);
        
        return dto;
    }
    
    /**
     * 生成模拟内容
     */
    private String generateMockContent(String type) {
        // 从本类定义的CONTENTS数组中随机选择内容
        Random random = new Random();
        return CONTENTS[random.nextInt(CONTENTS.length)];
    }

    @Override
    public PageInfo<PostItemDTO> getPostsWithMockData(Integer pageNum, Integer pageSize, Long topicId) {
        // 首先获取实际数据
        PageInfo<PostItemDTO> actualData = getPosts(pageNum, pageSize, topicId);
        
        // 如果实际数据不足或为空，则补充模拟数据
        List<PostItemDTO> actualList = actualData.getList() != null ? actualData.getList() : new ArrayList<>();
        List<PostItemDTO> allPosts = new ArrayList<>(actualList);
        
        // 计算需要的模拟数据数量
        int neededMockCount = pageSize - actualList.size();
        
        if (neededMockCount > 0) {
            // 根据当前查询条件决定生成哪种类型的模拟数据
            // 如果有特定话题ID，可能需要根据话题内容决定类型，这里简化为根据页码奇偶性决定
            String mockType = (pageNum % 2 == 0) ? "article" : "question";
            
            // 生成模拟数据
            List<PostItemDTO> mockPosts = mockDataService.generateMockPosts(neededMockCount, mockType);
            allPosts.addAll(mockPosts);
        }
        
        // 创建包含实际数据和模拟数据的PageInfo
        PageInfo<PostItemDTO> result = new PageInfo<>();
        result.setList(allPosts);
        long actualTotal = actualData.getTotal();
        result.setTotal(actualTotal + (allPosts.size() - actualList.size())); // 更新总数
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        return result;
    }

    @Override
    public PageInfo<PostItemDTO> getPostsByType(String type, Integer pageNum, Integer pageSize, Long topicId) {
        // 手动实现分页逻辑，避免MyBatis-Plus分页插件问题
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0); // 未删除的
        queryWrapper.eq("type", type); // 按类型过滤
        
        // 如果指定了话题ID，则查询该话题下的内容
        if (topicId != null && topicId > 0) {
            queryWrapper.eq("topic_id", topicId);
        }
        
        queryWrapper.orderByDesc("create_time"); // 按创建时间倒序
        
        // 获取总记录数
        long total = this.count(queryWrapper);
        
        // 手动分页
        int offset = (pageNum - 1) * pageSize;
        queryWrapper.last("LIMIT " + offset + ", " + pageSize);
        
        List<Post> postList = this.list(queryWrapper);
        
        // 转换为DTO
        List<PostItemDTO> postItemDTOList = new ArrayList<>();
        for (Post post : postList) {
            PostItemDTO dto = new PostItemDTO();
            BeanUtils.copyProperties(post, dto);
            
            // 设置作者信息
            User author = userService.getById(post.getAuthorId());
            if (author != null) {
                // 只保留需要的字段，符合API公约
                AuthorDTO authorDTO = new AuthorDTO();
                authorDTO.setId(author.getId());
                authorDTO.setNickname(author.getNickname());
                authorDTO.setAvatar(author.getAvatar());
                dto.setAuthor(authorDTO);
            }
            
            // 设置话题信息
            if (post.getTopicId() != null && post.getTopicId() > 0) {
                Topic topic = topicService.getById(post.getTopicId());
                if (topic != null) {
                    List<TopicDTO> topics = new ArrayList<>();
                    TopicDTO topicDTO = new TopicDTO();
                    topicDTO.setId(topic.getId());
                    topicDTO.setTitle(topic.getName());
                    topics.add(topicDTO);
                    dto.setTopics(topics);
                }
            }
            
            postItemDTOList.add(dto);
        }
        
        // 创建PageInfo并设置数据
        PageInfo<PostItemDTO> pageInfo = new PageInfo<>();
        pageInfo.setList(postItemDTOList);
        pageInfo.setTotal(total);
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        
        return pageInfo;
    }

    @Override
    public PageInfo<PostItemDTO> getPostsWithMockDataByType(String type, Integer pageNum, Integer pageSize, Long topicId) {
        // 首先获取实际数据
        PageInfo<PostItemDTO> actualData = getPostsByType(type, pageNum, pageSize, topicId);
        
        // 如果实际数据不足或为空，则补充模拟数据
        List<PostItemDTO> actualList = actualData.getList() != null ? actualData.getList() : new ArrayList<>();
        List<PostItemDTO> allPosts = new ArrayList<>(actualList);
        
        // 计算需要的模拟数据数量
        int neededMockCount = pageSize - actualList.size();
        
        if (neededMockCount > 0) {
            // 生成指定类型的模拟数据
            List<PostItemDTO> mockPosts = mockDataService.generateMockPosts(neededMockCount, type);
            allPosts.addAll(mockPosts);
        }
        
        // 创建包含实际数据和模拟数据的PageInfo
        PageInfo<PostItemDTO> result = new PageInfo<>();
        result.setList(allPosts);
        long actualTotal = actualData.getTotal();
        result.setTotal(actualTotal + (allPosts.size() - actualList.size())); // 更新总数
        result.setPageNum(pageNum);
        result.setPageSize(pageSize);
        
        return result;
    }
}