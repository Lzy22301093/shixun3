<template>
  <div class="vertical-bar-container">
    <div class="vertical-bar"></div>
    <span class="text">{{ text }}</span>
  </div>
</template>

<script setup>

// 接收父组件传递的文字内容
defineProps({
  text: {
    type: String,
    required: true,
    default: '默认文字'
  }
});
</script>

<style scoped>
.vertical-bar-container {
  display: flex;
  align-items: center;
}

.vertical-bar {
  width: 5px;
  height: 20px;
  background-color: #5579bc;
  display: inline-block;
  border-radius: 10px;
  margin-right: 10px;
}

.text {
  font-size: 16px;
  color: #805e73;
}
</style>
