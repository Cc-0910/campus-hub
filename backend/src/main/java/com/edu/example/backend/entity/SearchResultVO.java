package com.edu.example.backend.entity;

import lombok.Data;

import java.util.List;

@Data
public class SearchResultVO {
    private List<com.edu.example.backend.entity.Article> articles;
    private List<com.edu.example.backend.entity.Topic> topics;
    private List<com.edu.example.backend.entity.User> users;
}