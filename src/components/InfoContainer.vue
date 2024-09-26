<template>
  <div class="info-container">
    <div class="header">
      <div class="vertical-bar"></div>
      <span class="text">{{ text }}</span>
    </div>
    <div class="content">
      {{ content }}
    </div>
  </div>
</template>

<script setup>

// 接收父组件传递的文字内容
defineProps({
  text: {
    type: String,
    required: true,
    default: '默认文字'
  },
  content: {
    type: String,
    required: true,
    default: '默认内容'
  }
});

</script>

<style scoped>
.info-container{
  margin-top: 10px;
  min-height: 100%;
  .header {
    display: flex;
    align-items: center;

    .vertical-bar {
      margin-left: 10px;
      width: 5px;
      height: 20px;
      background-color: #5579bc;
      display: inline-block;
      border-radius: 10px;
      margin-right: 10px;
    }
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
