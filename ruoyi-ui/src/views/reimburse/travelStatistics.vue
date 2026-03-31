<template>
  <div id="gantt-chart" style="width: 100%; height: 400px;"></div>
</template>

<script>
// ECharts 5.x 正确引入方式（统一入口）
import * as echarts from 'echarts';

export default {
  name: "TravelGanttWithEcharts",
  data() {
    return {
      chart: null,
      retData: [
        {
          employeeName: "张三",
          travelPeriods: [
            { startDay: 1, endDay: 8 },
            { startDay: 11, endDay: 19 },
            { startDay: 22, endDay: 29 }
          ]
        },
        {
          employeeName: "李四",
          travelPeriods: [
            { startDay: 3, endDay: 12 },
            { startDay: 15, endDay: 23 },
            { startDay: 27, endDay: 30 }
          ]
        },
        {
          employeeName: "王五",
          travelPeriods: [
            { startDay: 2, endDay: 7 },
            { startDay: 11, endDay: 19 },
            { startDay: 22, endDay: 25 }
          ]
        }
      ]
    };
  },
  mounted() {
    // 确保DOM完全渲染后初始化
    this.$nextTick(() => {
      this.initChart();
    });
  },
  beforeDestroy() {
    // 销毁图表，避免内存泄漏
    if (this.chart) {
      this.chart.dispose();
      this.chart = null;
    }
  },
  methods: {
    // 生成随机浅色系颜色
    getRandomLightColor() {
      const r = Math.floor(150 + Math.random() * 105);
      const g = Math.floor(150 + Math.random() * 105);
      const b = Math.floor(150 + Math.random() * 105);
      return `rgb(${r}, ${g}, ${b})`;
    },

    // 转换数据为ECharts格式
    transformData() {
      const echartsData = [];
      const employeeColors = {};

      // 为每个员工分配唯一颜色
      this.retData.forEach(emp => {
        employeeColors[emp.employeeName] = this.getRandomLightColor();
      });

      // 遍历出差时段生成ECharts数据
      this.retData.forEach(emp => {
        emp.travelPeriods.forEach(period => {
          echartsData.push({
            name: emp.employeeName,
            value: [period.startDay, period.endDay, emp.employeeName],
            itemStyle: {
              color: employeeColors[emp.employeeName]
            }
          });
        });
      });

      return {
        echartsData,
        employeeNames: this.retData.map(emp => emp.employeeName)
      };
    },

    // 初始化ECharts图表（适配5.4.0）
    initChart() {
      // 1. 校验DOM元素是否存在
      const chartDom = document.getElementById('gantt-chart');
      if (!chartDom) {
        console.error('图表容器DOM不存在');
        return;
      }

      // 2. 初始化图表（5.x 写法和4.x一致，只是引入方式不同）
      this.chart = echarts.init(chartDom);
      const { echartsData, employeeNames } = this.transformData();

      // 3. 图表配置项（5.x 完全兼容4.x的配置）
      const option = {
        grid: {
          left: '80px',   // 避免姓名覆盖日期轴
          right: '20px',
          top: '30px',
          bottom: '20px'
        },
        tooltip: {
          trigger: 'item',
          formatter: params => {
            const [startDay, endDay, name] = params.value;
            return `${name}<br/>出差时段：${startDay}日 - ${endDay}日`;
          }
        },
        xAxis: {
          type: 'value',
          min: 1,
          max: 30,
          axisLabel: { formatter: '{value}日' },
          axisLine: { show: true },
          splitLine: {
            show: true,
            lineStyle: { type: 'dashed' }
          }
        },
        yAxis: {
          type: 'category',
          data: employeeNames,
          axisLine: { show: true }
        },
        series: [
          {
            name: '出差时段',
            type: 'bar',
            data: echartsData,
            barWidth: '60%',    // 柱子高度
            barCategoryGap: '20%',
            itemStyle: { borderRadius: 2 }
          }
        ]
      };

      // 4. 渲染图表
      this.chart.setOption(option);

      // 5. 自适应窗口大小
      window.addEventListener('resize', () => {
        this.chart && this.chart.resize();
      });
    }
  }
};
</script>

<style scoped>
#gantt-chart {
  border: 1px solid #000;
  box-sizing: border-box;
  margin: 20px auto;
  max-width: 1200px;
}
</style>