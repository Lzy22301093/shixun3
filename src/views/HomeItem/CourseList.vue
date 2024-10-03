<template>
  <el-card class="course-container">
    <div class="header">
      <VerticalBar text="课程列表" />
    </div>
    <!-- 选择学期 -->
    <el-row class="select-semester">
      <el-select v-model="selectedSemester" placeholder="请选择学期" @change="handleSemesterChange" style="width: 300px">
        <el-option
            v-for="(semester, index) in semesters"
            :key="index"
            :label="semester"
            :value="semester">
        </el-option>
      </el-select>
    </el-row>
    <!-- 课程列表 -->
    <el-row class="course-list" :gutter="10">
      <el-col v-for="(course, index) in courses" :key="index" :span="8">
        <el-card shadow="hover" class="course-card" @click="goToCourse(course)">
          <!-- 课程图片 -->
          <img :src="course.image" alt="课程图片" class="course-image" />
          <!-- 课程信息 -->
          <div class="course-info">
            <h3>{{ course.name }}</h3>
            <p>课程号：{{ course.courseNumber }}</p>
            <p>课序号：{{ course.sectionNumber }}</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </el-card>
</template>

<script setup>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
import VerticalBar from "../../components/VerticalBar.vue";

const selectedSemester = ref('');
const semesters = [
  '2024-2025第一学期',
  '2023-2024第二学期',
];

const courses = [
  {
    id: 1,
    name: '课程一',
    courseNumber: '101',
    sectionNumber: '01',
    image: 'https://via.placeholder.com/150x100'
  },
  {
    id: 2,
    name: '课程二',
    courseNumber: '102',
    sectionNumber: '02',
    image: 'https://via.placeholder.com/150x100'
  },
  {
    id: 3,
    name: '课程三',
    courseNumber: '103',
    sectionNumber: '03',
    image: 'https://via.placeholder.com/150x100'
  },
  {
    id: 4,
    name: '课程四',
    courseNumber: '104',
    sectionNumber: '04',
    image: 'https://via.placeholder.com/150x100'
  },
  {
    id: 5,
    name: '课程五',
    courseNumber: '105',
    sectionNumber: '05',
    image: 'https://via.placeholder.com/150x100'
  },
  {
    id: 6,
    name: '课程六',
    courseNumber: '106',
    sectionNumber: '06',
    image: 'https://via.placeholder.com/150x100'
  },
  {
    id: 7,
    name: '课程七',
    courseNumber: '107',
    sectionNumber: '07',
    image: 'https://via.placeholder.com/150x100'
  }
];
const router = useRouter();

// 点击跳转到课程详情页面
const goToCourse = (course) => {
  // 获取完整的路由路径
  const routePath = router.resolve({
    name: 'CoursePage',
    params: { id: course.id ,
              courseNumber: course.courseNumber}
  });
  // 在新标签页中打开该路径
  window.open(routePath.href, '_blank');
};
const handleSemesterChange = (value) => {
  console.log(`选择的学期是: ${value}`);
};
</script>

<style lang="scss" scoped>
.course-container {
  background-color: rgba(252, 223, 233, 0.8);

  .header {
    display: flex;
    align-items: center;
    margin-bottom: 10px;
  }

  .select-semester {
    margin-bottom: 10px;
  }

  .course-list{
    max-height: 600px; /* 限制课程列表的最大高度 */
    overflow-y: auto; /* 启用垂直滚动条 */
    padding-right: 10px; /* 预留空间避免滚动条覆盖内容 */

    .course-card {
      display: flex;
      flex-direction: column;
      background-color: #f5cbdd;
      //justify-content: space-between;
      height: 250px;
      margin-bottom: 10px;

      .course-image {
        width: 100%;
        height: 60%;
        object-fit: cover;
      }

      .course-info {
        height: 40%;
      }

      .course-info h3 {
        margin: 0;
        font-size: 18px;
        font-weight: bold;
      }

      .course-info p {
        margin: 5px 0;
      }
    }
  }
}

</style>
