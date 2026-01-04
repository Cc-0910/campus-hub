package com.edu.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("campus_hub_article")
public class Article {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String title;
    
    private String content;
    
    private Long userId;
    
    private Long topicId;
    
    private Integer viewCount;
    
    private Integer likeCount;
    
    private Integer commentCount;
    
    private Integer shareCount;
    
    private Integer collectCount;
    
    private Integer status; // 0-草稿 1-发布 2-隐藏
    
    private Long createTime;
    
    private Long updateTime;
    
    @TableField(exist = false)
    private String authorName;
    
    @TableField(exist = false)
    private String topicName;
    
    @TableField(exist = false)
    private String authorAvatar;
}