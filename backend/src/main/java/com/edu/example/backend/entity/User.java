package com.edu.example.backend.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.FieldFill;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("campus_hub_user")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    
    private String username;
    
    private String password;
    
    private String nickname;
    
    private String avatar;
    
    private String bio; // 个人简介
    
    private String school;
    
    private String location;
    
    private String interests;
    
    // 关注状态，用于前端显示用户是否被当前用户关注
    @TableField(exist = false)
    private Boolean isFollowing;
    
    private Long createTime;
    
    private Long updateTime;
}