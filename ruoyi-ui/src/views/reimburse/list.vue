<template>
  <div class="app-container">
    <!-- <el-form :inline="true" :model="queryParams" class="demo-form-inline">
      <el-form-item label="单据编号" prop="billNo">
        <el-input
          v-model="queryParams.billNo"
          placeholder="请输入单据编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="月度选择" prop="monthSelect">
        <el-date-picker
          v-model="queryParams.monthSelect"
          type="month"
          placeholder="请选择月度"
          format="yyyy-MM"
          value-format="yyyy-MM"
          style="width: 100%"
        />
      </el-form-item>
      <el-form-item label="流程状态" prop="processStatus">
        <el-select v-model="queryParams.processStatus" placeholder="请选择流程状态" clearable>
          <el-option
            v-for="dict in dict.type.process_status"
            :key="dict.value"
            :label="dict.label"
            :value="dict.value"
          />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
      </el-form-item>
    </el-form> -->

    <!-- 新增的卡片式信息区域 -->
    <el-card class="info-card" shadow="hover" style="margin: 10px 0;">
      <div style="font-size: 16px; font-weight: 500; line-height: 1.8;">
        提交人：{{submitter}}；单据编号：{{ billNo }}； 月度选择：{{monthSelect}}；单据数量：{{billsNumber}}；总金额：￥{{amount}}；流程状态：{{processStatus}}；
      </div>
    </el-card>

    <el-row :gutter="10" class="mb8">
      <el-col :span="1.5">
        <el-button
          type="primary"
          plain
          icon="el-icon-plus"
          size="mini"
          @click="handleAdd"
          v-hasPermi="['system:reimburse:add']"
          v-if="processStatus === 'DRAFT' || processStatus === 'REJECTED'"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          @click="handleDelete"
          v-hasPermi="['system:reimburse:remove']"
          v-if="processStatus === 'DRAFT' || processStatus === 'REJECTED'"
        >删除</el-button>
      </el-col>
      <!-- <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:reimburse:export']"
        >导出</el-button>
      </el-col> -->
    </el-row>

    <el-table
      v-loading="loading"
      :data="reimburseList"
      @selection-change="handleSelectionChange"
      border
      fit
      highlight-current-row
    >
      <el-table-column type="selection" width="55" align="center" />
      <!-- <el-table-column label="单据编号" align="center" prop="billNo" />
      <el-table-column label="提交人" align="center" prop="nickName" /> -->
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
      <!-- <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程状态" align="center" prop="processStatus">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.process_status" :value="scope.row.processStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="提交时间" align="center" prop="submitTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.submitTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template> -->
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-view"
            @click="handleView(scope.row)"
            v-if="processStatus === 'APPROVING' || processStatus === 'APPROVED'"
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
          <!-- <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleSubmit(scope.row)"
            v-hasPermi="['system:reimburse:submit']"
            v-if="scope.row.processStatus === 'DRAFT' || scope.row.processStatus === 'REJECTED'"
          >提交审批</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-close"
            @click="handleCancel(scope.row)"
            v-hasPermi="['system:reimburse:cancel']"
            v-if="scope.row.processStatus === 'SUBMITTED' || scope.row.processStatus === 'APPROVING'"
          >撤销申请</el-button> -->
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

    <!-- <pagination
      v-show="total>0"
      :total="total"
      :page.sync="queryParams.pageNum"
      :limit.sync="queryParams.pageSize"
      @pagination="getList"
    /> -->
    <div slot="footer" class="dialog-footer">
      <el-button type="success" @click="reviewApprove" v-if="processStatus === 'APPROVING'" v-hasPermi="['system:reimburse:review']">审核通过</el-button>
        <el-button type="warning" @click="reviewReject" v-if="processStatus === 'APPROVING'" v-hasPermi="['system:reimburse:review']">审核拒绝</el-button>
      <el-button @click="returnMain">返 回</el-button>
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
      monthSelect:null,
      billNo:null,
      billsNumber:null,
      amount:0,
      processStatus: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        billNo: null,
        monthSelect: null,
        processStatus: null,
      },
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
        this.loading = false;
      });
    },
    // 搜索按钮操作
    handleQuery() {
      this.queryParams.pageNum = 1;
      this.getList();
    },
    // 重置按钮操作
    resetQuery() {
      this.queryParams = {
        pageNum: 1,
        pageSize: 10,
        billNo: null,
        monthSelect: null,
        processStatus: null,
      };
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
    // 导出按钮操作
    handleExport() {
      this.download('reimburse/export', {
        ...this.queryParams
      }, `reimburse_${new Date().getTime()}.xlsx`)
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
</style>