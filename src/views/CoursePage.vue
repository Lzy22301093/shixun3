<template>
  <el-container>
    <el-header id="head">
      <el-row class="header-up">
        <el-col class="course-title" :span="12">
          Course Page
        </el-col>
        <el-col class="academy" :span="12">
          所在学院12312
        </el-col>
      </el-row>
      <el-row>
        <el-col class="relevant-info" :span="8">
          <div class="teacher">主讲教师：</div>
          <div class="separator"></div>
          <div class="cno">课程编号：</div>
          <div class="separator"></div>
          <div class="cid">课程ID: {{ courseId }}</div>
        </el-col>
        <el-col class="time" :span="8" :offset="8">
          学期： 当前教学周：
        </el-col>
      </el-row>
    </el-header>
    <el-container class="layout-container">
      <el-aside width="250px">
        <el-menu
            active-text-color="#00050f"
            background-color="#DE98D1"
            :default-active="$route.path"
            text-color="#fff"
            router
        >
          <!-- 课程信息 -->
          <el-sub-menu  :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/courseInfo`">
            <template #title>
              <el-icon><InfoFilled /></el-icon>
              <span>课程信息</span>
            </template>
            <el-menu-item :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/courseIntro`">
              <span>课程介绍</span>
            </el-menu-item>
            <el-menu-item :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/courseOutline`">
              <span>课程大纲</span>
            </el-menu-item>
            <el-menu-item :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/teachingCalendar`">
              <span>教学日历</span>
            </el-menu-item>
            <el-menu-item :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/courseNotice`">
              <span>课程通知</span>
            </el-menu-item>
            <el-menu-item :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/teacherInfo`">
              <span>教师信息</span>
            </el-menu-item>
          </el-sub-menu>

          <!-- 课程资源 -->
          <el-sub-menu  :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/courseRecourse`">
            <template #title>
              <el-icon><FolderOpened /></el-icon>
              <span>课程资源</span>
            </template>
            <el-menu-item :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/courseware`">
              <span>课程课件</span>
            </el-menu-item>
          </el-sub-menu>

          <!-- 课程视频 -->
          <el-sub-menu  :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/courseVideo`">
            <template #title>
              <el-icon><VideoCamera /></el-icon>
              <span>课程视频</span>
            </template>
            <el-menu-item :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/videoReplay`">
              <span>视频回放</span>
            </el-menu-item>
          </el-sub-menu>

          <!-- 课程考核 -->
          <el-sub-menu  :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/courseExam`">
            <template #title>
              <el-icon><Tickets /></el-icon>
              <span>课程考核</span>
            </template>
            <el-menu-item :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/homeWork`">
              <span>作业</span>
            </el-menu-item>
            <el-menu-item :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/courseReport`">
              <span>课程报告</span>
            </el-menu-item>
            <el-menu-item :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/experiment`">
              <span>实验</span>
            </el-menu-item>
            <el-menu-item :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/regularTest`">
              <span>平时测验</span>
            </el-menu-item>
            <el-menu-item :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/finalAssessment`">
              <span>结课考核</span>
            </el-menu-item>
          </el-sub-menu>

          <!-- 答疑讨论 -->
          <el-sub-menu :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/discussArea`">
            <template #title>
              <el-icon><ChatDotSquare /></el-icon>
              <span>答疑讨论</span>
            </template>
            <el-menu-item :index="`/course/courseId=${courseId}&courseNumber=${courseNumber}/discussArea`">
              <span>讨论区</span>
            </el-menu-item>
          </el-sub-menu>

        </el-menu>
      </el-aside>

      <el-main class="main">
        <router-view/>
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { ref } from 'vue';
import { useRoute } from 'vue-router';
import {ChatDotSquare, FolderOpened, InfoFilled, Tickets, VideoCamera} from "@element-plus/icons-vue";

const route = useRoute();
const courseId = ref(route.params.id);
const courseNumber = ref(route.params.courseNumber);
</script>

<style lang="scss" scoped>
#head {
  background-image: url("../assets/bg1.jpg");
  background-size: cover;
  background-position: center;
  height: 80px;
  background-color: rgb(205, 194, 255);
  color: white;
  padding: 0 150px;
  .course-title{
    font-size: 30px;
    font-weight: bold;
  }
  .academy{
    display: flex;
    justify-content: flex-end;
    align-items: center;
  }
  .relevant-info{
    //border: 1px solid black;
    background-color: rgba(122,90,111,0.4);
    margin-top: 10px;
    border-top-left-radius: 3px;
    border-top-right-radius: 3px;
    padding:0 20px;
    display: flex;
    align-items: center;
    justify-content: space-between;
    .separator {
      width: 2px;
      height: 70%; /* 根据需要调整高度 */
      align-content: center;
      background-color: #fdf2f7; /* 分隔线颜色 */
      border-radius: 10px;
    }
  }
  .time{
    text-align: right;
  }
}

.layout-container{
  height: calc(100vh - 80px);
  padding: 20px 0;
  background-image: url("../assets/bg3.jpg");
  background-size: cover;
  background-position: center;
  .el-aside{
    background-color: rgba(222, 152, 209, 0.7);
    border-radius: 5px;
    .el-menu-item{
      background-color: rgba(239, 177, 228, 0.5);
    }
  }

  .main {
    margin: 0 20px;
    padding: 0;
    border-radius: 5px;
    background-color: rgba(229, 200, 221, 0.6);
    //border: 1px solid darkslategrey;
  }
}
</style>
