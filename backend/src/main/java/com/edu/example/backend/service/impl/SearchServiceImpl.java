package com.edu.example.backend.service.impl;

import com.edu.example.backend.entity.Article;
import com.edu.example.backend.entity.SearchResultVO;
import com.edu.example.backend.entity.Topic;
import com.edu.example.backend.mapper.ArticleMapper;
import com.edu.example.backend.mapper.TopicMapper;
import com.edu.example.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchServiceImpl implements SearchService {

    @Autowired
    private TopicMapper topicMapper;
    
    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public List<SearchResultVO> searchByKeyword(String keyword) {
        List<SearchResultVO> results = new ArrayList<>();
        
        // 搜索话题
        List<Topic> topics = topicMapper.searchByKeyword(keyword);
        for (Topic topic : topics) {
            SearchResultVO result = new SearchResultVO();
            result.setId(topic.getId());
            result.setTitle(topic.getName());
            result.setSummary(topic.getDescription());
            result.setType("topic");
            results.add(result);
        }
        
        // 搜索文章
        List<Article> articles = articleMapper.searchByKeyword(keyword);
        for (Article article : articles) {
            SearchResultVO result = new SearchResultVO();
            result.setId(article.getId());
            result.setTitle(article.getTitle());
            // 取文章内容的前100个字符作为摘要
            String content = article.getContent();
            if (content != null && content.length() > 100) {
                result.setSummary(content.substring(0, 100) + "...");
            } else {
                result.setSummary(content != null ? content : "");
            }
            result.setType("post");
            results.add(result);
        }
        
        return results;
    }
}