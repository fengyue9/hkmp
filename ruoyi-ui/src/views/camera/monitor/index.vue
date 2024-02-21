<template>
  <div class="app-container" id="monitor-container">
    <el-row>
      <!-- 左侧设备选择 -->
      <el-col :span="5">
        <el-card class="box-card">
          <h3 slot="header" class="header_card el-icon-coordinate"> 操作栏</h3>
          <div class="camera-container">
            <!--          选择设备栏 -->
            <span class="span_info">设备列表 </span>
            <el-select v-model="selectedItem" placeholder="请选择设备" @click="handleDeviceSelection"
                       style="padding-top:10px;padding-bottom:10px" size="small"
            >
              <el-option
                v-for="device in devices"
                :key="device.deviceId"
                :label="device.deviceName"
                :value="device"
              />
            </el-select>
            <br>
            <span class="span_info">云台操作</span>
            <br>
            <!--          云台操作按钮组-->
            <div class="el-button-group" style="border: 1px;padding-top:10px;">
              <div class="el-button primary el-icon-top-left" @click="remoteControlToLeftTop"></div>
              <div class="el-button primary el-icon-top"></div>
              <div class="el-button primary el-icon-top-right"></div>
            </div>
            <div class="el-button-group ">
              <div class="el-button primary el-icon-back"></div>
              <div class="el-button primary el-icon-mouse" @click=""></div>
              <div class="el-button primary el-icon-right"></div>
            </div>
            <div class="el-button-group" style="padding-bottom: 10px">
              <div class="el-button primary el-icon-bottom-left"></div>
              <div class="el-button primary el-icon-bottom"></div>
              <div class="el-button primary el-icon-bottom-right"></div>
            </div>
            <!--          高级显示设置-->
            <br>
            <span class="span_info">高级显示设置</span>
            <br>
            <!--          调整焦焦距按钮-->
            <div class="button-row" style="padding-top: 10px;">
              <el-button class="primary el-icon-circle-plus-outline">焦距变大
              </el-button>
              <el-button class="primary el-icon-remove-outline">焦距变小
              </el-button>
            </div>
            <!--          调整光圈按钮-->
            <div class="button-row" style="padding-top: 10px;">
              <el-button class="primary el-icon-circle-plus-outline">扩大光圈
              </el-button>
              <el-button class="primary el-icon-remove-outline">缩小光圈
              </el-button>
            </div>
            <!--          调整焦点按钮-->
            <div class="button-row" style="padding-top: 10px;">
              <el-button class="primary el-icon-circle-plus-outline">焦点前调
              </el-button>
              <el-button class="primary el-icon-remove-outline">焦点后调
              </el-button>
            </div>

          </div>

        </el-card>
      </el-col>
      <!-- 中间主体部分海康威视摄像头预览 -->
      <el-col :span="14">
        <el-card class="box-card">
          <h3 slot="header" class="header_card el-icon-vid eo-camera"> 摄像头预览</h3>
          <div class="camera-container">
            <video ref="video" class="video-element" controls poster="../../../assets/images/poster1.jpg"></video>
          </div>
        </el-card>
      </el-col>
      <!-- 右侧操作日志记录 -->
      <el-col :span="5">
        <el-card class="box-card">
          <h3 slot="header" class="header_card el-icon-data-line"> 操作记录</h3>
          <div class="camera-container">
            <el-scrollbar style="height: 100%; overflow-y: auto;">
              <el-timeline style="padding-left: 5px;height: 100%" reverse>
                <el-timeline-item
                  v-for="(event, index) in eventCallbacks"
                  :key="index"
                  :timestamp="event.timestamp"
                  :placement="index % 2 === 0 ? 'left' : 'right'"
                >
                  <el-card>
                    <div class="event-title">{{ event.title }}</div>
                    <div class="event-timestamp">{{ event.timestamp }}</div>
                    <div class="event-description">{{ event.description }}</div>
                  </el-card>
                </el-timeline-item>
              </el-timeline>
            </el-scrollbar>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-row style="height: 80px">
      <el-card>
        <div class="block"
             style="display: flex; align-items: center; justify-content: space-between; padding: 15px 20px;">
          <div class="bottom_left">
            <el-button type="warning" style="margin-right: 10px;" @click="disconnectWebRtcStreamer">停止预览</el-button>
            <el-button type="success" @click="startMonitor()">开始预览</el-button>
          </div>

          <div style="flex: 1;"></div>

          <div class="volume-control" style="display: flex; align-items: center;">
            <el-button class="demonstration" @click="captureImage">抓图1</el-button>
            <el-button class="demonstration">抓图2</el-button>
            <el-button class="demonstration">抓图3</el-button>
            <el-button class="demonstration">抓图4</el-button>
            <el-button class="demonstration">抓图5</el-button>
            <el-button class="demonstration">抓图6</el-button>
            <el-button class="demonstration">抓图7</el-button>
            <el-button class="demonstration">抓图8</el-button>
            <el-button class="demonstration">抓图9</el-button>
            <el-button class="demonstration">抓图10</el-button>
          </div>
        </div>
      </el-card>
    </el-row>

  </div>
</template>
<script>
import {listDevice} from '@/api/camera/device'
import WebRtcStreamer from './webrtcstreamer.js';
import {saveImage, remoteControl} from "@/api/camera/monitor/monitor";

