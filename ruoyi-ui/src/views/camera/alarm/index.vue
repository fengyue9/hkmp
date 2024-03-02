<template>
  <div class="alarm-center">
    <el-row>
      <el-col :xs="24" :sm="8" class="device-list">
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
      <el-col :xs="24" :sm="16" class="alarm-records">
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
                  type="primary"
                  plain
                  icon="el-icon-plus"
                  size="mini"
                  @click="handleAdd"
                >新增
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="success"
                  plain
                  icon="el-icon-edit"
                  size="mini"
                  :disabled="single"
                  @click="handleUpdate"
                  v-hasPermi="['device:record:edit']"
                >修改
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="danger"
                  plain
                  icon="el-icon-delete"
                  size="mini"
                  :disabled="multiple"
                  @click="handleDelete"
                >删除
                </el-button>
              </el-col>
              <el-col :span="1.5">
                <el-button
                  type="warning"
                  plain
                  icon="el-icon-download"
                  size="mini"
                  @click="handleExport"
                >导出
                </el-button>
              </el-col>
              <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
            </el-row>

            <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
              <el-table-column type="selection" width="55" align="center"/>
              <el-table-column label="设备id" align="center" prop="deviceId"/>
              <el-table-column label="报警时间" align="center" prop="alarmTime" width="180">
                <template slot-scope="scope">
                  <span>{{ scope.row.alarmTime }}</span>
                </template>
              </el-table-column>
              <el-table-column label="报警类型" align="center" prop="alarmType">
                <template slot-scope="scope">
                  <el-tag>{{ scope.row.alarmType }}</el-tag>

                </template>
              </el-table-column>
              <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
                <template slot-scope="scope">
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-edit"
                    @click="handle(scope.row)"
                  >处理
                  </el-button>
                  <el-button
                    size="mini"
                    type="text"
                    icon="el-icon-delete"
                    @click="handleDelete(scope.row)"
                  >删除
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

            <!-- 添加或修改报警记录对话框 -->
            <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
              <el-form ref="form" :model="form" :rules="rules" label-width="80px">
                <el-form-item label="报警时间" prop="alarmTime">
                  <el-date-picker clearable
                                  v-model="form.alarmTime"
                                  type="date"
                                  value-format="yyyy-MM-dd HH:mm:ss"
                                  placeholder="请选择报警时间">
                  </el-date-picker>
                </el-form-item>
              </el-form>
              <div slot="footer" class="dialog-footer">
                <el-button type="primary" @click="submitForm">确 定</el-button>
                <el-button @click="cancel">取 消</el-button>
              </div>
            </el-dialog>
          </div>

        </el-card>
      </el-col>
    </el-row>
    <!--    报警处理对话框-->
    <el-dialog :visible.sync="dialogVisible" width="50%">
      <img :src="selectedRecord.image" alt="报警抓图" style="width: 100%">
      <video :src="selectedRecord.video" controls autoplay style="width: 100%"></video>
      <span>处理描述：</span>
      <el-row style="padding-top: 10px">
        <el-input type="textarea" v-model="form.desc"></el-input>
        <el-button type="primary" style="padding-top: 20px" @click="submitForm">确认处理</el-button>
      </el-row>
    </el-dialog>
  </div>
</template>

<script>
import {listDevice} from "@/api/camera/device";
import {listRecord, getRecord, delRecord, addRecord, updateRecord} from "@/api/camera/alarm/alarm_record";

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
        pageSize: 10,
        alarmTime: null,
        alarmType: null,
        deviceId: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
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
      // 示例：this.$http.get(`/api/alarm/records?deviceId=${deviceId}`).then(response => {
      //   this.alarmRecords = response.data;
      // }).catch(error => {
      //   console.error('Failed to load alarm records:', error);
      // });
    },
    handleSearch() {
      // 处理搜索逻辑，根据日期和类型搜索相应的数据
      // 示例：this.$http.get(`/api/alarm/records?date=${this.searchDate}&type=${this.searchType}`).then(response => {
      //   this.alarmRecords = response.data;
      // }).catch(error => {
      //   console.error('Failed to load data:', error);
      // });
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
    /** 修改按钮操作 */
    handle(row) {
      this.reset();
      this.dialogVisible = true;
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.deviceId != null) {
            updateRecord(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addRecord(this.form).then(response => {
              this.$modal.msgSuccess("新增成功");
              this.open = false;
              this.getList();
            });
          }
        }
      });
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

.search-container {
  margin-bottom: 20px; /* 右侧搜索框下边距 */
}

.device-menu-item {
  font-size: 16px;
}
</style>


