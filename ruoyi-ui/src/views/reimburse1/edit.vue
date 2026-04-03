<template>
  <div class="app-container">
    <el-page-header @back="goBack" content="报销单编辑"></el-page-header>
    <el-form ref="form" :model="form" :rules="rules" label-width="80px" class="mt20">
      <el-form-item label="单据编号" prop="billNo">
        <el-input v-model="form.billNo" placeholder="请输入单据编号" />
      </el-form-item>
      <el-form-item label="开始时间" prop="startTime">
        <el-date-picker clearable
          v-model="form.startTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择开始时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="结束时间" prop="endTime">
        <el-date-picker clearable
          v-model="form.endTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择结束时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="月度选择(如2024-05)" prop="monthSelect">
        <el-input v-model="form.monthSelect" placeholder="请输入月度选择(如2024-05)" />
      </el-form-item>
      <el-form-item label="票据总数" prop="ticketTotal">
        <el-input v-model="form.ticketTotal" placeholder="请输入票据总数" />
      </el-form-item>
      <el-form-item label="票据总金额" prop="totalAmount">
        <el-input v-model="form.totalAmount" placeholder="请输入票据总金额" />
      </el-form-item>
      <el-form-item label="备注" prop="remark">
        <el-input v-model="form.remark" type="textarea" placeholder="请输入内容" />
      </el-form-item>
      <el-form-item label="提交时间" prop="submitTime">
        <el-date-picker clearable
          v-model="form.submitTime"
          type="date"
          value-format="yyyy-MM-dd"
          placeholder="请选择提交时间">
        </el-date-picker>
      </el-form-item>
      <el-form-item label="删除标志" prop="delFlag">
        <el-input v-model="form.delFlag" placeholder="请输入删除标志" />
      </el-form-item>
      <el-form-item label="流程状态" prop="processStatus">
        <el-input v-model="form.processStatus" placeholder="请输入流程状态" />
      </el-form-item>
      
      <el-form-item class="mt20">
        <el-button type="primary" @click="submitForm">保存</el-button>
        <el-button @click="goBack">返回</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { getReimburse, addReimburse, updateReimburse } from "@/api/reimburse1"

export default {
  name: "ReimburseEdit",
  data() {
    return {
      // 表单参数
      form: {
        reimburseId: null,
        billNo: null,
        startTime: null,
        endTime: null,
        monthSelect: null,
        ticketTotal: null,
        totalAmount: null,
        remark: null,
        submitTime: null,
        processStatus: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
      },
      // 表单校验
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
      },
      // 操作类型 add/edit
      operateType: 'add'
    }
  },
  created() {
    // 获取路由参数
    const { type, reimburseId } = this.$route.query
    this.operateType = type || 'add'
    
    // 编辑模式下加载数据
    if (this.operateType === 'edit' && reimburseId) {
      this.getReimburseDetail(reimburseId)
    }
  },
  methods: {
    /** 获取报销单详情 */
    getReimburseDetail(reimburseId) {
      getReimburse(reimburseId).then(response => {
        this.form = response.data
      })
    },
    /** 提交表单 */
    submitForm() {
      this.$refs["form"].validate(valid => {
        if (valid) {
          if (this.operateType === 'edit') {
            updateReimburse(this.form).then(response => {
              this.$modal.msgSuccess("修改成功")
              this.goBack()
            })
          } else {
            addReimburse(this.form).then(response => {
              this.$modal.msgSuccess("新增成功")
              this.goBack()
            })
          }
        }
      })
    },
    /** 返回列表页 */
    goBack() {
      // 关闭当前标签页，返回列表页
      window.close()
      // 如果需要刷新列表页，可添加如下逻辑
      // window.opener.location.reload()
    },
    /** 重置表单 */
    resetForm() {
      this.form = {
        reimburseId: null,
        billNo: null,
        startTime: null,
        endTime: null,
        monthSelect: null,
        ticketTotal: null,
        totalAmount: null,
        remark: null,
        submitTime: null,
        processStatus: null,
        createBy: null,
        createTime: null,
        updateBy: null,
        updateTime: null,
        delFlag: null
      }
      this.resetForm("form")
    }
  }
}
</script>

<style scoped>
.mt20 {
  margin-top: 20px;
}
</style>