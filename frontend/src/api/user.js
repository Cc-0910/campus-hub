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

// 获取用户关注列表
export const getUserFollowing = (userId, page = 1, size = 10) => {
  return request({
    url: `/user/following/${userId}`,
    method: 'get',
    params: { page, size }
  })
}

// 获取用户粉丝列表
export const getUserFollowers = (userId, page = 1, size = 10) => {
  return request({
    url: `/user/followers/${userId}`,
    method: 'get',
    params: { page, size }
  })
}

// 关注用户
export const followUser = (userId) => {
  return request({
    url: `/user/follow/${userId}`,
    method: 'post'
  })
}

// 取消关注用户
export const unfollowUser = (userId) => {
  return request({
    url: `/user/follow/${userId}`,
    method: 'delete'
  })
}