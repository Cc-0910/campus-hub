package com.edu.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * 关注关系实体
 */
@Data
@TableName("follow")
public class Follow {
    
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private Long followerId; // 关注者ID
    
    private Long followingId; // 被关注者ID
    
    private java.sql.Timestamp createdAt; // 关注时间
}