<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="设备名称" prop="deviceName">
        <el-input
          v-model="queryParams.deviceName"
          placeholder="请输入设备名称"
          clearable
          @keyup.enter.native="handleQuery"
        />
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
          v-hasPermi="['camera:device:add']"
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
          v-hasPermi="['camera:device:edit']"
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
          v-hasPermi="['camera:device:remove']"
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
          v-hasPermi="['camera:device:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="deviceList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="设备id" align="center" prop="deviceId"/>
      <el-table-column label="设备名称" align="center" prop="deviceName"/>
      <el-table-column label="ip地址" align="center" prop="deviceIp"/>
      <el-table-column label="端口" align="center" prop="devicePort"/>
      <el-table-column label="设备序列号" align="center" prop="deviceSerialNumber"/>
      <el-table-column label="状态" align="center" prop="deviceStatus">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.deviceStatus === '0'" type="success">在线</el-tag>
          <el-tag v-if="scope.row.deviceStatus === '1'" type="danger">离线</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="备注" align="center" prop="remark"/>
      <el-table-column label="用户名" align="center" prop="deviceUsername"/>
      <el-table-column label="密码" align="center" prop="devicePassword"/>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['camera:device:edit']"
          >修改
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['camera:device:remove']"
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

    <!-- 添加或修改设备信息管理对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="设备名称" prop="deviceName">
          <el-input v-model="form.deviceName" placeholder="请输入设备名称"/>
        </el-form-item>
        <el-form-item label="ip地址" prop="deviceIp">
          <el-input v-model="form.deviceIp" placeholder="请输入ip地址"/>
        </el-form-item>
        <el-form-item label="端口" prop="devicePort">
          <el-input v-model="form.devicePort" placeholder="请输入端口"/>
        </el-form-item>
        <el-form-item label="序列号" prop="deviceSerialNumber">
          <el-input v-model="form.deviceSerialNumber" placeholder="请输入设备序列号"/>
        </el-form-item>
        <!--        <el-form-item label="状态" prop="deviceStatus">-->
        <!--          <el-radio-group v-model="form.deviceStatus">-->
        <!--            <el-radio-->
        <!--              v-for="dict in dict.type.sys_normal_disable"-->
        <!--              :key="dict.value"-->
        <!--              :label="dict.value"-->
        <!--            >{{ dict.label }}-->
        <!--            </el-radio>-->
        <!--          </el-radio-group>-->
        <!--        </el-form-item>-->
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容"/>
        </el-form-item>
        <el-form-item label="用户名" prop="deviceUsername">
          <el-input v-model="form.deviceUsername" placeholder="请输入用户名"/>
        </el-form-item>
        <el-form-item label="密码" prop="devicePassword">
          <el-input v-model="form.devicePassword" placeholder="请输入密码"/>
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
import {listDevice, getDevice, delDevice, addDevice, updateDevice} from '@/api/camera/device'
import Vue from 'vue'

export default {
  name: 'Device',
  dicts: ['sys_normal_disable'],
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
      // 设备信息管理表格数据
      deviceList: [],
      // 弹出层标题
      title: '',
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        deviceName: null,
        deviceStatus: null
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        deviceIp: [
          {required: true, message: 'ip地址不能为空', trigger: 'blur'}
        ],
        devicePort: [
          {required: true, message: '端口不能为空', trigger: 'blur'}
        ]
      },
      url: "ws://127.0.0.1:8080/websocket/message",
      message: "",
      text_content: "",
      ws: null,
    }
  },
  created() {
    this.getList()
    // 创建 EventBus 实例
    this.eventBus = new Vue()

    // 监听设备选择事件
    this.eventBus.$on('deviceSelected', (selectedDevice) => {
      // 处理选中的设备，比如更新组件数据等
      console.log('Selected Device:', selectedDevice)
    })
  },
  methods: {
    /** 查询设备信息管理列表 */
    getList() {
      this.loading = true
      listDevice(this.queryParams).then(response => {
        this.deviceList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    // 取消按钮
    cancel() {
      this.open = false
      this.reset()
    },
    // 表单重置
    reset() {
      this.form = {
        deviceId: null,
        deviceName: null,
        deviceIp: null,
        devicePort: null,
        deviceSerialNumber: null,
        deviceStatus: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        remark: null,
        deviceUsername: null,
        devicePassword: null
      }
      this.resetForm('form')
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm('queryForm')
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.deviceId)
      this.single = selection.length !== 1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = '添加设备信息管理'
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      this.reset()
      const deviceId = row.deviceId || this.ids
      getDevice(deviceId).then(response => {
        this.form = response.data
        this.open = true
        this.title = '修改设备信息管理'
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs['form'].validate(valid => {
        if (valid) {
          if (this.form.deviceId != null) {
            updateDevice(this.form).then(response => {
              this.$modal.msgSuccess('修改成功')
              this.open = false
              this.getList()
            })
          } else {
            addDevice(this.form).then(response => {
              this.$modal.msgSuccess('新增成功')
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const deviceIds = row.deviceId || this.ids
      this.$modal.confirm('是否确认删除设备信息管理编号为"' + deviceIds + '"的数据项？').then(function () {
        return delDevice(deviceIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess('删除成功')
      }).catch(() => {
      })
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('camera/device/export', {
        ...this.queryParams
      }, `device_${new Date().getTime()}.xlsx`)
    },
    /** 连接 */
    join() {
      const wsUri = this.url;
      this.ws = new WebSocket(wsUri);
      const self = this;
      this.ws.onopen = function (event) {
        self.text_content = self.text_content + "已经打开连接!" + "\n";
      };
      this.ws.onmessage = function (event) {
        self.text_content = event.data + "\n";
        //查询列表信息
        self.getList();
      };
      this.ws.onclose = function (event) {
        self.text_content = self.text_content + "已经关闭连接!" + "\n";
      };
    },
    /** 退出 */
    exit() {
      if (this.ws) {
        this.ws.close();
        this.ws = null;
      }
    },
    /** 发送消息 */
    send() {
      if (this.ws) {
        this.ws.send(this.message);
      } else {
        alert("未连接到服务器");
      }
    },
    dictTypeSysNormalDisable(status) {
      return status === '0' ? [{label: '在线', value: '0'}] : [{label: '离线', value: '1'}];
    }
  },
  //组件加载完成之后
  mounted() {
    //连接websocket
    this.join();
  }
  ,
  beforeDestroy() {
    //释放websocket
    this.exit();
  }
}
</script>
