package com.edu.example.backend.service.impl;

import cn.hutool.crypto.digest.DigestUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.edu.example.backend.entity.User;
import com.edu.example.backend.mapper.UserMapper;
import com.edu.example.backend.service.UserService;
import com.edu.example.backend.dto.UserProfileStats;
import com.edu.example.backend.mapper.FollowMapper;
import com.edu.example.backend.entity.Follow;
import com.edu.example.backend.common.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private FollowMapper followMapper;

    @Override
    public User findByUsername(String username) {
        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", username);
        return userMapper.selectOne(queryWrapper);
    }

    @Override
    public boolean register(String username, String password, String email) {
        try {
            // 检查用户名是否已存在
            if (findByUsername(username) != null) {
                return false;
            }
            
            // 创建新用户
            User user = new User();
            user.setUsername(username);
            user.setPassword(DigestUtil.md5Hex(password));
            user.setEmail(email);
            user.setNickname(username); // 默认昵称为用户名
            user.setAvatar(null); // 默认头像为空
            // 时间戳将由MyBatis-Plus自动填充
            
            int result = userMapper.insert(user);
            return result > 0;
        } catch (Exception e) {
            // 记录异常信息
            e.printStackTrace();
            System.err.println("注册失败: " + e.getMessage());
            return false;
        }
    }

    @Override
    public User login(String username, String password) {
        System.out.println("尝试登录用户名: " + username);
        
        if (username == null || username.trim().isEmpty()) {
            System.out.println("用户名无效");
            throw new IllegalArgumentException("用户名不能为空");
        }
        
        User user = findByUsername(username);
        if (user == null) {
            System.out.println("用户不存在: " + username);
            return null;
        }
        
        System.out.println("找到用户，ID: " + user.getId() + ", 密码格式: " + user.getPassword().substring(0, Math.min(10, user.getPassword().length())) + "...");
        
        // 使用MD5验证密码（简化版本，实际项目中应使用BCrypt）
        String md5Password = DigestUtil.md5Hex(password);
        System.out.println("MD5验证，密码哈希: " + md5Password);
        boolean md5Match = user.getPassword().equals(md5Password);
        System.out.println("MD5验证结果: " + md5Match);
        if (md5Match) {
            return user;
        }
        
        System.out.println("密码验证失败");
        return null;
    }

    @Override
    public UserProfileStats getUserStats(Long userId) {
        // 从实际的文章表、关注表等获取真实数据
        Integer articleCount = getArticleCount(userId);
        Integer fansCount = getFansCount(userId);
        Integer followingCount = getFollowingCount(userId);
        
        return new UserProfileStats(
            articleCount,
            fansCount,
            followingCount
        );
    }
    
    @Override
    public Integer getArticleCount(Long userId) {
        // TODO: 从实际的 ArticleMapper 获取数据
        // 暂时返回模拟数据
        return 10;
    }
    
    @Override
    public Integer getFansCount(Long userId) {
        // 从关注表获取粉丝数量
        try {
            return followMapper.getFollowersCount(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public Integer getFollowingCount(Long userId) {
        // 从关注表获取关注数量
        try {
            return followMapper.getFollowingCount(userId);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    @Override
    public Boolean isFollowing(Long currentUserId, Long targetUserId) {
        try {
            // 从关注表判断是否有关注关系
            return followMapper.isFollowing(currentUserId, targetUserId) > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean followUser(Long followerId, Long followedId) {
        try {
            // 检查是否已经关注
            if (isFollowing(followerId, followedId)) {
                return false;
            }
            
            // 检查是否关注自己
            if (followerId.equals(followedId)) {
                return false;
            }
            
            // 创建关注记录
            Follow follow = new Follow();
            follow.setFollowerId(followerId);
            follow.setFollowingId(followedId);
            follow.setCreatedAt(new Timestamp(System.currentTimeMillis()));
            
            int result = followMapper.insert(follow);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public boolean unfollowUser(Long followerId, Long followedId) {
        try {
            // 检查是否有关注关系
            if (!isFollowing(followerId, followedId)) {
                return false;
            }
            
            // 删除关注记录
            QueryWrapper<Follow> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("follower_id", followerId);
            queryWrapper.eq("following_id", followedId);
            
            int result = followMapper.delete(queryWrapper);
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    
    @Override
    public IPage<User> getFollowingList(Long userId, Page<User> page) {
        try {
            // 获取用户关注的所有用户ID
            List<Long> followingIds = followMapper.getFollowingUserIds(userId, page.getCurrent(), page.getSize());
            
            // 如果没有关注任何人，返回空结果
            if (followingIds.isEmpty()) {
                return new Page<>(page.getCurrent(), page.getSize(), 0);
            }
            
            // 根据ID列表查询用户信息
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", followingIds);
            
            // 查询用户列表
            IPage<User> userPage = userMapper.selectPage(page, queryWrapper);
            
            return userPage;
        } catch (Exception e) {
            e.printStackTrace();
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }
    }
    
    @Override
    public IPage<User> getFollowersList(Long userId, Page<User> page) {
        try {
            // 获取用户的粉丝ID
            List<Long> followerIds = followMapper.getFollowerUserIds(userId, page.getCurrent(), page.getSize());
            
            // 如果没有粉丝，返回空结果
            if (followerIds.isEmpty()) {
                return new Page<>(page.getCurrent(), page.getSize(), 0);
            }
            
            // 根据ID列表查询用户信息
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.in("id", followerIds);
            
            // 查询用户列表
            IPage<User> userPage = userMapper.selectPage(page, queryWrapper);
            
            // 为每个粉丝添加关注状态
            // 从安全上下文获取当前用户ID
            Long currentUserId = SecurityContextHolder.getUserId();
            if (currentUserId != null && !userPage.getRecords().isEmpty()) {
                List<User> users = userPage.getRecords();
                for (User user : users) {
                    user.setIsFollowing(isFollowing(currentUserId, user.getId()));
                }
            }
            
            return userPage;
        } catch (Exception e) {
            e.printStackTrace();
            return new Page<>(page.getCurrent(), page.getSize(), 0);
        }
    }
    
    @Override
    public List<User> searchByKeyword(String keyword) {
        try {
            // 使用MyBatis-Plus的QueryWrapper进行模糊查询
            QueryWrapper<User> queryWrapper = new QueryWrapper<>();
            queryWrapper.like("username", keyword)
                        .or()
                        .like("nickname", keyword)
                        .orderByDesc("create_time");
            
            return list(queryWrapper);
        } catch (Exception e) {
            e.printStackTrace();
            return List.of();
        }
    }
}