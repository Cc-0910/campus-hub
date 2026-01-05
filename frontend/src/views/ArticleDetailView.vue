<template>
  <div class="article-detail-page">
    <div class="article-container">
      <!-- 文章内容区域 -->
      <div class="article-content">
        <el-card class="article-card">
          <!-- 文章头部 -->
          <div class="article-header">
            <h1 class="article-title">{{ articleDetail.title }}</h1>
            <div class="article-meta">
              <div class="author-info">
                <el-avatar :size="32" :src="articleDetail.author?.avatar || defaultAvatar" />
                <div class="author-details">
                  <div class="author-name">{{ articleDetail.author?.nickname || '匿名用户' }}</div>
                  <div class="article-time">{{ formatTime(articleDetail.createTime) }}</div>
                </div>
              </div>
              <div class="article-stats">
                <div class="stat-item">
                  <el-icon><View /></el-icon>
                  <span>{{ articleDetail.viewCount || 0 }} 浏览</span>
                </div>
                <div class="stat-item">
                  <el-icon><Star /></el-icon>
                  <span>{{ articleDetail.likeCount || 0 }} 点赞</span>
                </div>
                <div class="stat-item">
                  <el-icon><ChatLineRound /></el-icon>
                  <span>{{ commentCount }} 评论</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 文章标签 -->
          <div v-if="articleDetail.topics && articleDetail.topics.length > 0" class="article-topics">
            <el-tag 
              v-for="topic in articleDetail.topics" 
              :key="topic.id" 
              size="small" 
              type="info"
              class="topic-tag"
            >
              {{ topic.name }}
            </el-tag>
          </div>

          <!-- 文章正文 -->
          <div class="article-body">
            <div class="article-text" v-html="articleDetail.content"></div>
          </div>

          <!-- 文章操作 -->
          <div class="article-actions">
            <el-button 
              :type="isLiked ? 'primary' : 'default'" 
              :icon="Star" 
              @click="toggleLike"
            >
              {{ isLiked ? '已点赞' : '点赞' }} {{ articleDetail.likeCount || 0 }}
            </el-button>
            <el-button 
              :type="isStared ? 'warning' : 'default'" 
              :icon="ChatLineRound"
              @click="focusComment"
            >
              评论 {{ commentCount }}
            </el-button>
            <el-button 
              :type="isShared ? 'success' : 'default'" 
              :icon="Share"
              @click="shareArticle"
            >
              分享
            </el-button>
          </div>
        </el-card>
      </div>

      <!-- 侧边栏 -->
      <div class="sidebar">
        <!-- 作者信息卡片 -->
        <el-card class="author-card">
          <div class="author-card-content">
            <el-avatar :size="60" :src="articleDetail.author?.avatar || defaultAvatar" />
            <div class="author-card-info">
              <div class="author-card-name">{{ articleDetail.author?.nickname || '匿名用户' }}</div>
              <div class="author-card-stats">
                <div class="stat">
                  <span class="stat-value">12</span>
                  <span class="stat-label">文章</span>
                </div>
                <div class="stat">
                  <span class="stat-value">36</span>
                  <span class="stat-label">粉丝</span>
                </div>
              </div>
            </div>
            <el-button type="primary" size="small">关注</el-button>
          </div>
        </el-card>

        <!-- 相关推荐 -->
        <el-card class="recommend-card">
          <template #header>
            <div class="card-header">
              <span>相关推荐</span>
            </div>
          </template>
          <div class="recommend-list">
            <div 
              v-for="item in relatedArticles" 
              :key="item.id" 
              class="recommend-item"
              @click="goToDetail(item.id)"
            >
              <h4 class="recommend-title">{{ item.title }}</h4>
              <div class="recommend-meta">
                <span class="recommend-author">{{ item.author?.nickname || '匿名用户' }}</span>
                <span class="recommend-views">{{ item.viewCount || 0 }} 浏览</span>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 评论区域 -->
    <div class="comments-section">
      <el-card>
        <template #header>
          <div class="comments-header">
            <span>评论 ({{ commentCount }})</span>
          </div>
        </template>

        <!-- 发表评论 -->
        <div class="comment-input-area">
          <el-input
            v-model="newComment"
            :rows="4"
            type="textarea"
            placeholder="请输入评论内容..."
            maxlength="500"
            show-word-limit
          />
          <div class="comment-actions">
            <el-button type="primary" @click="submitComment" :loading="commentSubmitting">
              发表评论
            </el-button>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="comments-list">
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <div class="comment-avatar">
              <el-avatar :size="40" :src="comment.user?.avatar || defaultAvatar" />
            </div>
            <div class="comment-content">
              <div class="comment-header">
                <span class="comment-author">{{ comment.user?.nickname || '匿名用户' }}</span>
                <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
              </div>
              <div class="comment-text">{{ comment.content }}</div>
              <div class="comment-actions">
                <el-button size="small" link :icon="Star">点赞 ({{ comment.likeCount || 0 }})</el-button>
                <el-button size="small" link @click="replyToComment(comment)">回复</el-button>
              </div>
              
              <!-- 回复列表 -->
              <div v-if="comment.children && comment.children.length > 0" class="replies-list">
                <div v-for="reply in comment.children" :key="reply.id" class="reply-item">
                  <div class="reply-content">
                    <span class="reply-author">{{ reply.user?.nickname || '匿名用户' }}</span>
                    <span class="reply-separator">回复</span>
                    <span class="reply-target">{{ reply.replyToUser?.nickname || '用户' }}:</span>
                    <span class="reply-text">{{ reply.content }}</span>
                  </div>
                  <div class="reply-meta">
                    <span class="reply-time">{{ formatTime(reply.createTime) }}</span>
                    <el-button size="small" link :icon="Star">点赞</el-button>
                    <el-button size="small" link @click="replyToComment(reply)">回复</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  View,
  ChatLineRound,
  Star,
  Share,
  Edit
} from '@element-plus/icons-vue'
import { commentApi } from '@/api/comment'
import { topicApi } from '@/api/topic'
import request from '@/utils/request'
import { usePostStore } from '@/stores/post'

