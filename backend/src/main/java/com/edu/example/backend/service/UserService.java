package com.edu.example.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.edu.example.backend.dto.UserProfileStats;
import com.edu.example.backend.entity.User;

import java.util.List;

public interface UserService extends IService<User> {
    User findByUsername(String username);

    boolean register(String username, String password, String email);

    User login(String username, String password);

    // 获取用户统计数据
    UserProfileStats getUserStats(Long userId);

    // 获取用户文章数量
    Integer getArticleCount(Long userId);

    // 获取用户粉丝数量
    Integer getFansCount(Long userId);

    // 获取用户关注数量
    Integer getFollowingCount(Long userId);

    // 判断当前用户是否关注目标用户
    Boolean isFollowing(Long currentUserId, Long targetUserId);

    // 关注用户
    boolean followUser(Long followerId, Long followedId);

    // 取消关注用户
    boolean unfollowUser(Long followerId, Long followedId);

    // 获取用户关注的人列表
    IPage<User> getFollowingList(Long userId, Page<User> page);

    // 获取用户粉丝列表
    IPage<User> getFollowersList(Long userId, Page<User> page);

    // 根据关键词搜索用户
    List<User> searchByKeyword(String keyword);
}