<!---------------------------------------------------------------------------------------------------->
<!--<template>-->
<!--  <div class="app-container">-->
<!--    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">-->
<!--      <el-form-item label="报警时间" prop="alarmTime">-->
<!--        <el-date-picker clearable-->
<!--                        v-model="queryParams.alarmTime"-->
<!--                        type="date"-->
<!--                        value-format="yyyy-MM-dd"-->
<!--                        placeholder="请选择报警时间">-->
<!--        </el-date-picker>-->
<!--      </el-form-item>-->
<!--      <el-form-item>-->
<!--        <el-button type="primary" icon="el-icon-search" size="mini" @click="handleQuery">搜索</el-button>-->
<!--        <el-button icon="el-icon-refresh" size="mini" @click="resetQuery">重置</el-button>-->
<!--      </el-form-item>-->
<!--    </el-form>-->

<!--    <el-row :gutter="10" class="mb8">-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="primary"-->
<!--          plain-->
<!--          icon="el-icon-plus"-->
<!--          size="mini"-->
<!--          @click="handleAdd"-->
<!--          v-hasPermi="['device:record:add']"-->
<!--        >新增-->
<!--        </el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="success"-->
<!--          plain-->
<!--          icon="el-icon-edit"-->
<!--          size="mini"-->
<!--          :disabled="single"-->
<!--          @click="handleUpdate"-->
<!--          v-hasPermi="['device:record:edit']"-->
<!--        >修改-->
<!--        </el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="danger"-->
<!--          plain-->
<!--          icon="el-icon-delete"-->
<!--          size="mini"-->
<!--          :disabled="multiple"-->
<!--          @click="handleDelete"-->
<!--          v-hasPermi="['device:record:remove']"-->
<!--        >删除-->
<!--        </el-button>-->
<!--      </el-col>-->
<!--      <el-col :span="1.5">-->
<!--        <el-button-->
<!--          type="warning"-->
<!--          plain-->
<!--          icon="el-icon-download"-->
<!--          size="mini"-->
<!--          @click="handleExport"-->
<!--          v-hasPermi="['device:record:export']"-->
<!--        >导出-->
<!--        </el-button>-->
<!--      </el-col>-->
<!--      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>-->
<!--    </el-row>-->

<!--    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">-->
<!--      <el-table-column type="selection" width="55" align="center"/>-->
<!--      <el-table-column label="设备id" align="center" prop="deviceId"/>-->
<!--      <el-table-column label="报警时间" align="center" prop="alarmTime" width="180">-->
<!--        <template slot-scope="scope">-->
<!--          <span>{{ scope.row.alarmTime }}</span>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="报警类型" align="center" prop="alarmType">-->
<!--        <template slot-scope="scope">-->
<!--          <el-tag>{{ scope.row.alarmType }}</el-tag>-->

<!--        </template>-->
<!--      </el-table-column>-->
<!--      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">-->
<!--        <template slot-scope="scope">-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-edit"-->
<!--            @click="handleUpdate(scope.row)"-->
<!--            v-hasPermi="['device:record:edit']"-->
<!--          >修改-->
<!--          </el-button>-->
<!--          <el-button-->
<!--            size="mini"-->
<!--            type="text"-->
<!--            icon="el-icon-delete"-->
<!--            @click="handleDelete(scope.row)"-->
<!--            v-hasPermi="['device:record:remove']"-->
<!--          >删除-->
<!--          </el-button>-->
<!--        </template>-->
<!--      </el-table-column>-->
<!--    </el-table>-->

<!--    <pagination-->
<!--      v-show="total>0"-->
<!--      :total="total"-->
<!--      :page.sync="queryParams.pageNum"-->
<!--      :limit.sync="queryParams.pageSize"-->
<!--      @pagination="getList"-->
<!--    />-->

<!--    &lt;!&ndash; 添加或修改报警记录对话框 &ndash;&gt;-->
<!--    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>-->
<!--      <el-form ref="form" :model="form" :rules="rules" label-width="80px">-->
<!--        <el-form-item label="报警时间" prop="alarmTime">-->
<!--          <el-date-picker clearable-->
<!--                          v-model="form.alarmTime"-->
<!--                          type="date"-->
<!--                          value-format="yyyy-MM-dd HH:mm:ss"-->
<!--                          placeholder="请选择报警时间">-->
<!--          </el-date-picker>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--      <div slot="footer" class="dialog-footer">-->
<!--        <el-button type="primary" @click="submitForm">确 定</el-button>-->
<!--        <el-button @click="cancel">取 消</el-button>-->
<!--      </div>-->
<!--    </el-dialog>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->
<!--import {listRecord, getRecord, delRecord, addRecord, updateRecord} from "@/api/camera/alarm/alarm_record";-->

