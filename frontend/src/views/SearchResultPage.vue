<template>
  <div class="search-result-page">
    <!-- 搜索结果标题 -->
    <div class="search-header">
      <h2>搜索结果：<span class="keyword">{{ keyword }}</span></h2>
    </div>

    <!-- 搜索结果标签页 -->
    <el-tabs v-model="activeTab" @tab-click="handleTabClick" class="search-tabs">
      <el-tab-pane label="文章" name="articles">
        <!-- 文章列表 -->
        <div v-if="loading.articles" class="loading-state">
          <el-skeleton :rows="3" animated />
        </div>
        <div v-else-if="articles.length === 0" class="empty-state">
          <p>暂无相关文章</p>
        </div>
        <div v-else class="articles-list">
          <div v-for="article in articles" :key="article.id" class="article-item">
            <div class="article-content">
              <h3 @click="goToArticle(article.id)" class="article-title">{{ article.title }}</h3>
              <p class="article-summary">{{ article.summary }}</p>
              <div class="article-meta">
                <span class="author">作者ID: {{ article.userId }}</span>
                <span class="date">{{ formatDate(article.createTime) }}</span>
                <div class="article-stats">
                  <span><i class="el-icon-view"></i> {{ article.viewCount || 0 }}</span>
                  <span><i class="el-icon-chat-dot-round"></i> {{ article.commentCount || 0 }}</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="话题" name="topics">
        <!-- 话题列表 -->
        <div v-if="loading.topics" class="loading-state">
          <el-skeleton :rows="3" animated />
        </div>
        <div v-else-if="topics.length === 0" class="empty-state">
          <p>暂无相关话题</p>
        </div>
        <div v-else class="topics-list">
          <div v-for="topic in topics" :key="topic.id" class="topic-item">
            <div class="topic-icon" :style="{ backgroundColor: topic.color || '#1976d2' }">
              {{ topic.icon || '话题' }}
            </div>
            <div class="topic-content">
              <h3 @click="goToTopic(topic.id)" class="topic-name">{{ topic.name }}</h3>
              <p class="topic-description">{{ topic.description || '暂无描述' }}</p>
              <div class="topic-stats">
                <span><i class="el-icon-document"></i> {{ topic.articleCount || 0 }} 篇文章</span>
                <span><i class="el-icon-collection"></i> {{ topic.followerCount || 0 }} 人关注</span>
              </div>
            </div>
          </div>
        </div>
      </el-tab-pane>

      <el-tab-pane label="用户" name="users">
        <!-- 用户列表 -->
        <div v-if="loading.users" class="loading-state">
          <el-skeleton :rows="3" animated />
        </div>
        <div v-else-if="users.length === 0" class="empty-state">
          <p>暂无相关用户</p>
        </div>
        <div v-else class="users-list">
          <div v-for="user in users" :key="user.id" class="user-item">
            <div class="user-avatar">
              <el-avatar :src="user.avatar" :size="60">{{ user.nickname ? user.nickname.charAt(0) : user.username.charAt(0) }}</el-avatar>
            </div>
            <div class="user-info">
              <h3 @click="goToUser(user.id)" class="user-name">{{ user.nickname || user.username }}</h3>
              <p class="user-title">{{ user.signature || '这个人很懒，什么都没有留下' }}</p>
              <div class="user-stats">
                <span><i class="el-icon-collection"></i> {{ user.followerCount || 0 }} 关注者</span>
                <span><i class="el-icon-document"></i> {{ user.articleCount || 0 }} 篇文章</span>
              </div>
            </div>
            <div class="user-actions">
              <el-button 
                v-if="!user.isFollowing" 
                type="primary" 
                @click="toggleFollow(user)"
                size="small"
              >
                关注
              </el-button>
              <el-button 
                v-else 
                @click="toggleFollow(user)"
                size="small"
              >
                已关注
              </el-button>
            </div>
          </div>
        </div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()

// 搜索关键词
const keyword = ref('')

// 激活的标签页
const activeTab = ref('articles')

// 搜索结果数据
const articles = ref([])
const topics = ref([])
const users = ref([])

// 加载状态
const loading = ref({
  articles: false,
  topics: false,
  users: false
})

// 组件挂载时初始化
onMounted(() => {
  // 获取路由中的搜索关键词
  keyword.value = route.query.q || ''
  
  // 加载搜索结果
  loadSearchResults()
})

