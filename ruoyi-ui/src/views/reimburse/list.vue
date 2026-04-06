<template>
  <div class="app-container">
    <!-- 新增的卡片式信息区域 -->
    <el-card class="info-card" shadow="hover" style="margin: 10px 0 20px 0;">
      <el-form class="info-form" label-width="100px">
        <el-row :gutter="20">
          <!-- 第一行两列 -->
          <el-col :span="12">
            <el-form-item label="提交人：">
              <span class="form-value">{{ submitter }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单据编号：">
              <span class="form-value">{{ billNo }}</span>
            </el-form-item>
          </el-col>
          <!-- 第二行两列 -->
          <el-col :span="12">
            <el-form-item label="月度选择：">
              <span class="form-value">{{ monthSelect }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="单据数量：">
              <span class="form-value">{{ billsNumber }}</span>
            </el-form-item>
          </el-col>
          <!-- 第三行两列 -->
          <el-col :span="12">
            <el-form-item label="总金额：">
              <span class="form-value">￥{{ amount.toFixed(2) }}</span>
            </el-form-item>
          </el-col>
          <el-col :span="12">
            <el-form-item label="流程状态：">
              <span class="form-value"><dict-tag :options="dict.type.process_status" :value="processStatus"/></span>
            </el-form-item>
          </el-col>
          <!-- 备注独占一行（两列） -->
          <el-col :span="24">
            <el-form-item label="备注：">
              <span class="form-value">{{ remark }}</span>
            </el-form-item>
          </el-col>
        </el-row>
      </el-form>
    </el-card>

    <el-row :gutter="10" class="mb8" style="margin-bottom: 15px;">
      <el-col :span="24" style="text-align: right;">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:reimburse:add']"
          v-if="processStatus === 'DRAFT' || processStatus === 'REJECTED'"
        >新增</el-button>
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleDelete"
          v-hasPermi="['system:reimburse:remove']"
          v-if="processStatus === 'DRAFT' || processStatus === 'REJECTED'"
          style="margin-left: 10px;"
        >删除</el-button>
      </el-col>
    </el-row>

    <!-- 表格容器 - 新增相对定位用于印章绝对定位 -->
    <div class="table-container" :class="{ 'approved': processStatus === 'APPROVED', 'rejected': processStatus === 'REJECTED' }">
      <!-- 审批状态印章 -->
      <div class="approval-seal approved-seal" v-if="processStatus === 'APPROVED'">通过</div>
      <div class="approval-seal rejected-seal" v-if="processStatus === 'REJECTED'">拒绝</div>

      <el-table
        v-loading="loading"
        :data="reimburseList"
        @selection-change="handleSelectionChange"
        border
        fit
        highlight-current-row
      >
        <el-table-column type="selection" width="55" align="center" />
        <el-table-column label="开始时间" align="center" prop="startTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="结束时间" align="center" prop="endTime" width="180">
          <template slot-scope="scope">
            <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d}') }}</span>
          </template>
        </el-table-column>
        <el-table-column label="票据总数" align="center" prop="ticketTotal" />
        <!-- <el-table-column label="月度" align="center" prop="monthSelect" /> -->
        <el-table-column label="票据总金额" align="center" prop="totalAmount">
          <template slot-scope="scope">
            <span>{{ scope.row.totalAmount.toFixed(2) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
          <template slot-scope="scope">
            <el-button
              size="mini"
              type="text"
              icon="el-icon-view"
              @click="handleView(scope.row)"
              v-hasPermi="['system:reimburse:query']"
            >查看</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-edit"
              @click="handleEdit(scope.row)"
              v-if="processStatus === 'DRAFT' || processStatus === 'REJECTED'"
              v-hasPermi="['system:reimburse:edit']"
            >修改</el-button>
            <el-button
              size="mini"
              type="text"
              icon="el-icon-delete"
              @click="handleDeleteOne(scope.row)"
              v-hasPermi="['system:reimburse:remove']"
              v-if="scope.row.processStatus === 'DRAFT' || scope.row.processStatus === 'CANCELED' || scope.row.processStatus === 'REJECTED'"
            >删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <div slot="footer" class="dialog-footer" style="margin-top: 15px; text-align: right;">
      <el-button type="success" @click="reviewApprove" v-if="processStatus === 'APPROVING'" v-hasPermi="['system:reimburse:review']">审核通过</el-button>
      <el-button type="warning" @click="reviewReject" v-if="processStatus === 'APPROVING'" v-hasPermi="['system:reimburse:review']" style="margin-left: 10px;">审核拒绝</el-button>
      <el-button @click="returnMain" style="margin-left: 10px;">返 回</el-button>
    </div>

    <!-- 新增/修改弹窗 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="80%"
      append-to-body
      :close-on-click-modal="false"
    >
      <reimburse-form
        ref="reimburseForm"
        :is-view="isView"
        :reimburse-id="reimburseId"
        :user-name="userName"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" v-if="!isView" v-hasPermi="['system:reimburse:edit']">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看弹窗 -->
    <el-dialog
      title="报销申请详情"
      :visible.sync="viewOpen"
      width="80%"
      append-to-body
      :close-on-click-modal="false"
    >
      <reimburse-form
        ref="reimburseViewForm"
        :is-view="true"
        :reimburse-id="viewReimburseId"
      />
      <div slot="footer" class="dialog-footer">
        <el-button @click="viewOpen = false">关 闭</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { listReimburse, listBydocId, delReimburse, changeProcessState } from "@/api/reimburse";
import ReimburseForm from "./form";

export default {
  name: "ReimburseList",
  components: { ReimburseForm },
  dicts: ['process_status'],
  data() {
    return {
      // 遮罩层
      loading: true,
      // 选中数组
      ids: [],
      // 总条数
      total: 0,
      // 报销申请列表
      reimburseList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 是否查看
      viewOpen: false,
      // 是否为查看模式
      isView: false,
      // 报销单ID
      reimburseId: null,
      viewReimburseId: null,
      docId: null,
      submitter:null,
      userName:null,
      monthSelect:null,
      billNo:null,
      billsNumber:null,
      amount:0,
      processStatus: null,
      remark: null,
    };
  },
  created() {
    // 获取路由参数
    this.docId = this.$route.query.docId
    this.getList();
  },
  methods: {
    // 获取列表
    getList() {
      this.loading = true;
      listBydocId({'docId':this.docId}).then((response) => {
        this.reimburseList = response.data
        this.amount = response.docInfo.amount
        this.monthSelect = response.docInfo.monthSelect
        this.submitter = response.docInfo.nickName
        this.billNo = response.docInfo.billNo
        this.billsNumber = response.docInfo.billsNumber
        this.processStatus = response.docInfo.processStatus
        this.remark = response.docInfo.remark
        this.userName = response.docInfo.userName
        this.loading = false
      });
    },
    // 搜索按钮操作
    handleQuery() {
      this.getList();
    },
    // 多选框选中数据
    handleSelectionChange(val) {
      this.ids = val.map((item) => item.reimburseId);
    },
    // 新增按钮操作
    handleAdd() {
      this.open = true;
      this.title = "新增报销申请";
      this.isView = false;
      this.reimburseId = null;
      this.$nextTick(() => {
        this.$refs.reimburseForm.resetForm();
      });
    },
    // 修改按钮操作
    handleEdit(row) {
      this.open = true;
      this.title = "修改报销申请";
      this.isView = false;
      this.reimburseId = row.reimburseId;
      this.$nextTick(() => {
        this.$refs.reimburseForm.initForm(row.reimburseId);
      });
    },
    // 查看按钮操作
    handleView(row) {
      this.viewOpen = true;
      this.viewReimburseId = row.reimburseId;
      this.$nextTick(() => {
        this.$refs.reimburseViewForm.initForm(row.reimburseId);
      });
    },
    // 单条删除
    handleDeleteOne(row) {
      this.ids = [row.reimburseId];
      this.handleDelete();
    },
    // 批量删除
    handleDelete() {
      this.$confirm('是否确认删除选中的报销申请？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'danger'
      }).then(() => {
        return delReimburse(this.ids);
      }).then(() => {
        this.getList();
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {});
    },
    // 提交表单
    submitForm() {
      this.$refs.reimburseForm.submitForm(this.docId).then(() => {
        this.open = false;
        this.getList();
      });
    },
    reviewApprove() {
      this.$confirm('确认通过该单据吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return new Promise((resolve, reject) => {
          const params = {
                docId: this.docId,
                processState: "APPROVED",
              };
          try {
            changeProcessState(params).then(() => {
                  this.$modal.msgSuccess("审核通过!");
                  resolve();
                  this.getList();
                }).catch(error => {
                  reject(error);
                });
          } catch (error) {
              this.$modal.msgError("审核操作失败");
              reject(error);
          }
        });
      }).catch(() => {});
    },
    reviewReject() {
      this.$confirm('确认驳回该单据吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        return new Promise((resolve, reject) => {
          const params = {
                docId: this.docId,
                processState: "REJECTED",
              };
          try {
            changeProcessState(params).then(() => {
                  this.$modal.msgSuccess("审核拒绝!");
                  resolve();
                  this.getList();
                }).catch(error => {
                  reject(error);
                });
          } catch (error) {
              this.$modal.msgError("审核操作失败");
              reject(error);
          }
        });
      }).catch(() => {});
    },
    // 取消按钮操作
    cancel() {
      this.open = false;
    },
    returnMain(){
      this.$router.push({ path: 'docIndex' });
    }
  },
};
</script>

<style scoped>
/* 可选：给信息卡片添加自定义样式，增强视觉效果 */
.info-card {
  --el-card-padding: 15px;
}

/* 表单行间距优化 */
.info-form .el-form-item {
  margin-bottom: 10px;
}

/* 表单值样式 */
.form-value {
  display: inline-block;
  min-height: 32px;
  line-height: 32px;
}

/* 表格容器 - 相对定位 */
.table-container {
  position: relative;
}

/* 审批印章通用样式 */
.approval-seal {
  position: absolute;
  top: 20px;
  left: 20px;
  width: 80px;
  height: 80px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 24px;
  font-weight: bold;
  color: #fff;
  opacity: 0.8;
  z-index: 10;
  /* 旋转角度增加印章效果 */
  transform: rotate(-15deg);
}

/* 通过印章样式 - 绿色 */
.approved-seal {
  background-color: #67c23a;
  border: 3px solid #52c41a;
}

/* 拒绝印章样式 - 红色 */
.rejected-seal {
  background-color: #f56c6c;
  border: 3px solid #ff4d4f;
}

/* 印章文字样式优化 */
.approval-seal span {
  display: inline-block;
  transform: rotate(15deg);
}
</style>