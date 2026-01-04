package com.edu.example.backend.controller;

import com.edu.example.backend.common.Result;
import com.edu.example.backend.common.SecurityContextHolder;
import com.edu.example.backend.entity.User;
import com.edu.example.backend.entity.Article;
import com.edu.example.backend.entity.Topic;
import com.edu.example.backend.entity.Question;
import com.edu.example.backend.service.UserService;
import com.edu.example.backend.service.ArticleService;
import com.edu.example.backend.service.TopicService;
import com.edu.example.backend.service.QuestionService;
import com.edu.example.backend.service.FollowService;
import com.edu.example.backend.vo.UserDetailVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private ArticleService articleService;

    @Autowired
    private TopicService topicService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private FollowService followService;

    // 获取用户详情（含粉丝数、关注数、文章数等）
    @GetMapping("/{id}")
    public Result<UserDetailVO> getUserProfile(@PathVariable Long id) {
        User user = userService.getById(id);
        if (user == null) return Result.fail("用户不存在");

        UserDetailVO vo = new UserDetailVO();
        vo.setUser(user);

        vo.setArticleCount(articleService.countByAuthorId(id));
        vo.setFollowingCount(followService.countFollowing(id));
        vo.setFollowerCount(followService.countFollower(id));

        return Result.success(vo);
    }

    // 获取用户发布的文章
    @GetMapping("/{id}/articles")
    public Result<List<Article>> getArticles(@PathVariable Long id) {
        List<Article> articles = articleService.listByAuthorId(id);
        return Result.success(articles);
    }

    // 获取用户关注的话题
    @GetMapping("/{id}/topics")
    public Result<List<Topic>> getFollowingTopics(@PathVariable Long id) {
        List<Topic> topics = topicService.getFollowingTopics(id);
        return Result.success(topics);
    }

    // 获取用户关注的文章
    @GetMapping("/{id}/followed-articles")
    public Result<List<Article>> getFollowedArticles(@PathVariable Long id) {
        List<Article> articles = articleService.getFollowedArticles(id);
        return Result.success(articles);
    }

    // 获取用户关注的问答
    @GetMapping("/{id}/questions")
    public Result<List<Question>> getFollowingQuestions(@PathVariable Long id) {
        List<Question> questions = questionService.getFollowingQuestions(id);
        return Result.success(questions);
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