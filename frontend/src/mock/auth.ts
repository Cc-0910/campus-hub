import type { User } from '@/stores/user'

export interface AuthResponse {
  code: number
  data: {
    token: string
    userInfo: User
  }
  msg: string
}

// 模拟用户数据
const mockUsers = [
  {
    id: 1,
    username: 'testuser',
    password: '123456',
    email: 'test@csu.edu.cn',
    nickname: '测试用户',
    avatar: '',
    role: 'user'
  },
  {
    id: 2,
    username: 'admin',
    password: 'admin123',
    email: 'admin@csu.edu.cn',
    nickname: '管理员',
    avatar: '',
    role: 'admin'
  }
]

// 模拟登录
export const mockLogin = (username: string, password: string): Promise<AuthResponse> => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      const user = mockUsers.find(u => u.username === username && u.password === password)
      
      if (user) {
        resolve({
          code: 200,
          data: {
            token: `mock-jwt-token-${user.id}-${Date.now()}`,
            userInfo: {
              id: user.id,
              username: user.username,
              nickname: user.nickname,
              avatar: user.avatar,
              email: user.email,
              role: user.role
            }
          },
          msg: '登录成功'
        })
      } else {
        reject(new Error('用户名或密码错误'))
      }
    }, 500)
  })
}

// 模拟注册
export const mockRegister = (username: string, password: string, email: string): Promise<AuthResponse> => {
  return new Promise((resolve, reject) => {
    setTimeout(() => {
      // 检查用户名是否已存在
      const existingUser = mockUsers.find(u => u.username === username)
      if (existingUser) {
        reject(new Error('用户名已存在'))
        return
      }
      
      // 检查邮箱是否已存在
      const existingEmail = mockUsers.find(u => u.email === email)
      if (existingEmail) {
        reject(new Error('邮箱已被注册'))
        return
      }
      
      // 创建新用户
      const newUser = {
        id: mockUsers.length + 1,
        username,
        password,
        email,
        nickname: username,
        avatar: '',
        role: 'user'
      }
      
      // 在实际应用中，这里应该调用后端API创建用户
      // 这里只是模拟，不会真正添加到mockUsers数组中
      
      resolve({
        code: 200,
        data: {
          token: `mock-jwt-token-${newUser.id}-${Date.now()}`,
          userInfo: {
            id: newUser.id,
            username: newUser.username,
            nickname: newUser.nickname,
            avatar: newUser.avatar,
            email: newUser.email,
            role: newUser.role
          }
        },
        msg: '注册成功'
      })
    }, 500)
  })
}