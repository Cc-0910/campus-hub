package com.edu.example.backend.vo;

import com.edu.example.backend.entity.User;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

/**
 * 用户详情视图对象
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDetailVO {
    private User user;
    private Integer articleCount;
    private Integer followingCount;
    private Integer followerCount;
}