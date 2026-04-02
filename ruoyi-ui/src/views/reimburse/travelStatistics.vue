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
        "0": "#6faed9",
        "1": "#f7c442",
        "默认": "#589eff"
      },
      // 存储查询范围的日期刻度（如["3/1", "3/2"... "4/30"]）
      dateScale: [],
      // 马卡龙色系基础色值（可根据需要扩展）
      macaronBaseColors: [
        '#FFC2E2', '#FFB3BA', '#FFDFBA', '#FFFFBA', '#BAFFC9', 
        '#BAE1FF', '#D9AEFC', '#FFC8DD', '#FFAFCC', '#BDE0FE', 
        '#A2D2FF', '#F4ACB7', '#9D8189', '#8E9AAF', '#C8B6FF'
      ],
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
            this.colorMap = this.generateMacaronColorMap(this.retData.length);
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
    // 初始化图表（核心：左边距调整 + X轴刻度改为1天）
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
        // 🌟 核心修改：左边距从15%改为8%，图表大幅左靠，其他边距微调
        grid: { left: "2%", right: "8%", top: "8%", bottom: "25%", containLabel: true },
        // X轴样式优化 - 核心：刻度改为1天显示
        xAxis: {
          type: "category",
          name: xAxisName,
          nameTextStyle: { fontSize: 12, color: '#666', padding: [0,0,10,0] },
          data: this.dateScale,
          axisLabel: {
            formatter: (value) => value,
            rotate: 45, // 🌟 增加旋转角度，避免日期标签重叠
            interval: 0, // 🌟 关键修改：显示所有刻度（1天1个），默认interval:2是隔1天显示
            fontSize: 10,
            color: '#666',
            align: 'center' // 标签居中对齐
          },
          axisLine: { lineStyle: { color: '#e6e6e6' } },
          axisTick: {
            alignWithLabel: true, // 刻度线与标签对齐
            lineStyle: { color: '#e6e6e6' }
          },
          splitLine: {
            show: true,
            // 🌟 可选：如果需要加深网格线颜色，可修改这里
            lineStyle: { color: '#dcdcdc', type: 'solid' }
          }
        },
        // Y轴样式优化：微调内边距，适配左间距缩小
        yAxis: {
          type: "category",
          data: employeeNames,
          axisLabel: {
            fontSize: 12,
            color: '#333',
            align: 'right',
            padding: [0,0,0,5] // 🌟 内边距从10改为5，配合左间距缩小
          },
          axisLine: { show: false },
          axisTick: { show: false },
          // 🌟 可选：如果需要显示行间横分割线，可打开下面配置
          splitLine: {
            show: true,
            lineStyle: { color: '#dcdcdc', type: 'solid', width: 1 }
          }
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
              const height = api.size([0, 1])[1] * 0.6;
              // 取配色，无映射则用默认色
              const baseColor = this.colorMap[yVal] || this.colorMap["默认"];
              return {
                type: "rect",
                shape: {
                  x: start[0],
                  y: start[1] - height / 2,
                  width: end[0] - start[0] || 20,
                  height,
                  r: 4
                },
                style: api.style({
                  fill: baseColor,
                  stroke: '#fff',
                  lineWidth: 2,
                  // 悬停高亮样式
                  emphasis: {
                    fill: echarts.color.lift(baseColor, 0.2),
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
    },
    // 生成随机马卡龙颜色（核心新增方法）
    generateMacaronColorMap(length) {
      const colorMap = {};
      // 如果需要的颜色数量超过基础色库，循环使用并微调亮度
      for (let i = 0; i < length; i++) {
        // let baseColor = this.macaronBaseColors[i % this.macaronBaseColors.length];
        // // 对超出基础色库的颜色进行轻微亮度调整，避免完全重复
        // if (i >= this.macaronBaseColors.length) {
        //   const hueStep = Math.floor(Math.random() * 10) - 5; // 亮度微调值
        //   baseColor = this.adjustColorBrightness(baseColor, hueStep);
        // }
        colorMap[i] = this.getRandomLightColor();
      }
      // 保留默认色（可选）
      colorMap['默认'] = '#589eff';
      return colorMap;
    },
    // 调整颜色亮度（辅助方法）
    adjustColorBrightness(color, step) {
      // 解析十六进制颜色为RGB
      const rgb = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(color);
      if (!rgb) return color;
      
      let r = parseInt(rgb[1], 16);
      let g = parseInt(rgb[2], 16);
      let b = parseInt(rgb[3], 16);
      
      // 调整亮度，确保在0-255范围内
      r = Math.max(0, Math.min(255, r + step));
      g = Math.max(0, Math.min(255, g + step));
      b = Math.max(0, Math.min(255, b + step));
      
      // 转回十六进制
      const toHex = (val) => val.toString(16).padStart(2, '0');
      return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
    },
    // 生成随机浅色系RGB颜色，返回十六进制格式
    getRandomLightColor() {
      const r = Math.floor(150 + Math.random() * 105);
      const g = Math.floor(150 + Math.random() * 105);
      const b = Math.floor(150 + Math.random() * 105);

      // 转为两位十六进制，并拼接成 #RRGGBB 格式
      const toHex = (c) => c.toString(16).padStart(2, '0');
      return `#${toHex(r)}${toHex(g)}${toHex(b)}`;
    },
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