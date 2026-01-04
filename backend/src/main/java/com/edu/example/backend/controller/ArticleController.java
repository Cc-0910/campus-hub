package com.edu.example.backend.controller;

import com.edu.example.backend.common.Result;
import com.edu.example.backend.dto.ArticleDTO;
import com.edu.example.backend.service.ArticleService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @GetMapping("/user/{userId}")
    public Result<IPage<ArticleDTO>> getUserArticles(
            @PathVariable Long userId,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long size) {
        
        try {
            Page<ArticleDTO> pageRequest = new Page<>(page, size);
            IPage<ArticleDTO> articles = articleService.getArticlesByUserId(userId, pageRequest);
            
            return Result.success(articles);
        } catch (Exception e) {
            return Result.error("获取文章列表失败：" + e.getMessage());
        }
    }

    @GetMapping("/{articleId}")
    public Result<ArticleDTO> getArticleById(@PathVariable Long articleId) {
        try {
            ArticleDTO article = articleService.getArticleById(articleId);
            if (article == null) {
                return Result.error("文章不存在");
            }
            
            return Result.success(article);
        } catch (Exception e) {
            return Result.error("获取文章详情失败：" + e.getMessage());
        }
    }
}