package com.edu.example.backend.controller;

import com.edu.example.backend.common.Result;
import com.edu.example.backend.common.SecurityContextHolder;
import com.edu.example.backend.entity.User;
import com.edu.example.backend.entity.Article;
import com.edu.example.backend.entity.Topic;
// import com.edu.example.backend.entity.Question;
import com.edu.example.backend.service.UserService;
import com.edu.example.backend.service.TopicService;
import com.edu.example.backend.service.PostService;
import com.edu.example.backend.dto.PostItemDTO;
// import com.edu.example.backend.service.QuestionService;
import com.edu.example.backend.vo.UserDetailVO;
import com.github.pagehelper.PageInfo;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private PostService postService;

    // 注意：由于 ArticleService、TopicService、QuestionService 可能不存在或未实现，
    // 这些方法暂时返回空结果或抛出未实现异常
    // 如需完整功能，请先实现相应的服务类

    // 获取用户详情（含粉丝数、关注数、文章数等）
    @GetMapping("/{id}")
    public Result<UserDetailVO> getUserProfile(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) return Result.error("用户不存在");

        UserDetailVO vo = new UserDetailVO();
        vo.setUser(user);

        // 使用 UserService 中的方法获取统计数据
        vo.setArticleCount(userService.getArticleCount(id));
        vo.setFollowingCount(userService.getFollowingCount(id));
        vo.setFollowerCount(userService.getFansCount(id));

        return Result.success(vo);
    }

    // 获取用户发布的文章
    @GetMapping("/{id}/articles")
    public Result<List<PostItemDTO>> getArticles(@PathVariable Long id) {
        try {
            // 使用PostService获取用户发布的文章
            PageInfo<PostItemDTO> posts = postService.getPostsByType("article", 1, 10, null);
            // 过滤出当前用户的文章
            List<PostItemDTO> userArticles = posts.getList().stream()
                .filter(post -> post.getAuthor() != null && post.getAuthor().getId().equals(id))
                .collect(Collectors.toList());
            return Result.success(userArticles);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取用户文章失败");
        }
    }

    // 获取用户发布的问答
    @GetMapping("/{id}/questions")
    public Result<List<PostItemDTO>> getQuestions(@PathVariable Long id) {
        try {
            // 使用PostService获取用户发布的问答
            PageInfo<PostItemDTO> posts = postService.getPostsByType("question", 1, 10, null);
            // 过滤出当前用户的问答
            List<PostItemDTO> userQuestions = posts.getList().stream()
                .filter(post -> post.getAuthor() != null && post.getAuthor().getId().equals(id))
                .collect(Collectors.toList());
            return Result.success(userQuestions);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.error("获取用户问答失败");
        }
    }

    // 获取用户关注的话题
    @GetMapping("/{id}/topics")
    public Result<List<Topic>> getFollowingTopics(@PathVariable Long id) {
        // TODO: 实现获取用户关注话题的方法
        // 暂时返回空结果
        return Result.success(List.of());
    }

    // 获取用户关注的文章
    @GetMapping("/{id}/followed-articles")
    public Result<List<Article>> getFollowedArticles(@PathVariable Long id) {
        // TODO: 实现获取用户关注的文章的方法
        // 暂时返回空结果
        return Result.success(List.of());
    }
    
    @PostMapping("/follow/{userId}")
    public Result<String> followUser(@PathVariable Long userId) {
        // 从安全上下文中获取当前用户
        User currentUser = SecurityContextHolder.getUser();
        if (currentUser == null) {
            return Result.error("请先登录");
        }
        
        boolean success = userService.followUser(currentUser.getId(), userId);
        if (success) {
            return Result.success("关注成功");
        } else {
            return Result.error("关注失败");
        }
    }
    
    @DeleteMapping("/follow/{userId}")
    public Result<String> unfollowUser(@PathVariable Long userId) {
        // 从安全上下文中获取当前用户
        User currentUser = SecurityContextHolder.getUser();
        if (currentUser == null) {
            return Result.error("请先登录");
        }
        
        boolean success = userService.unfollowUser(currentUser.getId(), userId);
        if (success) {
            return Result.success("取消关注成功");
        } else {
            return Result.error("取消关注失败");
        }
    }
}