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
    


    @Autowired
    private PostMapper postMapper;
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private TopicService topicService;
    


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
    public PageInfo<PostItemDTO> getPosts(Integer pageNum, Integer pageSize, Long topicId, String type, String sort) {
        // 使用PageHelper进行分页
        Page<Post> page = new Page<>(pageNum, pageSize);
        
        QueryWrapper<Post> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("is_deleted", 0); // 未删除的
        
        // 如果指定了类型，则按类型过滤
        if (type != null && !type.isEmpty()) {
            queryWrapper.eq("type", type);
        }
        
        // 如果指定了话题ID，则查询该话题下的内容
        if (topicId != null && topicId > 0) {
            queryWrapper.eq("topic_id", topicId);
        }
        
        // 根据sort参数排序
        if (sort != null && sort.equals("hot")) {
            queryWrapper.orderByDesc("view_count"); // 按访问量倒序
        } else {
            queryWrapper.orderByDesc("create_time"); // 默认按创建时间倒序
        }
        
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
        
        // 如果找不到帖子，直接返回null
        if (post == null) {
            return null;
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
    public PageInfo<PostItemDTO> getPostsWithMockData(Integer pageNum, Integer pageSize, Long topicId, String type, String sort) {
        // 调用普通的getPosts方法
        PageInfo<PostItemDTO> pageInfo = getPosts(pageNum, pageSize, topicId, type, sort);
        return pageInfo;
    }

    @Override
    public PageInfo<PostItemDTO> getPostsWithMockDataByType(String type, Integer pageNum, Integer pageSize, Long topicId) {
        // 调用普通的getPostsByType方法
        PageInfo<PostItemDTO> pageInfo = getPostsByType(type, pageNum, pageSize, topicId);
        return pageInfo;
    }

}