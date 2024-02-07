<template>
  <div class="app-container">
    <el-row>
      <el-col :span="24">
        <h2>抓图记录</h2>
        <el-table :data="screenshots" style="width: 100%">
          <el-table-column label="设备名称" prop="deviceName"></el-table-column>
          <el-table-column label="抓图时间" prop="screenshotTime"></el-table-column>
          <el-table-column label="抓图预览" prop="data"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button plain @click="">下载</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      screenshots: [{ deviceName: '摄像头A', screenshotTime: '2024.2.7 15:11:10', data: 101 }]

    }
  },
  methods: {
    download(type) {
      // 处理开始和结束时间下载逻辑
      // 根据form中的streamType、startTime和endTime使用Hikvision Web SDK进行下载操作
    },
    startPlayback() {
      // 使用Hikvision Web SDK开始回放
    },
    stopPlayback() {
      // 使用Hikvision Web SDK停止回放
    },
    reversePlayback() {
      // 使用Hikvision Web SDK倒放
    },
    singleFrame() {
      // 使用Hikvision Web SDK单帧
    },
    pausePlayback() {
      // 使用Hikvision Web SDK暂停回放
    },
    resumePlayback() {
      // 使用Hikvision Web SDK恢复回放
    },
    slowPlayback() {
      // 使用Hikvision Web SDK慢放
    },
    fastPlayback() {
      // 使用Hikvision Web SDK快放
    },
    captureImage() {
      // 使用Hikvision Web SDK抓图
    }
  }
}
</script>

<style scoped>
/* 在这里添加样式 */
</style>
