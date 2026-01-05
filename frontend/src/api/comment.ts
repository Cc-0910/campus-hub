import request from '@/utils/request'

export interface CommentDTO {
  id: number
  content: string
  user: {
    id: number
    username: string | null
    nickname: string
    avatar: string | null
    email: string | null
    createTime: string | null
  }
  parentId: number | null
  replyToUser: {
    id: number
    username: string
    nickname: string
  } | null
  createTime: string
  likeCount: number
  isLiked: boolean
  children: CommentDTO[]
}

export interface CreateCommentRequest {
  postId: number
  content: string
  parentId?: number
  replyToUserId?: number
}

export const commentApi = {
  // 获取评论列表（带楼中楼结构）
  getCommentsWithReplies: (postId: number) => {
    return request.get(`/comments/${postId}`)
  },
  
  // 发布评论
  createComment: (comment: CreateCommentRequest) => {
    return request.post('/comment', comment)
  }
}