<template>
  <el-container>
    <el-header id="head" height="80px">
      <h1 id="title" @click="goToPlatform" style="cursor: pointer;">课程平台11123</h1>
      <div class="header-right">
        <!-- 头像和姓名 -->
        <div class="user-info">
          <el-avatar src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
          <span class="username">张三</span>
        </div>
        <!-- 个人中心 -->
        <el-link @click="goToProfile" style="color: #865425">个人中心</el-link>
        <!-- 安全退出 -->
        <el-link type="danger" @click="logout">安全退出</el-link>
      </div>
    </el-header>
    <el-main class="main">
<!--      <el-row :gutter="20">-->
<!--        <el-col :span="6">-->
<!--          <div class="left-panel">-->
<!--            <UserInfo></UserInfo>-->
<!--          </div>-->
<!--        </el-col>-->
<!--        <el-col :span="12">-->
<!--          <div class="center-panel">-->
<!--            <CourseList></CourseList>-->
<!--          </div>-->
<!--        </el-col>-->
<!--        <el-col :span="6">-->
<!--          <div class="right-panel">-->
<!--            <RightPart></RightPart>-->
<!--          </div>-->
<!--        </el-col>-->
<!--      </el-row>-->
      <router-view/>
    </el-main>
  </el-container>
</template>

<script setup>
import { useRouter } from "vue-router";

const router = useRouter();

// 点击个人中心
const goToProfile = () => {
  router.push('/userInfo');
  console.log("进入个人中心");
};

const goToPlatform = () => {
  router.push('/home');
};

// 点击安全退出
const logout = () => {
  console.log("已安全退出");
};
</script>

<style lang="scss" scoped>
#head {
  background-color: rgb(205, 194, 255);;
  margin-left: 0;
  display: flex;
  justify-content: space-between;
  align-items: center;
  //border: 1px solid black;
  #title {
    color: white;
    font-size: 40px;
    //margin-top: 0;
    margin-left: 30px;
    //border: 1px solid black;
  }
  .header-right {
    display: flex;
    align-items: center;
    gap: 40px; /* 控制各部分间距 */
    margin-right: 30px;
    .user-info {
      display: flex;
      align-items: center;
      gap: 10px; /* 控制头像与姓名的间距 */
    }
    .username {
      font-size: 16px;
      color: white;
    }
  }
}
//.main {
//  //border: 2px solid darkslategrey;
//  margin-left: 50px;
//  margin-right: 50px
//}

.left-panel {
  background-color: #f4dbed;
  padding: 10px;
  border-radius: 8px;
}

.center-panel {
  background-color: #f4dbed;
  padding: 10px;
  border-radius: 8px;
}

.right-panel {
  background-color: #f4dbed;
  padding: 10px;
  border-radius: 8px;
}
</style>
