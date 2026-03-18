<template>
  <el-form
    ref="form"
    :model="form"
    :rules="rules"
    label-width="120px"
  >
    <!-- 基础信息 -->
    <el-card title="基础信息" shadow="never">
      <el-row :gutter="20">
        <el-col :span="8">
          <el-form-item label="开始时间" prop="startTime">
            <el-date-picker
              v-model="form.startTime"
              type="datetime"
              placeholder="请选择开始时间"
              style="width: 100%"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="结束时间" prop="endTime">
            <el-date-picker
              v-model="form.endTime"
              type="datetime"
              placeholder="请选择结束时间"
              style="width: 100%"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="月度选择" prop="monthSelect">
            <el-date-picker
              v-model="form.monthSelect"
              type="month"
              placeholder="请选择月度"
              format="yyyy-MM"
              value-format="yyyy-MM"
              style="width: 100%"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="票据总数" prop="ticketTotal">
            <el-input
              v-model="form.ticketTotal"
              placeholder="请输入票据总数"
              :disabled="isView"
            />
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item label="备注" prop="remark">
            <el-input
              v-model="form.remark"
              type="textarea"
              placeholder="请输入备注"
              :disabled="isView"
              rows="3"
            />
          </el-form-item>
        </el-col>
      </el-row>
    </el-card>

    <!-- 费用明细 -->
    <el-card title="报销费用明细" shadow="never" style="margin-top: 10px;">
      <el-table
        :data="detailList"
        border
        style="width: 100%; margin-bottom: 10px;"
        @selection-change="handleDetailSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" v-if="!isView" />
        <el-table-column label="票据类型" align="center" prop="ticketType">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.ticketType"
              placeholder="请输入票据类型"
              :disabled="isView"
            />
          </template>
        </el-table-column>
        <el-table-column label="票据张数" align="center" prop="ticketNum">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.ticketNum"
              type="number"
              placeholder="请输入张数"
              :disabled="isView"
            />
          </template>
        </el-table-column>
        <el-table-column label="起始地" align="center" prop="startPlace">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.startPlace"
              placeholder="请输入起始地"
              :disabled="isView"
            />
          </template>
        </el-table-column>
        <el-table-column label="目的地" align="center" prop="endPlace">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.endPlace"
              placeholder="请输入目的地"
              :disabled="isView"
            />
          </template>
        </el-table-column>
        <el-table-column label="票据日期" align="center" prop="ticketDate">
          <template slot-scope="scope">
            <el-date-picker
              v-model="scope.row.ticketDate"
              type="date"
              placeholder="请选择日期"
              style="width: 100%"
              :disabled="isView"
            />
          </template>
        </el-table-column>
        <el-table-column label="款项金额" align="center" prop="amount">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.amount"
              type="number"
              placeholder="请输入金额"
              :disabled="isView"
              precision="2"
            />
          </template>
        </el-table-column>
        <el-table-column label="住宿地点" align="center" prop="accommodation">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.accommodation"
              placeholder="请输入住宿地点"
              :disabled="isView"
            />
          </template>
        </el-table-column>
      </el-table>
      <el-button
        type="primary"
        icon="el-icon-plus"
        size="mini"
        @click="addDetail"
        v-if="!isView"
      >添加明细</el-button>
      <el-button
        type="danger"
        icon="el-icon-delete"
        size="mini"
        @click="delDetail"
        v-if="!isView"
      >删除选中</el-button>
    </el-card>

    <!-- 票据影像 -->
    <el-card title="票据影像" shadow="never" style="margin-top: 10px;">
      <el-upload
        ref="upload"
        :action="uploadUrl"
        :headers="uploadHeaders"
        :file-list="attachmentList"
        :on-success="handleUploadSuccess"
        :before-upload="beforeUpload"
        :limit="10"
        :on-exceed="handleExceed"
        list-type="picture-card"
        :disabled="isView"
      >
        <i class="el-icon-plus"></i>
        <div slot="tip" class="el-upload__tip">
          请上传jpg/png格式的图片，单张图片不超过5MB，最多上传10张
        </div>
      </el-upload>
      <!-- 预览 -->
      <el-dialog :visible.sync="previewOpen" title="图片预览" width="80%">
        <img :src="previewUrl" style="width: 100%;" />
      </el-dialog>
    </el-card>
  </el-form>
</template>

<script>
import { getReimburse, addReimburse, addReimComplete, updateReimburse } from "@/api/reimburse";
import { getToken } from "@/utils/auth";

