package com.edu.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.example.backend.entity.Comment;
import com.edu.example.backend.dto.CommentDTO;
import com.github.pagehelper.PageInfo;

import java.util.List;

public interface CommentService extends IService<Comment> {
    /**
     * 发布评论或回复
     */
    boolean createComment(Long postId, Long authorId, String content, Long parentId, Long replyToUserId);

    /**
     * 获取评论列表（带楼中楼结构）
     */
    List<CommentDTO> getCommentsWithReplies(Long postId, Long userId);

    /**
     * 获取评论列表（分页）
     */
    PageInfo<CommentDTO> getComments(Long postId, Integer pageNum, Integer pageSize, Long userId);
}