// 路由
const route = useRoute()
const router = useRouter()

// 响应式数据
const articleId = ref(Number(route.params.id))
const articleDetail = ref<Record<string, any>>({})
const commentCount = ref<number>(0)
const comments = ref<Array<Record<string, any>>>([])
const relatedArticles = ref<Array<Record<string, any>>>([])
const newComment = ref<string>('')
const commentSubmitting = ref<boolean>(false)
const isLiked = ref<boolean>(false)
const isStared = ref<boolean>(false)
const isShared = ref<boolean>(false)
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

// Store
const postStore = usePostStore()

// 获取文章详情
const fetchArticleDetail = async () => {
  try {
    // 从store中获取缓存的帖子数据
    const cachedPost = postStore.getCachedPost(articleId.value)
    
    // request.js 的响应拦截器会直接返回 res.data，所以 response 已经是 data 对象
    const response = await request.get(`post/${articleId.value}`)
    // 由于拦截器已经处理了 code !== 200 的情况，所以这里可以直接使用 response
    articleDetail.value = response || {}
    isLiked.value = response?.isLiked || false
    isStared.value = response?.isStared || false
    
    // 优先使用缓存的数据来保持标题和作者一致
    if (cachedPost) {
      articleDetail.value.title = cachedPost.title
      articleDetail.value.author = cachedPost.author
    }
  } catch (error) {
    console.error('获取文章详情失败:', error)
    ElMessage.error('获取文章详情失败')
    // 如果API调用失败，使用空对象或缓存数据
    const cachedPost = postStore.getCachedPost(articleId.value)
    articleDetail.value = cachedPost || {}
  }
}

// 获取评论列表
const fetchComments = async () => {
  try {
    // request.js 的响应拦截器会直接返回 res.data，所以 response 已经是 data 对象
    const response = await commentApi.getCommentsWithReplies(parseInt(articleId.value))
    // 由于拦截器已经处理了 code !== 200 的情况，所以这里可以直接使用 response
    comments.value = response || []
    commentCount.value = comments.value.length
  } catch (error) {
    console.error('获取评论失败:', error)
    ElMessage.error('获取评论失败')
    // 如果API调用失败，显示空列表
    comments.value = []
    commentCount.value = 0
  }
}

// 获取相关推荐
const fetchRelatedArticles = async () => {
  try {
    // 获取话题相关的帖子作为推荐
    // 这里可以使用现有的API获取帖子列表
    // request.js 的响应拦截器会直接返回 res.data，所以 response 已经是 data 对象
    const response = await request.get('posts', {
      params: {
        pageNum: 1,
        pageSize: 3
      }
    })
    // 由于拦截器已经处理了 code !== 200 的情况，所以这里可以直接使用 response
    relatedArticles.value = response?.list || []
  } catch (error) {
    console.error('获取相关推荐失败:', error)
    // API调用失败时显示空列表
    relatedArticles.value = []
  }
}

