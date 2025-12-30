<template>
  <div class="topic-detail-container">
    <!-- 浮动编辑器 -->
    <FloatingEditor 
      :current-topic-id="parseInt(route.params.id as string)"
      @submit="handleEditorSubmit"
      @close="handleEditorClose"
      @mode-change="handleEditorModeChange"
    />
    
    <!-- 话题头部信息 -->
    <div class="topic-header">
      <div class="topic-cover">
        <img 
          :src="topic.coverImage || '/src/assets/logo.svg'" 
          :alt="topic.name"
          @error="handleImageError"
        />
      </div>
      <div class="topic-info">
        <h1 class="topic-title">{{ topic.name }}</h1>
        <p class="topic-description">{{ topic.description }}</p>
        <div class="topic-stats">
          <div class="stat-item">
            <span class="stat-value">{{ topic.followerCount }}</span>
            <span class="stat-label">关注人数</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ articleCount }}</span>
            <span class="stat-label">文章数</span>
          </div>
          <div class="stat-item">
            <span class="stat-value">{{ questionCount }}</span>
            <span class="stat-label">问答数</span>
          </div>
        </div>
        <div class="topic-actions">
          <el-button 
            :type="topic.isFollowing ? 'primary' : 'default'"
            :icon="topic.isFollowing ? 'StarFilled' : 'Star'"
            @click="toggleFollow"
            size="large"
          >
            {{ topic.isFollowing ? '已关注' : '关注话题' }}
          </el-button>
          <el-button @click="goBack" type="default" size="large">
            返回列表
          </el-button>
        </div>
      </div>
    </div>

    <!-- 内容切换标签 -->
    <div class="content-tabs">
      <el-tabs v-model="activeTab" class="content-tabs">
        <el-tab-pane label="文章" name="articles">
          <!-- 文章列表 -->
          <div class="content-list">
            <div 
              v-for="article in articles" 
              :key="article.id" 
              class="content-item"
              @click="goToArticleDetail(article.id)"
            >
              <div class="content-main">
                <h3 class="content-title">{{ article.title }}</h3>
                <p class="content-summary">{{ article.content }}</p>
                <div class="content-meta">
                  <span class="author">{{ article.author }}</span>
                  <span class="time">{{ formatTime(article.createdAt) }}</span>
                </div>
              </div>
              <div class="content-stats">
                <div class="stat">
                  <el-icon><View /></el-icon>
                  <span>{{ article.viewCount }}</span>
                </div>
                <div class="stat">
                  <el-icon><Star /></el-icon>
                  <span>{{ article.likeCount }}</span>
                </div>
                <div class="stat">
                  <el-icon><ChatDotRound /></el-icon>
                  <span>{{ article.commentCount }}</span>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 空状态 -->
          <div class="empty-state" v-if="!loading && articles.length === 0">
            <el-empty description="暂无文章" />
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="问答" name="questions">
          <!-- 问答列表 -->
          <div class="content-list">
            <div 
              v-for="question in questions" 
              :key="question.id" 
              class="content-item question-item"
              @click="goToQuestionDetail(question.id)"
            >
              <div class="question-status" :class="{ solved: question.isSolved }">
                <el-icon v-if="question.isSolved"><CircleCheck /></el-icon>
                <span>{{ question.isSolved ? '已解决' : '待解决' }}</span>
              </div>
              <div class="content-main">
                <h3 class="content-title">{{ question.title }}</h3>
                <p class="content-summary">{{ question.content }}</p>
                <div class="content-meta">
                  <span class="author">{{ question.author }}</span>
                  <span class="time">{{ formatTime(question.createdAt) }}</span>
                </div>
              </div>
              <div class="content-stats">
                <div class="stat">
                  <el-icon><View /></el-icon>
                  <span>{{ question.viewCount }}</span>
                </div>
                <div class="stat">
                  <el-icon><Star /></el-icon>
                  <span>{{ question.likeCount }}</span>
                </div>
                <div class="stat">
                  <el-icon><ChatDotRound /></el-icon>
                  <span>{{ question.answerCount }}</span>
                </div>
              </div>
            </div>
          </div>
          
          <!-- 空状态 -->
          <div class="empty-state" v-if="!loading && questions.length === 0">
            <el-empty description="暂无问答" />
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>

    <!-- 加载状态 -->
    <div class="loading-state" v-if="loading">
      <el-skeleton :rows="5" animated />
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { View, Star, ChatDotRound, CircleCheck } from '@element-plus/icons-vue'
import { topicApi } from '@/api/topic'
import type { Topic } from '@/types/topic'
import type { Article } from '@/types/article'
import type { Question } from '@/types/qa'
import type { EditorContent } from '@/types/editor'
import FloatingEditor from '@/components/FloatingEditor.vue'

