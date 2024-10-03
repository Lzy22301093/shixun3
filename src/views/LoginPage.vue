<!--<script setup>-->
<!--import { User, Lock, UserFilled } from '@element-plus/icons-vue'-->
<!--import { ref, watch } from 'vue'-->
<!--const form = ref()-->
<!--const isRegister = ref(false)-->
<!--const formModel = ref({-->
<!--  id: '',-->
<!--  name: '',-->
<!--  password: ''-->
<!--})-->

<!--const rules = {-->
<!--  id: [-->
<!--    { required: true, message: '请输入用户id', trigger: 'blur' },-->
<!--    { min: 5, max: 10, message: 'id必须是 5-10位 的字符', trigger: 'blur' }-->
<!--  ],-->
<!--  name: [-->
<!--    { required: true, message: '请输入用户名', trigger: 'blur' },-->
<!--    {-->
<!--      pattern: /^\S{1,15}$/,-->
<!--      message: '用户名必须是 1-15位 的非空字符',-->
<!--      trigger: 'blur'-->
<!--    }-->
<!--  ],-->
<!--  password: [-->
<!--    { required: true, message: '请输入密码', trigger: 'blur' },-->
<!--    {-->
<!--      pattern: /^\S{6,15}$/,-->
<!--      message: '密码必须是 6-15位 的非空字符',-->
<!--      trigger: 'blur'-->
<!--    },-->
<!--  ]-->
<!--}-->
<!--</script>-->

<!--<template>-->
<!--  <el-row class="login-page">-->
<!--    <el-col :span="12" class="bg"></el-col>-->
<!--    <el-col :span="6" :offset="3" class="form">-->
<!--      &lt;!&ndash; 登录相关表单 &ndash;&gt;-->
<!--      <el-form :model="formModel"-->
<!--               :rules="rules"-->
<!--               size="large"-->
<!--               ref="form"-->
<!--               autocomplete="off">-->
<!--        <el-form-item>-->
<!--          <h1>登录</h1>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="id">-->
<!--          <el-input-->
<!--              v-model="formModel.id"-->
<!--              :prefix-icon="User"-->
<!--              placeholder="请输入用户id"-->
<!--          ></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item prop="password">-->
<!--          <el-input-->
<!--              v-model="formModel.password"-->
<!--              :prefix-icon="Lock"-->
<!--              type="password"-->
<!--              placeholder="请输入密码"-->
<!--          ></el-input>-->
<!--        </el-form-item>-->
<!--        <el-form-item class="flex">-->
<!--          <div class="flex">-->
<!--            <el-checkbox>记住我</el-checkbox>-->
<!--            <el-link type="primary" :underline="false">忘记密码？</el-link>-->
<!--          </div>-->
<!--        </el-form-item>-->
<!--        <el-form-item>-->
<!--          <el-button-->
<!--              @click="login"-->
<!--              class="button"-->
<!--              type="primary"-->
<!--              auto-insert-space-->
<!--          >登录</el-button>-->
<!--        </el-form-item>-->
<!--        <el-form-item class="flex">-->
<!--          <el-link type="info" :underline="false" @click="isRegister = true">-->
<!--            注册 →-->
<!--          </el-link>-->
<!--        </el-form-item>-->
<!--      </el-form>-->
<!--    </el-col>-->
<!--  </el-row>-->
<!--</template>-->

<!--<style lang="scss" scoped>-->
<!--.login-page {-->
<!--  margin: 0;-->
<!--  padding: 0;-->
<!--  border: 0;-->
<!--  height: 100vh;-->
<!--  background-color: #fff;-->
<!--  .bg {-->
<!--    background: url('../assets/kedaya.jpg') no-repeat center / cover;-->
<!--    border-radius: 0 20px 20px 0;-->
<!--  }-->
<!--  .form {-->
<!--    display: flex;-->
<!--    flex-direction: column;-->
<!--    justify-content: center;-->
<!--    user-select: none;-->
<!--    //border: 2px solid black;-->
<!--    .title {-->
<!--      margin: 0 auto;-->
<!--    }-->
<!--    .button {-->
<!--      width: 100%;-->
<!--    }-->
<!--    .flex {-->
<!--      width: 100%;-->
<!--      display: flex;-->
<!--      justify-content: space-between;-->
<!--    }-->
<!--  }-->
<!--}-->
<!--</style>-->

