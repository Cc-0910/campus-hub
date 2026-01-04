package com.edu.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.example.backend.entity.Article;
import com.edu.example.backend.vo.SearchResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {
    List<Article> searchByKeyword(@Param("keyword") String keyword);
    List<SearchResultVO> searchPosts(@Param("keyword") String keyword);
}