// 加载搜索结果
const loadSearchResults = async () => {
  try {
    // 同时加载文章、话题和用户的结果
    loading.value.articles = true
    loading.value.topics = true
    loading.value.users = true
    
    const response = await request.get('/search', {
      params: { q: keyword.value }
    })
    
    // 解析返回的数据
    articles.value = response.data.articles || []
    topics.value = response.data.topics || []
    users.value = response.data.users || []
    
  } catch (error) {
    console.error('搜索失败:', error)
    ElMessage.error('搜索失败')
  } finally {
    // 结束所有加载状态
    loading.value.articles = false
    loading.value.topics = false
    loading.value.users = false
  }
}

// 处理标签页切换
const handleTabClick = (tab) => {
  // 标签页切换只需要更新当前激活的标签页
  // 所有数据已经在初始加载时获取，无需额外请求
}

// 格式化日期
const formatDate = (dateString) => {
  const date = new Date(dateString)
  const now = new Date()
  const diff = now - date
  
  // 计算时间差
  const minute = 60 * 1000
  const hour = 60 * minute
  const day = 24 * hour
  const week = 7 * day
  const month = 30 * day
  
  if (diff < minute) {
    return '刚刚'
  } else if (diff < hour) {
    return `${Math.floor(diff / minute)}分钟前`
  } else if (diff < day) {
    return `${Math.floor(diff / hour)}小时前`
  } else if (diff < week) {
    return `${Math.floor(diff / day)}天前`
  } else if (diff < month) {
    return `${Math.floor(diff / week)}周前`
  } else {
    return `${date.getFullYear()}-${String(date.getMonth() + 1).padStart(2, '0')}-${String(date.getDate()).padStart(2, '0')}`
  }
}

// 跳转到文章详情页
const goToArticle = (id) => {
  router.push(`/article/${id}`)
}

// 跳转到话题详情页
const goToTopic = (id) => {
  router.push(`/topic/${id}`)
}

// 跳转到用户资料页
const goToUser = (id) => {
  router.push(`/user/${id}`)
}

// 关注/取消关注用户
const toggleFollow = async (user) => {
  try {
    if (user.isFollowing) {
      // 取消关注
      await request.delete(`/users/${user.id}/follow`)
      user.isFollowing = false
      if (user.followerCount) user.followerCount--
    } else {
      // 关注
      await request.post(`/users/${user.id}/follow`)
      user.isFollowing = true
      if (user.followerCount) user.followerCount++
    }
  } catch (error) {
    console.error('关注操作失败:', error)
    ElMessage.error('操作失败，请稍后重试')
  }
}
</script>

<style scoped>
.search-result-page {
  max-width: 900px;
  margin: 0 auto;
  padding: 20px;
}

.search-header {
  margin-bottom: 20px;
  border-bottom: 1px solid #eaeaea;
  padding-bottom: 10px;
}

.search-header h2 {
  font-size: 20px;
  color: #1e3a8a;
  font-weight: 500;
}

.keyword {
  color: #3b82f6;
  font-weight: 600;
}

.search-tabs {
  margin-top: 20px;
}

.loading-state,
.empty-state {
  text-align: center;
  padding: 40px 0;
  color: #909399;
}

.article-item,
.topic-item,
.user-item {
  border-bottom: 1px solid #f0f0f0;
  padding: 15px 0;
  display: flex;
  transition: background-color 0.2s;
}

.article-item:hover,
.topic-item:hover,
.user-item:hover {
  background-color: #f9fafb;
}

.article-title,
.topic-name,
.user-name {
  font-size: 16px;
  font-weight: 600;
  color: #1f2937;
  cursor: pointer;
  margin-bottom: 5px;
  transition: color 0.2s;
}

.article-title:hover,
.topic-name:hover,
.user-name:hover {
  color: #3b82f6;
}

.article-summary,
.topic-description,
.user-title {
  color: #4b5563;
  font-size: 14px;
  margin: 5px 0;
}

.article-meta,
.topic-stats,
.user-stats {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-top: 8px;
  font-size: 12px;
  color: #6b7280;
}

.author,
.date {
  color: #6b7280;
}

.article-stats,
.topic-stats,
.user-stats {
  display: flex;
  gap: 10px;
}

.article-stats span,
.topic-stats span,
.user-stats span {
  display: flex;
  align-items: center;
  gap: 3px;
}

.topic-item {
  align-items: center;
  gap: 15px;
}

.topic-icon {
  width: 40px;
  height: 40px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: 600;
  flex-shrink: 0;
}

.user-item {
  align-items: center;
  gap: 15px;
  padding: 15px;
  border-radius: 8px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.05);
}

.user-info {
  flex: 1;
}

.user-actions {
  flex-shrink: 0;
}

.articles-list,
.topics-list,
.users-list {
  margin-top: 10px;
}
</style>