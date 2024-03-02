import request from '@/utils/request'


//云台操作
export function remoteControl(data) {
  return request({
    url: '/camera/monitor/control',
    method: 'post',
    data: data
  })
}

//保存图片
export function saveImage(imageFile, deviceId) {
  const formData = new FormData();
  formData.append('deviceId', deviceId);
  formData.append('imageFile', imageFile);
  return request({
    url: '/camera/monitor/saveImage',
    method: 'post',
    data: formData,
    headers: {
      'Content-Type': 'multipart/form-data'
    }
  });
}

//首页获取系统资源
export function getSystemResourceUsage() {
  return request({
    url: '/camera/monitor/getSystemResourceUsage',
    method: 'get'
  })
}
