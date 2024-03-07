<template>
  <div class="alarm-center">
    <el-row>
      <el-col :xs="24" :sm="5" class="device-list">
        <el-card>
          <h3 slot="header">设备列表</h3>
          <el-input v-model="searchKeyword" placeholder="请输入设备名称搜索" clearable
                    @clear="handleClearSearch"></el-input>
          <div style="height: 730px;">
            <el-scrollbar>
              <el-menu :default-active="activeDevice" @select="handleDeviceSelect" class="device-menu">
                <el-menu-item class="device-menu-item" v-for="device in filteredDevices" :key="device.deviceId"
                              :index="device.deviceId.toString()">
                  {{ device.deviceName }}
                </el-menu-item>
              </el-menu>
            </el-scrollbar>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="19" class="alarm-records">
        <el-card>
          <h3 slot="header">报警记录</h3>
          <div style="height: 765px;">
            <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch"
                     label-width="68px">
              <el-form-item label="报警时间" prop="alarmTime">
                <el-date-picker clearable
                                v-model="queryParams.alarmTime"
                                type="date"
                                value-format="yyyy-MM-dd"
                                placeholder="请选择报警时间">
                </el-date-picker>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>
                <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>
              </el-form-item>
            </el-form>

            <el-row :gutter="10" class="mb8">
              <el-col :span="1.5">
                <el-button
                  type="warning"
                  plain
                  icon="el-icon-download"
                  size="mini"
                  @click="handleExport"
                >Excel导出
                </el-button>
              </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
            </el-row>
            <el-row>
              <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
                <el-table-column type="selection" width="55" align="center"/>
                <el-table-column label="报警抓图" align="center" prop="screenshotURL" width="300">
                  <template slot-scope="scope">
                    <image-preview :src="scope.row.screenshotURL" :width="70" :height="70"/>
                  </template>
                </el-table-column>
                <el-table-column label="报警类型" align="center" prop="alarmType">
                  <template slot-scope="scope">
                    <el-tag>{{ scope.row.alarmType }}</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="报警时间" align="center" prop="alarmTime" width="180">
                  <template slot-scope="scope">
                    <span>{{ scope.row.alarmTime }}</span>
                  </template>
                </el-table-column>
                <el-table-column label="状态" align="center" prop="deviceId">
                  <template slot-scope="scope">
                    <el-tag v-if="scope.row.alarmStatus === '0'" type="danger">未处理</el-tag>
                    <el-tag v-if="scope.row.alarmStatus === '1'" type="success">已处理</el-tag>
                  </template>
                </el-table-column>
                <el-table-column label="处理描述" align="center" prop="alarmDesc"/>
                <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                  <template slot-scope="scope">
                    <el-button
                      size="mini"
                      type="text"
                      icon="el-icon-edit"
                      @click="handle(scope.row)"
                    >处理
                    </el-button>
                  </template>
                </el-table-column>
              </el-table>

              <pagination
                v-show="total>0"
                :total="total"
                :page.sync="queryParams.pageNum"
                :limit.sync="queryParams.pageSize"
                @pagination="getList"
              />
            </el-row>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <!--    报警处理对话框-->
    <el-dialog :visible.sync="dialogVisible"
               width="60%"
               :show-close=false
               :close-on-click-modal=false
               :closeOnClickModal=false
               :close="handleClose">
      <el-container>
        <el-header height="50%" style="text-align:center;background-color:#fff;font-size: 30px">报警事件详情</el-header>
        <el-container>
          <el-container>
            <el-main style="background-color:#fff;padding-bottom: 20px;">
              <video ref="video" controls autoplay style="width: 100%;height: 60%;"></video>
              <el-row style="padding-top: 10px;">
                <el-card style="height: 200px;">
                  <div style="text-align: center;font-size: 20px; ">处理描述</div>
                  <el-input :autosize="{ minRows: 5, maxRows: 5}" style="padding-top: 10px;padding-bottom: 10px;"
                            type="textarea" v-model="handleAlarmDesc"></el-input>
                </el-card>
              </el-row>
              <el-row>
                <div style="text-align: center;padding-top: 10px;">
                  <span slot="footer">
                    <el-button type="primary" @click="handleClose">返回</el-button>
                  </span>
                  <el-button style="margin-left: 10px;" type="primary" @click="handleAlarm">确认处理
                  </el-button>
                </div>
              </el-row>
            </el-main>
          </el-container>
        </el-container>
      </el-container>
    </el-dialog>
  </div>
</template>

<script>
import {listDevice} from "@/api/camera/device";
import {
  listRecord,
  getRecord,
  delRecord,
  addRecord,
  updateRecord,
  downloadVideo, handleAlarm
} from "@/api/camera/alarm/alarm_record";

