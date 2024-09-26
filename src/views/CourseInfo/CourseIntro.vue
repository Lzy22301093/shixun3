<script setup>

import VerticalBar from "../../components/VerticalBar.vue";
</script>

<template>
  <div class="info-container">
      <div class="header">
        <vertical-bar text="课程简介"></vertical-bar>
      </div>
    <div class="content">
      23232321442342342342342333333333333333333333333333333333333333333333欢迎大家来到《软件项目管理与产品运维》的课程，希望这学期我们一起领略软件项目管理魅力，感受产品运维的魔力。愿我们一起进步，共同成长！ ：）
      <p>111</p>
      <p>222</p>
    </div>
  </div>
</template>

<style lang="scss" scoped>
.info-container{
  min-height: calc(100% - 10px);
  //background-color: rgba(100, 238, 176, 0.82);
  .header {
    padding-top: 10px;
    margin-left: 10px;
    //border: 1px solid black;
  }
  .content{
    padding: 20px;
    margin: 20px;
    border-radius: 5px;
    background-color: #9f7390;
    color: black;
    font-size: 14px;
    min-height: 20px;
  }
}

</style>