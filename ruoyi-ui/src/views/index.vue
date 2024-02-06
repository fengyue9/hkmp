<template>
  <div class="app-container home">
    <el-row :gutter="20">
      <el-col :sm="24" :lg="24">
        <h2><b><i>基于Spring和Vue的实时监控报警系统</i></b></h2>
        <blockquote class="text-warning" style="font-size: 14px">
          支持功能：摄像头实时预览、云台操作、回放下载、报警处理、设备信息管理、系统监控等
        </blockquote>

        <hr/>
      </el-col>
    </el-row>
    <el-row :gutter="20">
      <el-col :sm="24" :lg="12" style="padding-left: 20px">
        <h2>工具系统——功能类似海康iVMS-4200客户端的Web监控系统(hkmp)</h2>
        <p style="font-size: large">
          iVMS-4200客户端是海康威视提供的一款免费的、与网络监控设备配套使用的应用软件。它可与
          DVR、NVR、IPC、IPD、DVS、网络存储设备、报警设备、门禁设备、可视对讲设备等配套使用，提供网络服务（预览、回放、云台等操作），提供灵活、多样的部署方案，满足中、小型项目中各种不同环境的需求，并可广泛应用于金融、公安、部队、电信、交通、电力、教育、水利等领域的安防项目。然而，客户端软件无法集成到自有平台中。除了iVMS-4200客户端，海康威视还提供了Web开发接口供用户集成，支持网页上实现预览、回放、云台控制等功能。因此，基于海康的Web开发接口，实现类似iVMS-4200客户端功能的Web监控系统，能便捷地接入自有平台。 </p>
        <p>
          <b>当前版本:</b> <span>v{{ version }}</span>
        </p>


      </el-col>

      <el-col :sm="24" :lg="12" style="padding-left: 50px">
        <el-calendar v-model="value">
        </el-calendar>
        <!--        <el-row>-->
        <!--          <el-col :span="20">-->
        <!--            <h2>技术选型————截止至2023年10月22日</h2>-->
        <!--          </el-col>-->
        <!--        </el-row>-->
        <!--        <el-row>-->
        <!--          <el-col :span="6">-->
        <!--            <h4>后端技术</h4>-->
        <!--            <ul>-->
        <!--              <li>SpringBoot</li>-->
        <!--              <li>Spring Security</li>-->
        <!--              <li>JWT</li>-->
        <!--              <li>MyBatis</li>-->
        <!--              <li>Redis</li>-->
        <!--              <li>...</li>-->
        <!--            </ul>-->
        <!--          </el-col>-->
        <!--          <el-col :span="6">-->
        <!--            <h4>前端技术</h4>-->
        <!--            <ul>-->
        <!--              <li>Vue</li>-->
        <!--              <li>Vuex</li>-->
        <!--              <li>Element-ui</li>-->
        <!--              <li>Axios</li>-->
        <!--              <li>...</li>-->
        <!--            </ul>-->
        <!--          </el-col>-->
        <!--        </el-row>-->
      </el-col>
    </el-row>
    <el-divider/>

  </div>
</template>

<script>
export default {
  name: 'Index',
  data() {
    return {
      // 版本号
      version: '1.0.0.0',
      value: new Date()
    }
  },
  methods: {
    goTarget(href) {
      window.open(href, '_blank')
    }
  }
}
</script>

<style scoped lang="scss">
.home {
  blockquote {
    padding: 10px 20px;
    margin: 0 0 20px;
    font-size: 17.5px;
    border-left: 5px solid #eee;
  }

  hr {
    margin-top: 20px;
    margin-bottom: 20px;
    border: 0;
    border-top: 1px solid #eee;
  }

  .col-item {
    margin-bottom: 20px;
  }

  ul {
    padding: 0;
    margin: 0;
  }

  font-family: "open sans", "Helvetica Neue", Helvetica, Arial, sans-serif;
  font-size: 13px;
  color: #676a6c;
  overflow-x: hidden;

  ul {
    list-style-type: none;
  }

  h4 {
    margin-top: 0px;
  }

  h2 {
    margin-top: 10px;
    font-size: 26px;
    font-weight: 100;
  }

  p {
    margin-top: 10px;

    b {
      font-weight: 700;
    }
  }

  .update-log {
    ol {
      display: block;
      list-style-type: decimal;
      margin-block-start: 1em;
      margin-block-end: 1em;
      margin-inline-start: 0;
      margin-inline-end: 0;
      padding-inline-start: 40px;
    }
  }
}
</style>