export default {
  name: "ReimburseForm",
  props: {
    // 是否为查看模式
    isView: {
      type: Boolean,
      default: false,
    },
    // 报销单ID
    reimburseId: {
      type: [Number, String, null],
      default: null,
    },
  },
  data() {
    return {
      // 表单数据
      form: {
        startTime: null,
        endTime: null,
        monthSelect: null,
        ticketTotal: null,
        remark: "",
        reimburseId: null,
      },
      // 明细列表
      detailList: [],
      // 附件列表
      attachmentList: [],
      // 选中的明细ID
      detailIds: [],
      // 预览弹窗
      previewOpen: false,
      previewUrl: "",
      // 上传配置
      uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload",
      uploadHeaders: {
        Authorization: "Bearer " + getToken(),
      },
      // 表单校验规则
      rules: {
        startTime: [{ required: true, message: "开始时间不能为空", trigger: "change" }],
        endTime: [{ required: true, message: "结束时间不能为空", trigger: "change" }],
        monthSelect: [{ required: true, message: "月度选择不能为空", trigger: "change" }],
        ticketTotal: [{ required: true, message: "票据总数不能为空", trigger: "blur" },],
      },
    };
  },
  methods: {
    // 初始化表单
    initForm(reimburseId) {
      this.resetForm();
      if (reimburseId) {
        getReimburse(reimburseId).then((response) => {
          const data = response.data;
          this.form = data.reimburse;
          this.detailList = data.detailList || [];
          // 处理附件列表格式
          this.attachmentList = (data.attachmentList || []).map(item => {
            return {
              name: item.fileName,
              url: item.filePath,
              uid: item.attachmentId,
              response: {
                filePath: item.filePath,
                fileName: item.fileName,
                fileSize: item.fileSize,
                fileType: item.fileType,
              },
            };
          });
        });
      }
    },
    // 重置表单
    resetForm() {
      this.form = {
        startTime: null,
        endTime: null,
        monthSelect: null,
        ticketTotal: null,
        remark: "",
        reimburseId: null,
      };
      this.detailList = [];
      this.attachmentList = [];
    },
    // 添加明细
    addDetail() {
      this.detailList.push({
        ticketType: "",
        ticketNum: null,
        startPlace: "",
        endPlace: "",
        ticketDate: null,
        amount: null,
        accommodation: "",
        detailId: null,
        reimburseId: this.reimburseId,
      });
    },
    // 删除明细
    delDetail() {
      if (this.detailIds.length === 0) {
        this.$modal.msgWarning("请选择要删除的明细");
        return;
      }
      this.detailList = this.detailList.filter(item => !this.detailIds.includes(item.detailId));
      this.detailIds = [];
    },
    // 明细多选
    handleDetailSelectionChange(val) {
      this.detailIds = val.map(item => item.detailId);
    },
    // 文件上传前校验
    beforeUpload(file) {
      const isJPG = file.type === "image/jpeg" || file.type === "image/png";
      const isLt5M = file.size / 1024 / 1024 < 5;
      if (!isJPG) {
        this.$modal.msgError("请上传jpg/png格式的图片");
        return false;
      }
      if (!isLt5M) {
        this.$modal.msgError("图片大小不能超过5MB");
        return false;
      }
      return true;
    },
    // 文件上传成功
    handleUploadSuccess(response, file) {
      this.attachmentList.push({
        fileName: response.originalFilename,
        filePath: response.fileName,
        fileSize: file.size,
        fileType: file.raw.type,
        url: response.url,
      });
    },
    // 文件超出数量限制
    handleExceed(files, fileList) {
      this.$modal.msgError(`最多只能上传10张图片`);
    },
    // 提交表单
    submitForm() {
      return new Promise((resolve, reject) => {
        this.$refs.form.validate((valid) => {
          if (valid) {
            // 处理附件数据
            console.log("处理附件数据");
            const attachments = this.attachmentList.map(item => {
              return {
                fileName: item.fileName,
                filePath: item.filePath,
                fileSize: item.fileSize,
                fileType: item.fileType,
              };
            });
            // 组装参数
            const params = {
              reimburse: this.form,
              detailList: this.detailList,
              attachmentList: attachments,
            };
            if (this.form.reimburseId) {
              updateReimburse(params).then(() => {
                this.$modal.msgSuccess("修改成功");
                resolve();
              }).catch(error => {
                reject(error);
              });
            } else {
              addReimComplete(params).then(() => {
                this.$modal.msgSuccess("新增成功");
                resolve();
              }).catch(error => {
                reject(error);
              });
            }
          } else {
            reject("表单校验失败");
          }
        });
      });
    },
  },
};
</script>