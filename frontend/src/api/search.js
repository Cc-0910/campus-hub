import request from '@/utils/request'

// 搜索接口
export function search(keyword) {
  return request({
    url: '/search',
    method: 'get',
    params: { keyword }
  })
}