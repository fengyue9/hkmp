<template>
  <div class="app-container">
    <!-- 实时报警显示 -->
    <el-row>
      <el-col :span="24">
        <h2>实时报警</h2>
        <el-table :data="realTimeAlarms" style="width: 100%">
          <el-table-column label="报警类型" prop="alarmType"></el-table-column>
          <el-table-column label="位置" prop="location"></el-table-column>
          <el-table-column label="时间戳" prop="timestamp"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button plain @click="acknowledgeAlarm(scope.row);">确认</el-button>
              <el-button plain type="danger" @click="escalateAlarm(scope.row);">升级</el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <!-- 报警历史 -->
    <el-row>
      <el-col :span="24">
        <h2>报警历史</h2>
        <el-table :data="alarmHistory" style="width: 100%">
          <el-table-column label="报警类型" prop="alarmType"></el-table-column>
          <el-table-column label="位置" prop="location"></el-table-column>
          <el-table-column label="时间戳" prop="timestamp"></el-table-column>
          <el-table-column label="操作">
            <template slot-scope="scope">
              <el-button
                v-if="scope.row.alarmType !== null"
                @click="viewAlarmDetails(scope.row)"
              >
                详情
              </el-button>
              <el-button
                v-else
                disabled
              >
                无效详情
              </el-button>
            </template>
          </el-table-column>
        </el-table>
      </el-col>
    </el-row>
    <!-- 详情模态框 -->
    <el-dialog
      :visible="detailDialogVisible"
      title="报警详情"
      @close="detailDialogVisible = false"
    >
      <p v-if="selectedAlarm">报警类型: {{ selectedAlarm.alarmType }}</p>
      <p v-if="selectedAlarm">位置: {{ selectedAlarm.location }}</p>
      <p v-if="selectedAlarm">时间戳: {{ selectedAlarm.timestamp }}</p>
      <p v-if="selectedAlarm">待添加...</p>
      <!-- 在这里添加其他报警信息的详细内容 -->
    </el-dialog>
  </div>
</template>

<script>

export default {
  components: {},
  data() {
    return {
      realTimeAlarms: [
        { alarmType: '移动侦测', location: '大厅', timestamp: '2023-12-01 08:30:00' },
        { alarmType: '烟雾报警', location: '办公室', timestamp: '2023-12-01 09:15:00' },
        { alarmType: '入侵报警', location: '仓库', timestamp: '2023-12-01 10:00:00' }
      ],
      alarmHistory: [
        { alarmType: '门禁报警', location: '前门', timestamp: '2023-11-30 15:45:00' },
        { alarmType: '温度异常', location: '实验室', timestamp: '2023-11-30 16:20:00' },
        { alarmType: '燃气泄漏', location: '厨房', timestamp: '2023-11-30 17:10:00' }
      ],
      detailDialogVisible: false,
      selectedAlarm: null
    }
  },
  methods: {
    acknowledgeAlarm(alarm) {
      // 实现确认逻辑，可以将确认的报警移动到报警历史中
      this.alarmHistory.push(alarm)
      this.realTimeAlarms = this.realTimeAlarms.filter(a => a !== alarm)
      this.sendAlarmMessage()
    },
    escalateAlarm(alarm) {
      // 实现升级逻辑
      // 可以将升级后的报警发送到相应的处理系统
      this.sendHigherAlarmMessage()
    },
    viewAlarmDetails(alarm) {
      // 实现查看报警详情的逻辑
      this.selectedAlarm = alarm
      this.detailDialogVisible = true
    }, sendAlarmMessage() {
      const h = this.$createElement
      this.$notify({
        title: '报警处理',
        message: h('i', { style: 'color: teal' }, '已移入报警历史')
      })
      // 其他方法
    }, sendHigherAlarmMessage() {
      const h = this.$createElement

      this.$notify({
        title: '升级报警',
        message: h('i', { style: 'color: teal' }, '已升级报警')
      })
      // 其他方法
    }
  }
}
</script>
