import request from '@/utils/request'

// 查询报警记录列表
export function listRecord(query) {
  return request({
    url: '/device/record/list',
    method: 'get',
    params: query
  })
}

// 查询报警记录详细
export function getRecord(alarmRecordId) {
  return request({
    url: '/device/record/' + alarmRecordId,
    method: 'get'
  })
}

// 新增报警记录
export function addRecord(data) {
  return request({
    url: '/device/record',
    method: 'post',
    data: data
  })
}

// 修改报警记录
export function updateRecord(data) {
  return request({
    url: '/device/record',
    method: 'put',
    data: data
  })
}

// 删除报警记录
export function delRecord(deviceId) {
  return request({
    url: '/device/record/' + deviceId,
    method: 'delete'
  })
}

//下载视频
export function downloadVideo(alarmRecordId) {
  return request({
    url: '/device/record/download/' + alarmRecordId,
    method: 'get',
    responseType: 'blob'
  })
}

//处理报警
export function handleAlarm(data) {
  return request({
    url: '/device/record/handle',
    method: 'post',
    data: data
  })
}

