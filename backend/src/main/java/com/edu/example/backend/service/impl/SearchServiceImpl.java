package com.edu.example.backend.service.impl;

import com.edu.example.backend.mapper.ArticleMapper;
import com.edu.example.backend.mapper.TopicMapper;
import com.edu.example.backend.service.SearchService;
import com.edu.example.backend.vo.SearchResultVO;
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
    public List<SearchResultVO> search(String keyword) {
        List<SearchResultVO> results = new ArrayList<>();

        // Search topics by name
        List<SearchResultVO> topicResults = topicMapper.searchTopics(keyword);
        results.addAll(topicResults);

        // Search articles by title/content
        List<SearchResultVO> articleResults = articleMapper.searchPosts(keyword);
        results.addAll(articleResults);

        return results;
    }
}