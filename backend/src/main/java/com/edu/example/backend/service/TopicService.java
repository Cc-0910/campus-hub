package com.edu.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.edu.example.backend.entity.Topic;

import java.util.List;

public interface TopicService extends IService<Topic> {
    // 根据关键词搜索话题
    List<Topic> searchByKeyword(String keyword);
}