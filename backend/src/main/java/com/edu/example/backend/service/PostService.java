package com.edu.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.example.backend.entity.Post;
import com.edu.example.backend.dto.PostItemDTO;
import com.edu.example.backend.dto.PostDetailDTO;
import com.github.pagehelper.PageInfo;

public interface PostService extends IService<Post> {
    /**
     * 发布文章/提问
     */
    boolean createPost(String type, String title, String content, Long authorId, Long topicId);

    /**
     * 获取文章/问答列表
     */
    PageInfo<PostItemDTO> getPosts(Integer pageNum, Integer pageSize, Long topicId);

    /**
     * 获取文章/问答详情
     */
    PostDetailDTO getPostDetail(Long id, Long userId);

    /**
     * 获取文章/问答列表（带模拟数据功能）
     */
    PageInfo<PostItemDTO> getPostsWithMockData(Integer pageNum, Integer pageSize, Long topicId);
    
    /**
     * 根据类型获取文章/问答列表
     */
    PageInfo<PostItemDTO> getPostsByType(String type, Integer pageNum, Integer pageSize, Long topicId);
    
    /**
     * 根据类型获取文章/问答列表（带模拟数据功能）
     */
    PageInfo<PostItemDTO> getPostsWithMockDataByType(String type, Integer pageNum, Integer pageSize, Long topicId);
}