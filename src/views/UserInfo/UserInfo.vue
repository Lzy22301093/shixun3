<template>
  <div class="bg">
    <el-row style="height: 50px"></el-row>
    <el-row type="flex" justify="center" align="middle">
      <el-col :span="8">
        <el-card class="info-card">
          <el-row class="avatar">
            <el-avatar size="large" src="https://via.placeholder.com/50" />
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
              <el-descriptions class="info" title="基本信息" :column="1">
                <el-descriptions-item class="info" label="姓名">{{ personalInfo.name }}</el-descriptions-item>
                <el-descriptions-item class="info" label="年龄">{{ personalInfo.age }}</el-descriptions-item>
                <el-descriptions-item class="info" label="邮箱">{{ personalInfo.email }}</el-descriptions-item>
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
      </el-col>
    </el-row>
  </div>
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
.bg{
  //border: 5px solid black;
  margin: 0;
  width: 100vw; /* 使背景图覆盖整个页面宽度 */
  height: calc(100vh - 80px); /* 使背景图覆盖整个页面高度 */
  background-image: url('../../assets/bg2.jpg');
  background-size: cover; /* 使背景图适应容器大小 */
  //display: flex; /* 使用 flex 布局 */
  //justify-content: center; /* 水平居中内容 */
  //align-items: center; /* 垂直居中内容 */
}

.info-card {
  border: 1px solid #c1ecec;
  width: 600px;
  //margin: 10px auto;
  min-height: 700px;
  background-color: rgba(55, 73, 129, 0.5);

  .avatar{
    display: flex;
    justify-content: center;
  }

  .info-title {
    margin-left: 10px;
    font-weight: bold;
    font-size: 16px;
  }
  .info{
    background-color: rgba(255, 255, 255, 0.1);
  }
}
</style>
