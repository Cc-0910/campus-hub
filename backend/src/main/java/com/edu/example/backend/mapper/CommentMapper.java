package com.edu.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.example.backend.entity.Comment;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comment> {
}