export interface Article {
  id: number
  title: string
  content: string
  author: string
  authorAvatar: string
  topicId: number
  topicName: string
  viewCount: number
  likeCount: number
  commentCount: number
  createdAt: string
  updatedAt: string
}

export interface ArticleListResponse {
  code: number
  data: Article[]
  total: number
  message: string
}