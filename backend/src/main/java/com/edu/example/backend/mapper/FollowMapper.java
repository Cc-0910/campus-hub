package com.edu.example.backend.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.edu.example.backend.entity.Follow;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 关注关系Mapper
 */
@Mapper
public interface FollowMapper extends BaseMapper<Follow> {
    
    /**
     * 判断是否已关注
     * @param followerId 关注者ID
     * @param followedId 被关注者ID
     * @return 关注记录数量
     */
    int isFollowing(@Param("followerId") Long followerId, @Param("followedId") Long followedId);
    
    /**
     * 获取用户粉丝数量
     * @param userId 用户ID
     * @return 粉丝数量
     */
    int getFollowersCount(@Param("userId") Long userId);
    
    /**
     * 获取用户关注数量
     * @param userId 用户ID
     * @return 关注数量
     */
    int getFollowingCount(@Param("userId") Long userId);
    
    /**
     * 获取用户关注的人的ID列表
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 关注的人的ID列表
     */
    List<Long> getFollowingUserIds(@Param("userId") Long userId, @Param("offset") Long offset, @Param("limit") Long limit);
    
    /**
     * 获取用户粉丝的ID列表
     * @param userId 用户ID
     * @param offset 偏移量
     * @param limit 限制数量
     * @return 粉丝的ID列表
     */
    List<Long> getFollowerUserIds(@Param("userId") Long userId, @Param("offset") Long offset, @Param("limit") Long limit);
}