package com.edu.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.example.backend.entity.Article;
import com.edu.example.backend.dto.ArticleDTO;
import com.edu.example.backend.mapper.ArticleMapper;
import com.edu.example.backend.service.ArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {

    @Override
    public IPage<ArticleDTO> getArticlesByUserId(Long userId, Page<ArticleDTO> page) {
        // 查询用户文章
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", userId);
        queryWrapper.orderByDesc("create_time");
        
        IPage<Article> articlesPage = page(new Page<>(page.getCurrent(), page.getSize()), queryWrapper);
        
        // 转换为DTO
        List<ArticleDTO> articleDTOs = articlesPage.getRecords().stream()
                .map(article -> {
                    ArticleDTO articleDTO = new ArticleDTO();
                    BeanUtils.copyProperties(article, articleDTO);
                    return articleDTO;
                })
                .collect(Collectors.toList());
        
        // 创建返回页面
        IPage<ArticleDTO> resultPage = new Page<>(articlesPage.getCurrent(), articlesPage.getSize(), articlesPage.getTotal());
        resultPage.setRecords(articleDTOs);
        
        return resultPage;
    }

    @Override
    public ArticleDTO getArticleById(Long articleId) {
        Article article = getById(articleId);
        if (article == null) {
            return null;
        }
        
        ArticleDTO articleDTO = new ArticleDTO();
        BeanUtils.copyProperties(article, articleDTO);
        return articleDTO;
    }

    @Override
    public List<Article> searchByKeyword(String keyword) {
        // 使用MyBatis-Plus的QueryWrapper进行模糊查询
        QueryWrapper<Article> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("title", keyword)
                    .or()
                    .like("content", keyword)
                    .orderByDesc("create_time");
        
        return list(queryWrapper);
    }
}