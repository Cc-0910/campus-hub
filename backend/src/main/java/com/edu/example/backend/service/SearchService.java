package com.edu.example.backend.service;

import com.edu.example.backend.vo.SearchResultVO;
import java.util.List;

public interface SearchService {
    List<SearchResultVO> search(String keyword);
}