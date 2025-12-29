<template>
  <div class="question-detail-container">
    <div class="placeholder-page">
      <h1>问答详情页面</h1>
      <p>这是问答详情页面的占位内容，问题ID: {{ questionId }}</p>
      <el-button @click="goBack">返回话题详情</el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()
const questionId = ref<string>('')

onMounted(() => {
  questionId.value = route.params.id as string
})

const goBack = () => {
  // 返回上一页或话题详情页
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/app/topics')
  }
}
</script>

<style scoped>
.question-detail-container {
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