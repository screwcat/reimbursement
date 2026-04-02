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

    <div ref="chart" style="width: 100%; height: 500px;"></div>
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
      colorMap: {
        "系统管理员": "#6faed9",
        "张巍": "#f7c442"
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
    // 初始化图表
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
        tooltip: {
          formatter: params => {
            const [sIndex, eIndex, name, sMonth, eMonth, amount, sDay, eDay] = params.value;
            return `
              <div>${name}</div>
              <div>出差时间：${sMonth}月${sDay}日 - ${eMonth}月${eDay}日</div>
              <div>总金额：${amount}元</div>
            `;
          }
        },
        grid: { left: "10%", right: "5%", top: "10%", bottom: "20%" }, // 增加底部间距防止刻度被遮挡
        xAxis: {
          type: "category",
          name: xAxisName,
          data: this.dateScale, // 绑定完整日期刻度
          axisLabel: {
            formatter: (value) => value, // 显示如"3/1" "4/30"
            rotate: 45, // 旋转刻度避免重叠
            interval: 0 // 显示所有刻度（可根据需要调整为1/2/3显示间隔）
          },
          splitLine: { show: false },
          axisTick: {
            alignWithLabel: true // 刻度线与标签对齐
          }
        },
        yAxis: {
          type: "category",
          data: employeeNames,
          axisLine: { show: false },
          axisTick: { show: false }
        },
        series: [
          {
            name: "出差行程",
            type: "custom",
            renderItem: (params, api) => {
              const yVal = api.value(2);
              const start = api.coord([api.value(0), yVal]);
              const end = api.coord([api.value(1), yVal]);
              const height = api.size([0, 1])[1] * 0.3;

              return {
                type: "rect",
                shape: {
                  x: start[0],
                  y: start[1] - height / 2,
                  width: end[0] - start[0],
                  height
                },
                style: api.style({
                  fill: this.colorMap[yVal] || "#589eff",
                  stroke: "#555",
                  lineWidth: 1
                })
              };
            },
            encode: { x: [0, 1], y: 2, tooltip: [0, 1, 2, 3, 4, 5, 6, 7] },
            data: chartData
          }
        ]
      };

      this.chartInstance.setOption(option);
    }
  }
};
</script>

<style scoped>
.chart-wrapper {
  padding: 20px;
  background-color: #fff;
  border: 1px solid #589eff;
  border-radius: 4px;
}
</style>