package com.edu.example.backend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.example.backend.entity.Topic;
import com.edu.example.backend.mapper.TopicMapper;
import com.edu.example.backend.service.TopicService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TopicServiceImpl extends ServiceImpl<TopicMapper, Topic> implements TopicService {

    @Override
    public List<Topic> searchByKeyword(String keyword) {
        // 使用MyBatis-Plus的QueryWrapper进行模糊查询
        QueryWrapper<Topic> queryWrapper = new QueryWrapper<>();
        queryWrapper.like("name", keyword)
                    .or()
                    .like("description", keyword)
                    .orderByDesc("create_time");
        
        return list(queryWrapper);
    }
}