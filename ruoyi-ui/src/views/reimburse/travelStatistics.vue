<template>
  <div class="chart-wrapper">
    <h2>员工出差日程甘特图</h2>
    <div ref="chart" style="width: 100%; height: 500px;"></div>
  </div>
</template>

<script>
import * as echarts from 'echarts';

export default {
  name: 'TravelGantt',
  data() {
    return {
      chartInstance: null,
      // 原始数据
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
            { startDay: 22, endDay: 29 }
          ]
        }
      ],
      // 定义每个员工对应的颜色
      colorMap: {
        "张三": "#6faed9", // 浅蓝色
        "李四": "#f7c442", // 黄色
        "王五": "#8bc34a"  // 绿色
      }
    };
  },
  mounted() {
    this.initChart();
  },
  methods: {
    initChart() {
      this.chartInstance = echarts.init(this.$refs.chart);

      // 1. 数据处理：将层级数据扁平化为图表可用的格式
      // 格式：[startDay, endDay, employeeName]
      const chartData = [];
      this.retData.forEach(item => {
        item.travelPeriods.forEach(period => {
          chartData.push({
            name: item.employeeName,
            value: [period.startDay, period.endDay, item.employeeName]
          });
        });
      });

      // 提取员工姓名作为 Y 轴类目
      const employeeNames = this.retData.map(item => item.employeeName);

      const option = {
        tooltip: {
          formatter: function (params) {
            const data = params.value;
            return `${data[2]} : ${data[0]}日 - ${data[1]}日`;
          }
        },
        grid: {
          left: '10%',
          right: '5%',
          top: '10%',
          bottom: '10%'
        },
        xAxis: {
          type: 'value',
          name: '日期',
          min: 1,
          max: 30,
          axisLabel: {
            formatter: '{value}日'
          },
          splitLine: { show: false }
        },
        yAxis: {
          type: 'category',
          data: employeeNames,
          axisLine: { show: false },
          axisTick: { show: false }
        },
        series: [
          {
            name: '出差行程',
            type: 'custom',
            renderItem: (params, api) => {
              // 获取当前数据项的 Y 轴坐标（对应员工姓名）
              const yValue = api.value(2);
              // 获取 StartDay 的 X 轴坐标
              const startCoord = api.coord([api.value(0), yValue]);
              // 获取 EndDay 的 X 轴坐标
              const endCoord = api.coord([api.value(1), yValue]);

              // 计算柱状图的高度
              // api.size([width, height]) 返回像素尺寸，这里我们利用它计算单行的高度
              const height = api.size([0, 1])[1] * 0.3; // 0.3 是柱子高度占行高的比例

              return {
                type: 'rect',
                shape: {
                  x: startCoord[0],
                  y: startCoord[1] - height / 2,
                  width: endCoord[0] - startCoord[0],
                  height: height
                },
                style: api.style({
                  // 根据员工姓名从 colorMap 中获取颜色
                  fill: this.colorMap[yValue] || '#589eff',
                  stroke: '#555' // 边框颜色
                })
              };
            },
            encode: {
              x: [0, 1], // 使用 value 的第0项和第1项作为 x 范围
              y: 2,      // 使用 value 的第2项（姓名）作为 y 轴
              tooltip: [0, 1, 2]
            },
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