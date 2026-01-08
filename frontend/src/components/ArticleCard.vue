<template>
  <el-card class="article-card" shadow="hover" @click="goToArticle">
    <template #header>
      <div class="article-header">
        <h3 class="article-title">{{ article.title }}</h3>
        <div class="article-meta">
          <span>{{ formatDate(article.createTime) }}</span>
        </div>
      </div>
    </template>
    
    <div class="article-content">
      <p class="article-excerpt">{{ article.summary || article.content.substring(0, 150) + '...' }}</p>
    </div>
    
    <template #footer>
      <div class="article-footer">
        <div class="article-stats">
          <span class="stat-item">
            <el-icon><View /></el-icon>
            {{ article.viewCount || 0 }}
          </span>
          <span class="stat-item">
            <el-icon><ChatDotRound /></el-icon>
            {{ article.commentCount || 0 }}
          </span>
          <span class="stat-item">
            <el-icon><Star /></el-icon>
            {{ article.likeCount || 0 }}
          </span>
        </div>
      </div>
    </template>
  </el-card>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { View, ChatDotRound, Star } from '@element-plus/icons-vue'

const props = defineProps({
  article: {
    type: Object,
    required: true
  }
})

const router = useRouter()

const formatDate = (timestamp) => {
  if (!timestamp) return ''
  const date = new Date(timestamp)
  return `${date.getFullYear()}-${date.getMonth() + 1}-${date.getDate()}`
}

const goToArticle = () => {
  if (props.article.type === 'question') {
    router.push(`/app/questions/${props.article.id}`)
  } else {
    router.push(`/app/articles/${props.article.id}`)
  }
}
</script>

<style scoped>
.article-card {
  margin-bottom: 20px;
  cursor: pointer;
  transition: transform 0.3s;
}

.article-card:hover {
  transform: translateY(-5px);
}

.article-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.article-title {
  margin: 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--campus-text-primary);
  line-height: 1.4;
}

.article-meta {
  font-size: 12px;
  color: var(--campus-text-secondary);
}

.article-content {
  min-height: 60px;
  margin: 10px 0;
}

.article-excerpt {
  margin: 0;
  color: var(--campus-text-secondary);
  line-height: 1.6;
}

.article-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.article-stats {
  display: flex;
  gap: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  font-size: 12px;
  color: var(--campus-text-secondary);
}

.stat-item .el-icon {
  margin-right: 4px;
}
</style>