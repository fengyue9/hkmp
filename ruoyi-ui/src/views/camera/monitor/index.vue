<template>
  <div class="app-container" id="monitor-container">
    <el-row>
      <!-- 左侧设备选择 -->
      <el-col :span="6" style="padding-right: 10px;padding-bottom: 10px">
        <el-card class="box-card">
          <h3 slot="header" class="header_card el-icon-coordinate"> 操作栏</h3>
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
            <br>
            <div style="padding-top: 20px;padding-bottom: 20px"><span class="span_info">云台控制区</span>
            </div>
            <!-- 云台操作按钮组 -->
            <div class="button-group-container">
              <div class="el-button-group" style="border: 1px solid #ebeef5; padding: 10px;">
                <el-button size="large" type="primary" icon="el-icon-top-left"
                           @click="remoteControlByCode(1)"></el-button>
                <el-button size="large" type="primary" icon="el-icon-top" @click="remoteControlByCode(2)"></el-button>
                <el-button size="large" type="primary" icon="el-icon-top-right"
                           @click="remoteControlByCode(3)"></el-button>
              </div>
              <div class="el-button-group" style="border: 1px solid #ebeef5; padding: 10px;">
                <el-button size="large" type="primary" icon="el-icon-back" @click="remoteControlByCode(4)"></el-button>
                <el-button size="large" type="primary" icon="el-icon-mouse" @click="remoteControlByCode(5)"></el-button>
                <el-button size="large" type="primary" icon="el-icon-right" @click="remoteControlByCode(6)"></el-button>
              </div>
              <div class="el-button-group" style="border: 1px solid #ebeef5; padding: 10px;">
                <el-button size="large" type="primary" icon="el-icon-bottom-left"
                           @click="remoteControlByCode(7)"></el-button>
                <el-button size="large" type="primary" icon="el-icon-bottom"
                           @click="remoteControlByCode(8)"></el-button>
                <el-button size="large" type="primary" icon="el-icon-bottom-right"
                           @click="remoteControlByCode(9)"></el-button>
              </div>
            </div>
            <!--          高级显示设置-->
            <br>
            <div style="padding-top: 10px;padding-bottom: 10px">
              <span class="span_info">高级显示设置区</span>
            </div>

            <br>
            <!--          调整焦焦距按钮-->
            <div class="button-row" style="padding-top: 10px;">
              <el-button style="margin-right: 20px" size="large" type="primary"
                         class="primary el-icon-circle-plus-outline"
                         @click="remoteControlByCode(10)">焦距变大
              </el-button>
              <el-button style="margin-right: 20px" size="large" type="primary" class="primary el-icon-remove-outline"
                         @click="remoteControlByCode(11)">焦距变小
              </el-button>
            </div>
            <!--          调整光圈按钮-->
            <div class="button-row" style="padding-top: 10px;">
              <el-button style="margin-right: 20px" size="large" type="primary"
                         class="primary el-icon-circle-plus-outline"
                         @click="remoteControlByCode(12)">扩大光圈
              </el-button>
              <el-button style="margin-right: 20px" size="large" type="primary" class="primary el-icon-remove-outline"
                         @click="remoteControlByCode(13)">缩小光圈
              </el-button>
            </div>
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
      <!-- 中间主体部分海康威视摄像头预览 -->
      <el-col :span="18">
        <el-card class="box-card">
          <h3 slot="header" class="header_card el-icon-video-camera"> 摄像头预览</h3>
          <div class="camera-container">
            <video ref="video" class="video-element" @click="handleVideoClick" controls
                   poster="../../../assets/images/poster1.jpg"></video>
            <!-- 画布 -->
            <canvas ref="canvas" v-show="isAnnotationMode" style="position: absolute; top: 0; left: 0;"
                    @mousedown="startDrawing" @mousemove="draw" @mouseup="endDrawing"></canvas>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="height: 80px">
      <el-card>
        <div class="block"
             style="display: flex; align-items: center; justify-content: flex-end; padding: 15px 20px;">
          <div class="bottom_left">
            <el-button type="warning" style="margin-right: 10px;" @click="disconnectWebRtcStreamer">停止预览</el-button>
            <el-button type="success" @click="startMonitor()">开始预览</el-button>
          </div>
          <div style="flex: 1;"></div>
          <div class="volume-control" style="display: flex; align-items: center;">
            <el-button size="medium" class="demonstration" @click="captureImage">抓图</el-button>
            <el-button class="demonstration" @click="toggleAnnotationMode">绘制</el-button>
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
import WebRtcStreamer from './webrtcstreamer.js';
import {remoteControl, saveImage} from "@/api/camera/monitor/monitor";
import {saveVideo} from "@/api/system/record";
import moment from 'moment';


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
      selectedDate: '', // 用户选择的日期时间
      isoString: '', // 转换后的 ISO 8601 格式字符串
      //是否开始预览
      isStartMonitor: false,
      isAnnotationMode: false, // 是否处于标注模式
      drawing: false, // 是否正在绘制
      lastX: 0, // 上一个点的横坐标
      lastY: 0, // 上一个点的纵坐标
      isRecording: false, //是否正在录制，初始为false
      mediaRecorder: null, //视频路线记录
      recordedChunks: [],
      alertTitle: '视频录制中', // 录制状态提示标题
      startTime: '',//开始录制时间
      endTime: '',//结束录制时间
    }
  },
  mounted() {
    // 在页面加载后，将画布的尺寸设置为与视频相同
    const video = this.$refs.video;
    const canvas = this.$refs.canvas;
    canvas.width = video.clientWidth;
    canvas.height = video.clientHeight;

  },
  created() {
    //调用查询设备信息列表查询所有设备列表并绑定到设备选择下拉框中
    this.queryDeviceList();
  },
  methods: {
    // 将选择的日期时间转换为 ISO 8601 格式字符串
    convertToISO() {
      if (this.selectedDate) {
        // 创建日期对象
        const date = new Date(this.selectedDate);
        // 转换为 ISO 8601 格式字符串
        this.isoString = date.toISOString();
      }
    },
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
    startMonitor() {
      // 获取选中的设备对象
      const device = this.selectedItem;
      // 检查设备对象是否为空
      if (!device) {
        this.$message.error('请先选择设备');
        return;
      }
      //开始预览
      this.$message({
        message: '开始预览！',
        type: 'success',
        center: true
      });
      this.initWebRtcStreamer(device);
      this.isStartMonitor = true;
    },
    //初始化实时预览
    initWebRtcStreamer(device) {
      // 获取 video 元素的引用
      const videoElement = this.$refs.video;
      // 初始化 WebRtcStreamer
      this.webRtcServer = new WebRtcStreamer(videoElement, "http://127.0.0.1:8000");
      // 连接到 RTSP 流地址 如:this.webRtcServer.connect("rtsp://admin:hrj,2002527@192.168.1.64:554/Streaming/Channels/101");
      const url = 'rtsp://' + device.deviceUsername + ':' + device.devicePassword + '@' + device.deviceIp + ':554/Streaming/Channels/101';
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
    //断开实时预览
    disconnectWebRtcStreamer() {
      if (!this.webRtcServer) {
        return;
      }
      this.$message({
        message: '停止预览！',
        type: 'warning',
        center: true
      });
      // 断开连接
      if (this.webRtcServer) {
        this.webRtcServer.disconnect();
        this.isStartMonitor = false;
      }
    },
    //云台控制公共方法
    remoteControlByCode(code) {
      // 获取选中的设备对象
      const device = this.selectedItem;
      // 检查是否开始预览为空
      if (!this.isStartMonitor) {
        this.$message({
          title: '操作失败',
          message: '请先开始预览',
          type: 'error',
          center: true
        });
        return;
      }
      // 构造发送给后端的数据对象
      const data = {
        deviceIp: device.deviceIp,
        deviceUsername: device.deviceUsername,
        devicePassword: device.devicePassword,
        devicePort: device.devicePort,
        code: code
      };
      remoteControl(data).then(response => {
        this.$message({
          title: '操作成功',
          message: '云台操作成功',
          type: 'success',
          duration: 2000
        });
      });
    },
    //抓图
    captureImage() {
      if (!this.isStartMonitor) {
        this.$message({
          message: '请先开始预览',
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
    // 切换标注模式
    toggleAnnotationMode() {
      this.isAnnotationMode = !this.isAnnotationMode;
      if (!this.isStartMonitor) {
        this.$message({
          message: '请先开始预览',
          type: 'error',
          center: true
        });
        return;
      }
      this.$message({
        message: '开始绘制！请在预览区域长按鼠标进行拖动,松开保存',
        type: 'success',
        center: true
      });
    },
    // 开始绘制
    startDrawing(event) {
      this.drawing = true;
      const rect = this.$refs.canvas.getBoundingClientRect();
      this.lastX = event.clientX - rect.left;
      this.lastY = event.clientY - rect.top;
    },
    // 绘制
    draw(event) {
      if (this.drawing) {
        const ctx = this.$refs.canvas.getContext('2d');
        const rect = this.$refs.canvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;
        ctx.beginPath();
        ctx.moveTo(this.lastX, this.lastY);
        ctx.lineTo(x, y);
        ctx.stroke();
        this.lastX = x;
        this.lastY = y;
      }
    },
    // 结束绘制
    endDrawing() {
      this.drawing = false;
      if (!this.isAnnotationMode) return;
      // 结束绘制后调用后端保存接口
      // 获取绘制部分的画布
      const canvas1 = this.$refs.canvas;
      // 获取视频帧画布
      const video = this.$refs.video;
      // 获取视频的原始尺寸
      const width = video.videoWidth;
      const height = video.videoHeight;
      // 创建画布，并设置与视频尺寸相同的尺寸
      const canvas2 = document.createElement("canvas");
      const context2 = canvas2.getContext("2d");
      canvas2.width = width;
      canvas2.height = height;
      // 绘制视频帧到画布
      context2.drawImage(video, 0, 0, width, height);
      this.combineCanvases(canvas1, canvas2);
      // 将画布转换为图像数据
      const imageData = canvas2.toDataURL("image/png");
      // 调用发送图标数据到后端的方法，并传递 deviceId
      if (this.selectedItem) {
        //获取特定格式的当前时间
        const currentTime = moment().format('YYYY.MM.DD-HH.mm.ss');
        const file = this.dataURLtoFile(imageData, 'image' + currentTime + '.png');
        this.sendImageData(file, this.selectedItem.deviceId);
      } else {
        this.$message({
          message: '请先选择设备！',
          type: 'warn',
          center: true
        });
      }
      this.$message({
        message: '结束绘制！',
        type: 'success',
        center: true
      });
      this.clearCanvas(canvas1);
    },
    //清空画布
    clearCanvas(canvas) {
      const context = canvas.getContext('2d');
      context.clearRect(0, 0, canvas.width, canvas.height);
    },
    // 处理视频点击事件
    handleVideoClick(event) {
      if (this.isAnnotationMode) {
        const rect = this.$refs.canvas.getBoundingClientRect();
        const x = event.clientX - rect.left;
        const y = event.clientY - rect.top;
        // 在视频标签内部才进行绘制操作
        if (x >= 0 && x <= rect.width && y >= 0 && y <= rect.height) {
          this.startDrawing(event);
        }
      }
    },
    // 将第一个画布的内容绘制到第二个画布上
    combineCanvases(canvas1, canvas2) {
      const context1 = canvas1.getContext('2d');
      const context2 = canvas2.getContext('2d');
      // 清空第二个画布
      this.clearCanvas(canvas2);
      // 获取第一个画布的内容
      const imageData = context1.getImageData(0, 0, canvas1.width, canvas1.height);
      // 将第一个画布的内容绘制到第二个画布上
      context2.putImageData(imageData, 0, 0);
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
      this.startTime = moment().format('YYYY-MM-DD HH:mm:ss');
      console.log(9999999999999999999);
      console.log(this.startTime);
      // 开始录制逻辑
      if (!this.isStartMonitor) {
        this.$message({
          message: '请先开始预览',
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
      this.endTime = moment().format('YYYY-MM-DD HH:mm:ss');
      console.log(this.endTime);
      // 结束录制逻辑
      this.$message({
        message: '结束录制!',
        type: 'success',
        center: true
      });
      this.isRecording = false;
      this.mediaRecorder.stop();
      this.alertTitle = '视频录制中'; // 修改录制状态提示标题
      this.$confirm('录制已结束，您想要直接下载视频还是保存视频到服务器？', '结束录制', {
        confirmButtonText: '直接下载',
        cancelButtonText: '保存到服务器',
        type: 'warning'
      }).then(() => {
        // 用户选择直接下载视频
        this.downloadRecording();
      }).catch(() => {
        // 用户选择保存视频到服务器
        this.saveVideo();
      });
    },
    //下载录像
    downloadRecording() {
      const blob = new Blob(this.recordedChunks, {type: 'video/webm'});
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement('a');
      a.href = url;
      a.download = 'recording.webm';
      document.body.appendChild(a);
      a.click();
      window.URL.revokeObjectURL(url);
    },
    //保存录像
    saveVideo() {
      const blob = new Blob(this.recordedChunks, {type: 'video/webm'});
      //构造请求表单数据
      const formData = new FormData();
      formData.append('file', blob, 'recording_' + moment().format("YYYY.MM.DD-HH.mm.ss") + '.webm');
      formData.append('deviceId', this.selectedItem.deviceId);
      formData.append('startTime', this.startTime);
      formData.append('endTime', this.endTime);
      saveVideo(formData).then(response => {
        this.$message({
          message: '视频保存成功！',
          type: 'success',
          center: true
        });
      })
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
  /*实时预览页面背景色*/
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
  height: 600px; /* 调整预览区域的高度 */

}

.video-element {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
