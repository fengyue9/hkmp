<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="100px">
      <el-form-item label="开始回放时间" prop="startTime">
        <el-date-picker clearable
                        v-model="queryParams.startTime"
                        type="datetime"
                        placeholder="请选择开始回放时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束回放时间" prop="endTime">
        <el-date-picker clearable
                        v-model="queryParams.endTime"
                        type="datetime"
                        placeholder="请选择结束回放时间">
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
          v-hasPermi="['system:record:remove']"
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
          v-hasPermi="['system:record:export']"
        >导出
        </el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="recordList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center"/>
      <el-table-column label="设备id" width="150" align="center" prop="deviceId"/>
      <el-table-column label="设备名称" width="200" align="center" prop="deviceName"/>
      <el-table-column label="回放视频关键字" width="400" align="center" prop="recordingKey"/>
      <el-table-column label="开始回放时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.startTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束回放时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ scope.row.endTime }}</span>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handlePlay(scope.row)"
          >播放
          </el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleDownload(scope.row)"
          >下载
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

    <!-- 添加或修改回放对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="回放视频关键字" prop="recordingKey">
          <el-input v-model="form.recordingKey" placeholder="请输入回放视频关键字"/>
        </el-form-item>
        <el-form-item label="开始回放时间" prop="startTime">
          <el-date-picker clearable
                          v-model="form.startTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择开始回放时间">
          </el-date-picker>
        </el-form-item>
        <el-form-item label="结束回放时间" prop="endTime">
          <el-date-picker clearable
                          v-model="form.endTime"
                          type="date"
                          value-format="yyyy-MM-dd"
                          placeholder="请选择结束回放时间">
          </el-date-picker>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>
    <!--视频播放对话框-->
    <el-dialog
      title="视频播放"
      :visible.sync="dialogVisible"
      width="60%"
      center
      :show-close=false
      :close-on-click-modal=false
      :closeOnClickModal=false
      :close="handleClose">
      <el-card>
        <div class="camera-container_record">
          <video ref="video" class="video-element" id="video_record_element" autoplay controls></video>
        </div>
      </el-card>
      <el-card>
        <span>倍速选择：</span>
        <!-- 倍速按钮 -->
        <el-button @click="setPlaybackRate(0.5)">0.5x</el-button>
        <el-button @click="setPlaybackRate(1)">1x</el-button>
        <el-button @click="setPlaybackRate(1.5)">1.5x</el-button>
        <el-button @click="setPlaybackRate(2)">2x</el-button>
      </el-card>
      <span slot="footer" class="dialog-footer">
        <el-button type="primary" @click="handleClose">返回</el-button>
      </span>
    </el-dialog>
  </div>
</template>

<script>
import {listRecord, getRecord, delRecord, addRecord, updateRecord, downloadVideo} from "@/api/system/record";
import moment from "moment";

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
      // 回放表格数据
      recordList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        startTime: '',
        endTime: '',
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {},
      dialogVisible: false, //视频播放对话框是否可见
      videoUrl: '',//视频播放的URL
    };
  },
  created() {
    this.getList();
  },
  methods: {
    /** 查询回放列表 */
    getList() {
      this.loading = true;
      // 格式化开始时间和结束时间
      if (this.queryParams.startTime !== '' && this.queryParams.endTime !== '') {
        let formattedStartTime = moment(this.queryParams.startTime).format('YYYY-MM-DD HH:mm:ss');
        let formattedEndTime = moment(this.queryParams.endTime).format('YYYY-MM-DD HH:mm:ss');
        // 将格式化后的时间设置回查询参数对象
        this.queryParams.startTime = formattedStartTime;
        this.queryParams.endTime = formattedEndTime;
      }
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
        recordingKey: null,
        startTime: null,
        endTime: null
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
      const recordingKey = row.recordingKey;
      this.$modal.confirm('是否确认删除回放编号为"' + recordingKey + '"的数据项？').then(function () {
        return delRecord(recordingKey);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
      });
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/record/export', {
        ...this.queryParams
      }, `record_${new Date().getTime()}.xlsx`)
    },
    //下载操作
    handleDownload(row) {
      this.reset();
      const recordingKey = row.recordingKey;
      downloadVideo(recordingKey).then(response => {
        this.$message({
          message: '下载视频成功',
          type: 'success',
          center: true
        })
        //处理响应,将二进制数据保存成文件
        const url = window.URL.createObjectURL(response);
        const link = document.createElement('a');
        link.href = url;
        link.download = recordingKey; // 下载文件名，根据实际情况修改
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);
        window.URL.revokeObjectURL(url);
      }).catch(error => {
        console.error('下载视频失败:', error);
      });
    },
    //播放视频
    handlePlay(row) {
      this.dialogVisible = true;
      this.reset();
      const recordingKey = row.recordingKey;
      downloadVideo(recordingKey).then(response => {
        // 生成视频的URL
        const videoUrl = URL.createObjectURL(response);
        this.$refs.video.src = videoUrl;
        this.$refs.video.play();
        //默认播放速度为1x
        this.$refs.video.playbackRate = 1;
      }).catch(error => {
        console.error('播放视频失败:', error);
      });
    },
    handleClose() {
      // 停止视频播放
      this.$refs.video.pause();
      // 释放视频 URL 对应的资源
      URL.revokeObjectURL(this.videoUrl);
      this.dialogVisible = false;
    },
    // 设置视频播放速率
    setPlaybackRate(rate) {
      this.$refs.video.playbackRate = rate;
    }
  }
};
</script>
<style scoped>
.camera-container_record {
  max-width: 100%;
  margin: 0 auto;
  position: relative;
  overflow: hidden;
  height: 500px; /* 调整预览区域的高度 */
}

#video_record_element {
  height: 100%;
  width: 100%;
}
</style>
