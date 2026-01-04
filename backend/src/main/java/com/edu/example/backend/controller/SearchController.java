package com.edu.example.backend.controller;

import com.edu.example.backend.common.Result;
import com.edu.example.backend.entity.SearchResultVO;
import com.edu.example.backend.service.ArticleService;
import com.edu.example.backend.service.TopicService;
import com.edu.example.backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private UserService userService;

    @GetMapping
    public Result<SearchResultVO> search(@RequestParam String q) {
        try {
            // 获取搜索结果
            SearchResultVO vo = new SearchResultVO();
            vo.setArticles(articleService.searchByKeyword(q));
            vo.setTopics(topicService.searchByKeyword(q));
            vo.setUsers(userService.searchByKeyword(q));

            return Result.success(vo);
        } catch (Exception e) {
            return Result.error("搜索失败：" + e.getMessage());
        }
    }
}