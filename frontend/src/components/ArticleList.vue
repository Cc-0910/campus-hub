<template>
  <div class="article-list">
    <div v-if="!articles || articles.length === 0" class="empty-state">
      <el-empty description="暂无文章" />
    </div>
    
    <div v-else class="article-items">
      <div 
        v-for="article in articles" 
        :key="article.id"
        class="article-item"
        @click="goToArticle(article)"
      >
        <div class="article-header">
          <h3 class="article-title">{{ article.title }}</h3>
          <span class="article-time">{{ formatTime(article.createdAt || article.createTime) }}</span>
        </div>
        
        <p class="article-summary" v-if="article.summary">{{ article.summary }}</p>
        
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
        
        <div class="article-tags" v-if="article.tags && article.tags.length > 0">
          <el-tag 
            v-for="tag in article.tags" 
            :key="tag"
            size="small"
            type="info"
            class="article-tag"
          >
            {{ tag }}
          </el-tag>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'
import { View, ChatDotRound, Star } from '@element-plus/icons-vue'

const props = defineProps({
  articles: {
    type: Array,
    default: () => []
  }
})

const router = useRouter()

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  
  // 小于1分钟
  if (diff < 60 * 1000) {
    return '刚刚'
  }
  // 小于1小时
  if (diff < 60 * 60 * 1000) {
    return `${Math.floor(diff / (60 * 1000))}分钟前`
  }
  // 小于1天
  if (diff < 24 * 60 * 60 * 1000) {
    return `${Math.floor(diff / (60 * 60 * 1000))}小时前`
  }
  // 小于30天
  if (diff < 30 * 24 * 60 * 60 * 1000) {
    return `${Math.floor(diff / (24 * 60 * 60 * 1000))}天前`
  }
  
  return date.toLocaleDateString('zh-CN')
}

// 跳转到文章详情
const goToArticle = (article) => {
  if (article.id) {
    router.push(`/app/articles/${article.id}`)
  }
}
</script>

<style scoped>
.article-list {
  min-height: 200px;
}

.empty-state {
  padding: 40px 0;
  text-align: center;
}

.article-items {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.article-item {
  background: #fafbfc;
  border: 1px solid #f0f0f0;
  border-radius: 12px;
  padding: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
}

.article-item:hover {
  background: #f5f7fa;
  border-color: var(--campus-primary);
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
}

.article-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 8px;
  gap: 12px;
}

.article-title {
  font-size: 16px;
  font-weight: 600;
  color: var(--campus-text-primary);
  margin: 0;
  line-height: 1.4;
  flex: 1;
}

.article-time {
  font-size: 12px;
  color: var(--campus-text-secondary);
  white-space: nowrap;
}

.article-summary {
  font-size: 14px;
  color: var(--campus-text-secondary);
  line-height: 1.6;
  margin: 8px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-stats {
  display: flex;
  gap: 16px;
  margin: 12px 0;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: var(--campus-text-secondary);
}

.stat-item .el-icon {
  font-size: 14px;
}

.article-tags {
  display: flex;
  flex-wrap: wrap;
  gap: 6px;
  margin-top: 8px;
}

.article-tag {
  font-size: 11px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .article-header {
    flex-direction: column;
    align-items: flex-start;
  }
  
  .article-stats {
    flex-wrap: wrap;
  }
}
</style>