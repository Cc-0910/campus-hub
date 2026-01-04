package com.edu.example.backend.dto;

import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDTO {
    private Long id;
    private String nickname;
    private String avatar;
    private String bio; // 个人简介
    private String school;
    private String location;
    private String interests;
    private Integer articleCount;
    private Integer fansCount;
    private Integer followingCount;
    private Boolean isFollowing;
    private Boolean isMe;
}