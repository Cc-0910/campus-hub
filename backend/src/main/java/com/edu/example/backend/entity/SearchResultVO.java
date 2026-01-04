package com.edu.example.backend.entity;

import lombok.Data;

@Data
public class SearchResultVO {
    private Long id;
    private String title;
    private String summary;
    private String type; // "topic" 或 "post"
}