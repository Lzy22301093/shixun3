<script setup>

import VerticalBar from "../../components/VerticalBar.vue";
</script>

<template>
  <div class="t-info-container">
    <div class="header">
      <vertical-bar text="教师信息"></vertical-bar>
    </div>
    <div class="content">
    </div>
  </div>
</template>

<style lang="scss" scoped>
.t-info-container{
  //margin-top: 10px;
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