<template>
  <div class="post-list">
    <div 
      v-for="post in posts" 
      :key="post.id" 
      class="post-item"
      @click="goToDetail(post.id)"
    >
      <div class="post-header">
        <div class="post-meta">
          <el-avatar :size="32" :src="post.author?.avatar || defaultAvatar" class="author-avatar" />
          <div class="author-info">
            <div class="author-name">{{ post.author?.nickname || '匿名用户' }}</div>
            <div class="post-time">{{ formatTime(post.createTime) }}</div>
          </div>
        </div>
        <div class="post-type" :class="post.type === 'question' ? 'question' : 'article'">
          {{ post.type === 'question' ? '问答' : '文章' }}
        </div>
      </div>
      
      <div class="post-body">
        <h3 class="post-title">
          {{ post.title }}
        </h3>
        <p class="post-summary">{{ post.summary }}</p>
      </div>
      
      <div class="post-footer">
        <div class="post-stats">
          <div class="stat-item">
            <el-icon><View /></el-icon>
            <span>{{ post.viewCount || 0 }}</span>
          </div>
          <div class="stat-item">
            <el-icon><ChatLineRound /></el-icon>
            <span>{{ post.commentCount || 0 }}</span>
          </div>
          <div class="stat-item">
            <el-icon><Star /></el-icon>
            <span>{{ post.likeCount || 0 }}</span>
          </div>
        </div>
        
        <div v-if="post.topics && post.topics.length > 0" class="post-topics">
          <el-tag 
            v-for="topic in post.topics" 
            :key="topic.id" 
            size="small" 
            type="info"
            class="topic-tag"
          >
            {{ topic.title || topic.name }}
          </el-tag>
        </div>
      </div>
    </div>
    
    <!-- 空状态 -->
    <el-empty v-if="posts.length === 0" description="暂无内容" />
  </div>
</template>

<script setup>
import { defineProps, defineEmits } from 'vue'
import { useRouter } from 'vue-router'
import { 
  View, 
  ChatLineRound, 
  Star,
  Edit
} from '@element-plus/icons-vue'

// 定义属性
const props = defineProps({
  posts: {
    type: Array,
    default: () => []
  },
  postType: {
    type: String,
    default: 'article' // 默认为文章类型，也可以是 'question'
  }
})

// 定义事件
const emit = defineEmits(['post-click'])

// 路由
const router = useRouter()

// 默认头像
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  const days = Math.floor(diff / (24 * 60 * 60 * 1000))
  
  if (days > 0) {
    return `${days}天前`
  }
  
  const hours = Math.floor(diff / (60 * 60 * 1000))
  if (hours > 0) {
    return `${hours}小时前`
  }
  
  const minutes = Math.floor(diff / (60 * 1000))
  if (minutes > 0) {
    return `${minutes}分钟前`
  }
  
  return '刚刚'
}

// 跳转到详情页
const goToDetail = (id) => {
  // 根据帖子类型跳转到对应的详情页
  if (props.postType === 'question') {
    router.push(`/app/qa/${id}`)
  } else {
    router.push(`/app/articles/${id}`)
  }
  emit('post-click', id)
}
</script>

<style scoped>
.post-list {
  width: 100%;
}

.post-item {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 8px;
  padding: 20px;
  margin-bottom: 16px;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.post-item:hover {
  border-color: #409eff;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.15);
  transform: translateY(-2px);
}

.post-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 12px;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 10px;
}

.author-avatar {
  width: 32px;
  height: 32px;
}

.author-info {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  margin: 0;
}

.post-time {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}

.post-type {
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.post-type.question {
  background: #fdf6ec;
  color: #e6a23c;
  border: 1px solid #faecd8;
}

.post-type.article {
  background: #f0f9ff;
  color: #409eff;
  border: 1px solid #d2ebff;
}

.post-body {
  margin-bottom: 12px;
}

.post-title {
  margin: 0 0 10px 0;
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
  transition: color 0.2s;
}

.post-title:hover {
  color: #409eff;
}

.post-summary {
  margin: 0 0 12px 0;
  font-size: 14px;
  color: #6b7280;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.post-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-top: 12px;
  border-top: 1px solid #f2f6fc;
}

.post-stats {
  display: flex;
  gap: 16px;
  align-items: center;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #909399;
}

.stat-item .el-icon {
  font-size: 14px;
}

.post-topics {
  display: flex;
  gap: 6px;
  flex-wrap: wrap;
  justify-content: flex-end;
}

.topic-tag {
  cursor: pointer;
  height: 24px;
  padding: 0 8px;
  line-height: 22px;
}
</style>