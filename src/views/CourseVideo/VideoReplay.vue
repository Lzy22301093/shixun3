<template>
  <div class="course-container">
    <!-- 左侧章节列表 -->
    <div class="chapter-list">
      <div v-for="(module, index) in modules" :key="index" class="module">
        <h3>{{ module.name }}</h3>
        <ul>
          <li v-for="(video, idx) in module.videos" :key="idx" @click="selectVideo(video)">
            {{ video.title }} ({{ video.duration }})
          </li>
        </ul>
      </div>
    </div>

    <!-- 右侧视频播放器 -->
    <div class="video-player">
      <div v-if="selectedVideo">
        <h3>Now Playing: {{ selectedVideo.title }}</h3>
        <video controls :src="selectedVideo.url" style="width: 100%;" />
      </div>
      <div v-else>
        <p>Please select a video to play.</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { userGetVideoService} from '../../api/user.js';
// 模块与视频数据
const modules = ref([
  {
    name: "Module 1: Introduction",
    videos: [
      { title: "Introduction to the Course", duration: "10:45", url: "../../../assets/1343082754-1-16.mp4" },
      { title: "Course Objectives", duration: "8:30", url: "path/to/video3.mp4" }
    ]
  },
  {
    name: "Module 2: Core Concepts",
    videos: [
      { title: "Concept 1", duration: "12:00", url: "path/to/video3.mp4" },
      { title: "Concept 2", duration: "15:20", url: "path/to/video4.mp4" }
    ]
  },
  {
    name: "Module 3: Advanced Topics",
    videos: [
      { title: "Advanced Topic 1", duration: "20:00", url: "path/to/video5.mp4" },
      { title: "Advanced Topic 2", duration: "18:40", url: "path/to/video6.mp4" }
    ]
  }
])

// 选中的视频
const selectedVideo = ref(null)

// 选择视频后显示在播放器中
const selectVideo = (video) => {
  selectedVideo.value = video
}
</script>

<style scoped>
.course-container {
  display: flex;
  width: 100%;
  height: 100vh;
}

.chapter-list {
  width: 30%;
  padding: 20px;
  background-color: #f5f5f5;
  border-right: 1px solid #ccc;
  overflow-y: auto;
}

.module {
  margin-bottom: 20px;
}

.module h3 {
  color: #5579bc;
}

ul {
  list-style: none;
  padding-left: 10px;
}

li {
  cursor: pointer;
  padding: 8px;
  color: #333;
}

li:hover {
  background-color: #ececec;
}

.video-player {
  width: 70%;
  padding: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

video {
  border: 1px solid #ccc;
  border-radius: 10px;
  width: 100%;  /* 将宽度设置为100%以适应容器 */
  max-height: 500px;  /* 设置最大高度 */
}

p {
  color: #666;
  font-size: 18px;
}
</style>