<!--export default {-->
<!--  name: "Record",-->
<!--  data() {-->
<!--    return {-->
<!--      // 遮罩层-->
<!--      loading: true,-->
<!--      // 选中数组-->
<!--      ids: [],-->
<!--      // 非单个禁用-->
<!--      single: true,-->
<!--      // 非多个禁用-->
<!--      multiple: true,-->
<!--      // 显示搜索条件-->
<!--      showSearch: true,-->
<!--      // 总条数-->
<!--      total: 0,-->
<!--      // 报警记录表格数据-->
<!--      recordList: [],-->
<!--      // 弹出层标题-->
<!--      title: "",-->
<!--      // 是否显示弹出层-->
<!--      open: false,-->
<!--      // 查询参数-->
<!--      queryParams: {-->
<!--        pageNum: 1,-->
<!--        pageSize: 10,-->
<!--        alarmTime: null,-->
<!--        alarmType: null-->
<!--      },-->
<!--      // 表单参数-->
<!--      form: {},-->
<!--      // 表单校验-->
<!--      rules: {}-->
<!--    };-->
<!--  },-->
<!--  created() {-->
<!--    this.getList();-->
<!--  },-->
<!--  methods: {-->
<!--    /** 查询报警记录列表 */-->
<!--    getList() {-->
<!--      this.loading = true;-->
<!--      listRecord(this.queryParams).then(response => {-->
<!--        this.recordList = response.rows;-->
<!--        this.total = response.total;-->
<!--        this.loading = false;-->
<!--      });-->
<!--    },-->
<!--    // 取消按钮-->
<!--    cancel() {-->
<!--      this.open = false;-->
<!--      this.reset();-->
<!--    },-->
<!--    // 表单重置-->
<!--    reset() {-->
<!--      this.form = {-->
<!--        deviceId: null,-->
<!--        alarmTime: null,-->
<!--        alarmType: null-->
<!--      };-->
<!--      this.resetForm("form");-->
<!--    },-->
<!--    /** 搜索按钮操作 */-->
<!--    handleQuery() {-->
<!--      this.queryParams.pageNum = 1;-->
<!--      this.getList();-->
<!--    },-->
<!--    /** 重置按钮操作 */-->
<!--    resetQuery() {-->
<!--      this.resetForm("queryForm");-->
<!--      this.handleQuery();-->
<!--    },-->
<!--    // 多选框选中数据-->
<!--    handleSelectionChange(selection) {-->
<!--      this.ids = selection.map(item => item.deviceId)-->
<!--      this.single = selection.length !== 1-->
<!--      this.multiple = !selection.length-->
<!--    },-->
<!--    /** 新增按钮操作 */-->
<!--    handleAdd() {-->
<!--      this.reset();-->
<!--      this.open = true;-->
<!--      this.title = "添加报警记录";-->
<!--    },-->
<!--    /** 修改按钮操作 */-->
<!--    handleUpdate(row) {-->
<!--      this.reset();-->
<!--      const deviceId = row.deviceId || this.ids-->
<!--      getRecord(deviceId).then(response => {-->
<!--        this.form = response.data;-->
<!--        this.open = true;-->
<!--        this.title = "修改报警记录";-->
<!--      });-->
<!--    },-->
<!--    /** 提交按钮 */-->
<!--    submitForm() {-->
<!--      this.$refs["form"].validate(valid => {-->
<!--        if (valid) {-->
<!--          if (this.form.deviceId != null) {-->
<!--            updateRecord(this.form).then(response => {-->
<!--              this.$modal.msgSuccess("修改成功");-->
<!--              this.open = false;-->
<!--              this.getList();-->
<!--            });-->
<!--          } else {-->
<!--            addRecord(this.form).then(response => {-->
<!--              this.$modal.msgSuccess("新增成功");-->
<!--              this.open = false;-->
<!--              this.getList();-->
<!--            });-->
<!--          }-->
<!--        }-->
<!--      });-->
<!--    },-->
<!--    /** 删除按钮操作 */-->
<!--    handleDelete(row) {-->
<!--      const deviceIds = row.deviceId || this.ids;-->
<!--      this.$modal.confirm('是否确认删除报警记录编号为"' + deviceIds + '"的数据项？').then(function () {-->
<!--        return delRecord(deviceIds);-->
<!--      }).then(() => {-->
<!--        this.getList();-->
<!--        this.$modal.msgSuccess("删除成功");-->
<!--      }).catch(() => {-->
<!--      });-->
<!--    },-->
<!--    /** 导出按钮操作 */-->
<!--    handleExport() {-->
<!--      this.download('device/record/export', {-->
<!--        ...this.queryParams-->
<!--      }, `record_${new Date().getTime()}.xlsx`)-->
<!--    }-->
<!--  }-->
<!--};-->
<!--</script>-->


