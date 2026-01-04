import request from '@/utils/request';

export const searchAPI = {
  // Search with keyword
  search: (keyword) => {
    return request({
      url: '/search',
      method: 'get',
      params: {
        keyword
      }
    });
  }
};