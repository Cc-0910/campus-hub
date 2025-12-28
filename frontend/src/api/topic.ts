import request from '@/utils/request'
import type { Topic, TopicListResponse, FollowTopicRequest } from '@/types/topic'
import { mockTopics } from '@/mock/topics'

export const topicApi = {
  // 获取话题列表
  getTopics: (params?: { page?: number; size?: number; sort?: string }) => {
    return new Promise<TopicListResponse>((resolve) => {
      // 模拟网络延迟
      setTimeout(() => {
        let topics = [...mockTopics]
        
        // 排序处理
        if (params?.sort === 'followerCount') {
          topics.sort((a, b) => b.followerCount - a.followerCount)
        } else if (params?.sort === 'createdAt') {
          topics.sort((a, b) => new Date(b.createdAt).getTime() - new Date(a.createdAt).getTime())
        }
        
        // 分页处理
        const page = params?.page || 1
        const size = params?.size || 12
        const startIndex = (page - 1) * size
        const endIndex = startIndex + size
        const paginatedTopics = topics.slice(startIndex, endIndex)
        
        resolve({
          code: 200,
          data: paginatedTopics,
          total: topics.length,
          msg: 'success'
        })
      }, 500)
    })
  },

  // 关注/取消关注话题
  followTopic: (data: FollowTopicRequest) => {
    return new Promise((resolve) => {
      setTimeout(() => {
        resolve({
          code: 200,
          msg: data.follow ? '关注成功' : '取消关注成功'
        })
      }, 300)
    })
  },

  // 获取话题详情
  getTopicDetail: (id: number) => {
    return new Promise<Topic>((resolve, reject) => {
      setTimeout(() => {
        const topic = mockTopics.find(t => t.id === id)
        if (topic) {
          resolve(topic)
        } else {
          reject(new Error('话题不存在'))
        }
      }, 300)
    })
  }
}