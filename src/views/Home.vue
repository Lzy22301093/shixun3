<template>
  <div class="whole">
    <el-container>
      <el-header id="head" height="80px">
        <h1 id="title" @click="goToPlatform" style="cursor: pointer;">唐朝例子课程平台</h1>
        <div class="header-right">
          <!-- 头像和姓名 -->
          <div class="user-info">
            <el-avatar src="https://cube.elemecdn.com/3/7c/3ea6beec64369c2642b92c6726f1epng.png" />
            <span class="username">张三</span>
          </div>
          <!-- 个人中心 -->
          <el-link @click="goToProfile" style="color: #865425">个人中心</el-link>
          <!-- 安全退出 -->
          <el-link @click="logout" style="color: #fc0303">安全退出</el-link>
        </div>
      </el-header>
      <el-main class="main">
        <router-view/>
      </el-main>
    </el-container>
  </div>
</template>

<script setup>
import { useRouter } from "vue-router";
import { useUserStore } from "@/stores/user.js";

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
  const userStore = useUserStore()
  userStore.removeToken()
  router.push('/login');
  console.log("已安全退出");
};
</script>

<style lang="scss" scoped>
.whole{}

#head {
  background-image: url("../assets/bg1.jpg");
  background-size: cover;
  background-position: center;
  background-color: rgb(107, 192, 236);
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
.main {
  height: calc(100vh - 80px);
  //border: 2px solid darkslategrey;
  padding: 0;// el-main中自带padding: 20
  //margin-left: 50px;
  //margin-right: 50px
  background-image: url("../assets/bg3.jpg");
  background-size: cover;
  background-position: center;
  margin-bottom: 0;
}
</style>
