export interface Question {
  id: number
  title: string
  content: string
  author: string
  authorAvatar: string
  topicId: number
  topicName: string
  answerCount: number
  viewCount: number
  likeCount: number
  isSolved: boolean
  createdAt: string
  updatedAt: string
}

export interface QuestionListResponse {
  code: number
  data: Question[]
  total: number
  message: string
}