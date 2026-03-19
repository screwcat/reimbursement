<template>
  <el-form
    ref="form"
    :model="form"
    :rules="rules"
    label-width="120px"
  >
    <el-card title="基础信息" shadow="never">
      <el-row :gutter="20">
        <el-col :span="10">    
          <el-form-item label="提交人" prop="nickName" v-if="form.nickName!=undefined">
            <el-input
              v-model="form.nickName"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="16">
          <el-form-item label="时间范围" prop="dateRange">
            <el-date-picker
              v-model="form.dateRange"
              type="daterange"
              range-separator="至"
              start-placeholder="开始日期"
              end-placeholder="结束日期"
              style="width: 100%"
              :disabled="isView"
              value-format="yyyy-MM-dd"
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
              placeholder="票据总数自动计算"
              disabled
            />
          </el-form-item>
        </el-col>
        <el-col :span="8">
          <el-form-item label="总金额" prop="totalAmount">
            <el-input
              v-model="form.totalAmount"
              placeholder="总金额自动计算"
              disabled
              type="number"
              precision="2"
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
    <el-card title="报销费用明细" shadow="never" style="margin-top: 10px;">
      <el-table
        ref="detailTable"
        :data="detailList"
        border
        style="width: 100%; margin-bottom: 10px;"
        @selection-change="handleDetailSelectionChange"
      >
        <el-table-column type="selection" width="55" align="center" v-if="!isView" />
        <el-table-column label="票据类型" align="center" prop="ticketType">
          <template slot-scope="scope">
            <el-select
              v-model="scope.row.ticketType"
              placeholder="请选择票据类型"
              style="width: 100%"
              :disabled="isView"
            >
              <el-option label="交通" value="交通" />
              <el-option label="办公用品" value="办公用品" />
              <el-option label="餐饮" value="餐饮" />
              <el-option label="其他" value="其他" />
            </el-select>
          </template>
        </el-table-column>
        <el-table-column label="票据张数" align="center" prop="ticketNum">
          <template slot-scope="scope">
            <el-input
              v-model="scope.row.ticketNum"
              type="number"
              placeholder="请输入张数"
              :disabled="isView"
              @change="calculateTotalAmount"
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
              @change="calculateTotalAmount"
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
        <template slot="file" slot-scope="{file}">
          <div class="el-upload-list__item">
            <img class="el-upload-list__item-thumbnail" :src="file.url" alt="" />
            <span class="el-upload-list__item-actions">
              <span
                class="el-upload-list__item-preview"
                @click="handlePictureCardPreview(file)"
              >
                <i class="el-icon-zoom-in"></i>
              </span>
              <span
                v-if="!isView"
                class="el-upload-list__item-delete"
                @click="handleDownload(file)"
              >
                <i class="el-icon-download"></i>
              </span>
              <span
                v-if="!isView"
                class="el-upload-list__item-delete"
                @click="handleRemove(file)"
              >
                <i class="el-icon-delete"></i>
              </span>
            </span>
          </div>
        </template>
      </el-upload>
      <el-dialog :visible.sync="previewOpen" title="图片预览" width="80%">
        <img :src="previewUrl" style="width: 100%;" />
      </el-dialog>
    </el-card>
  </el-form>
</template>

<script>
import { getReimburse, addReimburse, addReimComplete, updateReimburse, changeProcessState } from "@/api/reimburse";
import { getToken } from "@/utils/auth";

