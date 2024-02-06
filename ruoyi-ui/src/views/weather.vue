<template>
  <div v-loading="loading">
    <el-row style="margin-top: 30px;" :gutter="20">
      <el-col :offset="10" :span="4">
        <el-button type="success" @click="handleWeather">当前城市天气</el-button>
      </el-col>
    </el-row>
    <el-row :gutter="20" v-if="city.length>0">
      <el-col :offset="2" :span="20">
        <el-descriptions title="当前实时天气">
          <el-descriptions-item label="当前城市">{{ city }}</el-descriptions-item>
          <el-descriptions-item label="温度">{{ weather.realtime.temperature }}℃</el-descriptions-item>
          <el-descriptions-item label="风向">{{ weather.realtime.direct }}</el-descriptions-item>
          <el-descriptions-item label="风力">{{ weather.realtime.power }}</el-descriptions-item>
          <el-descriptions-item label="湿度">{{ weather.realtime.humidity }}%</el-descriptions-item>
          <el-descriptions-item label="天气状况">{{ weather.realtime.info }}</el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
    <el-row v-for="item in weather.future" :key="item.date" style="margin-top: 30px;" :gutter="20">
      <el-col :offset="2" :span="20">
        <el-descriptions :title="item.date" :column="4">
          <el-descriptions-item label="风向">{{ item.direct }}</el-descriptions-item>
          <el-descriptions-item label="温度">{{ item.temperature }}</el-descriptions-item>
          <el-descriptions-item label="天气情况">{{ item.weather }}</el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
  </div>
</template>

<script>
import { getWeather } from '@/api/gzh/weather'

export default {
  name: 'weather',
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
      })
    }
  }
}
</script>

<style scoped>

</style>
