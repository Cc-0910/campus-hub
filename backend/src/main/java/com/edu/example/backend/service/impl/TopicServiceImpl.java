package com.edu.example.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.example.backend.entity.Topic;
import com.edu.example.backend.mapper.TopicMapper;
import com.edu.example.backend.service.TopicService;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {
}