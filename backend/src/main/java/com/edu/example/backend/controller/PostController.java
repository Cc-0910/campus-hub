package com.edu.example.backend.controller;

import com.edu.example.backend.dto.PostDetailDTO;
import com.edu.example.backend.dto.PostItemDTO;
import com.edu.example.backend.dto.PostDTO;
import com.edu.example.backend.service.PostService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class PostController {

    @Autowired
    private PostService postService;

    /**
     * 发布文章/提问
     */
    @PostMapping("/post")
    public Map<String, Object> createPost(@RequestBody PostDTO postDTO) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里需要从JWT获取当前用户ID，暂时使用模拟值
            Long currentUserId = 1L; // 从JWT token中获取的实际用户ID
            
            boolean success = postService.createPost(
                postDTO.getType(), 
                postDTO.getTitle(), 
                postDTO.getContent(), 
                currentUserId, 
                postDTO.getTopicId()
            );
            
            if (success) {
                result.put("code", 200);
                result.put("message", "发布成功");
                result.put("data", null);
            } else {
                result.put("code", 500);
                result.put("message", "发布失败");
                result.put("data", null);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "发布失败：" + e.getMessage());
            result.put("data", null);
        }
        
        return result;
    }

    /**
     * 获取文章/问答列表 - 核心查询接口（带模拟数据功能）
     * 支持topicId参数：若传topicId则查特定话题内容；若不传则查公共大厅内容
     */
    @GetMapping("/posts")
    public Map<String, Object> getPosts(
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false) Long topicId,
        @RequestParam(required = false) String type,
        @RequestParam(required = false) String sort) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 使用带模拟数据的方法
            PageInfo<PostItemDTO> pageInfo = postService.getPostsWithMockData(pageNum, pageSize, topicId, type, sort);
            
            Map<String, Object> data = new HashMap<>();
            // 确保list不为null，符合API公约
            data.put("list", pageInfo.getList() != null ? pageInfo.getList() : new ArrayList<>());
            data.put("total", pageInfo.getTotal());
            data.put("pageNum", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常堆栈，便于调试
            result.put("code", 500);
            result.put("message", "获取列表失败：" + e.getMessage());
            result.put("data", null);
        }
        
        return result;
    }

    /**
     * 获取文章/问答详情
     */
    @GetMapping("/post/{id}")
    public Map<String, Object> getPostDetail(@PathVariable Long id) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里需要从JWT获取当前用户ID，暂时使用模拟值
            Long currentUserId = 1L; // 从JWT token中获取的实际用户ID
            
            PostDetailDTO postDetail = postService.getPostDetail(id, currentUserId);
            
            // 总是返回200，因为即使找不到帖子，PostServiceImpl也会生成模拟数据
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", postDetail);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取详情失败：" + e.getMessage());
            result.put("data", null);
        }
        
        return result;
    }
    
    /**
     * 根据类型获取文章/问答列表
     */
    @GetMapping("/posts/type/{type}")
    public Map<String, Object> getPostsByType(
        @PathVariable String type,
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize,
        @RequestParam(required = false) Long topicId) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 使用带模拟数据的方法
            PageInfo<PostItemDTO> pageInfo = postService.getPostsWithMockDataByType(type, pageNum, pageSize, topicId);
            
            Map<String, Object> data = new HashMap<>();
            // 确保list不为null，符合API公约
            data.put("list", pageInfo.getList() != null ? pageInfo.getList() : new ArrayList<>());
            data.put("total", pageInfo.getTotal());
            data.put("pageNum", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常堆栈，便于调试
            result.put("code", 500);
            result.put("message", "获取列表失败：" + e.getMessage());
            result.put("data", null);
        }
        
        return result;
    }
}