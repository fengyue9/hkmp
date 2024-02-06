import request from '@/utils/request'

// 查询在线用户列表
export function list1(query) {
  console.log('2023-10-16 21:25aaaaaaa')
  return request({
    url: '/monitor/online/list',
    method: 'get',
    params: query
  })
}
