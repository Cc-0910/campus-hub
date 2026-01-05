export interface User {
  id: number
  username: string
  nickname: string
  avatar: string
  email: string
  createTime?: string
  isLogin?: boolean
  role?: string
}