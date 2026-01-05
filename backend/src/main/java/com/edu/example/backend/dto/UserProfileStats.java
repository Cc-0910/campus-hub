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
}