package com.edu.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 评论DTO
 */
public class CommentDTO {
    private Long id;
    private String content;             // 评论内容
    
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;   // 创建时间
    private Integer likeCount;          // 点赞数
    private Boolean isLiked;            // 当前用户是否已点赞
    
    private AuthorDTO user;                  // 评论者信息
    private AuthorDTO replyToUser;           // 回复的用户信息（楼中楼用）
    
    // 楼中楼 - 子评论列表
    private List<CommentDTO> children;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public Integer getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(Integer likeCount) {
        this.likeCount = likeCount;
    }

    public Boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(Boolean isLiked) {
        this.isLiked = isLiked;
    }

    public AuthorDTO getUser() {
        return user;
    }

    public void setUser(AuthorDTO user) {
        this.user = user;
    }

    public AuthorDTO getReplyToUser() {
        return replyToUser;
    }

    public void setReplyToUser(AuthorDTO replyToUser) {
        this.replyToUser = replyToUser;
    }

    public List<CommentDTO> getChildren() {
        return children;
    }

    public void setChildren(List<CommentDTO> children) {
        this.children = children;
    }
}