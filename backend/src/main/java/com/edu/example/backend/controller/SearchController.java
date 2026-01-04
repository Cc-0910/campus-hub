package com.edu.example.backend.controller;

import com.edu.example.backend.service.SearchService;
import com.edu.example.backend.vo.SearchResultVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public List<SearchResultVO> search(@RequestParam String keyword) {
        return searchService.search(keyword);
    }
}