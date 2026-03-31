<template>
  <div class="gantt-container">
    <!-- 头部：姓名列 + 日期轴 -->
    <div class="gantt-header">
      <div class="name-col header-name">姓名</div>
      <div class="date-axis">
        <div v-for="day in 30" :key="day" class="date-item">{{ day }}日</div>
      </div>
    </div>
    <!-- 主体：员工行 -->
    <div class="gantt-body">
      <div v-for="(employee, empIndex) in retData" :key="empIndex" class="employee-row">
        <!-- 员工姓名列 -->
        <div class="name-col">{{ employee.employeeName }}</div>
        <!-- 出差条容器（和日期轴同宽） -->
        <div class="travel-container">
          <div
            v-for="(period, pIndex) in employee.travelPeriods"
            :key="pIndex"
            class="travel-bar"
            :style="{
              left: `${(period.startDay - 1) * (100 / 30)}%`,
              width: `${(period.endDay - period.startDay + 1) * (100 / 30)}%`,
              backgroundColor: employee.color
            }"
          ></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  name: "TravelGanttChart",
  data() {
    return {
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
  created() {
    // 为每个员工生成随机浅色系颜色
    this.retData.forEach(emp => {
      emp.color = this.getRandomLightColor();
    });
  },
  methods: {
    // 生成随机浅色系RGB颜色
    getRandomLightColor() {
      const r = Math.floor(150 + Math.random() * 105);
      const g = Math.floor(150 + Math.random() * 105);
      const b = Math.floor(150 + Math.random() * 105);
      return `rgb(${r}, ${g}, ${b})`;
    }
  }
};
</script>

<style scoped>
/* 整体容器 - 边框包裹 */
.gantt-container {
  width: 100%;
  max-width: 1200px;
  margin: 20px auto;
  border: 1px solid #000;
  box-sizing: border-box;
}

/* 头部：姓名列 + 日期轴 横向排列 */
.gantt-header {
  display: flex;
  border-bottom: 1px solid #000;
}
/* 姓名列（固定宽度） */
.name-col {
  width: 80px;
  text-align: center;
  line-height: 30px;
  font-size: 14px;
  border-right: 1px solid #000;
  box-sizing: border-box;
}
.header-name {
  font-weight: bold;
}

/* 日期轴 - 和出差容器同宽 */
.date-axis {
  display: flex;
  flex: 1;
}
.date-item {
  flex: 1;
  text-align: center;
  font-size: 12px;
  line-height: 30px;
  box-sizing: border-box;
}

/* 主体：员工行容器 */
.gantt-body {
  display: flex;
  flex-direction: column;
}
.employee-row {
  display: flex;
  height: 40px;
  border-bottom: 1px solid #eee;
}

/* 出差条容器（和日期轴完全对齐） */
.travel-container {
  position: relative;
  flex: 1;
  height: 100%;
  box-sizing: border-box;
}
/* 出差条样式 */
.travel-bar {
  position: absolute;
  top: 50%;
  transform: translateY(-50%); /* 垂直居中 */
  height: 80%; /* 留边距更美观 */
  border-radius: 2px;
  box-sizing: border-box;
}
</style>