const route = useRoute()
const router = useRouter()

// 响应式数据
const topic = ref<Topic>({
  id: 0,
  name: '',
  description: '',
  coverImage: '',
  followerCount: 0,
  isFollowing: false,
  createdAt: '',
  updatedAt: ''
})

const articles = ref<Article[]>([])
const questions = ref<Question[]>([])
const loading = ref(false)
const activeTab = ref('articles')
const articleCount = ref(0)
const questionCount = ref(0)

// 加载话题详情
const loadTopicDetail = async () => {
  const topicId = parseInt(route.params.id as string)
  if (!topicId) return
  
  try {
    loading.value = true
    const topicData = await topicApi.getTopicDetail(topicId)
    topic.value = topicData
  } catch (error) {
    console.error('加载话题详情失败:', error)
    ElMessage.error('加载话题详情失败')
  } finally {
    loading.value = false
  }
}

// 加载文章列表
const loadArticles = async () => {
  const topicId = parseInt(route.params.id as string)
  if (!topicId) return
  
  try {
    const response = await topicApi.getTopicArticles(topicId)
    if (response.code === 200) {
      articles.value = response.data
      articleCount.value = response.total
    }
  } catch (error) {
    console.error('加载文章列表失败:', error)
  }
}

// 加载问答列表
const loadQuestions = async () => {
  const topicId = parseInt(route.params.id as string)
  if (!topicId) return
  
  try {
    const response = await topicApi.getTopicQuestions(topicId)
    if (response.code === 200) {
      questions.value = response.data
      questionCount.value = response.total
    }
  } catch (error) {
    console.error('加载问答列表失败:', error)
  }
}

