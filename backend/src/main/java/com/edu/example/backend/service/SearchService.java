package com.edu.example.backend.service;

import com.edu.example.backend.entity.SearchResultVO;
import java.util.List;

public interface SearchService {
    List<SearchResultVO> searchByKeyword(String keyword);
}