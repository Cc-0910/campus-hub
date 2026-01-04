package com.edu.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.example.backend.dto.ArticleDTO;
import com.edu.example.backend.entity.Article;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;

import java.util.List;

public interface ArticleService extends IService<Article> {
    
    // 根据用户ID分页获取文章列表
    IPage<ArticleDTO> getArticlesByUserId(Long userId, Page<ArticleDTO> page);
    
    // 根据文章ID获取文章详情
    ArticleDTO getArticleById(Long articleId);
    
    // 根据关键词搜索文章
    List<Article> searchByKeyword(String keyword);
}