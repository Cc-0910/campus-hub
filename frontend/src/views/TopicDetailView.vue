<template>
  <div class="topic-detail-container">
    <div class="placeholder-page">
      <h1>话题详情页面</h1>
      <p>这是话题详情页面的占位内容，话题ID: {{ topicId }}</p>
      <el-button @click="goBack">返回话题列表</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const topicId = ref<string>('')

onMounted(() => {
  topicId.value = route.params.id as string
})

const goBack = () => {
  router.push('/app/topics')
}
</script>

<style scoped>
.topic-detail-container {
  padding: 24px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.placeholder-page {
  text-align: center;
  padding: 60px 0;
}

.placeholder-page h1 {
  font-size: 24px;
  color: #1f2937;
  margin-bottom: 16px;
}

.placeholder-page p {
  font-size: 16px;
  color: #6b7280;
  margin-bottom: 24px;
}
</style>