// 切换关注状态
const toggleFollow = async () => {
  if (!topic.value.id) return
  
  try {
    await topicApi.followTopic({
      topicId: topic.value.id,
      follow: !topic.value.isFollowing
    })
    
    // 更新本地状态
    topic.value.isFollowing = !topic.value.isFollowing
    topic.value.followerCount += topic.value.isFollowing ? 1 : -1
    
    ElMessage.success(topic.value.isFollowing ? '关注成功' : '取消关注成功')
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 跳转到文章详情
const goToArticleDetail = (articleId: number) => {
  router.push(`/app/articles/${articleId}`)
}

// 跳转到问答详情
const goToQuestionDetail = (questionId: number) => {
  router.push(`/app/qa/${questionId}`)
}

// 返回话题列表
const goBack = () => {
  router.push('/app/topics')
}

// 图片加载失败处理
const handleImageError = (event: Event) => {
  const img = event.target as HTMLImageElement
  img.src = '/src/assets/logo.svg'
}

// 格式化时间
const formatTime = (timeString: string) => {
  const date = new Date(timeString)
  return date.toLocaleDateString('zh-CN')
}

// 编辑器相关方法
const handleEditorSubmit = async (content: EditorContent) => {
  try {
    console.log('编辑器提交内容:', content)
    
    // 根据内容类型处理不同的发布逻辑
    switch (content.type) {
      case 'article':
        // 发布文章逻辑
        if (content.title && content.content && content.topicId) {
          await topicApi.publishArticle({
            title: content.title,
            content: content.content,
            topicId: content.topicId
          })
          ElMessage.success('文章发布成功!')
          // 重新加载文章列表
          loadArticles()
        }
        break
        
      case 'question':
        // 发布问题逻辑
        if (content.title && content.content && content.topicId) {
          await topicApi.publishQuestion({
            title: content.title,
            content: content.content,
            topicId: content.topicId
          })
          ElMessage.success('问题发布成功!')
          // 重新加载问答列表
          loadQuestions()
        }
        break
        
      case 'comment':
        // 发布评论逻辑
        if (content.content) {
          // 这里需要实现发布评论的API
          ElMessage.success('评论发布成功!')
        }
        break
    }
  } catch (error) {
    console.error('发布失败:', error)
    ElMessage.error('发布失败，请重试')
  }
}

const handleEditorClose = () => {
  console.log('编辑器关闭')
}

const handleEditorModeChange = (mode: string) => {
  console.log('编辑器模式变化:', mode)
}

// 监听标签切换
watch(activeTab, (newTab) => {
  if (newTab === 'articles') {
    loadArticles()
  } else if (newTab === 'questions') {
    loadQuestions()
  }
})

// 初始化加载
onMounted(() => {
  loadTopicDetail()
  loadArticles() // 默认加载文章
})
</script>

<style scoped>
.topic-detail-container {
  padding: 24px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  min-height: 600px;
}

.topic-header {
  display: flex;
  gap: 24px;
  margin-bottom: 32px;
  padding-bottom: 24px;
  border-bottom: 1px solid #f0f0f0;
}

.topic-cover {
  flex-shrink: 0;
  width: 200px;
  height: 150px;
  border-radius: 12px;
  overflow: hidden;
}

.topic-cover img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.topic-info {
  flex: 1;
}

.topic-title {
  font-size: 28px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 12px 0;
}

.topic-description {
  font-size: 16px;
  color: #6b7280;
  line-height: 1.6;
  margin: 0 0 24px 0;
}

.topic-stats {
  display: flex;
  gap: 32px;
  margin-bottom: 24px;
}

.stat-item {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 24px;
  font-weight: 600;
  color: #3b82f6;
}

.stat-label {
  display: block;
  font-size: 14px;
  color: #9ca3af;
  margin-top: 4px;
}

.topic-actions {
  display: flex;
  gap: 12px;
}

.content-tabs {
  margin-top: 0;
}

.content-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.content-item {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  padding: 20px;
  border: 1px solid #e5e7eb;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.3s ease;
  background: white;
}

.content-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
  border-color: #3b82f6;
}

.question-item {
  position: relative;
}

.question-status {
  position: absolute;
  top: 12px;
  right: 12px;
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  padding: 4px 8px;
  border-radius: 6px;
  background: #f3f4f6;
  color: #6b7280;
}

.question-status.solved {
  background: #d1fae5;
  color: #065f46;
}

.content-main {
  flex: 1;
}

.content-title {
  font-size: 18px;
  font-weight: 600;
  color: #1f2937;
  margin: 0 0 8px 0;
  line-height: 1.4;
}

.content-summary {
  font-size: 14px;
  color: #6b7280;
  line-height: 1.5;
  margin: 0 0 12px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.content-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #9ca3af;
}

.content-stats {
  display: flex;
  gap: 16px;
  align-items: center;
}

.stat {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 12px;
  color: #9ca3af;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

.loading-state {
  padding: 40px 0;
}

@media (max-width: 768px) {
  .topic-header {
    flex-direction: column;
    gap: 16px;
  }
  
  .topic-cover {
    width: 100%;
    height: 200px;
  }
  
  .topic-stats {
    gap: 16px;
  }
  
  .content-item {
    flex-direction: column;
    gap: 12px;
  }
  
  .content-stats {
    align-self: flex-end;
  }
}
</style>