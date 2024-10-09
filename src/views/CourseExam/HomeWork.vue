<template>
  <div class="homework-container">
    <h2>作业</h2>
    <ul>
      <li v-for="(homework, index) in homeworkList" :key="index" class="homework-item">
        <h3>{{ homework.title }}</h3>
        <p>截止日期: {{ homework.dueDate }}</p>
        <p>状态: {{ homework.status }}</p>

        <!-- 替换提交作业按钮为上传组件 -->
        <el-upload
            v-if="homework.status === '未提交'"
            v-model:file-list="homework.fileList"
            class="upload-demo"
            action="https://run.mocky.io/v3/9d059bf9-4660-45f2-925d-ce80ad6c4d15"
        :on-preview="handlePreview"
        :on-remove="handleRemove"
        :before-remove="beforeRemove"
        :limit="3"
        :on-exceed="handleExceed"
        :on-success="(response, file) => handleUploadSuccess(response, file, homework)">

        <el-button type="primary">点击上传作业</el-button>
        <template #tip>
          <div class="el-upload__tip">
            支持jpg/png文件，大小不超过500KB。
          </div>
        </template>
        </el-upload>
        <p v-else>作业已提交</p>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { userGetHomeworkListService, userSubmitHomeworkService } from '../../api/user.js'; // 导入接口
import { ElMessage, ElMessageBox } from 'element-plus'; // 导入 Element Plus 组件

const homeworkList = ref([]);

// 获取作业列表
const fetchHomeworkList = async () => {
  homeworkList.value = await userGetHomeworkListService();
};

// 提交作业
const submitHomework = async (homework) => {
  const response = await userSubmitHomeworkService(homework);
  if (response.success) {
    homework.status = '已提交';
  }
};

// 上传成功处理
const handleUploadSuccess = (response, file, homework) => {
  // 处理文件上传成功后的逻辑
  if (response.success) {
    homework.status = '已提交';
    ElMessage.success('作业提交成功！');
  } else {
    ElMessage.error('作业提交失败，请重试。');
  }
};

// 其他上传组件的处理函数
const handleRemove = (file, uploadFiles) => {
  console.log('Removed file:', file);
};

const handlePreview = (uploadFile) => {
  console.log('Previewing file:', uploadFile);
};

const handleExceed = (files, uploadFiles) => {
  ElMessage.warning(
      `限制为3个文件，您选择了 ${files.length} 个文件。`
  );
};

const beforeRemove = (uploadFile, uploadFiles) => {
  return ElMessageBox.confirm(
      `确定取消上传 ${uploadFile.name} 吗？`
  ).then(
      () => true,
      () => false
  );
};

onMounted(fetchHomeworkList); // 页面加载时获取作业列表
</script>

<style scoped>
.homework-container {
  padding: 20px;
}

.homework-item {
  margin-bottom: 20px;
}
</style>
