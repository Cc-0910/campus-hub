export interface Topic {
  id: number
  name: string
  description: string
  coverImage: string
  followerCount: number
  isFollowing: boolean
  createdAt: string
  updatedAt: string
}

export interface TopicListResponse {
  code: number
  data: Topic[]
  total: number
  msg: string
}

export interface FollowTopicRequest {
  topicId: number
  follow: boolean
}