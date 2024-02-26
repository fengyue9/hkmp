import request from '@/utils/request'


//回放
export function playback(url) {
  return request({
    url: '/camera/playback/playback',
    method: 'post',
    data: url
  })
}



