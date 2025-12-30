<template>
  <div class="topics-container">
    <!-- 页面标题和排序选项 -->
    <div class="topics-header">
      <h1 class="page-title">话题总览</h1>
      <div class="sort-options">
        <el-radio-group v-model="sortBy" @change="loadTopics">
          <el-radio-button label="followerCount">按关注人数</el-radio-button>
          <el-radio-button label="createdAt">按创建时间</el-radio-button>
        </el-radio-group>
      </div>
    </div>

    <!-- 话题列表 -->
    <div class="topics-grid">
      <div 
        v-for="topic in topics" 
        :key="topic.id" 
        class="topic-card"
        @click="goToTopicDetail(topic.id)"
      >
        <!-- 话题图片 -->
        <div class="topic-image">
          <img 
            :src="topic.coverImage || '/src/assets/logo.svg'" 
            :alt="topic.name"
            @error="handleImageError"
            @click="goToTopicDetail(topic.id)"
          />
        </div>
        
        <!-- 话题信息 -->
        <div class="topic-info">
          <h3 class="topic-name">{{ topic.name }}</h3>
          <p class="topic-description">{{ topic.description }}</p>
          <div class="topic-meta">
            <span class="follower-count">{{ topic.followerCount }} 人关注</span>
            <el-button 
              class="follow-btn" 
              :type="topic.isFollowing ? 'primary' : 'default'"
              :icon="topic.isFollowing ? 'StarFilled' : 'Star'"
              @click.stop="toggleFollow(topic)"
              size="small"
            >
              {{ topic.isFollowing ? '已关注' : '关注' }}
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 加载更多 -->
    <div class="load-more" v-if="hasMore">
      <el-button 
        :loading="loading" 
        @click="loadMore" 
        type="primary" 
        link
      >
        加载更多话题
      </el-button>
    </div>

    <!-- 空状态 -->
    <div class="empty-state" v-if="!loading && topics.length === 0">
      <el-empty description="暂无话题" />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { topicApi } from '@/api/topic'
import type { Topic } from '@/types/topic'

const router = useRouter()

// 响应式数据
const topics = ref<Topic[]>([])
const loading = ref(false)
const hasMore = ref(true)
const sortBy = ref('followerCount')
const page = ref(1)
const pageSize = 12

// 加载话题列表
const loadTopics = async (reset = false) => {
  if (loading.value) return
  
  loading.value = true
  
  if (reset) {
    page.value = 1
    topics.value = []
    hasMore.value = true
  }
  
  try {
    const response = await topicApi.getTopics({
      page: page.value,
      size: pageSize,
      sort: sortBy.value
    })
    
    if (response.code === 200) {
      const newTopics = response.data || []
      
      if (reset) {
        topics.value = newTopics
      } else {
        topics.value.push(...newTopics)
      }
      
      // 判断是否还有更多数据
      hasMore.value = newTopics.length === pageSize
    }
  } catch (error) {
    console.error('加载话题失败:', error)
    ElMessage.error('加载话题失败')
  } finally {
    loading.value = false
  }
}

// 加载更多
const loadMore = () => {
  page.value++
  loadTopics()
}

// 切换关注状态
const toggleFollow = async (topic: Topic) => {
  if (!topic.id) return
  
  try {
    await topicApi.followTopic({
      topicId: topic.id,
      follow: !topic.isFollowing
    })
    
    // 更新本地状态
    topic.isFollowing = !topic.isFollowing
    topic.followerCount += topic.isFollowing ? 1 : -1
    
    ElMessage.success(topic.isFollowing ? '关注成功' : '取消关注成功')
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 跳转到话题详情
const goToTopicDetail = (topicId: number) => {
  router.push(`/app/topics/${topicId}`)
}

// 图片加载失败处理
const handleImageError = (event: Event) => {
  const img = event.target as HTMLImageElement
  img.src = '/src/assets/logo.svg'
}

// 初始化加载
onMounted(() => {
  loadTopics(true)
})
</script>

<style scoped>
.topics-container {
  padding: 24px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.topics-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.topics-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 24px;
  margin-bottom: 24px;
}

.topic-card {
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  cursor: pointer;
  background: white;
}

.topic-card:hover {
  transform: translateY(-4px);
  box-shadow: 0 8px 25px rgba(0, 0, 0, 0.15);
  border-color: #3b82f6;
}

.topic-image {
  width: 100%;
  height: 160px;
  overflow: hidden;
}

.topic-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.3s ease;
}

.topic-card:hover .topic-image img {
  transform: scale(1.05);
}

.topic-info {
  padding: 16px;
}

.topic-name {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 8px 0;
  line-height: 1.4;
}

.topic-description {
  font-size: 14px;
  color: #6b7280;
  margin: 0 0 12px 0;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.topic-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.follower-count {
  font-size: 14px;
  color: #9ca3af;
}

.follow-btn {
  min-width: 80px;
}

.load-more {
  text-align: center;
  padding: 20px 0;
}

.empty-state {
  padding: 60px 0;
}

@media (max-width: 768px) {
  .topics-grid {
    grid-template-columns: 1fr;
    gap: 16px;
  }
  
  .topics-header {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }
  
  .topics-container {
    padding: 16px;
  }
}
</style>