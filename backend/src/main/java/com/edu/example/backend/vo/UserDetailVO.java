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

    // 手动添加setter方法以确保编译通过
    public void setUser(User user) {
        this.user = user;
    }

    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }

    public void setFollowerCount(Integer followerCount) {
        this.followerCount = followerCount;
    }
}