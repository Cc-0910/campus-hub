package com.edu.example.backend.controller;

import com.edu.example.backend.common.Result;
import com.edu.example.backend.entity.Topic;
import com.edu.example.backend.service.TopicService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/topics")
public class TopicController {

    @Autowired
    private TopicService topicService;

    @Value("${file.upload-dir:src/main/resources/static/images/topics}")
    private String uploadDir;

    /**
     * 上传话题图片
     */
    @PostMapping("/upload-image")
    public Result<String> uploadImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return Result.error(400, "文件不能为空");
            }

            // 检查文件类型
            String contentType = file.getContentType();
            if (contentType == null || !contentType.startsWith("image/")) {
                return Result.error(400, "只能上传图片文件");
            }

            // 生成唯一文件名
            String originalFilename = file.getOriginalFilename();
            String fileExtension = "";
            if (originalFilename != null && originalFilename.contains(".")) {
                fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
            }
            String filename = UUID.randomUUID().toString() + fileExtension;

            // 创建上传目录
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                directory.mkdirs();
            }

            // 保存文件
            Path filePath = Paths.get(uploadDir, filename);
            Files.copy(file.getInputStream(), filePath);

            // 返回图片访问URL
            String imageUrl = "/images/topics/" + filename;
            return Result.success(imageUrl);

        } catch (IOException e) {
            log.error("图片上传失败", e);
            return Result.error(500, "图片上传失败");
        }
    }

    /**
     * 创建话题（包含图片上传）
     */
    @PostMapping
    public Result<Topic> createTopic(@RequestBody Topic topic) {
        try {
            topic.setFollowerCount(0);
            topic.setCreateTime(LocalDateTime.now());
            topic.setUpdateTime(LocalDateTime.now());
            
            boolean success = topicService.save(topic);
            if (success) {
                return Result.success(topic);
            } else {
                return Result.error(500, "创建话题失败");
            }
        } catch (Exception e) {
            log.error("创建话题失败", e);
            return Result.error(500, "创建话题失败");
        }
    }

    /**
     * 更新话题图片
     */
    @PutMapping("/{id}/cover-image")
    public Result<Topic> updateCoverImage(@PathVariable Long id, @RequestParam String coverImage) {
        try {
            Topic topic = topicService.getById(id);
            if (topic == null) {
                return Result.error(404, "话题不存在");
            }

            topic.setCoverImage(coverImage);
            topic.setUpdateTime(LocalDateTime.now());
            
            boolean success = topicService.updateById(topic);
            if (success) {
                return Result.success(topic);
            } else {
                return Result.error(500, "更新封面图片失败");
            }
        } catch (Exception e) {
            log.error("更新封面图片失败", e);
            return Result.error(500, "更新封面图片失败");
        }
    }

    /**
     * 获取话题列表
     */
    @GetMapping
    public Result<Object> getTopics(@RequestParam(defaultValue = "1") Integer page,
                                   @RequestParam(defaultValue = "10") Integer size,
                                   @RequestParam(required = false) String sort) {
        try {
            // 这里实现分页查询逻辑
            // 暂时返回空列表，后续实现具体逻辑
            return Result.success(new Object());
        } catch (Exception e) {
            log.error("获取话题列表失败", e);
            return Result.error(500, "获取话题列表失败");
        }
    }

    /**
     * 获取话题详情
     */
    @GetMapping("/{id}")
    public Result<Topic> getTopicDetail(@PathVariable Long id) {
        try {
            Topic topic = topicService.getById(id);
            if (topic == null) {
                return Result.error(404, "话题不存在");
            }
            return Result.success(topic);
        } catch (Exception e) {
            log.error("获取话题详情失败", e);
            return Result.error(500, "获取话题详情失败");
        }
    }
}