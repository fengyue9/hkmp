<template>
  <div class="app-container" id="monitor-container">
    <el-row>
      <!-- 左侧设备选择 -->
      <el-col :span="6" style="padding-right: 10px;padding-bottom: 10px">
        <el-card class="box-card">
          <h3 slot="header" class="header_card el-icon-coordinate"> 操作区</h3>
          <div class="camera-container">
            <!--          选择设备栏 -->
            <span class="span_info">设备列表 ：</span>
            <el-select v-model="selectedItem.deviceName" placeholder="请选择在线设备"
                       @change="handleDeviceSelection"
                       style="padding-top:10px;padding-bottom:10px" size="small"
            >
              <el-option
                v-for="device in devices"
                :key="device.deviceId"
                :label="device.deviceName"
                :value="device.deviceName"
              />
            </el-select>
            <!--            开始回放时间选择-->
            <div style="padding-top: 20px;padding-bottom: 15px">
              <span class="demonstration">开始回放时间</span>
            </div>
            <el-date-picker
              v-model="startTime"
              type="datetime"
              placeholder="请选择开始回放时间"
              format="yyyy-MM-dd HH:mm:ss">
            </el-date-picker>
            <!--            结束回放时间选择-->
            <div style="padding-top: 20px;padding-bottom: 15px">
              <span class="demonstration">结束回放时间</span>
            </div>
            <el-date-picker
              v-model="endTime"
              type="datetime"
              value-format="yyyy-MM-dd HH:mm:ss"
              placeholder="请选择结束回放时间">
            </el-date-picker>


            <el-button @click="drawer = true" type="primary" style="margin-left: 16px;">
              查看录制记录
            </el-button>

            <el-drawer
              title="录制记录列表"
              :visible.sync="drawer"
              :direction="direction"
              size="50%">
              <div style="padding-left: 20px;padding-right: 20px">
                <el-table :data="videoRecordList">
                  <el-table-column property="deviceId" label="设备id" width="200"></el-table-column>
                  <el-table-column property="deviceName" label="设备名称" width="300"></el-table-column>
                  <el-table-column property="beginRecordTime" label="开始录制时间" width="400"></el-table-column>
                  <el-table-column property="endRecordTime" label="结束录制时间" width="400"></el-table-column>
                  <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                    <template slot-scope="scope">
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-edit"
                        @click="handleUpdate(scope.row)"
                      >播放
                      </el-button>
                      <el-button
                        size="mini"
                        type="text"
                        icon="el-icon-delete"
                        @click="handleDelete(scope.row)"
                      >下载
                      </el-button>
                    </template>
                  </el-table-column>
                </el-table>
              </div>
            </el-drawer>
            <!--          调整焦点按钮-->
            <div class="button-row" style="padding-top: 10px;">
              <el-button style="margin-right: 20px" size="large" type="primary"
                         class="primary el-icon-circle-plus-outline"
                         @click="remoteControlByCode(15)">焦点前调
              </el-button>
              <el-button style="margin-right: 20px" size="large" type="primary" class="primary el-icon-remove-outline"
                         @click="remoteControlByCode(16)">焦点后调
              </el-button>
            </div>


          </div>

        </el-card>
      </el-col>
      <!-- 中间主体部分海康威视摄像头回放 -->
      <el-col :span="18">
        <el-card class="box-card">
          <h3 slot="header" class="header_card el-icon-video-camera"> 回放录像</h3>
          <div class="camera-container">
            <video ref="video" class="video-element" controls
                   poster="../../../assets/images/poster1.jpg"></video>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="height: 80px">
      <el-card>
        <div class="block"
             style="display: flex; align-items: center; justify-content: flex-end; padding: 15px 20px;">
          <div class="bottom_left">
            <el-button type="warning" style="margin-right: 10px;" @click="endPlayback">停止回放</el-button>
            <el-button type="success" @click="startPlayback()">开始回放</el-button>
          </div>
          <div style="flex: 1;"></div>
          <div class="volume-control" style="display: flex; align-items: center;">
            <el-button size="medium" class="demonstration" @click="captureImage">抓图</el-button>
            <el-button class="demonstration" @click="toggleRecording">
              {{ isRecording ? '结束录制' : '开始录制' }}
            </el-button>
            <!-- 录制状态提示 -->
            <el-alert
              :title="alertTitle"
              type="success"
              :closable="false"
              :center="true"
              v-if="isRecording"
            >
            </el-alert>
          </div>
        </div>
      </el-card>
    </el-row>

  </div>
</template>
<script>
import {listDevice} from '@/api/camera/device'
import WebRtcStreamer from '../monitor/webrtcstreamer';
import {playback} from "@/api/camera/playback/playback";

