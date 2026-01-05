package com.edu.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.example.backend.entity.Follow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关注 Mapper 接口
 */
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {
    
    /**
     * 获取用户粉丝数量
     */
    Integer getFollowersCount(@Param("userId") Long userId);
    
    /**
     * 获取用户关注数量
     */
    Integer getFollowingCount(@Param("userId") Long userId);
    
    /**
     * 判断是否关注
     */
    Integer isFollowing(@Param("followerId") Long followerId, @Param("followingId") Long followingId);
    
    /**
     * 获取用户关注的用户ID列表
     */
    List<Long> getFollowingUserIds(@Param("userId") Long userId, @Param("current") Long current, @Param("size") Long size);
    
    /**
     * 获取用户粉丝ID列表
     */
    List<Long> getFollowerUserIds(@Param("userId") Long userId, @Param("current") Long current, @Param("size") Long size);
}