export default {
  data() {
    return {
      activeDevice: '',
      searchDate: '', // 搜索日期
      searchType: '', // 搜索类型
      devices: [],
      alarmRecords: [
        {time: "2024-03-05 10:12:20", type: "入侵报警", description: "检测到人员入侵"},
        {time: "2024-03-05 10:15:45", type: "异常行为", description: "检测到异常行为"},
        {time: "2024-03-05 10:18:30", type: "火警报警", description: "检测到火焰"}
      ],
      dialogVisible: false,
      selectedRecord: {image: "", video: ""},
      searchKeyword: '', // 搜索关键词
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,// 非多个禁用
      multiple: true,
      // 显示搜索条件
      showSearch: true,
      // 总条数
      total: 0,
      // 报警记录表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 6,
        alarmTime: null,
        alarmType: null,
        deviceId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      //报警处理描述
      handleAlarmDesc: null,
      //点击处理的报警记录id
      selectedAlarmRecordId: null,
      //视频播放的URL
      videoUrl: '',
    }
  },
  created() {
    this.getList();
  },
  mounted() {
    this.queryDeviceList();
  },
  methods: {
    handleDeviceSelect(id) {
      // 根据选中的设备ID加载相应的报警记录
      console.log(id);
      this.activeDevice = id;
      this.loadAlarmRecords(id);
    },
    loadAlarmRecords(deviceId) {
      // 调用接口加载该设备的报警记录
      this.queryParams.deviceId = deviceId;
      this.getList();
    },
    handleView(record) {
      // 查看报警详情，展示抓图和视频
      this.selectedRecord = record;
      this.dialogVisible = true;
    },
    //查询左侧设备列表
    queryDeviceList() {
      listDevice(this.queryParams).then(response => {
        this.devices = response.rows;
      })
    },
    handleClearSearch() {
      // 清空搜索关键词时恢复原始设备列表
      this.searchKeyword = '';
    },
    /** 查询报警记录列表 */
    getList() {
      this.loading = true;
      listRecord(this.queryParams).then(response => {
        this.recordList = response.rows;
        this.total = response.total;
        this.loading = false;
      });
    },
    // 取消按钮
    cancel() {
      this.open = false;
      this.reset();
    },
    // 表单重置
    reset() {
      this.form = {
        deviceId: null,
        alarmTime: null,
        alarmType: null
      };
      this.resetForm("form");
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm");
      this.handleQuery();
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.deviceId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset();
      this.open = true;
      this.title = "添加报警记录";
    },
    //处理按钮点击事件
    handle(row) {
      this.dialogVisible = true;
      this.selectedAlarmRecordId = row.alarmRecordId;
      //播放视频
      const alarmRecordId = row.alarmRecordId;
      downloadVideo(alarmRecordId).then(response => {
        //生成视频的URL
        const videoUrl = URL.createObjectURL(response);
        this.videoUrl = videoUrl;
        this.$refs.video.src = videoUrl;
        this.$refs.video.play();
        //默认播放速度为1x
        this.$refs.video.playbackRate = 1;
      }).catch(error => {
        console.error('播放视频失败:', error);
      });
    },
    //报警处理点击事件
    handleAlarm() {
      //构造参数
      const data = {
        alarmRecordId: this.selectedAlarmRecordId,
        alarmDesc: this.handleAlarmDesc
      }
      //发送处理报警请求
      handleAlarm(data).then(response => {
        this.$message({
          message: '报警处理成功!',
          type: 'success',
          center: true
        });
      });
    },
    handleClose() {
      // 停止视频播放
      this.$refs.video.pause();
      // 释放视频 URL 对应的资源
      URL.revokeObjectURL(this.videoUrl);
      this.dialogVisible = false;
      this.handleAlarmDesc = null;
      this.getList();
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const deviceIds = row.deviceId || this.ids;
      this.$modal.confirm('是否确认删除报警记录编号为"' + deviceIds + '"的数据项？').then(function () {
        return delRecord(deviceIds);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('device/record/export', {
        ...this.queryParams
      }, `record_${new Date().getTime()}.xlsx`)
    },
  },
  computed: {
    // 根据搜索关键词过滤设备列表
    filteredDevices() {
      return this.devices.filter(device => device.deviceName.toLowerCase().includes(this.searchKeyword.toLowerCase()));
    }
  }
  ,
};
</script>

<style scoped>
.alarm-center {
  padding: 20px;
}

.device-menu {
  border-right: none; /* 隐藏右侧虚线 */
}

.device-list {
  margin-bottom: 20px; /* 左侧设备列表下边距 */
}

.device-menu-item {
  font-size: 16px;
}


.el-container:nth-child(5) .el-aside,
.el-container:nth-child(6) .el-aside {
  line-height: 260px;
}

.el-container:nth-child(7) .el-aside {
  line-height: 320px;
}
</style>


