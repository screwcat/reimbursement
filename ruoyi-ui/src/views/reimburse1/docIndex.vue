<template>
  <div class="app-container">
    <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" v-show="showSearch" label-width="68px">
      <el-form-item label="提交人" prop="nickName">
        <el-input
          v-model="queryParams.nickName"
          placeholder="请输入提交人"
          clearable
          @keyup.enter.native="handleQuery"
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
          v-hasPermi="['system:doc:add']"
        >新增</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="danger"
          plain
          icon="el-icon-delete"
          size="mini"
          :disabled="multiple"
          @click="handleDelete"
          v-hasPermi="['system:doc:remove']"
        >删除</el-button>
      </el-col>
      <el-col :span="1.5">
        <el-button
          type="warning"
          plain
          icon="el-icon-download"
          size="mini"
          @click="handleExport"
          v-hasPermi="['system:doc:export']"
        >导出</el-button>
      </el-col>
      <right-toolbar :showSearch.sync="showSearch" @queryTable="getList"></right-toolbar>
    </el-row>

    <el-table v-loading="loading" :data="docList" @selection-change="handleSelectionChange">
      <el-table-column type="selection" width="55" align="center" />
      <el-table-column label="单据编号" align="center" prop="billNo" width="120" />
      <el-table-column label="提交人" align="center" prop="nickName" width="120" />
      <el-table-column label="报销月度" align="center" prop="monthSelect" width="120" />
      <el-table-column label="票据总数" align="center" prop="billsNumber" width="80" />
      <el-table-column label="总天数" align="center" prop="daysBetween" width="80" />
      <el-table-column label="票据总金额" align="center" prop="amount" width="100" >
        <template slot-scope="scope">
          <span>{{ scope.row.amount.toFixed(2) }}</span>
        </template>
      </el-table-column>
      <el-table-column label="创建时间" align="center" prop="createTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.createTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="提交时间" align="center" prop="submitTime" width="180">
        <template slot-scope="scope">
          <span>{{ parseTime(scope.row.submitTime, '{y}-{m}-{d} {h}:{i}:{s}') }}</span>
        </template>
      </el-table-column>
      <el-table-column label="流程状态" align="center" prop="processStatus" width="100">
        <template slot-scope="scope">
          <dict-tag :options="dict.type.process_status" :value="scope.row.processStatus"/>
        </template>
      </el-table-column>
      <el-table-column label="操作" align="center" class-name="small-padding fixed-width">
        <template slot-scope="scope">
          <el-button
            size="mini"
            type="text"
            icon="el-icon-edit"
            @click="handleUpdate1(scope.row)"
            v-hasPermi="['system:doc:edit']"
            v-if="scope.row.processStatus === 'DRAFT' || scope.row.processStatus === 'REJECTED'"
          >修改</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-s-finance"
            @click="handleUpdate(scope.row)"
            v-hasPermi="['system:doc:view']"
          >明细</el-button>
          <el-button
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
          >撤销申请</el-button>
          <el-button
            size="mini"
            type="text"
            icon="el-icon-delete"
            @click="handleDelete(scope.row)"
            v-hasPermi="['system:doc:remove']"
            v-if="scope.row.processStatus === 'DRAFT' || scope.row.processStatus === 'REJECTED'"
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

    <!-- 添加或修改报销单据对话框 -->
    <el-dialog :title="title" :visible.sync="open" width="500px" append-to-body>
      <el-form ref="form" :model="form" :rules="rules" label-width="80px">
        <el-form-item label="月度选择" prop="monthSelect">
          <el-date-picker
            v-model="form.monthSelect"
            type="month"
            placeholder="请选择月度"
            format="yyyy-MM"
            value-format="yyyy-MM"
            style="width: 100%"
          />
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
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
import { listDoc, getDoc, delDoc, addDoc, updateDoc, listSummary, submitReimburse, cancelReimburse } from "@/api/reimburse1/doc"

export default {
  name: "Doc",
  dicts: ['process_status'],
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
      // 报销单据表格数据
      docList: [],
      // 弹出层标题
      title: "",
      // 是否显示弹出层
      open: false,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        userName: null,
        monthSelect: null,
        ticketTotal: null,
        totalAmount: null,
        submitTime: null,
        processStatus: null,
        nickName: null,
      },
      // 表单参数
      form: {},
      // 表单校验
      rules: {
        userName: [
          { required: true, message: "用户名不能为空", trigger: "blur" }
        ],
        monthSelect: [
          { required: true, message: "请选择月度", trigger: "blur" }
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
        createTime: [
          { required: true, message: "创建时间不能为空", trigger: "blur" }
        ]
      }
    }
  },
  created() {
    this.getList()
  },
  methods: {
    /** 查询报销单据列表 */
    getList() {
      this.loading = true
      listSummary(this.queryParams).then(response => {
        this.docList = response.rows
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
        docId: null,
        billNo: null,
        userName: null,
        monthSelect: null,
        ticketTotal: null,
        totalAmount: null,
        remark: null,
        submitTime: null,
        processStatus: null,
        createTime: null
      }
      this.resetForm("form")
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
      this.ids = selection.map(item => item.docId)
      this.single = selection.length!==1
      this.multiple = !selection.length
    },
    /** 新增按钮操作 */
    handleAdd() {
      this.reset()
      this.open = true
      this.title = "添加报销单据"
    },
    /** 修改按钮操作 */
    handleUpdate(row) {
      const docId = row.docId || this.ids
      if (!docId) {
        this.$modal.msgError("请选择需要修改的报销单")
        return
      }
      this.$router.push({
        path: 'list', // 请根据实际路由配置调整路径
        query: {
          docId: docId,
        }
      })
    },
    handleUpdate1(row) {
      this.reset()
      const docId = row.docId || this.ids
      getDoc(docId).then(response => {
        this.form = response.data
        this.open = true
        this.title = "修改报销单据"
      })
    },
    /** 提交按钮 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.form.docId != null) {
            updateDoc(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.open = false
              this.getList()
            })
          } else {
            addDoc(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.open = false
              this.getList()
            })
          }
        }
      })
    },
    // 提交审批
    handleSubmit(row) {
      console.log(row);
      if(row.billsNumber===0){
        this.$alert("请先填写明细在提交该单据！");
        return;
      }
      this.$confirm('确认提交审批吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        submitReimburse(row.docId).then(response => {
          this.$modal.msgSuccess("提交成功");
          this.getList();
        });
      }).catch(() => {});
    },
    // 撤销申请
    handleCancel(row) {
      this.$confirm('确认撤销该单据吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        cancelReimburse(row.docId).then(response => {
          this.$modal.msgSuccess("撤销成功");
          this.getList();
        });
      }).catch(() => {});
    },
    /** 删除按钮操作 */
    handleDelete(row) {
      const docIds = row.docId || this.ids
      this.$modal.confirm('是否确认删除选中的数据吗？').then(function() {
        return delDoc(docIds)
      }).then(() => {
        this.getList()
        this.$modal.msgSuccess("删除成功")
      }).catch(() => {})
    },
    /** 导出按钮操作 */
    handleExport() {
      this.download('reimburseDoc/export', {
        ...this.queryParams
      }, `报销统计表${new Date().getTime()}.xlsx`)
    }
  }
}
</script>
