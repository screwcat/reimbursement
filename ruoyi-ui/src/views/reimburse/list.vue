<template>
  <div class="app-container">
    <el-form :inline="true" :model="queryParams" class="demo-form-inline">
      <el-form-item label="单据编号" prop="billNo">
        <el-input
          v-model="queryParams.billNo"
          placeholder="请输入单据编号"
          clearable
          @keyup.enter="handleQuery"
        />
      </el-form-item>
      <el-form-item label="月度选择" prop="monthSelect">
        <el-input
          v-model="queryParams.monthSelect"
          placeholder="请输入月度(如2024-05)"
          clearable
        />
      </el-form-item>
      <el-form-item label="流程状态" prop="processStatus">
        <el-select v-model="queryParams.processStatus" placeholder="请选择流程状态" clearable>
          <el-option label="草稿" value="DRAFT" />
          <el-option label="已提交" value="SUBMITTED" />
          <el-option label="审批中" value="APPROVING" />
          <el-option label="已审批" value="APPROVED" />
          <el-option label="已驳回" value="REJECTED" />
          <el-option label="已撤销" value="CANCELED" />
        </el-select>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" icon="el-icon-search" @click="handleQuery">查询</el-button>
        <el-button icon="el-icon-refresh" @click="resetQuery">重置</el-button>
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
          v-hasPermi="['system:reimburse:add']"
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
      <el-table-column label="单据编号" align="center" prop="billNo" />
      <el-table-column label="开始时间" align="center" prop="startTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.startTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="结束时间" align="center" prop="endTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.endTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="票据总数" align="center" prop="ticketTotal" />
      <el-table-column label="月度" align="center" prop="monthSelect" />
      <el-table-column label="票据总金额" align="center" prop="totalAmount">
        <template slot-scope="scope">
          <span>{{ scope.row.totalAmount.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程状态" align="center" prop="processStatus">
        <template slot-scope="scope">
          <el-tag v-if="scope.row.processStatus === 'DRAFT'" type="info">草稿</el-tag>
          <el-tag v-else-if="scope.row.processStatus === 'SUBMITTED'" type="primary">已提交</el-tag>
          <el-tag v-else-if="scope.row.processStatus === 'APPROVING'" type="warning">审批中</el-tag>
          <el-tag v-else-if="scope.row.processStatus === 'APPROVED'" type="success">已审批</el-tag>
          <el-tag v-else-if="scope.row.processStatus === 'REJECTED'" type="danger">已驳回</el-tag>
          <el-tag v-else-if="scope.row.processStatus === 'CANCELED'" type="gray">已撤销</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
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
            v-hasPermi="['system:reimburse:edit']"
            v-if="scope.row.processStatus === 'DRAFT'"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-check"
            @click="handleSubmit(scope.row)"
            v-hasPermi="['system:reimburse:submit']"
            v-if="scope.row.processStatus === 'DRAFT'"
          >提交审批</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-circle-close"
            @click="handleCancel(scope.row)"
            v-hasPermi="['system:reimburse:cancel']"
            v-if="scope.row.processStatus === 'SUBMITTED' || scope.row.processStatus === 'APPROVING'"
          >撤销申请</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDeleteOne(scope.row)"
            v-hasPermi="['system:reimburse:remove']"
            v-if="scope.row.processStatus === 'DRAFT' || scope.row.processStatus === 'CANCELED'"
          >删除</el-button>
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

    <!-- 新增/修改弹窗 -->
    <el-dialog
      :title="title"
      :visible.sync="open"
      width="80%"
      append-to-body
    >
      <reimburse-form
        ref="reimburseForm"
        :is-view="isView"
        :reimburse-id="reimburseId"
      />
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitForm" v-if="!isView">确 定</el-button>
        <el-button @click="cancel">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 查看弹窗 -->
    <el-dialog
      title="报销申请详情"
      :visible.sync="viewOpen"
      width="80%"
      append-to-body
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
import { listReimburse, getReimburse, delReimburse, submitReimburse, cancelReimburse } from "@/api/reimburse";
import ReimburseForm from "./form";

export default {
  name: "ReimburseList",
  components: { ReimburseForm },
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
    this.getList();
  },
  methods: {
    // 获取列表
    getList() {
      this.loading = true;
      listReimburse(this.queryParams).then((response) => {
        this.reimburseList = response.rows;
        this.total = response.total;
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
      console.log("查看报销单ID：", row.reimburseId);
      this.$nextTick(() => {
        this.$refs.reimburseViewForm.initForm(row.reimburseId);
      });
    },
    // 提交审批
    handleSubmit(row) {
      this.$confirm('确认提交审批吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        submitReimburse(row.reimburseId).then(response => {
          this.$modal.msgSuccess("提交成功");
          this.getList();
        });
      }).catch(() => {});
    },
    // 撤销申请
    handleCancel(row) {
      this.$confirm('确认撤销申请吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        cancelReimburse(row.reimburseId).then(response => {
          this.$modal.msgSuccess("撤销成功");
          this.getList();
        });
      }).catch(() => {});
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
      this.download('system/reimburse/export', {
        ...this.queryParams
      }, `reimburse_${new Date().getTime()}.xlsx`)
    },
    // 提交表单
    submitForm() {
      this.$refs.reimburseForm.submitForm().then(() => {
        this.open = false;
        this.getList();
      });
    },
    // 取消按钮操作
    cancel() {
      this.open = false;
    }
  },
};
</script>