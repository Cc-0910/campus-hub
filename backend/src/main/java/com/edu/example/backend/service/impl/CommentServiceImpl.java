package com.edu.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.example.backend.dto.AuthorDTO;
import com.edu.example.backend.dto.CommentDTO;
import com.edu.example.backend.entity.Comment;
import com.edu.example.backend.entity.User;
import com.edu.example.backend.mapper.CommentMapper;
import com.edu.example.backend.service.CommentService;
import com.edu.example.backend.service.UserService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comment> implements CommentService {

    @Autowired
    private CommentMapper commentMapper;
    
    @Autowired
    private UserService userService;

    @Override
    public boolean createComment(Long postId, Long authorId, String content, Long parentId, Long replyToUserId) {
        try {
            Comment comment = new Comment();
            comment.setPostId(postId);
            comment.setAuthorId(authorId);
            comment.setContent(content);
            comment.setParentId(parentId);
            comment.setReplyToUserId(replyToUserId);
            comment.setCreateTime(LocalDateTime.now());
            comment.setUpdateTime(LocalDateTime.now());
            
            return this.save(comment);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<CommentDTO> getCommentsWithReplies(Long postId, Long userId) {
        // 首先获取顶级评论（parentId为null或0）
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId)
                   .eq("is_deleted", 0)
                   .and(wrapper -> wrapper.isNull("parent_id").or().eq("parent_id", 0))
                   .orderByDesc("create_time");
        
        List<Comment> topComments = this.list(queryWrapper);
        
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment : topComments) {
            CommentDTO commentDTO = convertToDTO(comment, userId);
            
            // 获取该评论的子评论（楼中楼）
            List<CommentDTO> children = getChildrenComments(comment.getId(), userId);
            commentDTO.setChildren(children);
            
            commentDTOList.add(commentDTO);
        }
        
        return commentDTOList;
    }

    @Override
    public PageInfo<CommentDTO> getComments(Long postId, Integer pageNum, Integer pageSize, Long userId) {
        Page<Comment> page = new Page<>(pageNum, pageSize);
        
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("post_id", postId)
                   .eq("is_deleted", 0)
                   .and(wrapper -> wrapper.isNull("parent_id").or().eq("parent_id", 0))
                   .orderByDesc("create_time");
        
        Page<Comment> commentPage = this.page(page, queryWrapper);
        
        List<CommentDTO> commentDTOList = new ArrayList<>();
        for (Comment comment : commentPage.getRecords()) {
            CommentDTO commentDTO = convertToDTO(comment, userId);
            commentDTOList.add(commentDTO);
        }
        
        PageInfo<CommentDTO> pageInfo = new PageInfo<>();
        pageInfo.setList(commentDTOList);
        pageInfo.setTotal(commentPage.getTotal());
        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);
        
        return pageInfo;
    }
    
    /**
     * 获取子评论（楼中楼）
     */
    private List<CommentDTO> getChildrenComments(Long parentId, Long userId) {
        QueryWrapper<Comment> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", parentId)
                   .eq("is_deleted", 0)
                   .orderByDesc("create_time");
        
        List<Comment> children = this.list(queryWrapper);
        
        List<CommentDTO> childrenDTOList = new ArrayList<>();
        for (Comment child : children) {
            CommentDTO childDTO = convertToDTO(child, userId);
            // 子评论的子评论（三层结构，可根据需要扩展）
            childDTO.setChildren(new ArrayList<>()); 
            childrenDTOList.add(childDTO);
        }
        
        return childrenDTOList;
    }
    
    /**
     * 将评论实体转换为DTO
     */
    private CommentDTO convertToDTO(Comment comment, Long userId) {
        CommentDTO commentDTO = new CommentDTO();
        BeanUtils.copyProperties(comment, commentDTO);
        
        // 设置用户信息
        User user = userService.getById(comment.getAuthorId());
        if (user != null) {
            AuthorDTO authorDTO = new AuthorDTO();
            authorDTO.setId(user.getId());
            authorDTO.setNickname(user.getNickname());
            authorDTO.setAvatar(user.getAvatar());
            commentDTO.setUser(authorDTO);
        } else {
            // 如果用户不存在，创建一个默认用户信息
            AuthorDTO authorDTO = new AuthorDTO();
            authorDTO.setId(comment.getAuthorId());
            authorDTO.setNickname("已删除用户");
            authorDTO.setAvatar(null);
            commentDTO.setUser(authorDTO);
        }
        
        // 设置回复用户信息
        if (comment.getReplyToUserId() != null) {
            User replyToUser = userService.getById(comment.getReplyToUserId());
            if (replyToUser != null) {
                AuthorDTO replyAuthorDTO = new AuthorDTO();
                replyAuthorDTO.setId(replyToUser.getId());
                replyAuthorDTO.setNickname(replyToUser.getNickname());
                replyAuthorDTO.setAvatar(replyToUser.getAvatar());
                commentDTO.setReplyToUser(replyAuthorDTO);
            } else {
                // 如果回复的用户不存在，创建一个默认用户信息
                AuthorDTO replyAuthorDTO = new AuthorDTO();
                replyAuthorDTO.setId(comment.getReplyToUserId());
                replyAuthorDTO.setNickname("已删除用户");
                replyAuthorDTO.setAvatar(null);
                commentDTO.setReplyToUser(replyAuthorDTO);
            }
        }
        
        // 设置是否已点赞（简化处理）
        commentDTO.setIsLiked(false);
        
        return commentDTO;
    }
}