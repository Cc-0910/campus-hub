import axios from 'axios'
import { useUserStore } from '@/stores/user'

// 创建 axios 实例
const request = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 请求拦截器
request.interceptors.request.use(
  (config) => {
    const userStore = useUserStore()
    const token = userStore.token
    if (token) {
      config.headers.Authorization = `Bearer ${token}`
    }
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// 响应拦截器
request.interceptors.response.use(
  (res) => {
    // 如果响应数据存在且code为200，返回数据
    if (res.data && res.data.code === 200) {
      return res.data
    } else {
      // 如果code不是200，抛出错误，包含错误信息
      const errorMessage = res.data?.message || '请求失败'
      return Promise.reject(new Error(errorMessage))
    }
  },
  (error) => {
    console.error('请求错误:', error)
    // 处理网络错误或HTTP错误
    if (error.response) {
      // 服务器返回了错误状态码
      const errorMessage = error.response.data?.message || `请求失败: ${error.response.status}`
      return Promise.reject(new Error(errorMessage))
    } else if (error.request) {
      // 请求已发出但没有收到响应
      return Promise.reject(new Error('网络错误，请检查网络连接'))
    } else {
      // 其他错误
      return Promise.reject(error)
    }
  }
)

export default request