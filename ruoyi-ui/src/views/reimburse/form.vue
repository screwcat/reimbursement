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
            <!-- 新增：显示计算的天数 -->
            <div v-if="form.dateRange && form.dateRange.length === 2" style="margin-top: 8px; color: #606266; font-size: 12px;">
              共计：{{ daysCount }} 天
            </div>
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
              :disabled="isView">
              <el-option
                v-for="dict in dict.type.invoice_type"
                :key="dict.value"
                :label="dict.label"
                :value="dict.value"
              />
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
              type="text"
              placeholder="请输入金额"
              :disabled="isView"
              @input="handleAmountInput(scope.row)"
              @change="handleAmountBlur(scope.row)"
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
          请上传jpg/png/pdf格式的文件，单个文件不超过5MB，最多上传10个
        </div>
        <template slot="file" slot-scope="{file}">
          <div class="el-upload-list__item">
            <!-- 图片显示缩略图，PDF显示生成的缩略图 -->
            <div v-if="file.type.includes('image')" class="file-preview-img">
              <img class="el-upload-list__item-thumbnail" :src="'../profile/upload/'+file.url" alt="" />
            </div>
            <div v-else-if="file.type.includes('pdf')" class="file-preview-pdf">
              <!-- PDF显示生成的缩略图 -->
              <img 
                v-if="file.thumbnailUrl" 
                class="el-upload-list__item-thumbnail" 
                :src="'../profile/upload/'+file.thumbnailUrl" 
                alt="PDF缩略图"
              />
              <div v-else class="pdf-placeholder">
                <i class="el-icon-file-pdf"></i>
                <span class="pdf-name">{{ file.name || file.fileName }}</span>
              </div>
            </div>
            <span class="el-upload-list__item-actions">
              <span
                class="el-upload-list__item-preview"
                @click="handleFilePreview(file)"
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
      <!-- 图片预览弹窗 -->
      <el-dialog :visible.sync="previewOpen" title="文件预览" width="80%" append-to-body>
        <img v-if="previewType === 'image'" :src="previewUrl" style="width: 100%;" />
        <div v-else-if="previewType === 'pdf'" class="pdf-preview-container">
          <embed :src="previewUrl" type="application/pdf" width="100%" height="600px" />
        </div>
        <div v-else class="preview-error">
          <i class="el-icon-warning"></i>
          暂不支持该类型文件预览
        </div>
      </el-dialog>
    </el-card>
  </el-form>
</template>

<script>
import { getReimburse, addReimburse, addReimComplete, updateReimburse, changeProcessState, checkTimePeriod } from "@/api/reimburse";
import { getToken } from "@/utils/auth";