<template>
  <div class="login-page">
    <div class="login-form-container">
      <el-card class="login-form" shadow="hover" style="border: 2px solid #805e73; border-radius: 30px">
        <h2 class="login-title">Login</h2>
        <el-form :model="loginFormModel"
                 :rules="rules"
                 ref="form"
                 class="form"
                 autocomplete="off">
          <el-form-item prop="userid">
            <el-input
                v-model="loginFormModel.userid"
                placeholder="userid"
                :prefix-icon="User"
            ></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input
                v-model="loginFormModel.password"
                type="password"
                placeholder="Password"
                :prefix-icon="Lock"
                show-password
            ></el-input>
          </el-form-item>
          <el-form-item>
            <el-checkbox v-model="loginFormModel.rememberMe">Remember me</el-checkbox>
            <a href="#" class="forgot-password" style="margin-left: 40px">Forgot Password?</a>
          </el-form-item>
          <el-form-item>
            <el-button class="login-button" type="primary" @click="handleLogin">
              Login
            </el-button>
          </el-form-item>
          <div class="register-link">
            Don't have an account? <a href="#">Register</a>
          </div>
        </el-form>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue';
import { Lock, User } from '@element-plus/icons-vue';
import { ElMessage } from "element-plus";
// import router from "../router/index.js"; // 用这个导入router不能初始化loginPage组件，从而无法加载，原因：循环依赖问题（见笔记）
import { useRouter } from "vue-router";
import {useUserStore} from "@/stores/user.js";

const form = ref()
const loginFormModel = ref({
  userid: '',
  password: '',
  rememberMe: false,
});

// 验证规则
const rules = {
  userid: [
    { required: true, message: '请输入用户id', trigger: 'blur' },
    { min: 5, max: 10, message: 'id必须是 5-10位 的字符', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    {
      pattern: /^\S{6,15}$/,
      message: '密码必须是 6-15位 的非空字符',
      trigger: 'blur'
    },
  ]
};

// 登录处理函数
const userStore = useUserStore() // 用户仓库
const router = useRouter()
const handleLogin = async () => {
  await form.value.validate()// 点击登录后等待再一次校验完成
  // const res = await userLoginService(formModel.value) // 发送登录表单内容
  // userStore.setToken(res.data.token) // 接收后端的token来设置当前用户的token
  userStore.setToken(loginFormModel.value.userid) // 暂时代替
  ElMessage.success('登录成功')
  await router.push('/')
};
</script>

<style lang="scss" scoped>
.login-page {
  height: 100vh;
  background-color: #c48db0;
  background-image: url("../assets/bg1.jpg");
  background-size: cover;
  background-position: center;
  display: flex;
  justify-content: center;
  align-items: center;
}

.login-form-container {
  width: 350px;
  max-width: 90%;
}

.login-form {
  padding: 20px;
  background-color: rgba(241, 167, 214, 0.6);
  border-radius: 15px;
}

.login-title {
  text-align: center;
  font-size: 30px;
  margin-bottom: 30px;
  color: #9f7390;
}

.forgot-password {
  float: right;
  font-size: 12px;
  color: #5e1d41;
}

.el-input {

}

.el-input::placeholder {
  color: #bbb;
}

.login-button {
  width: 100%;
  background: linear-gradient(45deg, #e5a0dd, #d35e94);
  color: #ffffff;
  transition: background 0.3s;
  border-radius: 12px;
}

.login-button:hover {
  background: linear-gradient(45deg, #38a1db, #42b883);
}

.register-link {
  text-align: center;
  margin-top: 10px;
  color: #fff;
}

.register-link a {
  color: #42b883;
  margin-left: 20px;
  text-decoration: none;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>