export default {
  data() {
    return {
      selectedDevice: null,
      selectedWindowCount: null,
      //设备列表，用于下拉框选择
      devices: [],
      //设备列表下拉框默认选中的值
      selectedItem: null,
      windowCounts: [
        {
          id: 1, count: '1×1'
        }, {
          id: 2, count: '2×2'
        }, {
          id: 3, count: '3×3'
        }, {
          id: 4, count: '4×4'
        }
      ],
      eventCallbacks: [
        {timestamp: '2023-01-01 08:30:00', title: '开始预览成功', description: ''},
        {timestamp: '2023-01-01 09:15:00', title: '开启云台失败', description: '403,notSupport'},
        {timestamp: '2023-01-01 10:00:00', title: '停止云台失败', description: '403,notSupport'},
        {timestamp: '2023-01-01 11:30:00', title: '开启云台失败', description: '403,notSupport'},
        {timestamp: '2023-01-01 13:45:00', title: '停止云台失败', description: '403,notSupport'}
      ]
    }
  },
  mounted() {
    // 在组件挂载后初始化海康威视摄像头预览
    // this.initCameraPreview();
    // 在组件创建后立即调用接口获取数据
  },
  created() {
    //调用查询设备信息列表查询所有设备列表并绑定到设备选择下拉框中
    this.queryDeviceList();
  },
  methods: {
    initCameraPreview() {
      // 使用海康威视SDK初始化摄像头预览
      // 这里假设你有一个叫做initCamera的方法来初始化摄像头
      // 并且该方法接受设备ID作为参数
      // if (this.selectedDevice) {
      //   this.$refs.cameraContainer.innerHTML = '' // 清空容器，以便重新初始化
      //   initCamera(this.selectedDevice, this.$refs.cameraContainer)
      // }
    },
    // 当用户选择设备时触发
    handleDeviceSelection() {
      if (this.selectedItem) {
        console.log('用户选择了设备:', this.selectedItem);
        // 在这里可以添加需要执行的逻辑
      } else {
        console.log('用户未选择设备');
        // 在这里可以添加处理用户未选择设备的逻辑
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
      console.log(11111111)
      console.log(device)
      this.initWebRtcStreamer(device);
    },
    //初始化实时预览
    initWebRtcStreamer(device) {
      // 获取 video 元素的引用
      const videoElement = this.$refs.video;
      // 初始化 WebRtcStreamer
      this.webRtcServer = new WebRtcStreamer(videoElement, "http://127.0.0.1:8000");
      // 连接到 RTSP 流地址
      const url = 'rtsp://' + device.deviceUsername + ':' + device.devicePassword + '@' + device.deviceIp + ':554/Streaming/Channels/101';
      // 如:this.webRtcServer.connect("rtsp://admin:hrj,2002527@192.168.1.64:554/Streaming/Channels/101");
      console.log("RTSP流的URL为:" + url);
      this.webRtcServer.connect(url);
    },
    //断开实时预览
    disconnectWebRtcStreamer() {
      if (!this.webRtcServer) {
        this.$message({
          message: '未开始预览！',
          type: 'warning',
          center: true
        });
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
      }
    },
    //云台控制-左上
    remoteControlToLeftTop() {
      // remoteControl(data).then(response => {
      //   this.$notify({
      //     title: '操作成功',
      //     message: '设备云台操作成功',
      //     type: 'success',
      //     duration: 3000
      //   });
      // })
    },
    //云台控制-上
    remoteControlToTop() {

    },
    //云台控制-右上
    remoteControlToRightTop() {

    },
    //云台控制-左
    remoteControlToLeft() {

    },
    //云台控制-左右自动
    remoteControlAuto() {

    },
    //云台控制-右
    remoteControlToRight() {

    },
    //云台控制-左下
    remoteControlToLeftDown() {

    },
    //云台控制-下
    remoteControlToDown() {

    },
    //云台控制-右下
    remoteControlToRightDown() {

    },
    //高级显示设置-焦距变大
    remoteControlIncreaseFocalLength() {

    },
    //高级显示设置-焦距变小
    remoteControlDecreaseFocalLength() {

    },
    //高级显示设置-扩大光圈
    remoteControlExpandAperture() {

    },
    //高级显示设置-缩小光圈
    remoteControlNarrowAperture() {

    },
    //高级显示设置-焦点前调
    remoteControlFocusTopNote() {

    },
    //高级显示设置-焦点后调
    remoteControlFocusBaseNote() {

    },
    //抓图
    captureImage() {
      const video = this.$refs.video;
      const scale = 0.25;
      const canvas = document.createElement("canvas");
      const context = canvas.getContext("2d");
      canvas.width = video.videoWidth * scale;
      canvas.height = video.videoHeight * scale;
      context.drawImage(video, 0, 0, canvas.width, canvas.height);
      const img = document.createElement("img");
      img.src = canvas.toDataURL("image/png");
      const outputContainer = document.getElementById("output");
      outputContainer.prepend(img);
      // 调用发送图标数据到后端的方法，并传递 deviceId
      if (this.selectedItem) {
        this.sendImageData(canvas.toDataURL("image/png"), this.selectedItem.deviceId);
      } else {
        console.error('请先选择设备');
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
        console.error('抓图失败:', error);
        this.$message({
          message: '抓图失败，请重试！',
          type: 'error',
          center: true
        });
      });
    }
  }
  ,
  watch: {
    selectedDevice() {
      // 当选中的设备发生变化时，重新初始化摄像头预览
      this.initCameraPreview();
    }
  }
  ,
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

.event-title {
  font-size: 16px;
  font-weight: bold;
}

.event-description {
  margin-top: 10px;
}


.camera-container {
  max-width: 100%;
  margin: 0 auto;
  position: relative;
  overflow: hidden;
  height: 440px; /* 调整预览区域的高度 */

}

.video-element {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
