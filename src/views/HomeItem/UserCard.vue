<script setup>
import { Edit } from '@element-plus/icons-vue';
import VerticalBar from "../../components/VerticalBar.vue";
</script>

<template>
  <el-card class="userCard" shadow="hover">
    <div class="userCard-header">
      <div class="left">
        <VerticalBar text="个人信息" />
      </div>
      <div class="right">
        <el-icon><Edit /></el-icon>
        <span class="label" @click="handleEdit" style="color: #ec2ba7">编辑</span>  <!-- 编辑文字 -->
      </div>
    </div>
    <div class="userCard-mid">
      <img src="../../assets/kedaya.jpg" alt="无法加载" style="width: 100px; height: 100px; border-radius: 50%">
<!--      <el-avatar :size="100" src="../../assets/kedaya.jpg"></el-avatar>-->
      <div class="name"
           style="margin-top: 10px; font-size: 20px">
        张三
      </div>
<!--      <div class="identity"
           style="margin-top: 10px; background-color: bisque; font-size: 20px">
        学生
      </div>-->
      <el-tag type="warning" size="large" style="font-size: 20px; margin-top: 5px">学生</el-tag>

    </div>
    <div class="userCard-down">
      <el-col  :offset="6" :span="8">
        <el-row>学院:</el-row>
        <el-row>邮箱:</el-row>
        <el-row>电话:</el-row>
      </el-col>
    </div>
  </el-card>
  <el-card class="announceRemind" shadow="hover">
    <div class="card-header">
      <VerticalBar text="通知提醒" />
    </div>
    <div class="announceContent">
      0条通知未读
    </div>
  </el-card>
</template>

<style lang="scss" scoped>
.userCard{
  background-color: #fcdfe9;

  .userCard-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 15px;
    //height: 40px;
    //padding: 15px;
    //border: 2px solid black;
    .left, .right {
      display: flex;
      align-items: center;
    }
  }
  .userCard-mid {
    display: flex;
    flex-direction: column;
    align-items: center;
    height: 180px;
    //border: 2px solid black;
  }
  .userCard-down {
    margin-top: 20px;
    //display: flex;
    //flex-direction: column;
    //align-items: center;
    height: 150px;
    //border: 2px solid black;
    font-size: 18px;
    color: #989898;
  }
}

.announceRemind {
  background-color: #fcdfe9;
  margin-top: 20px;
  .announceContent{}
}
</style>