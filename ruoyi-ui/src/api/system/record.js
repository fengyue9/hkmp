import request from '@/utils/request'

// 查询回放列表
export function listRecord(query) {
  return request({
    url: '/system/record/list',
    method: 'get',
    params: query
  })
}

// 查询回放详细
export function getRecord(deviceId) {
  return request({
    url: '/system/record/' + deviceId,
    method: 'get'
  })
}

// 新增回放
export function addRecord(data) {
  return request({
    url: '/system/record',
    method: 'post',
    data: data
  })
}

// 修改回放
export function updateRecord(data) {
  return request({
    url: '/system/record',
    method: 'put',
    data: data
  })
}

// 删除回放
export function delRecord(deviceId) {
  return request({
    url: '/system/record/' + deviceId,
    method: 'delete'
  })
}

//保存视频
export function saveVideo(data) {
  return request({
    url: '/system/record/saveVideo',
    method: 'post',
    data: data
  })
}

