package com.edu.example.backend.controller;

import com.edu.example.backend.common.Result;
import com.edu.example.backend.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/search")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping
    public Result<List<Map<String, Object>>> search(@RequestParam String keyword) {
        // 调用搜索服务
        var searchResults = searchService.searchByKeyword(keyword);
        
        // 转换为 Map 格式以匹配前端 API 格式
        List<Map<String, Object>> result = searchResults.stream().map(item -> Map.of(
            "id", item.getId(),
            "title", item.getTitle(),
            "summary", (Object) item.getSummary(),
            "type", item.getType()
        )).toList();
        
        return Result.success(result);
    }
}