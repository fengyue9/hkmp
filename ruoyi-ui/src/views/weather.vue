<template>
  <div class="weather-container">
    <el-row>
      <el-col :span="12">
        <el-card class="weather-card" v-loading="loading">
          <template #header>
            <div class="weather-header">
              <span class="title">天气预报</span>
              <el-button type="text" @click="handleWeather" icon="el-icon-refresh-right">刷新</el-button>
            </div>
          </template>
          <div v-if="city">
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
          </div>
          <div v-else-if="!loading && !city && weather.future.length === 0" class="no-data">无数据</div>
        </el-card>
      </el-col>
      <el-col :span="12">
        <el-card class="datav-chart">
          <template #header>
            <div class="datav-header">
              <span class="title">天气数据可视化</span>
            </div>
          </template>
          <div class="datav-content">
            <!-- Add your DataV visualization components here -->
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import {getWeather} from '@/api/gzh/weather'

export default {
  name: 'Weather',
  data() {
    return {
      loading: false,
      city: '',
      weather: {
        realtime: {},
        future: []
      }
    }
  },
  mounted() {
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
    }
  }
}
</script>

<style scoped>
.weather-container {
  margin-top: 30px;
}

.weather-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.title {
  font-size: 15px;
}

.weather-info .label {
  font-weight: bold;
}

.future-weather-item {
  margin-bottom: 20px;
}

.datav-header {
  text-align: center;
}

.datav-content {
  height: 300px; /* Adjust height according to your visualization */
}

.no-data {
  text-align: center;
  font-size: 16px;
}
</style>
