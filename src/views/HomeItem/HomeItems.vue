<script setup>
import UserInfo from "./UserCard.vue";
import CourseList from "./CourseList.vue";
import RightPart from "./RightPart.vue";
</script>

<template>
  <el-main class="h-main">
    <el-row :gutter="20">
      <el-col :span="6">
        <div class="left-panel">
          <UserInfo></UserInfo>
        </div>
      </el-col>
      <el-col :span="12">
        <div class="center-panel">
          <CourseList></CourseList>
        </div>
      </el-col>
      <el-col :span="6">
        <div class="right-panel">
          <RightPart></RightPart>
        </div>
      </el-col>
    </el-row>
  </el-main>
</template>

<style lang="scss" scoped>
.h-main {
  //border: 2px solid darkslategrey;
  //margin-left: 50px;
  //margin-right: 50px;
  margin: 20px 70px 20px 70px;
}
</style>