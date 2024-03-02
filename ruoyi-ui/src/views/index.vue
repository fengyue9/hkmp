<template>
  <div class="app-container home">
    <d-title :style="{textAlign: 'center'}">
      <h1 style="font-size: 36px; color: #333; margin-bottom: 10px;">基于Spring和Vue的实时监控报警系统</h1>
      <p style="font-size: 18px; color: #666;">
        摄像头实时预览、云台操作、抓图、录像、回放、下载、报警、设备管理、系统监控等</p>
    </d-title>
    <el-row>
      <el-card>
        <h2><b>系统资源仪表盘</b></h2>
        <!-- 实时监控图表 -->
        <div class="charts-container">
          <div class="chart" ref="cpuChart" style="height: 300px;"></div>
          <div class="chart" ref="memoryChart" style="height: 300px;"></div>
          <div class="chart" ref="diskChart" style="height: 300px;"></div>

        </div>
      </el-card>
    </el-row>
    <div class="weather-container">
      <el-row>
        <el-col>
          <el-card class="weather-card" v-loading="loading">
            <template #header>
              <div class="weather-header">
                <span class="title">天气预报</span>
                <el-button type="text" @click="handleWeather" icon="el-icon-refresh-right">刷新</el-button>
              </div>
            </template>
            <div v-if="city">
              <el-row :gutter="20">
                <!-- Current Weather -->
                <el-col :span="12">
                  <div class="current-weather">
                    <h2>{{ city }}当前实时天气</h2>
                    <div class="weather-info">
                      <p><span class="label">温度:</span> {{ weather.realtime.temperature }}℃</p>
                      <p><span class="label">风向:</span> {{ weather.realtime.direct }}</p>
                      <p><span class="label">风力:</span> {{ weather.realtime.power }}</p>
                      <p><span class="label">湿度:</span> {{ weather.realtime.humidity }}%</p>
                      <p><span class="label">天气状况:</span> {{ weather.realtime.info }}</p>
                    </div>
                  </div>
                </el-col>
                <!-- Future Weather -->
                <el-col :span="12">
                  <div class="future-weather" v-if="weather.future.length > 0">
                    <h2>未来天气预报</h2>
                    <el-row :gutter="20">
                      <el-col :span="24">
                        <el-carousel :interval="4000">
                          <el-carousel-item v-for="(item, index) in weather.future" :key="index">
                            <el-card class="future-weather-item">
                              <h3>{{ item.date }}</h3>
                              <p><span class="label">温度:</span> {{ item.temperature }}</p>
                              <p><span class="label">风向:</span> {{ item.direct }}</p>
                              <p><span class="label">天气情况:</span> {{ item.weather }}</p>
                            </el-card>
                          </el-carousel-item>
                        </el-carousel>
                      </el-col>
                    </el-row>
                  </div>
                </el-col>
              </el-row>
            </div>
            <div v-else-if="!loading && !city && weather.future.length === 0" class="no-data">
              无数据，或已超出今日查询次数
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import * as echarts from 'echarts'
import {getWeather} from "@/api/gzh/weather";
import {getSystemResourceUsage} from "@/api/camera/monitor/monitor";

export default {
  name: 'Index',
  data() {
    return {
      // 版本号
      version: '1.0.0.0',
      diskData: null,
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
      city: '',
      weather: {
        realtime: {},
        future: []
      },
      loading: false,

    }
  },
  mounted() {
    this.cpuChart = echarts.init(this.$refs.cpuChart);
    this.memoryChart = echarts.init(this.$refs.memoryChart);
    this.diskChart = echarts.init(this.$refs.diskChart);
    // 每隔 5 秒更新一次系统资源信息
    setInterval(() => {
      this.fetchSystemUsage();
    }, 5000);
    // 初始加载系统资源信息
    this.fetchSystemUsage();
    //加载天气预报信息
    this.handleWeather();
  },
  methods: {
    handleWeather() {
      this.loading = true
      getWeather().then(res => {
        const weatherInfo = JSON.parse(res.msg)
        this.city = weatherInfo.result.city
        this.weather.realtime = weatherInfo.result.realtime
        this.weather.future = weatherInfo.result.future
        console.log(weatherInfo)
        this.loading = false
      }).catch(err => {
        console.error(err)
        this.loading = false
      })
    },
    updateChart(chart, title, value) {
      chart.setOption({
        title: {
          text: title,
          left: 'center'
        },
        series: [{
          type: 'gauge',
          detail: {formatter: '{value}%'},
          data: [{value: value, name: '使用率'}]
        }]
      });
    },
    fetchSystemUsage() {
      getSystemResourceUsage().then(response => {
        // 成功获取数据后更新实时数据变量
        const cpuUsageFixed = (response.cpuUsage * 100).toFixed(3);
        // 将小数转换为百分比并保留小数点后三位
        const memeryUsageFixed = response.memoryUsage.toFixed(3); // 将小数转换为百分比并保留小数点后三位
        // 更新磁盘使用情况数据
        this.diskData = [
          {name: '已使用空间', value: response.usedSpacePercentage},
          {name: '剩余空间', value: response.freeSpacePercentage}
        ];

        // 渲染磁盘使用情况饼图
        this.renderDiskChart(this.diskData);
        // 将小数转换为百分比并保留小数点后三位
        this.updateChart(this.cpuChart, 'CPU使用率', cpuUsageFixed)
        this.updateChart(this.memoryChart, '内存使用率', memeryUsageFixed)
      })
    },
    renderDiskChart(data) {
      this.diskChart.setOption({
        title: {
          text: '磁盘使用情况',
          left: 'center'
        },
        tooltip: {
          trigger: 'item',
          formatter: '{a} <br/>{b}: {c} ({d}%)'
        },
        series: [
          {
            name: '磁盘使用情况',
            type: 'pie',
            radius: ['50%', '70%'],
            avoidLabelOverlap: false,
            label: {
              show: true,
              position: 'inside', // 或者 'outside'
              formatter: '{b}: {d}%', // 修改标签格式
              fontSize: 14
            },
            emphasis: {
              label: {
                show: true,
                fontSize: 16,
                fontWeight: 'bold'
              }
            },
            labelLine: {
              show: true,
              length: 20,
              length2: 5
            },
            data: data
          }
        ]
      });

    }
  }
}
</script>

<style scoped>


.charts-container {
  display: flex;
}

.chart {
  flex: 1;
  height: 300px;
  margin-right: 20px;
}


.circle text {
  font-family: Arial, sans-serif;
}


.title-container {
  text-align: center;
  margin-bottom: 20px;
}

.title {
  font-size: 36px;
  color: #333;
  margin-bottom: 10px;
}

.subtitle {
  font-size: 18px;
  color: #666;
}

</style>

