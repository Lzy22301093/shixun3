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
<!--          <hr style="height:1px; border-width:0; background-color:#dadde4; margin:20px auto">-->

          <el-row>
            <el-col :span="12">
              <table>
                <tbody>
                <tr class="info-row">
                  <th class="info-left">姓名</th>
                  <td>{{ personalInfo.name }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">性别</th>
                  <td>{{ personalInfo.gender }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">学号</th>
                  <td>{{ personalInfo.studentId }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">学院</th>
                  <td>{{ personalInfo.academy }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">班级</th>
                  <td>{{ personalInfo.class }}</td>
                </tr>
                <tr class="info-row">
                  <th class="info-left">邮件</th>
                  <td>{{ personalInfo.email }}</td>
                </tr>
                </tbody>
              </table>
            </el-col>
          </el-row>
          <el-row :span="12"></el-row>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { reactive } from 'vue'

const personalInfo = reactive({
  name: '张三',
  gender: '男',
  studentId: '22300000',
  academy: '软件学院',
  class: '软件2000',
  email: 'zhangsan@example.com'
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
  background-image: url('../../assets/bg3.jpg');
  background-size: cover;
  background-position: center;
  //display: flex; /* 使用 flex 布局 */
  //justify-content: center; /* 水平居中内容 */
  //align-items: center; /* 垂直居中内容 */
}

.info-card {
  border: 1px solid #de98d1;
  width: 600px;
  //margin: 10px auto;
  min-height: 700px;
  background-color: rgb(228, 147, 187, 0.5);

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
    background-color: rgba(222, 152, 209, 0);
    .info-item {
      background-color: rgba(255, 255, 255, 0); // 半透明白色背景
    }
  }
  .info-row{
    height: 40px;
  }
  .info-left{
    width: 100px;
    //border: 1px solid #de98d1;
  }
}
</style>