export default {
  name: "ReimburseForm",
  props: {
    isView: {
      type: Boolean,
      default: false,
    },
    reimburseId: {
      type: [Number, String, null],
      default: null,
    },
  },
  data() {
    return {
      form: {
        dateRange: null, 
        monthSelect: null,
        ticketTotal: 0,
        totalAmount: 0.00,
        remark: "",
        reimburseId: null,
        startTime: null,
        endTime: null
      },
      detailList: [],
      attachmentList: [],
      selectedDetailRows: [],
      previewOpen: false,
      previewUrl: "",
      uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload",
      uploadHeaders: {
        Authorization: "Bearer " + getToken(),
      },
      rules: {
        // 校验时间范围
        dateRange: [{ 
          required: true, 
          message: "时间范围不能为空", 
          trigger: "change" 
        }],
        monthSelect: [{ 
          required: true, 
          message: "月度选择不能为空", 
          trigger: "change" 
        }],
        ticketTotal: [{ 
          required: true, 
          message: "票据总数不能为空", 
          trigger: "change" 
        }],
        totalAmount: [{ 
          required: true, 
          message: "总金额不能为空", 
          trigger: "change" 
        }],
      },
    };
  },
  watch: {
    detailList: {
      handler(newList) {
        this.form.ticketTotal = newList.length;
        this.calculateTotalAmount();
      },
      deep: true
    },
    'form.dateRange': {
      handler(val) {
        if (val && val.length === 2) {
          this.form.startTime = val[0];
          this.form.endTime = val[1];
        } else {
          this.form.startTime = null;
          this.form.endTime = null;
        }
      },
      immediate: true
    }
  },
  methods: {
    calculateTotalAmount() {
      let total = 0;
      this.detailList.forEach(row => {
        const amount = Number(row.amount) || 0;
        total += amount;
      });
      this.form.totalAmount = Number(total.toFixed(2));
    },

    initForm(reimburseId) {
      this.resetForm();
      if (reimburseId) {
        getReimburse(reimburseId).then((response) => {
          const data = response.data;
          this.form = {
            ...data.reimburse,
            totalAmount: data.reimburse.totalAmount || 0.00,
            dateRange: data.reimburse.startTime && data.reimburse.endTime 
              ? [data.reimburse.startTime, data.reimburse.endTime] 
              : null
          };
          this.detailList = data.detailList || [];
          this.form.ticketTotal = this.detailList.length;
          this.calculateTotalAmount();
          this.attachmentList = (data.attachmentList || []).map((item, index) => ({
            ...item,
            url: item.filePath,
            name: item.fileName,
          }));
        });
      }
    },

    resetForm() {
      this.form = {
        dateRange: null,
        monthSelect: null,
        ticketTotal: 0,
        totalAmount: 0.00,
        remark: "",
        reimburseId: null,
        startTime: null,
        endTime: null
      };
      this.detailList = [];
      this.attachmentList = [];
      this.selectedDetailRows = [];
    },

    validateLastDetailRow() {
      if (this.detailList.length === 0) return true;
      const lastRow = this.detailList[this.detailList.length - 1];
      const requiredFields = [
        'ticketType', 'ticketNum', 'startPlace', 
        'endPlace', 'ticketDate', 'amount'
      ];
      for (const field of requiredFields) {
        const value = lastRow[field];
        if (value === "" || value === null || value === undefined) {
          this.$modal.msgWarning(`请先填写完最后一行明细的【${this.getFieldName(field)}】字段`);
          return false;
        }
        if ((field === 'ticketNum' || field === 'amount') && isNaN(Number(value))) {
          this.$modal.msgWarning(`请填写有效的【${this.getFieldName(field)}】数字`);
          return false;
        }
      }
      return true;
    },

    getFieldName(field) {
      const fieldMap = {
        ticketType: '票据类型',
        ticketNum: '票据张数',
        startPlace: '起始地',
        endPlace: '目的地',
        ticketDate: '票据日期',
        amount: '款项金额',
        accommodation: '住宿地点',
        totalAmount: '总金额',
        dateRange: '时间范围'
      };
      return fieldMap[field] || field;
    },

    addDetail() {
      if (!this.validateLastDetailRow()) {
        return;
      }
      
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

    delDetail() {
      if (this.selectedDetailRows.length === 0) {
        this.$modal.msgWarning("请选择要删除的明细行");
        return;
      }
      
      this.$modal.confirm('此操作将永久删除选中的明细行, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const selectedIds = this.selectedDetailRows.map(row => {
          return row.detailId || this.detailList.indexOf(row);
        });
        this.detailList = this.detailList.filter(row => {
          const rowId = row.detailId || this.detailList.indexOf(row);
          return !selectedIds.includes(rowId);
        });

        this.selectedDetailRows = [];
        if (this.$refs.detailTable) {
          this.$refs.detailTable.clearSelection();
        }
        
        this.$modal.msgSuccess("删除成功");
      }).catch(() => {
        this.$modal.msgInfo("已取消删除");
      });
    },

    handleDetailSelectionChange(val) {
      this.selectedDetailRows = val;
    },

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

    handleUploadSuccess(response, file) {
      this.attachmentList.push({
        fileName: response.originalFilename,
        filePath: response.fileName,
        fileSize: file.size,
        fileType: file.raw.type,
        url: response.url,
      });
    },

    handleExceed(files, fileList) {
      this.$modal.msgError(`最多只能上传10张图片`);
    },

    handlePictureCardPreview(file) {
      this.previewUrl = file.url;
      this.previewOpen = true;
    },
    // handlePictureCardPreview(file) {
    //   // 1. 校验 URL 有效性
    //   if (!file.url) {
    //     this.$modal.msgError("图片预览地址无效");
    //     return;
    //   }
    //   // 2. 先预加载图片，避免弹窗空白
    //   const img = new Image();
    //   img.onload = () => {
    //     this.previewUrl = file.url;
    //     this.previewOpen = true; // 图片加载完成后再打开弹窗
    //   };
    //   img.onerror = () => {
    //     this.$modal.msgError("图片加载失败，请检查图片地址");
    //   };
    //   img.src = file.url; // 触发图片预加载
    // },
    handleRemove(file) {
      const index = this.attachmentList.findIndex(item => item.uid === file.uid);
      if (index > -1) {
        this.attachmentList.splice(index, 1);
      }
    },
    handleDownload(file) {
      const link = document.createElement("a");
      link.href = file.url;
      link.download = file.name || file.fileName;
      link.click();
    },

    submitForm() {
      return new Promise((resolve, reject) => {
        this.$refs.form.validate((valid) => {
          if (valid) {
            const allValid = this.detailList.every(row => {
              const requiredFields = ['ticketType', 'ticketNum', 'startPlace', 'endPlace', 'ticketDate', 'amount'];
              for (const field of requiredFields) {
                const value = row[field];
                if (value === "" || value === null || value === undefined || 
                    ((field === 'ticketNum' || field === 'amount') && isNaN(Number(value)))) {
                  this.$modal.msgWarning(`存在未填写完整的明细行，请检查【${this.getFieldName(field)}】字段`);
                  return false;
                }
              }
              return true;
            });
            
            if (!allValid) {
              reject("存在未填写完整的明细行");
              return;
            }
            
            this.calculateTotalAmount();
            
            console.log("处理附件数据");
            const attachments = this.attachmentList.map(item => {
              return {
                fileName: item.fileName,
                filePath: item.filePath,
                fileSize: item.fileSize,
                fileType: item.fileType,
              };
            });
            if (this.form.dateRange && this.form.dateRange.length === 2) {
              this.form.startTime = this.form.dateRange[0];
              this.form.endTime = this.form.dateRange[1];
            }
            
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
    reviewApprove() {
      return new Promise((resolve, reject) => {
        const params = {
              reimburseId: this.form.reimburseId,
              processState: "APPROVED",
            };
        try {
          changeProcessState(params).then(() => {
                this.$modal.msgSuccess("审核通过!");
                resolve();
              }).catch(error => {
                reject(error);
              });
        } catch (error) {
            this.$modal.msgError("审核操作失败");
            reject(error);
        }
      });
    },
    reviewReject() {
      return new Promise((resolve, reject) => {
        const params = {
              reimburseId: this.form.reimburseId,
              processState: "REJECTED",
            };
        try {
          changeProcessState(params).then(() => {
                this.$modal.msgSuccess("审核拒绝!");
                resolve();
              }).catch(error => {
                reject(error);
              });
        } catch (error) {
            this.$modal.msgError("审核操作失败");
            reject(error);
        }
      });
    },
  },
};
</script>