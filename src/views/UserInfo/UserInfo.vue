<template>
  <el-card class="info-card">
    <el-row>
      <el-avatar size="medium" src="https://via.placeholder.com/50" />
    </el-row>
    <el-row justify="space-between" align="middle">
      <el-col span="12">
        <span class="info-title">个人信息</span>
      </el-col>
      <el-col span="12">
        <el-button type="primary" @click="editInfo">编辑</el-button>
      </el-col>
    </el-row>

    <el-divider></el-divider>

    <el-row>
      <el-col :span="12">
        <el-descriptions title="基本信息" :column="1">
          <el-descriptions-item label="姓名">{{ personalInfo.name }}</el-descriptions-item>
          <el-descriptions-item label="年龄">{{ personalInfo.age }}</el-descriptions-item>
          <el-descriptions-item label="邮箱">{{ personalInfo.email }}</el-descriptions-item>
        </el-descriptions>
      </el-col>
      <el-col :span="12">
        <el-descriptions title="其他信息" :column="1">
          <el-descriptions-item label="职业">{{ personalInfo.job }}</el-descriptions-item>
          <el-descriptions-item label="地址">{{ personalInfo.address }}</el-descriptions-item>
        </el-descriptions>
      </el-col>
    </el-row>
  </el-card>
</template>

<script setup>
import { reactive } from 'vue'

const personalInfo = reactive({
  name: '张三',
  age: 30,
  email: 'zhangsan@example.com',
  job: '软件工程师',
  address: '北京市海淀区'
})

const editInfo = () => {
  console.log('编辑个人信息')
}
</script>

<style lang="scss" scoped>
.info-card {
  width: 600px;
  margin: 20px auto;
}

.info-title {
  margin-left: 10px;
  font-weight: bold;
  font-size: 16px;
}
</style>
