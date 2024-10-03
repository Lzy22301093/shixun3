<script setup>
import { ref, onMounted } from 'vue'

// 模拟获取讨论区帖子列表的数据
const discussions = ref([])

// 模拟获取数据的方法
const fetchDiscussions = () => {
  // 假数据，可以替换为API请求
  discussions.value = [
    {
      id: 1,
      author: '学生A',
      content: '关于第3章的内容，有几个不太明白的地方。',
      replies: [
        { id: 101, author: '学生B', content: '可以看看课程视频的5分30秒的部分，讲得很清楚。' },
        { id: 102, author: '老师', content: '如果还是有疑问，可以私下发邮件给我。' }
      ]
    },
    {
      id: 2,
      author: '学生C',
      content: '这周的作业有什么好的解决思路？',
      replies: []
    }
  ]
}

// 新发帖和回复内容的输入
const newPost = ref('')
const newReply = ref('')

// 处理新发帖
const postDiscussion = () => {
  if (newPost.value.trim()) {
    discussions.value.push({
      id: Date.now(),
      author: '我',
      content: newPost.value,
      replies: []
    })
    newPost.value = ''
  }
}

// 处理新回复
const postReply = (discussionId) => {
  const discussion = discussions.value.find(d => d.id === discussionId)
  if (discussion && newReply.value.trim()) {
    discussion.replies.push({
      id: Date.now(),
      author: '我',
      content: newReply.value
    })
    newReply.value = ''
  }
}

onMounted(() => {
  fetchDiscussions()
})
</script>

<template>
  <div class="discussion-container">
    <h1>课程讨论区</h1>

    <!-- 发布新帖子 -->
    <div class="new-post">
      <h2>发起新讨论</h2>
      <textarea v-model="newPost" placeholder="输入你的问题或讨论..."></textarea>
      <button @click="postDiscussion">发布</button>
    </div>

    <!-- 讨论列表 -->
    <div v-if="discussions.length > 0" class="discussion-list">
      <h2>讨论列表</h2>
      <div v-for="discussion in discussions" :key="discussion.id" class="discussion-item">
        <p><strong>{{ discussion.author }}:</strong> {{ discussion.content }}</p>

        <!-- 回复列表 -->
        <div class="replies">
          <p v-if="discussion.replies.length === 0">暂无回复</p>
          <div v-for="reply in discussion.replies" :key="reply.id" class="reply-item">
            <p><strong>{{ reply.author }}:</strong> {{ reply.content }}</p>
          </div>
        </div>

        <!-- 回复输入 -->
        <div class="new-reply">
          <textarea v-model="newReply" placeholder="输入你的回复..."></textarea>
          <button @click="postReply(discussion.id)">回复</button>
        </div>
      </div>
    </div>
    <div v-else>
      <p>暂无讨论，成为第一个发帖的人吧！</p>
    </div>
  </div>
</template>

<style scoped>
.discussion-container {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

h1, h2 {
  margin-bottom: 10px;
}

.new-post, .discussion-list {
  margin-bottom: 30px;
}

textarea {
  width: 100%;
  height: 80px;
  margin-bottom: 10px;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  resize: none;
}

button {
  padding: 10px 15px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #36a772;
}

.discussion-item {
  border-bottom: 1px solid #ddd;
  padding: 10px 0;
}

.reply-item {
  margin-left: 20px;
  padding: 5px 0;
}

.new-reply textarea {
  height: 60px;
}
</style>
