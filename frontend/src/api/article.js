import request from '@/utils/request'

// 获取用户文章列表
export const getUserArticles = (userId, page = 1, size = 10) => {
  return request({
    url: `/article/user/${userId}`,
    method: 'get',
    params: { page, size }
  })
}

// 获取文章详情
export const getArticleDetail = (articleId) => {
  return request({
    url: `/article/${articleId}`,
    method: 'get'
  })
}