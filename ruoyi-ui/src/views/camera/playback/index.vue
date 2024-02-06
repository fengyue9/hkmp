<template>
  <div class="app-container">
    <el-row>
      <el-col :span="8">
        <!-- 选择码流类型 -->
        <el-form :model="form" label-position="top">
          <el-form-item label="码流类型">
            <el-select v-model="form.streamType" placeholder="请选择">
              <el-option label="主码流" value="main"></el-option>
              <el-option label="子码流" value="sub"></el-option>
            </el-select>
          </el-form-item>
          <!-- 选择开始和结束时间搜索 -->
          <el-form-item label="开始时间">
            <el-date-picker v-model="form.startTime" type="datetime" placeholder="选择开始时间"></el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间">
            <el-date-picker v-model="form.endTime" type="datetime" placeholder="选择结束时间"></el-date-picker>
          </el-form-item>
          <!-- 开始和结束时间下载按钮 -->
          <el-form-item>
            <el-button type="primary" @click="download('start')">开始时间下载</el-button>
            <el-button type="primary" @click="download('end')">结束时间下载</el-button>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>

    <!-- 控制按钮 -->
    <el-row>
      <el-col :span="24">
        <el-button @click="startPlayback">开始回放</el-button>
        <el-button @click="stopPlayback">停止回放</el-button>
        <el-button @click="reversePlayback">倒放</el-button>
        <el-button @click="singleFrame">单帧</el-button>
        <el-button @click="pausePlayback">暂停</el-button>
        <el-button @click="resumePlayback">恢复</el-button>
        <el-button @click="slowPlayback">慢放</el-button>
        <el-button @click="fastPlayback">快放</el-button>
        <el-button @click="captureImage">抓图</el-button>
      </el-col>
    </el-row>
  </div>
</template>
<script>
export default {
  data() {
    return {
      form: {
        streamType: 'main',
        startTime: '',
        endTime: ''
      }
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
