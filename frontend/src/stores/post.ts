import { defineStore } from 'pinia'
import { ref } from 'vue'

export interface Post {
  id: number
  title: string
  content: string
  summary?: string
  author: {
    nickname: string
    avatar: string
  }
  commentCount: number
  viewCount: number
  createTime: string
  tags?: string[]
  type: string // 用于区分是question还是article
}

export const usePostStore = defineStore(
  'post',
  () => {
    // State
    const postsCache = ref<Record<number, Post>>({})

    // Actions
    const cachePost = (post: Post) => {
      if (post && post.id) {
        postsCache.value[post.id] = post
      }
    }

    const getCachedPost = (id: number): Post | null => {
      return postsCache.value[id] || null
    }

    const clearPostCache = () => {
      postsCache.value = {}
    }

    // 批量缓存帖子列表
    const cachePosts = (posts: Post[]) => {
      if (Array.isArray(posts)) {
        posts.forEach(post => {
          if (post && post.id) {
            postsCache.value[post.id] = post
          }
        })
      }
    }

    return {
      postsCache,
      cachePost,
      getCachedPost,
      clearPostCache,
      cachePosts
    }
  }
)
