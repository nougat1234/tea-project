<template>
  <div>
    <div style="text-align: left; margin: 5px 10px">

    </div>

    <!--列表-->
    <div style="margin: 0px 10px;text-align: center;">
      <el-table
        :data="orderInfoAdmins"
        border
        style="width: 100%"
        :cell-style="tableRowStyle"
        :header-cell-style="tableHeaderColor"
        stripe
        :default-sort="{prop: 'createTime', order: 'descending'}">
        <el-table-column   prop="orderNo" label="订单号" width="200"></el-table-column>
        <el-table-column prop="orderStatus" label="订单状态" width="150"></el-table-column>
        <el-table-column prop="takeType" label="取餐方式" width="150"></el-table-column>
        <el-table-column prop="addressDetail" label="收货地址" show-overflow-tooltip width="150"></el-table-column>
        <el-table-column prop="goodsPreview" label="商品信息" width="150"></el-table-column>
        <el-table-column prop="goodsTotalNum" label="商品总数" width="150"></el-table-column>
        <el-table-column prop="totalPrice" label="总价格" width="150">
          <template slot-scope="scope" width="150">{{'￥' + scope.row.totalPrice / 100}}</template>
        </el-table-column>
        <el-table-column prop="verifyNum" label="取单号" width="150"></el-table-column>
        <el-table-column prop="createTime" label="下单时间" width="200"></el-table-column>
        <el-table-column prop="finishTime" label="完成时间" show-overflow-tooltip width="150"></el-table-column>
        <el-table-column prop="userPhone" label="用户联系电话" show-overflow-tooltip width="150"></el-table-column>
        <el-table-column prop="receiver" label="取餐人" show-overflow-tooltip width="150"></el-table-column>
        <el-table-column prop="extraInfo" label="订单备注" show-overflow-tooltip width="150"></el-table-column>
      </el-table>

      <!--分页组件-->
      <el-row style="float: right">
        <el-pagination
          @current-change="changePageNo"
          @size-change="changePageSize"
          :current-page.sync="searchParams.pageNo"
          :page-sizes="[5, 10, 20, 30, 50, 100]"
          :page-size.sync="searchParams.pageSize"
          layout="total, sizes, prev, pager, next, jumper"
          :total="total">
        </el-pagination>
      </el-row>

    </div>
  </div>
</template>

<script>
  import {
    getOrderInfoAdmins
  } from "@/api/modules/app/orderInfoAdminApi.js";

  export default {
    name: "orderInfoAdminComponent",
    data() {
      return {
        // 查询参数
        searchParams: {
          searchParam1: null,
          pageNo: 1,
          pageSize: 10
        },
        total: 0,
        orderInfoAdmins: []
      }
    },
    mounted() {
      this.getOrderInfoAdmins()
    },
    methods: {
      // 获取数据
      getOrderInfoAdmins() {
        let that = this;
        getOrderInfoAdmins(that.searchParams.pageNo, that.searchParams.pageSize).then(result => {
          that.orderInfoAdmins = result.data.records;
          that.total = result.data.total;
        })
      },
      // 切换页数
      changePageNo(pageNo) {
        this.searchParams.pageNo = pageNo;
        this.getOrderInfoAdmins()
      },
      // 改变页面大小
      changePageSize(pageSize) {
        this.searchParams.pageSize = pageSize;
        this.getOrderInfoAdmins()
      },
      // 清除查询参数
      clearSearchParams() {
        this.searchParams.searchParam1 = null;
      },
        //设置表格行的样式
        tableRowStyle({row,rowIndex}){
        return 'text-align:center'
      },
      //设置表头行的样式
      tableHeaderColor({row,column,rowIndex,columnIndex}){
        return 'text-align:center'
 
      }
    }
    
  }
</script>
