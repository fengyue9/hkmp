import request from '@/utils/request'

// 查询参数列表
export function getWeather() {
  return request({
    url: '/getWeatherByLocalIP',
    method: 'get'
  })
}
