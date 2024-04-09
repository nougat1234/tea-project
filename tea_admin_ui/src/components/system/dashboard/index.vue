<template>
    <div style="padding: 10px;">
        <el-row style="background:#fff; padding:16px 16px 0;margin-bottom:32px;">
            <div id="myChart"  :style="{width: '100%', height: '500px'}"></div>
        </el-row>
    </div>
</template>

<script>

import {getStatData} from "../../../api/modules/system/AppConfig";

import echarts from 'echarts'
export default {
  name: 'xwPassengerFlow',
  data() {
    return {
      xAxisData: [], // x轴数据，可根据需求
      yAxisData1: [], // 数据1
      yAxisData2: [], // 数据2
      yAxisData3: [], // 数据3
      yAxisData4: [], // 数据4
      yAxisData5: [], // 数据5
      // 初始化为当前日期
      today: new Date()
      
    }
  },
  mounted() {
    this.loadLine();
  },
  methods: {
     // 日期格式化函数
     formatDate(date) {
      let year = date.getFullYear();
      let month = (date.getMonth() + 1).toString().padStart(2, '0');
      let day = date.getDate().toString().padStart(2, '0');
      return `${year}-${month}-${day}`;
    },
    // 计算属性生成近7天的字符串数组
    recent7Days() {
      let days = [];
      for (let i = 0; i < 7; i++) {
        // 获取当前日期前后6天的日期，并格式化为YYYY-MM-DD字符串
        days.push(this.formatDate(new Date(this.today.getTime() - i * 24 * 60 * 60 * 1000)));
      }
      // 将数组倒序，得到从今天开始的最近7天
      return days.reverse();
    },
    loadLine() {
        
      getStatData().then(response => {
        let days = this.recent7Days();
        this.xAxisData = days;
        if (response.status < 200 || response.status >= 400) {
            Notification.error("未知异常");
            return Promise.reject();
        }
        let data = response.data;
        for (let i = 0; i < days.length; i++) {
            var flag = false;
            for (let j = 0; j < data.length; j++) {
                if(data[j].days == days[i]){
                    flag = true;
                    this.yAxisData1[i] = data[j].total;
                    this.yAxisData2[i] = data[j].doCount;
                    this.yAxisData3[i] = data[j].finishCount;
                    this.yAxisData4[i] = data[j].refundCount;
                    this.yAxisData5[i] = data[j].sendCount;
                }
            }
            if(!flag){
                this.yAxisData1[i] = 0;
                this.yAxisData2[i] = 0;
                this.yAxisData3[i] = 0;
                this.yAxisData4[i] = 0;
                this.yAxisData5[i] = 0;
            }
        }

        let option = {
        title: {
          text: '近7天订单报表'
        },
        tooltip: {
          trigger: 'axis'
        },
        legend: {
          data: ['订单总数', '制作中', '已送达', '已退款', '已完成']
        },
        grid: {
          left: '3%',
          right: '4%',
          bottom: '3%',
          containLabel: true
        },
        xAxis: {
          type: 'category',
          boundaryGap: false,
          data: this.xAxisData // x轴数据
        },
        yAxis: {
          type: 'value'
        },
        series: [
          {
            name: '订单总数',
            type: 'line',
            stack: '总量',
            data: this.yAxisData1 // y轴数据1
          },
          {
            name: '制作中',
            type: 'line',
            stack: '总量',
            data: this.yAxisData2 // y轴数据2
          },
          {
            name: '已送达',
            type: 'line',
            stack: '总量',
            data: this.yAxisData3 // y轴数据3
          },
          {
            name: '已退款',
            type: 'line',
            stack: '总量',
            data: this.yAxisData4 // y轴数据5
          },
          {
            name: '已完成',
            type: 'line',
            stack: '总量',
            data: this.yAxisData5 // y轴数据5
          }
        ]
      }
      this.myChartOne = echarts.init(document.getElementById('myChart'))
      this.myChartOne.setOption(option)
      });
     
    },
  }
}
</script>
