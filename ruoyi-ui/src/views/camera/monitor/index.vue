<template>
  <div class="app-container" id="monitor-container">
    <el-row>
      <!-- 左侧设备选择 -->
      <el-col :span="5">
        <el-card class="box-card">
          <h3 slot="header" class="header_card el-icon-coordinate"> 操作栏</h3>
          <!--          选择设备栏 -->
          <span class="span_info">设备列表 </span>
          <el-select v-model="selectedItem" placeholder="请选择设备" @click="handleDeviceSelection"
                     style="padding-top:10px;padding-bottom:10px" size="small"
          >
            <el-option
              v-for="device in devices"
              :key="device.deviceId"
              :label="device.deviceName"
              :value="device.deviceId"
            />
          </el-select>
          <br>
<!--          <span class="span_info">窗口分割数 </span>-->
<!--          <el-select v-model="selectedWindowCount" placeholder="请选择窗口分割数"-->
<!--                     style="padding-top:10px;padding-bottom:10px"-->
<!--                     size="small"-->
<!--          >-->
<!--            <el-option-->
<!--              v-for="windowCount in windowCounts"-->
<!--              :key="windowCount.id"-->
<!--              :label="windowCount.count"-->
<!--              :value="windowCount.id"-->
<!--            />-->
<!--          </el-select>-->
<!--          <br>-->
          <span class="span_info">云台操作</span>
          <br>
          <!--          云台操作按钮组-->
          <div class="el-button-group" style="border: 1px;padding-top:5px;">
            <div class="el-button primary el-icon-top-left"></div>
            <div class="el-button primary el-icon-top"></div>
            <div class="el-button primary el-icon-top-right"></div>
          </div>
          <div class="el-button-group ">
            <div class="el-button primary el-icon-back"></div>
            <div class="el-button primary el-icon-mouse"></div>
            <div class="el-button primary el-icon-right"></div>
          </div>
          <div class="el-button-group ">
            <div class="el-button primary el-icon-bottom-left"></div>
            <div class="el-button primary el-icon-bottom"></div>
            <div class="el-button primary el-icon-bottom-right"></div>
          </div>
          <!--          高级显示设置-->
          <br>
          <span class="span_info">高级显示设置</span>
          <br>
          <!--          调焦按钮-->
          <div class="button-row">
            <el-button class="primary el-icon-circle-plus-outline">调焦
            </el-button>
            <el-button class="primary el-icon-remove-outline">调焦
            </el-button>
          </div>
          <!--          聚焦按钮-->
          <div class="button-row">
            <el-button class="primary el-icon-circle-plus-outline">聚焦
            </el-button>
            <el-button class="primary el-icon-remove-outline">聚焦
            </el-button>
          </div>
          <!--          调光圈按钮-->
          <div class="button-row">
            <el-button class="primary el-icon-circle-plus-outline">光圈
            </el-button>
            <el-button class="primary el-icon-remove-outline">光圈
            </el-button>
          </div>
        </el-card>
      </el-col>
      <!-- 中间主体部分海康威视摄像头预览 -->
      <el-col :span="14">
        <el-card class="box-card">
          <h3 slot="header" class="header_card el-icon-vid eo-camera"> 摄像头预览</h3>
          <div class="camera-container">
            <video ref="video" class="video-element" autoplay muted></video>
          </div>
        </el-card>
      </el-col>
      <!-- 右侧操作日志记录 -->
      <el-col :span="5">
        <el-card class="box-card">
          <h3 slot="header" class="header_card el-icon-data-line"> 操作记录</h3>
          <div class="content" style="height: 420px;">
            <el-scrollbar style="height: 420px; overflow-y: auto;">
              <el-timeline style="padding-left: 5px;" reverse>
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
        <!-- 底部音量 -->
        <div class="block"
             style="display: flex; align-items: center; justify-content: space-between; padding: 15px 20px;">
          <div class="bottom_left">
            <el-button type="warning" style="margin-right: 10px;" @click="disconnectWebRtcStreamer">停止预览</el-button>
            <el-button type="success" @click="startMonitor()">开始预览</el-button>
          </div>

          <div style="flex: 1;"></div>

          <div class="volume-control" style="display: flex; align-items: center;">
            <el-slider style="width: 100px; margin: 0 10px;" v-model="voice"></el-slider>
            <el-button class="demonstration">抓图1</el-button>
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

export default {
  data() {
    return {
      selectedDevice: null,
      selectedWindowCount: null,
      //设备列表，用于下拉框选择
      devices: [],
      //设备列表下拉框默认选中的值
      selectedItem: '',
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
      voice: 50,
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
    handleDeviceSelection(device) {
      // 使用 EventBus 触发设备选择事件
      console.log('哈哈哈我选择了设备');
      // this.$parent.eventBus.$emit('deviceSelected', device)
    },
    queryDeviceList() {
      listDevice(this.queryParams).then(response => {
        this.devices = response.rows.filter(device => device.deviceStatus === "0");
      })
    },
    startMonitor() {
      this.initWebRtcStreamer();
    },
    //初始化实时预览
    initWebRtcStreamer() {
      // 获取 video 元素的引用
      const videoElement = this.$refs.video;
      // 初始化 WebRtcStreamer
      this.webRtcServer = new WebRtcStreamer(videoElement, "http://127.0.0.1:8000");
      // 连接到 RTSP 流地址
      this.webRtcServer.connect("rtsp://admin:hrj,2002527@192.168.1.64:554/Streaming/Channels/101");
    },
    //断开实时预览
    disconnectWebRtcStreamer() {
      // 断开连接
      if (this.webRtcServer) {
        this.webRtcServer.disconnect();
      }
    }
  },
  watch: {
    selectedDevice() {
      // 当选中的设备发生变化时，重新初始化摄像头预览
      this.initCameraPreview();
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

.camera-container {
  height: 420px; /* 调整预览区域的高度 */
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
}

.video-element {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>
