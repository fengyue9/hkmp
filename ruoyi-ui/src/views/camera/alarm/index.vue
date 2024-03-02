<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
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
          v-hasPermi="['device:record:add']"
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
          v-hasPermi="['device:record:remove']"
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
          v-hasPermi="['device:record:export']"
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
            @click="handleUpdate(scope.row)"
            v-hasPermi="['device:record:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['device:record:remove']"
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
</template>

<script>
import {listRecord, getRecord, delRecord, addRecord, updateRecord} from "@/api/camera/alarm/alarm_record";

export default {
  name: "Record",
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 非单个禁用
      single: true,
      // 非多个禁用
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
        alarmType: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {}
    };
  },
  created() {
    this.getList();
  },
  methods: {
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
    handleUpdate(row) {
      this.reset();
      const deviceId = row.deviceId || this.ids
      getRecord(deviceId).then(response => {
        this.form = response.data;
        this.open = true;
        this.title = "修改报警记录";
      });
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
    }
  }
};
</script>

<!---------------------------------------------------------------------------------------------------->
<!--<template>-->
<!--  <div class="app-container">-->
<!--    &lt;!&ndash; 实时报警显示 &ndash;&gt;-->
<!--    <el-row>-->
<!--      <el-col :span="24">-->
<!--        <h2>实时报警</h2>-->
<!--        <el-table :data="realTimeAlarms" style="width: 100%">-->
<!--          <el-table-column label="报警类型" prop="alarmType"></el-table-column>-->
<!--          <el-table-column label="位置" prop="location"></el-table-column>-->
<!--          <el-table-column label="时间戳" prop="timestamp"></el-table-column>-->
<!--          <el-table-column label="操作">-->
<!--            <template slot-scope="scope">-->
<!--              <el-button plain @click="acknowledgeAlarm(scope.row);">确认</el-button>-->
<!--              <el-button plain type="danger" @click="escalateAlarm(scope.row);">升级</el-button>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--        </el-table>-->
<!--      </el-col>-->
<!--    </el-row>-->
<!--    &lt;!&ndash; 报警历史 &ndash;&gt;-->
<!--    <el-row>-->
<!--      <el-col :span="24">-->
<!--        <h2>报警历史</h2>-->
<!--        <el-table :data="alarmHistory" style="width: 100%">-->
<!--          <el-table-column label="报警类型" prop="alarmType"></el-table-column>-->
<!--          <el-table-column label="位置" prop="location"></el-table-column>-->
<!--          <el-table-column label="时间戳" prop="timestamp"></el-table-column>-->
<!--          <el-table-column label="操作">-->
<!--            <template slot-scope="scope">-->
<!--              <el-button-->
<!--                v-if="scope.row.alarmType !== null"-->
<!--                @click="viewAlarmDetails(scope.row)"-->
<!--              >-->
<!--                详情-->
<!--              </el-button>-->
<!--              <el-button-->
<!--                v-else-->
<!--                disabled-->
<!--              >-->
<!--                无效详情-->
<!--              </el-button>-->
<!--            </template>-->
<!--          </el-table-column>-->
<!--        </el-table>-->
<!--      </el-col>-->
<!--    </el-row>-->
<!--    &lt;!&ndash; 详情模态框 &ndash;&gt;-->
<!--    <el-dialog-->
<!--      :visible="detailDialogVisible"-->
<!--      title="报警详情"-->
<!--      @close="detailDialogVisible = false"-->
<!--    >-->
<!--      <p v-if="selectedAlarm">报警类型: {{ selectedAlarm.alarmType }}</p>-->
<!--      <p v-if="selectedAlarm">位置: {{ selectedAlarm.location }}</p>-->
<!--      <p v-if="selectedAlarm">时间戳: {{ selectedAlarm.timestamp }}</p>-->
<!--      <p v-if="selectedAlarm">待添加...</p>-->
<!--      &lt;!&ndash; 在这里添加其他报警信息的详细内容 &ndash;&gt;-->
<!--    </el-dialog>-->
<!--  </div>-->
<!--</template>-->

<!--<script>-->

<!--export default {-->
<!--  components: {},-->
<!--  data() {-->
<!--    return {-->
<!--      realTimeAlarms: [-->
<!--        { alarmType: '移动侦测', location: '大厅', timestamp: '2023-12-01 08:30:00' },-->
<!--        { alarmType: '移动侦测', location: '办公室', timestamp: '2023-12-01 09:15:00' },-->
<!--        { alarmType: '移动侦测', location: '仓库', timestamp: '2023-12-01 10:00:00' }-->
<!--      ],-->
<!--      alarmHistory: [-->
<!--        { alarmType: '移动侦测', location: '前门', timestamp: '2023-11-30 15:45:00' },-->
<!--        { alarmType: '移动侦测', location: '实验室', timestamp: '2023-11-30 16:20:00' },-->
<!--        { alarmType: '移动侦测', location: '厨房', timestamp: '2023-11-30 17:10:00' }-->
<!--      ],-->
<!--      detailDialogVisible: false,-->
<!--      selectedAlarm: null-->
<!--    }-->
<!--  },-->
<!--  methods: {-->
<!--    acknowledgeAlarm(alarm) {-->
<!--      // 实现确认逻辑，可以将确认的报警移动到报警历史中-->
<!--      this.alarmHistory.push(alarm)-->
<!--      this.realTimeAlarms = this.realTimeAlarms.filter(a => a !== alarm)-->
<!--      this.sendAlarmMessage()-->
<!--    },-->
<!--    escalateAlarm(alarm) {-->
<!--      // 实现升级逻辑-->
<!--      // 可以将升级后的报警发送到相应的处理系统-->
<!--      this.sendHigherAlarmMessage()-->
<!--    },-->
<!--    viewAlarmDetails(alarm) {-->
<!--      // 实现查看报警详情的逻辑-->
<!--      this.selectedAlarm = alarm-->
<!--      this.detailDialogVisible = true-->
<!--    }, sendAlarmMessage() {-->
<!--      const h = this.$createElement-->
<!--      this.$notify({-->
<!--        title: '报警处理',-->
<!--        message: h('i', { style: 'color: teal' }, '已移入报警历史')-->
<!--      })-->
<!--      // 其他方法-->
<!--    }, sendHigherAlarmMessage() {-->
<!--      const h = this.$createElement-->

<!--      this.$notify({-->
<!--        title: '升级报警',-->
<!--        message: h('i', { style: 'color: teal' }, '已升级报警')-->
<!--      })-->
<!--      // 其他方法-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</script>-->
