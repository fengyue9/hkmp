import request from '@/utils/request'

// 查询抓图记录列表
export function listScreenshot_record(query) {
  return request({
    url: '/screenshot/screenshot_record/list',
    method: 'get',
    params: query
  })
}

// 查询抓图记录详细
export function getScreenshot_record(deviceId) {
  return request({
    url: '/screenshot/screenshot_record/' + deviceId,
    method: 'get'
  })
}

// 新增抓图记录
export function addScreenshot_record(data) {
  return request({
    url: '/screenshot/screenshot_record',
    method: 'post',
    data: data
  })
}

// 修改抓图记录
export function updateScreenshot_record(data) {
  return request({
    url: '/screenshot/screenshot_record',
    method: 'put',
    data: data
  })
}

// 删除抓图记录
export function delScreenshot_record(screenshotKey) {
  return request({
    url: '/screenshot/screenshot_record/' + screenshotKey,
    method: 'delete'
  })
}

// 下载抓图
export function downloadScreenshot(screenshotKey) {
  return request({
    url: '/screenshot/screenshot_record/download/' + screenshotKey,
    method: 'get',
    responseType: 'blob', // 指定响应数据类型为 Blob
  })
}
