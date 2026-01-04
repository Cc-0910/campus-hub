package com.edu.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.example.backend.entity.Topic;
import com.edu.example.backend.vo.SearchResultVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.util.List;

@Mapper
public interface TopicMapper extends BaseMapper<Topic> {
    List<Topic> searchByKeyword(@Param("keyword") String keyword);
    List<SearchResultVO> searchTopics(@Param("keyword") String keyword);
}