package com.edu.example.backend.controller;

import com.edu.example.backend.dto.CommentDTO;
import com.edu.example.backend.entity.Comment;
import com.edu.example.backend.service.CommentService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class CommentController {

    @Autowired
    private CommentService commentService;

    /**
     * 发布评论或回复
     */
    @PostMapping("/comment")
    public Map<String, Object> createComment(@RequestBody Comment comment) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里需要从JWT获取当前用户ID，暂时使用模拟值
            Long currentUserId = 1L; // 从JWT token中获取的实际用户ID
            
            boolean success = commentService.createComment(
                comment.getPostId(),
                currentUserId,
                comment.getContent(),
                comment.getParentId(),
                comment.getReplyToUserId()
            );
            
            if (success) {
                result.put("code", 200);
                result.put("message", "评论发布成功");
                result.put("data", null);
            } else {
                result.put("code", 500);
                result.put("message", "评论发布失败");
                result.put("data", null);
            }
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "评论发布失败：" + e.getMessage());
            result.put("data", null);
        }
        
        return result;
    }

    /**
     * 获取评论列表（带楼中楼结构）
     */
    @GetMapping("/comments/{postId}")
    public Map<String, Object> getCommentsWithReplies(@PathVariable Long postId) {
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里需要从JWT获取当前用户ID，暂时使用模拟值
            Long currentUserId = 1L; // 从JWT token中获取的实际用户ID
            
            List<CommentDTO> comments = commentService.getCommentsWithReplies(postId, currentUserId);
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", comments);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取评论失败：" + e.getMessage());
            result.put("data", null);
        }
        
        return result;
    }

    /**
     * 获取评论列表（分页）
     */
    @GetMapping("/comments")
    public Map<String, Object> getComments(
        @RequestParam Long postId,
        @RequestParam(defaultValue = "1") Integer pageNum,
        @RequestParam(defaultValue = "10") Integer pageSize) {
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 这里需要从JWT获取当前用户ID，暂时使用模拟值
            Long currentUserId = 1L; // 从JWT token中获取的实际用户ID
            
            PageInfo<CommentDTO> pageInfo = commentService.getComments(postId, pageNum, pageSize, currentUserId);
            
            Map<String, Object> data = new HashMap<>();
            data.put("list", pageInfo.getList());
            data.put("total", pageInfo.getTotal());
            data.put("pageNum", pageInfo.getPageNum());
            data.put("pageSize", pageInfo.getPageSize());
            
            result.put("code", 200);
            result.put("message", "success");
            result.put("data", data);
        } catch (Exception e) {
            result.put("code", 500);
            result.put("message", "获取评论失败：" + e.getMessage());
            result.put("data", null);
        }
        
        return result;
    }
}