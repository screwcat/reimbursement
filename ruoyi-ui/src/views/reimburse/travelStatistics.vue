<template>
  <div class="chart-wrapper">
    <h2>员工出差日程甘特图</h2>
    <!-- 顶部：月份范围选择器 + 搜索按钮 -->
    <div class="search-bar" style="margin-bottom: 20px; display: flex; align-items: center; gap: 10px;">
      <el-date-picker
        v-model="dateRange"
        type="monthrange"
        range-separator="至"
        start-placeholder="开始月份"
        end-placeholder="结束月份"
        format="yyyy-MM"
        value-format="yyyy-MM"
      ></el-date-picker>
      <el-button type="primary" @click="fetchTravelData">搜索</el-button>
    </div>
    <div ref="chart" style="width: 100%; height: 600px;"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';
import { getTravelStatistics } from "@/api/reimburse";
export default {
  name: "TravelStatistics",
  data() {
    return {
      chartInstance: null,
      // 接口返回数据
      retData: [],
      // 月份范围选择器绑定 - 默认当前月
      dateRange: this.getDefaultMonthRange(),
      // 扩展配色映射，增加多员工适配
      colorMap: {
        "系统管理员": "#6faed9",
        "张巍": "#f7c442",
        "默认": "#589eff"
      },
      // 存储查询范围的日期刻度（如["3/1", "3/2"... "4/30"]）
      dateScale: []
    };
  },
  mounted() {
    this.initChart();
    // 页面加载后自动加载数据
    this.fetchTravelData();
    window.addEventListener('resize', this.resizeChart);
  },
  beforeDestroy() {
    window.removeEventListener('resize', this.resizeChart);
    if (this.chartInstance) {
      this.chartInstance.dispose();
    }
  },
  methods: {
    // 获取默认月份范围（当前月）
    getDefaultMonthRange() {
      const now = new Date();
      const year = now.getFullYear();
      const month = now.getMonth() + 1;
      const monthStr = month < 10 ? `0${month}` : month;
      const currentMonth = `${year}-${monthStr}`;
      // 月份范围选择器需要数组格式 [开始月, 结束月]
      return [currentMonth, currentMonth];
    },
    // 图表自适应
    resizeChart() {
      this.chartInstance && this.chartInstance.resize();
    },
    // 点击搜索调用接口（改用.then方式）
    fetchTravelData() {
      // 构造参数
      const params = {
        startMonth: this.dateRange?.[0] || "",
        endMonth: this.dateRange?.[1] || ""
      };
      
      // 调用后台接口（改用.then/.catch）
      getTravelStatistics(params)
        .then(res => {
          // 把 rows 赋值给 retData
          if (res && res.rows) {
            this.retData = res.rows;
          } else {
            this.retData = [];
          }
          // 重新渲染图表
          this.initChart();
        })
        .catch(err => {
          console.error("查询统计数据失败：", err);
          this.$message.error("查询失败，请稍后重试");
        });
    },
    // 日期解析
    parseDateStr(dateStr) {
      const date = new Date(dateStr);
      return {
        year: date.getFullYear(),
        month: date.getMonth() + 1,
        day: date.getDate(),
        timestamp: date.getTime()
      };
    },
    // 生成查询时间范围的所有日期刻度（核心修改）
    generateDateScale() {
      this.dateScale = [];
      if (!this.dateRange || this.dateRange.length < 2) {
        // 无查询条件时默认显示当月
        const now = new Date();
        const year = now.getFullYear();
        const month = now.getMonth() + 1;
        const lastDay = new Date(year, month, 0).getDate();
        for (let day = 1; day <= lastDay; day++) {
          this.dateScale.push(`${month}/${day}`);
        }
        return;
      }
      // 解析查询的开始/结束月份
      const [startMonthStr, endMonthStr] = this.dateRange;
      const [startYear, startMonth] = startMonthStr.split('-').map(Number);
      const [endYear, endMonth] = endMonthStr.split('-').map(Number);
      // 生成开始月份的所有日期
      const startLastDay = new Date(startYear, startMonth, 0).getDate();
      for (let day = 1; day <= startLastDay; day++) {
        this.dateScale.push(`${startMonth}/${day}`);
      }
      // 生成中间月份（如果有）的所有日期（跨多月场景）
      if (endMonth - startMonth > 1) {
        for (let m = startMonth + 1; m < endMonth; m++) {
          const lastDay = new Date(endYear, m, 0).getDate();
          for (let day = 1; day <= lastDay; day++) {
            this.dateScale.push(`${m}/${day}`);
          }
        }
      }
      // 生成结束月份的所有日期
      if (startMonth !== endMonth) {
        const endLastDay = new Date(endYear, endMonth, 0).getDate();
        for (let day = 1; day <= endLastDay; day++) {
          this.dateScale.push(`${endMonth}/${day}`);
        }
      }
    },
    // 转换出差日期到刻度索引
    getDateIndex(month, day) {
      const target = `${month}/${day}`;
      return this.dateScale.findIndex(item => item === target);
    },
    // 初始化图表（核心样式改造）
    initChart() {
      // 生成日期刻度
      this.generateDateScale();
      if (this.chartInstance) {
        this.chartInstance.dispose();
      }
      this.chartInstance = echarts.init(this.$refs.chart);
      const chartData = [];
      this.retData.forEach(item => {
        item.travelPeriodList.forEach(period => {
          const start = this.parseDateStr(period.startTime);
          const end = this.parseDateStr(period.endTime);
          // 转换为刻度索引
          const startIndex = this.getDateIndex(start.month, start.day);
          const endIndex = this.getDateIndex(end.month, end.day);
          
          if (startIndex > -1 && endIndex > -1) {
            chartData.push({
              name: item.nickName,
              value: [
                startIndex,
                endIndex,
                item.nickName,
                start.month,
                end.month,
                period.totalAmount,
                start.day,
                end.day
              ]
            });
          }
        });
      });
      const employeeNames = this.retData.map(item => item.nickName);
      // 拼接X轴名称
      const xAxisName = this.dateRange 
        ? `${this.dateRange[0].split('-')[1]}月1日 - ${this.dateRange[1].split('-')[1]}月${new Date(this.dateRange[1].split('-')[0], this.dateRange[1].split('-')[1], 0).getDate()}日`
        : `${new Date().getMonth() + 1}月 日期`;
      const option = {
        // 全局tooltip优化
        tooltip: {
          formatter: params => {
            const [sIndex, eIndex, name, sMonth, eMonth, amount, sDay, eDay] = params.value;
            return `
              <div style="font-weight: bold; margin-bottom: 4px;">${name}</div>
              <div style="line-height: 1.6;">出差时间：${sMonth}月${sDay}日 - ${eMonth}月${eDay}日</div>
              <div style="line-height: 1.6;">总金额：<span style="color: #f56c6c; font-weight: bold;">${amount}元</span></div>
            `;
          },
          trigger: 'item',
          padding: 10,
          backgroundColor: 'rgba(255,255,255,0.95)',
          borderColor: '#e6e6e6',
          borderWidth: 1,
          boxShadow: '0 2px 12px 0 rgba(0,0,0,0.1)',
          textStyle: { color: '#333' }
        },
        // 网格优化：增大左侧员工名称间距，底部预留更多刻度空间
        grid: { left: "15%", right: "3%", top: "8%", bottom: "25%", containLabel: true },
        // X轴样式优化
        xAxis: {
          type: "category",
          name: xAxisName,
          nameTextStyle: { fontSize: 12, color: '#666', padding: [0,0,10,0] },
          data: this.dateScale,
          axisLabel: {
            formatter: (value) => value,
            rotate: 0, // 取消旋转，改为横向换行
            interval: 2, // 每3个刻度显示一个，避免重叠
            fontSize: 10,
            color: '#666'
          },
          axisLine: { lineStyle: { color: '#e6e6e6' } },
          axisTick: {
            alignWithLabel: true,
            lineStyle: { color: '#e6e6e6' }
          },
          splitLine: { 
            show: true,
            lineStyle: { color: '#f5f5f5', type: 'dashed' } 
          }
        },
        // Y轴样式优化（员工名称）
        yAxis: {
          type: "category",
          data: employeeNames,
          axisLabel: {
            fontSize: 12,
            color: '#333',
            align: 'right', // 名称右对齐，贴近甘特图
            padding: [0,0,0,10]
          },
          axisLine: { show: false },
          axisTick: { show: false },
          splitLine: { show: false } // 隐藏Y轴分割线
        },
        // 系列配置（核心甘特图样式）
        series: [
          {
            name: "出差行程",
            type: "custom",
            // 自定义渲染甘特图块
            renderItem: (params, api) => {
              const yVal = api.value(2);
              const start = api.coord([api.value(0), yVal]);
              const end = api.coord([api.value(1), yVal]);
              const height = api.size([0, 1])[1] * 0.6; // 增大任务条高度，更醒目
              // 取配色，无映射则用默认色
              const baseColor = this.colorMap[yVal] || this.colorMap["默认"];
              return {
                type: "rect",
                shape: {
                  x: start[0],
                  y: start[1] - height / 2,
                  width: end[0] - start[0] || 20, // 避免单日出差宽度为0
                  height,
                  r: 4 // 圆角处理，核心样式优化
                },
                style: api.style({
                  fill: baseColor,
                  stroke: '#fff',
                  lineWidth: 2, // 白色描边，增加层次感
                  // 悬停高亮样式
                  emphasis: {
                    fill: echarts.color.lift(baseColor, 0.2), // 悬停变亮
                    stroke: '#409eff',
                    lineWidth: 2,
                    shadowBlur: 6,
                    shadowColor: 'rgba(64,158,255,0.3)'
                  }
                })
              };
            },
            encode: { x: [0, 1], y: 2, tooltip: [0, 1, 2, 3, 4, 5, 6, 7] },
            data: chartData,
            // 取消鼠标悬浮时的全局高亮，仅高亮当前块
            emphasis: {
              focus: 'self'
            }
          }
        ],
        // 全局样式
        textStyle: { fontFamily: 'Microsoft YaHei' }
      };
      this.chartInstance.setOption(option);
    }
  }
};
</script>

<style scoped>
.chart-wrapper {
  padding: 24px;
  background-color: #fff;
  border: 1px solid #e6e6e6;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0,0,0,0.04);
}
.chart-wrapper h2 {
  margin: 0 0 20px 0;
  font-size: 16px;
  color: #333;
  font-weight: 600;
}
/* 适配element-ui日期选择器高度 */
:deep(.el-date-editor) {
  height: 36px;
}
:deep(.el-button--primary) {
  height: 36px;
  padding: 0 20px;
}
</style>