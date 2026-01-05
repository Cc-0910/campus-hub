<template>
  <div class="recommendations-section">
    <h3 class="section-title">相关推荐</h3>
    <div class="recommendations-list">
      <div 
        v-for="post in recommendations" 
        :key="post.id" 
        class="recommendation-item"
        @click="handleRecommendationClick(post)"
      >
        <div class="recommendation-content">
          <div class="recommendation-title">{{ post.title }}</div>
          <div class="recommendation-meta">
            <span class="author">{{ post.author?.nickname || '匿名用户' }}</span>
            <span class="time">{{ formatTime(post.createTime) }}</span>
            <span class="views">{{ post.viewCount || 0 }} 浏览</span>
          </div>
        </div>
      </div>
    </div>
    <div v-if="recommendations.length === 0" class="no-recommendations">
      暂无相关推荐
    </div>
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import { ElMessage } from 'element-plus'

const props = defineProps({
  recommendations: {
    type: Array,
    default: () => []
  },
  currentPostType: {
    type: String,
    default: 'question'
  }
})

const emit = defineEmits(['recommendation-click'])

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  const days = Math.floor(diff / (24 * 60 * 60 * 1000))
  
  if (days === 0) {
    const hours = Math.floor(diff / (60 * 60 * 1000))
    if (hours === 0) {
      const minutes = Math.floor(diff / (60 * 1000))
      return `${minutes}分钟前`
    }
    return `${hours}小时前`
  } else if (days < 7) {
    return `${days}天前`
  } else {
    return date.toLocaleDateString()
  }
}

// 处理推荐项点击
const handleRecommendationClick = (post) => {
  emit('recommendation-click', post)
}
</script>

<style scoped>
.recommendations-section {
  margin-top: 32px;
  padding: 20px;
  background: #f8f9fa;
  border-radius: 8px;
  border: 1px solid #e9ecef;
}

.section-title {
  margin: 0 0 16px 0;
  font-size: 18px;
  font-weight: 600;
  color: #2c3e50;
}

.recommendations-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.recommendation-item {
  padding: 12px;
  background: white;
  border-radius: 6px;
  border: 1px solid #e9ecef;
  cursor: pointer;
  transition: all 0.2s ease;
}

.recommendation-item:hover {
  border-color: #409eff;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.1);
  transform: translateY(-1px);
}

.recommendation-title {
  font-size: 14px;
  font-weight: 500;
  color: #2c3e50;
  margin-bottom: 6px;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.recommendation-meta {
  display: flex;
  gap: 12px;
  font-size: 12px;
  color: #6c757d;
}

.recommendation-meta span {
  display: flex;
  align-items: center;
}

.no-recommendations {
  text-align: center;
  color: #6c757d;
  font-size: 14px;
  padding: 20px;
}
</style>