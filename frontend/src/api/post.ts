import request from '@/utils/request'
import type { Post } from '@/stores/post'

export interface PostListParams {
  pageNum?: number
  pageSize?: number
  topicId?: number
}

export interface PostResponse {
  code: number
  data: {
    list: Post[]
    total: number
    pageNum: number
    pageSize: number
  }
  message: string
}

// 获取文章/问答列表
export function getPosts(params: PostListParams = {}) {
  const { pageNum = 1, pageSize = 10, topicId } = params
  return request.get<PostResponse>('/posts', {
    params: {
      pageNum,
      pageSize,
      topicId
    }
  })
}

// 根据类型获取文章/问答列表
export function getPostsByType(type: string, params: PostListParams = {}) {
  const { pageNum = 1, pageSize = 10, topicId } = params
  return request.get<PostResponse>(`/posts/type/${type}`, {
    params: {
      pageNum,
      pageSize,
      topicId
    }
  })
}

// 获取问答列表
export function getQuestions(params: PostListParams = {}) {
  return getPostsByType('qa', params)
}

// 获取文章列表
export function getArticles(params: PostListParams = {}) {
  return getPostsByType('article', params)
}

// 获取文章/问答详情
export function getPostDetail(id: number) {
  return request.get<any>(`/post/${id}`)
}