export default {
  name: "ReimburseForm",
  dicts: ['invoice_type'],
  props: {
    isView: {
      type: Boolean,
      default: false,
    },
    reimburseId: {
      type: [Number, String, null],
      default: null,
    },
    userName: {
      type: String,
      default: '',
  },
  },
  data() {
    const validateTimePeriod = (rule, value, callback) => {
      // 获取开始时间和结束时间（根据实际表单字段名调整）
      const startTime = this.form.startTime;
      const endTime = this.form.endTime;
      const userName = this.userName; 

      // 先校验时间必填（可选）
      if (!startTime || !endTime) {
        return callback(new Error("请选择开始时间和结束时间"));
      }

      // 调用后台校验接口
      checkTimePeriod({
        userName: userName,
        reimburseId: this.reimburseId,
        startDate: startTime,
        endDate: endTime,
      }).then(response => {
        if (response) {
          // 返回true，校验通过
          callback();
        } else {
          // 返回false，提示重复
          callback(new Error("时间范围和其他行程有重叠"));
        }
      }).catch(error => {
        // 接口异常处理
        callback(new Error("时间范围校验失败，请重试"));
      });
    };
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
      previewType: "", // image/pdf/other
      uploadUrl: process.env.VUE_APP_BASE_API + "/common/upload",
      uploadHeaders: {
        Authorization: "Bearer " + getToken(),
      },
      rules: {
        dateRange: [{ 
          required: true, 
          message: "时间范围不能为空", 
          trigger: "change" 
        }, { validator: validateTimePeriod, trigger: 'blur' }],
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
  // 新增：计算天数的计算属性
  computed: {
    daysCount() {
      if (!this.form.dateRange || this.form.dateRange.length !== 2) {
        return 0;
      }
      // 转换为Date对象
      const startDate = new Date(this.form.dateRange[0]);
      const endDate = new Date(this.form.dateRange[1]);
      
      // 计算时间差（毫秒）
      const timeDiff = endDate.getTime() - startDate.getTime();
      // 转换为天数（向上取整，包含开始和结束日）
      const days = Math.ceil(timeDiff / (1000 * 60 * 60 * 24)) + 1;
      
      return days;
    }
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
            type: item.fileType, // 确保文件类型字段存在
            uid: item.uid || index, // 确保有唯一标识
            thumbnailUrl: item.thumbnailUrl,
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
      // 支持的文件类型
      const allowedTypes = [
        "image/jpeg", 
        "image/png", 
        "application/pdf",
        "application/x-pdf",
        "application/octet-stream" // 兼容部分PDF的MIME类型
      ];
      // 文件大小限制5MB
      const isLt5M = file.size / 1024 / 1024 < 5;

      // 校验文件类型
      const fileType = file.type;
      const fileName = file.name.toLowerCase();
      const isAllowedType = allowedTypes.includes(fileType) || fileName.endsWith('.pdf');
      
      if (!isAllowedType) {
        this.$modal.msgError("请上传jpg/png/pdf格式的文件");
        return false;
      }
      if (!isLt5M) {
        this.$modal.msgError("文件大小不能超过5MB");
        return false;
      }
      return true;
    },

    handleUploadSuccess(response, file) {
      // 构建附件对象，包含缩略图地址
      const attachment = {
        fileName: response.originalFilename,
        filePath: response.fileName,
        fileSize: file.size,
        fileType: response.fileType || file.raw.type,
        url: response.url,
        thumbnailUrl: response.thumbnailUrl, // 新增：保存缩略图地址
        name: response.originalFilename,
        uid: file.uid,
        type: response.fileType || file.raw.type
      };
      this.attachmentList.push(attachment);
    },

    handleExceed(files, fileList) {
      this.$modal.msgError(`最多只能上传10个文件`);
    },

    // 处理文件预览（图片+PDF）
    handleFilePreview(file) {
      if (!file.url) {
        this.$modal.msgError("文件预览地址无效");
        return;
      }
      let fileUrl = "../profile/upload/" + file.url;
      // 判断文件类型
      if (file.type.includes('image') || file.name?.toLowerCase().endsWith(('.jpg', '.png'))) {
        this.previewType = 'image';
        // 图片预加载
        const img = new Image();
        img.src = fileUrl;
        img.onload = () => {
          this.previewUrl = fileUrl;
          this.previewOpen = true;
        };
        img.onerror = () => {
          this.$modal.msgError("图片加载失败，请检查文件地址");
        };
      } else if (file.type.includes('pdf') || file.name?.toLowerCase().endsWith('.pdf')) {
        this.previewType = 'pdf';
        this.previewUrl = fileUrl;
        this.previewOpen = true;
      } else {
        this.previewType = 'other';
        this.$modal.msgWarning("暂不支持该类型文件预览");
        this.previewOpen = true;
      }
    },

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
      // PDF下载需要添加target=_blank兼容部分浏览器
      link.target = '_blank';
      // 解决跨域下载问题
      link.rel = 'noopener noreferrer';
      document.body.appendChild(link);
      link.click();
      document.body.removeChild(link);
    },

    submitForm(docId) {
      return new Promise((resolve, reject) => {
        this.$refs.form.validate((valid) => {
          if (valid) {
            if (this.detailList.length === 0) {
              this.$modal.msgWarning("明细列表不能为空，请至少添加一条明细！");
              return false;
            }
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
                thumbnailUrl: item.thumbnailUrl,
              };
            });
            if (this.form.dateRange && this.form.dateRange.length === 2) {
              this.form.startTime = this.form.dateRange[0];
              this.form.endTime = this.form.dateRange[1];
            }
            this.form.docId = docId;
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
                this.$modal.msgWarning("审核拒绝!");
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
    // 新增：金额输入过滤（仅允许数字+单个小数点+小数点后两位）
    handleAmountInput(row) {
      if (row.amount === null || row.amount === undefined) {
        row.amount = '';
        return;
      }
      // 1. 转为字符串并过滤非法字符
      let val = row.amount.toString()
        .replace(/[^0-9.]/g, '') // 过滤非数字和小数点
        .replace(/^\./, '0.')    // 以小数点开头 → 补0（如 .5 → 0.5）
        .replace(/\.{2,}/g, '.') // 多个小数点保留第一个
        .replace(/(\.\d{2}).+/, '$1'); // 小数点后最多两位
      
      // 2. 仅在值变化时赋值（避免死循环）
      if (val !== row.amount.toString()) {
        row.amount = val;
      }
      // 3. 实时计算总金额
      this.calculateTotalAmount();
    },

    // 新增：失焦时格式化（补全两位小数，如 10 → 10.00，10.5 → 10.50）
    handleAmountBlur(row) {
      if (!row.amount || row.amount === '' || row.amount === '0.') {
        row.amount = '0.00';
        this.calculateTotalAmount();
        return;
      }
      // 转为数字后格式化，避免纯字符串导致计算错误
      const num = Number(row.amount);
      row.amount = isNaN(num) ? '0.00' : num.toFixed(2);
      this.calculateTotalAmount();
    },

    // 优化原有 calculateTotalAmount 方法（兼容字符串格式的金额）
    calculateTotalAmount() {
      let total = 0;
      this.detailList.forEach(row => {
        // 兼容空值/字符串格式，转为数字计算
        const amount = row.amount ? Number(row.amount) : 0;
        total += amount;
      });
      this.form.totalAmount = Number(total.toFixed(2));
    },
  },
};
</script>

<style scoped>
/* PDF预览样式 */
.pdf-preview-container {
  width: 100%;
  height: 600px;
}

/* 文件预览错误样式 */
.preview-error {
  text-align: center;
  padding: 50px 0;
  color: #f56c6c;
  font-size: 16px;
}

.preview-error i {
  font-size: 24px;
  margin-right: 8px;
}

/* PDF文件预览卡片样式 */
.file-preview-pdf {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  background-color: #f5f7fa;
  color: #409eff;
}

.file-preview-pdf i {
  font-size: 36px;
  margin-bottom: 8px;
}

.pdf-name {
  font-size: 12px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 80%;
  text-align: center;
}

/* 图片预览样式 */
.file-preview-img {
  width: 100%;
  height: 100%;
}

.el-upload-list__item-thumbnail {
  width: 100%;
  height: 100%;
  object-fit: cover;
}
</style>