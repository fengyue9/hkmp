<template>
  <div class="app-container home">
    <el-row>
      <el-card>
        <h2><b><i>基于Spring和Vue的实时监控报警系统</i></b></h2>
        <blockquote class="text-warning" style="font-size: 14px">
          支持功能：摄像头实时预览、云台操作、回放下载、报警处理、设备信息管理、系统监控等
        </blockquote>
      </el-card>
    </el-row>
    <el-row>
      <el-card>
        <el-col :span="6">
          <div>
            <el-statistic
              group-separator=","
              :precision="2"
              :value="value2"
              :title="title"
            ></el-statistic>
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-statistic title="男女比">
              <template slot="formatter">
                456/2
              </template>
            </el-statistic>
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-statistic
              group-separator=","
              :precision="2"
              decimal-separator="."
              :value="value1"
              :title="title"
            >
              <template slot="prefix">
                <i class="el-icon-s-flag" style="color: red"></i>
              </template>
              <template slot="suffix">
                <i class="el-icon-s-flag" style="color: blue"></i>
              </template>
            </el-statistic>
          </div>
        </el-col>
        <el-col :span="6">
          <div>
            <el-statistic :value="like ? 521 : 520" title="Feedback">
              <template slot="suffix">
                  <span @click="like = !like" class="like">
                    <i
                      class="el-icon-star-on"
                      style="color:red"
                      v-show="!!like"
                    ></i>
                    <i class="el-icon-star-off" v-show="!like"></i>
                  </span>
              </template>
            </el-statistic>
          </div>
        </el-col>
      </el-card>
    </el-row>
    <el-row>
      <el-card>
        <!-- 实时监控图表 -->
        <div class="real-time-chart">
          <div ref="realTimeChart" class="chart-container"></div>
        </div>
        <!-- 报警处理功能 -->
        <div class="alert-handling">
          <el-card>
            <div slot="header" class="clearfix">
              <span>报警处理</span>
            </div>
            <div class="text item">
              <span>最新报警：</span>
              <span v-if="latestAlert">{{ latestAlert }}</span>
              <span v-else>暂无报警</span>
            </div>
            <el-button type="primary" @click="handleAlert">处理报警</el-button>
          </el-card>
        </div>
      </el-card>
    </el-row>
    <el-row>
      <el-card> <!--    设备数量展示-->
        <div class="device-count">
          <svg class="circle" width="100" height="100">
            <circle cx="50" cy="50" r="40" stroke="#409EFF" stroke-width="2" fill="none"></circle>
            <text x="50" y="50" text-anchor="middle" dominant-baseline="middle" font-size="24" fill="#409EFF">{{
                deviceCount
              }}
            </text>
          </svg>
        </div>
      </el-card>
    </el-row>
  </div>
</template>

<script>
import * as echarts from 'echarts'

export default {
  name: 'Index',
  data() {
    return {
      // 版本号
      version: '1.0.0.0',
      value: new Date(),
      latestAlert: null,
      deviceCount: 50, // 模拟设备数量
      // 模拟实时监控数据
      realtimeData: {
        cpu: [],
        memory: [],
        network: []
      },
      like: true,
      value1: 4154.564,
      value2: 1314,
      title: "用户人数",
    }
  },
  mounted() {
    // 初始化实时监控图表
    this.initRealtimeChart()
    // 模拟实时监控数据更新
    this.simulateRealtimeData()
  },
  methods: {
    goTarget(href) {
      window.open(href, '_blank')
    },
    initRealtimeChart() {
      this.realtimeChart = echarts.init(this.$refs.realTimeChart)
      const option = {
        title: {
          text: '系统资源占用监控'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['CPU', '内存', '网络']
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: []
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: 'CPU',
            type: 'line',
            data: this.realtimeData.cpu
          },
          {
            name: '内存',
            type: 'line',
            data: this.realtimeData.memory
          },
          {
            name: '网络',
            type: 'line',
            data: this.realtimeData.network
          }
        ]
      }
      this.realtimeChart.setOption(option)
    },
    simulateRealtimeData() {
      setInterval(() => {
        // 模拟实时监控数据更新
        const time = new Date().toLocaleTimeString().replace(/^\D*/, '')
        const cpuUsage = Math.floor(Math.random() * 100)
        const memoryUsage = Math.floor(Math.random() * 100)
        const networkTraffic = Math.floor(Math.random() * 100)
        this.realtimeData.cpu.push({name: time, value: cpuUsage})
        this.realtimeData.memory.push({name: time, value: memoryUsage})
        this.realtimeData.network.push({name: time, value: networkTraffic})
        if (this.realtimeData.cpu.length > 20) {
          this.realtimeData.cpu.shift()
          this.realtimeData.memory.shift()
          this.realtimeData.network.shift()
        }
        this.realtimeChart.setOption({
          xAxis: {
            data: this.realtimeData.cpu.map(item => item.name)
          },
          series: [
            {
              name: 'CPU',
              data: this.realtimeData.cpu
            },
            {
              name: '内存',
              data: this.realtimeData.memory
            },
            {
              name: '网络',
              data: this.realtimeData.network
            }
          ]
        })
        // 模拟报警触发
        if (cpuUsage > 90 || memoryUsage > 90 || networkTraffic > 90) {
          this.latestAlert = `系统发生异常！CPU：${cpuUsage}%, 内存：${memoryUsage}%, 网络：${networkTraffic}%`
        } else {
          this.latestAlert = null
        }
      }, 3000)
    },
    handleAlert() {
      // 处理报警的具体操作，可以根据需求添加
      this.$message.success('报警已处理！')
    }
  }
}
</script>

<style scoped>
.real-time-chart {
  margin-bottom: 20px;
}

.chart-container {
  height: 400px;
  padding-top: 10px;
  padding-bottom: 10px;
}

.alert-handling {
  width: 300px;
  margin-left: auto;
}

.text {
  margin-bottom: 20px;
}

.device-count {
  text-align: left;
}

.circle text {
  font-family: Arial, sans-serif;
}

.chart-container[data-v-a83bd3b0] {
  height: 290px;
  padding-top: 10px;
  padding-bottom: 10px;
}

.like {
  cursor: pointer;
  font-size: 25px;
  display: inline-block;
}
</style>

