import axios from 'axios'

// 创建 axios 实例
const request = axios.create({
  baseURL: '/api',
  timeout: 5000
})

// 响应拦截器
request.interceptors.response.use(
  (res) => {
    if (res.data.code === 200) {
      return res.data
    } else {
      // 抛出错误
      return Promise.reject(new Error(res.data.msg || 'Error'))
    }
  },
  (error) => {
    console.error('请求错误:', error)
    return Promise.reject(error)
  }
)

export default request