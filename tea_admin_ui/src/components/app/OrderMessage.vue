<template>
  <div>
    <!--达到要求弹出div-->
    <el-card class="box-card" v-if="messageList && messageList.length">
      <div style="font-size: 25px">
        <span>您有新的订单来了,请注意查看</span>
      </div>
      <div v-for="message in messageList" :key="message.orderNo" style="font-size: 15px;">
        订单号[{{message.orderNo}}] [{{message.address}}]
        <el-button style="" size="default" type="text" @click="confirmReceiveMessage(message.orderNo)">收到</el-button>
        <br>
      </div>
    </el-card>
  </div>
</template>


<style>

  .box-card {
    position: fixed;
    top: 70vh;
    right: 1vw;
    width: 600px;
    height: 29vh;
    z-index: 100;
    background: #ff9966 !important;
  }
</style>
