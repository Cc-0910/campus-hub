package com.edu.example.backend.dto;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class ArticleDTO {
    private Long id;
    private Long userId;
    private String title;
    private String summary;
    private Integer viewCount;
    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createTime;
}