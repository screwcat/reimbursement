<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="单据编号" prop="billNo">
        <el-input
          v-model="queryParams.billNo"
          placeholder="请输入单据编号"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker clearable
          v-model="queryParams.startTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker clearable
          v-model="queryParams.endTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="月度选择(如2024-05)" prop="monthSelect">
        <el-input
          v-model="queryParams.monthSelect"
          placeholder="请输入月度选择(如2024-05)"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="票据总数" prop="ticketTotal">
        <el-input
          v-model="queryParams.ticketTotal"
          placeholder="请输入票据总数"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="票据总金额" prop="totalAmount">
        <el-input
          v-model="queryParams.totalAmount"
          placeholder="请输入票据总金额"
          clearable
          @keyup.enter.native="handleQuery"
        />
      </el-form-item>
      <el-form-item label="提交时间" prop="submitTime">
        <el-date-picker clearable
          v-model="queryParams.submitTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择提交时间">
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
          v-hasPermi="['system:reimburse:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="success"
          plain
          icon="el-icon-edit"
          size="mini"
          :disabled="single"
          @click="handleUpdate"
          v-hasPermi="['system:reimburse:edit']"
        >修改</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:reimburse:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:reimburse:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="reimburseList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="报销单ID" align="center" prop="reimburseId" />
      <el-table-column label="单据编号" align="center" prop="billNo" />
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
      <el-table-column label="月度选择(如2024-05)" align="center" prop="monthSelect" />
      <el-table-column label="票据总数" align="center" prop="ticketTotal" />
      <el-table-column label="票据总金额" align="center" prop="totalAmount" />
      <el-table-column label="备注" align="center" prop="remark" />
      <el-table-column label="提交时间" align="center" prop="submitTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.submitTime, '{y}-{m}-{d}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程状态：" align="center" prop="processStatus" />
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:reimburse:edit']"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:reimburse:remove']"
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
  </div>
</template>

<script>
import { listReimburse, getReimburse, delReimburse } from "@/api/reimburse1"

export default {
  name: "Reimburse",
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
      // 报销申请单主表格数据
      reimburseList: [],
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        billNo: null,
        startTime: null,
        endTime: null,
        monthSelect: null,
        ticketTotal: null,
        totalAmount: null,
        submitTime: null,
        processStatus: null,
      },
      // 表单校验（仅保留查询表单可能用到的，弹窗相关已移除）
      rules: {
        billNo: [
          { required: true, message: "单据编号不能为空", trigger: "blur" }
        ],
        startTime: [
          { required: true, message: "开始时间不能为空", trigger: "blur" }
        ],
        endTime: [
          { required: true, message: "结束时间不能为空", trigger: "blur" }
        ],
        monthSelect: [
          { required: true, message: "月度选择(如2024-05)不能为空", trigger: "blur" }
        ],
        ticketTotal: [
          { required: true, message: "票据总数不能为空", trigger: "blur" }
        ],
        totalAmount: [
          { required: true, message: "票据总金额不能为空", trigger: "blur" }
        ],
        processStatus: [
          { required: true, message: "流程状态不能为空", trigger: "change" }
        ],
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询报销申请单主列表 */
    getList() {
      this.loading = true
      listReimburse(this.queryParams).then(response => {
        this.reimburseList = response.rows
        this.total = response.total
        this.loading = false
      })
    },
    /** 搜索按钮操作 */
    handleQuery() {
      this.queryParams.pageNum = 1
      this.getList()
    },
    /** 重置按钮操作 */
    resetQuery() {
      this.resetForm("queryForm")
      this.handleQuery()
    },
    // 多选框选中数据
    handleSelectionChange(selection) {
      this.ids = selection.map(item => item.reimburseId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 - 跳转到编辑页 */
    handleAdd() {
      this.$router.push({
        path: 'edit', // 请根据实际路由配置调整路径
        query: { type: 'add' }
      })
    },
    /** 修改按钮操作 - 跳转到编辑页并传递reimburseId */
    handleUpdate(row) {
      const reimburseId = row?.reimburseId || this.ids[0]
      if (!reimburseId) {
        this.$modal.msgError("请选择需要修改的报销单")
        return
      }
      this.$router.push({
        path: 'edit', // 请根据实际路由配置调整路径
        query: { 
          type: 'edit',
          reimburseId: reimburseId 
        }
      })
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const reimburseIds = row?.reimburseId || this.ids
      if (!reimburseIds || reimburseIds.length === 0) {
        this.$modal.msgError("请选择需要删除的报销单")
        return
      }
      this.$modal.confirm('是否确认删除报销申请单主编号为"' + reimburseIds + '"的数据项？').then(() => {
        return delReimburse(reimburseIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('system/reimburse/export', {
        ...this.queryParams
      }, `reimburse_${new Date().getTime()}.xlsx`)
    }
  }
}
</script>