// 格式化时间
const formatTime = (time: string): string => {
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

// 切换点赞状态
const toggleLike = () => {
  isLiked.value = !isLiked.value
  if (isLiked.value) {
    articleDetail.value.likeCount = (articleDetail.value.likeCount || 0) + 1
    ElMessage.success('已点赞')
  } else {
    articleDetail.value.likeCount = Math.max(0, (articleDetail.value.likeCount || 0) - 1)
    ElMessage.info('已取消点赞')
  }
}

// 聚焦评论输入框
const focusComment = () => {
  // 滚动到评论区域
  document.querySelector('.comment-input-area')?.scrollIntoView({ behavior: 'smooth' })
}

// 分享文章
const shareArticle = () => {
  isShared.value = true
  // 这里可以实现具体的分享功能
  navigator.clipboard.writeText(window.location.href)
  ElMessage.success('链接已复制到剪贴板')
}

// 提交评论
const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  commentSubmitting.value = true
  try {
    // request.js 的响应拦截器会直接返回 res.data，所以 response 已经是 data 对象
    await commentApi.createComment({
      postId: parseInt(articleId.value),
      content: newComment.value
    })
    
    // 重新获取评论列表以获取最新评论
    await fetchComments()
    newComment.value = ''
    ElMessage.success('评论发表成功')
  } catch (error) {
    console.error('发表评论失败:', error)
    ElMessage.error('发表评论失败')
  } finally {
    commentSubmitting.value = false
  }
}

// 回复评论
const replyToComment = (comment: Record<string, any>): void => {
  console.log('回复评论:', comment)
  // 这里可以打开回复输入框
  ElMessage.info('回复功能待实现')
}

// 跳转到详情页
const goToDetail = (id: number): void => {
  router.push(`/app/articles/${id}`)
}

// 初始化数据
onMounted(async () => {
  await fetchArticleDetail()
  await fetchComments()
  await fetchRelatedArticles()
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
.article-detail-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.article-container {
  display: flex;
  gap: 20px;
}

.article-content {
  flex: 1;
}

.article-card {
  margin-bottom: 20px;
}

.article-header {
  margin-bottom: 16px;
}

.article-title {
  margin: 0 0 12px 0;
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  line-height: 1.3;
}

.article-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-details {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-weight: 500;
  color: #303133;
}

.article-time {
  font-size: 13px;
  color: #909399;
}

.article-stats {
  display: flex;
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #909399;
}

.article-topics {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.topic-tag {
  cursor: pointer;
}

.article-body {
  margin: 24px 0;
}

.article-text {
  line-height: 1.8;
  color: #303133;
}

.article-text :deep(h3) {
  margin: 24px 0 12px 0;
  font-size: 18px;
  font-weight: 600;
}

.article-text :deep(p) {
  margin: 12px 0;
}

.article-text :deep(pre) {
  background-color: #f6f8fa;
  border-radius: 6px;
  padding: 12px;
  overflow-x: auto;
  margin: 12px 0;
}

.article-actions {
  display: flex;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #e4e7ed;
}

.sidebar {
  width: 300px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.author-card {
  height: fit-content;
}

.author-card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.author-card-info {
  text-align: center;
}

.author-card-name {
  font-weight: 500;
  margin-bottom: 8px;
}

.author-card-stats {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.stat {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

.recommend-card {
  height: fit-content;
}

.recommend-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.recommend-item {
  padding: 8px 0;
  border-bottom: 1px solid #e4e7ed;
  cursor: pointer;
}

.recommend-item:last-child {
  border-bottom: none;
}

.recommend-title {
  margin: 0 0 4px 0;
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.recommend-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

.comments-section {
  margin-top: 20px;
}

.comments-header {
  font-size: 16px;
  font-weight: 600;
}

.comment-input-area {
  margin-bottom: 24px;
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  display: flex;
  gap: 12px;
  padding: 16px 0;
  border-bottom: 1px solid #e4e7ed;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-avatar {
  flex-shrink: 0;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.comment-author {
  font-weight: 500;
  color: #303133;
}

.comment-time {
  font-size: 12px;
  color: #909399;
}

.comment-text {
  margin-bottom: 8px;
  line-height: 1.6;
  color: #606266;
}

.comment-actions {
  display: flex;
  gap: 16px;
}

.replies-list {
  margin-top: 12px;
  padding-left: 20px;
  border-left: 2px solid #e4e7ed;
}

.reply-item {
  padding: 8px 0;
  border-bottom: 1px solid #f4f4f5;
}

.reply-item:last-child {
  border-bottom: none;
}

.reply-content {
  margin-bottom: 4px;
}

.reply-author {
  font-weight: 500;
  color: #303133;
}

.reply-separator {
  color: #909399;
  margin: 0 4px;
}

.reply-target {
  font-weight: 500;
  color: #409eff;
}

.reply-text {
  color: #606266;
}

.reply-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 12px;
  color: #909399;
}

@media (max-width: 768px) {
  .article-container {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
  }
  
  .article-detail-page {
    padding: 10px;
  }
}
</style>