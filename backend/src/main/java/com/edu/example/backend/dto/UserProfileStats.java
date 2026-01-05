package com.edu.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 用户资料统计信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileStats {
    private Integer articleCount;
    private Integer fansCount;
    private Integer followingCount;

    // 手动添加setter方法以确保编译通过
    public void setArticleCount(Integer articleCount) {
        this.articleCount = articleCount;
    }

    public void setFansCount(Integer fansCount) {
        this.fansCount = fansCount;
    }

    public void setFollowingCount(Integer followingCount) {
        this.followingCount = followingCount;
    }
}