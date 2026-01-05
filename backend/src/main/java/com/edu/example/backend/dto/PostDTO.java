package com.edu.example.backend.dto;

import java.util.List;

/**
 * 用于创建文章/问答的DTO
 */
public class PostDTO {
    private String type;        // 类型: "question"(问答) | "article"(文章)
    private String title;       // 标题
    private String content;     // 内容
    private Long topicId;       // 话题ID
    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }
}