export default {
  data() {
    return {
      //设备列表，用于下拉框选择
      devices: [],
      //设备列表下拉框默认选中的值
      selectedItem: {
        deviceName: '',
        deviceId: '',
        deviceUsername: '',
        devicePassword: '',
        deviceIp: '',
        devicePort: '',
      },
      //是否开始回放
      isStartPlayback: false,
      isAnnotationMode: false, // 是否处于标注模式
      isRecording: false, //是否正在录制，初始为false
      recordedChunks: [],
      alertTitle: '视频录制中', // 录制状态提示标题
      startTime: '',
      endTime: '',
      drawer: false,//抽屉开关
      direction: 'btt',//抽屉开关方向
      videoRecordList: []
    }
  },
  mounted() {
  },
  created() {
    //调用查询设备信息列表查询所有设备列表并绑定到设备选择下拉框中
    this.queryDeviceList();
  },
  methods: {
    // 当用户选择设备时触发
    handleDeviceSelection(selectedDeviceName) {
      // 根据设备名称从设备列表中找到对应的设备对象
      const selectedDevice = this.devices.find(device => device.deviceName === selectedDeviceName);
      if (this.selectedItem) {
        // 将选中的设备对象的属性复制到selectedItem中 使用 Vue.set 方法手动通知 Vue.js 对象的属性已经发生了变化 不能使用直接赋值的方式
        Object.keys(selectedDevice).forEach(key => {
          this.$set(this.selectedItem, key, selectedDevice[key]);
        });
      }
    },
    queryDeviceList() {
      listDevice(this.queryParams).then(response => {
        this.devices = response.rows.filter(device => device.deviceStatus === "0");
      })
    },
    //将时间格式化成海康威视支持的时间字符串
    formatDateToUTCString(dateString) {
      // 将时间字符串转换成 Date 对象
      const date = new Date(dateString);
      // 获取年月日时分秒
      const year = date.getFullYear();
      const month = (date.getMonth() + 1).toString().padStart(2, '0'); // 月份从0开始，需要加1，并且格式化为两位数
      const day = date.getDate().toString().padStart(2, '0');
      const hours = date.getHours().toString().padStart(2, '0');
      const minutes = date.getMinutes().toString().padStart(2, '0');
      const seconds = date.getSeconds().toString().padStart(2, '0');
      // 拼接成类似20240221t150000z的格式
      return `${year}${month}${day}t${hours}${minutes}${seconds}z`;
    },
    //开始回放
    startPlayback() {
      // 获取选中的设备对象
      const device = this.selectedItem;
      // 检查设备对象是否为空
      if (!device) {
        this.$message.error('请先选择设备');
        return;
      }
      let url = 'rtsp://' + device.deviceUsername + ':' + device.devicePassword + '@' + device.deviceIp + ':554/Streaming/tracks/101';
      let startTime = this.startTime;
      let endTime = this.endTime;
      if (startTime !== '') {
        let startTimeHK = this.formatDateToUTCString(startTime);
        url = url.concat('?starttime=' + startTimeHK);
      }
      if (endTime !== '') {
        let endTimeHK = this.formatDateToUTCString(endTime);
        url = url.concat(' & endtime=' + endTimeHK);
      }
      const data = {
        url: url
      }
      // playback(data).then(response => {
      //   console.log(response);
      // })
      this.$message({
        message: '开始回放！',
        type: 'success',
        center: true
      });
      this.initWebRtcStreamer(url);
      this.isStartPlayback = true;
    },
    //停止回放
    endPlayback() {
      if (this.isStartPlayback) {
        this.disconnectWebRtcStreamer();
      } else {
        this.$message({
          message: '未开始回放！',
          type: 'error',
          center: true
        });
      }
    },
    //初始化WebRtcStreamer
    initWebRtcStreamer(url) {
      console.log(11111);
      console.log(url);
      // 获取 video 元素的引用
      const videoElement = this.$refs.video;
      // 初始化 WebRtcStreamer
      this.webRtcServer = new WebRtcStreamer(videoElement, "http://127.0.0.1:8000");
      try {
        this.webRtcServer.connect(url);
      } catch (error) {
        this.$message({
          message: '连接到 RTSP 流地址失败，请检查网络连接和摄像头配置\'！',
          type: 'error',
          center: true
        });
        console.error('连接到 RTSP 流地址失败:', error);
      }
    },
    //断开WebRtcStreamer
    disconnectWebRtcStreamer() {
      if (!this.webRtcServer) {
        return;
      }
      this.$message({
        message: '停止回放！',
        type: 'warning',
        center: true
      });
      // 断开连接
      if (this.webRtcServer) {
        this.webRtcServer.disconnect();
        this.isStartPlayback = false;
      }
    },
    //抓图
    captureImage() {
      if (!this.isStartPlayback) {
        this.$message({
          message: '请先开始回放',
          type: 'error',
          center: true
        });
        return;
      }
      const video = this.$refs.video;
      // 获取视频的原始尺寸
      const width = video.videoWidth;
      const height = video.videoHeight;
      // 创建画布，并设置与视频尺寸相同的尺寸
      const canvas = document.createElement("canvas");
      const context = canvas.getContext("2d");
      canvas.width = width;
      canvas.height = height;
      // 绘制视频帧到画布
      context.drawImage(video, 0, 0, width, height);
      // 将画布转换为图像数据
      const imageData = canvas.toDataURL("image/png");
      // 调用发送图标数据到后端的方法，并传递 deviceId
      if (this.selectedItem) {
        //获取特定格式的当前时间
        const now = new Date();
        const year = now.getFullYear();
        const month = String(now.getMonth() + 1).padStart(2, '0'); // 月份从0开始计算，因此需要加1
        const day = String(now.getDate()).padStart(2, '0');
        const hours = String(now.getHours()).padStart(2, '0');
        const minutes = String(now.getMinutes()).padStart(2, '0');
        const seconds = String(now.getSeconds()).padStart(2, '0');
        const currentTime = `${year}.${month}.${day}-${hours}.${minutes}.${seconds}`;
        const file = this.dataURLtoFile(imageData, 'image' + currentTime + '.png');
        this.sendImageData(file, this.selectedItem.deviceId);
      } else {
        this.$message({
          message: '请先选择设备！',
          type: 'warn',
          center: true
        });
      }
    },
    // 发送图片数据
    sendImageData(imageData, deviceId) {
      saveImage(imageData, deviceId).then(response => {
        this.$message({
          message: '抓图成功！',
          type: 'success',
          center: true
        });
      }).catch(error => {
        this.$message({
          message: '抓图失败，请重试！',
          type: 'error',
          center: true
        });
      });
    },
    // 将 base64 编码的图片数据转换为文件对象
    dataURLtoFile(dataURL, fileName) {
      const arr = dataURL.split(',');
      const mime = arr[0].match(/:(.*?);/)[1];
      const bstr = atob(arr[1]);
      let n = bstr.length;
      const u8arr = new Uint8Array(n);
      while (n--) {
        u8arr[n] = bstr.charCodeAt(n);
      }
      return new File([u8arr], fileName, {type: mime});
    },
    //触发录制
    toggleRecording() {
      if (this.isRecording) {
        //停止录制
        this.stopRecording();
      } else {
        //开始录制
        this.startRecording();
      }
    },
    //开始录制
    startRecording() {
      // 开始录制逻辑
      if (!this.isStartPlayback) {
        this.$message({
          message: '请先开始回放',
          type: 'error',
          center: true
        });
        return;
      }
      this.$message({
        message: '开始录制!',
        type: 'success',
        center: true
      });
      this.isRecording = true;
      const video = this.$refs.video;
      this.recordedChunks = [];
      const stream = video.captureStream();
      console.log('捕获的视频流:', stream);
      this.mediaRecorder = new MediaRecorder(stream, {mimeType: 'video/webm'});
      const videoTracks = stream.getVideoTracks();
      if (videoTracks.length > 0) {
        this.mediaRecorder.start(100),
          this.mediaRecorder.ondataavailable = (e) => {
            this.recordedChunks.push(e.data);
          };
      }
    },
    //结束录制
    stopRecording() {
      // 结束录制逻辑
      this.$message({
        message: '结束录制!',
        type: 'success',
        center: true
      });
      this.isRecording = false;
      this.mediaRecorder.stop();
      this.saveRecording();
      this.alertTitle = '视频录制中'; // 修改录制状态提示标题
    },
    //保存录像
    saveRecording() {
      const blob = new Blob(this.recordedChunks, {type: 'video/webm'});
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'recording.webm';
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
    }
  },
  beforeDestroy() {
    // 在组件销毁前断开连接
    this.disconnectWebRtcStreamer();
  }
}
</script>
<style>
#monitor-container {
  /*实时回放页面背景色*/
  background-color: #f2fafa;
}

.header_card {
  height: 10px;
  font-size: 15px;
}

.span_info {
  font-size: 16.5px;
}

.camera-container {
  max-width: 100%;
  margin: 0 auto;
  position: relative;
  overflow: hidden;
  height: 600px; /* 调整回放区域的高度 */

}

.video-element {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
