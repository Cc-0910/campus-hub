package com.edu.example.backend.vo;

public class SearchResultVO {
    private Long id;
    private String title;
    private String summary;
    private String type; // "topic" or "post"

    public SearchResultVO() {
    }

    public SearchResultVO(Long id, String title, String summary, String type) {
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}