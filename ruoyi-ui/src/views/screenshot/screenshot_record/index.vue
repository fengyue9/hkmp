<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="抓图日期" prop="screenshotTime">
        <el-date-picker clearable
                        v-model="queryParams.screenshotTime"
                        type="date"
                        value-format="yyyy-MM-dd"
                        placeholder="请选择抓图日期">
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
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['screenshot:screenshot_record:remove']"
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
          v-hasPermi="['screenshot:screenshot_record:export']"
        >Excel导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="screenshot_recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="设备id" width="100" align="center" prop="deviceId"/>
      <el-table-column label="图片" align="center" prop="screenshotURL" width="400">
        <template slot-scope="scope">
          <image-preview :src="scope.row.screenshotURL" :width="80" :height="80"/>
        </template>
      </el-table-column>
      <el-table-column label="抓图时间" align="center" prop="screenshotTime" width="180">
        <template slot-scope="scope">
          <span>{{ (scope.row.screenshotTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-download"
            @click="handleDownload(scope.row)"
          >下载
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['screenshot:screenshot_record:remove']"
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

  </div>
</template>

<script>
import {
  listScreenshot_record,
  getScreenshot_record,
  delScreenshot_record,
  addScreenshot_record,
  updateScreenshot_record, downloadScreenshot
} from "@/api/screenshot/screenshot_record";

export default {
  name: "Screenshot_record",
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
      // 抓图记录表格数据
      screenshot_recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        screenshotKey: null,
        screenshotTime: null
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
    /** 查询抓图记录列表 */
    getList() {
      this.loading = true;
      listScreenshot_record(this.queryParams).then(response => {
        this.screenshot_recordList = response.rows;
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
        screenshotKey: null,
        screenshotTime: null
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
      this.title = "添加抓图记录";
    },
    /** 下载按钮操作 */
    handleDownload(row) {
      this.reset();
      const screenshotKey = row.screenshotKey || this.ids
      downloadScreenshot(screenshotKey).then(response => {
        this.$message({
          message: '下载抓图成功！',
          type: 'success',
          center: true
        });
        // 处理响应，将二进制数据保存为文件
        const blob = new Blob([response.data], {type: response.headers['content-type']});
        const url = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = url;
        link.download = screenshotKey; // 下载文件名，根据实际情况修改
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
      })
        .catch(error => {
          console.error('下载失败:', error);
        });
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.deviceId != null) {
            updateScreenshot_record(this.form).then(response => {
              this.$modal.msgSuccess("修改成功");
              this.open = false;
              this.getList();
            });
          } else {
            addScreenshot_record(this.form).then(response => {
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
      const screenshotKeys = row.screenshotKey || this.ids;
      this.$modal.confirm('是否确认删除抓图名称为"' + screenshotKeys + '"的数据项？').then(function () {
        return delScreenshot_record(screenshotKeys);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('screenshot/screenshot_record/export', {
        ...this.queryParams
      }, `screenshot_record_${new Date().getTime()}.xlsx`)
    }
  }
};
</script>
