<template>
  <div class="articles-view">
    <div class="view-header">
      <h2>文章列表</h2>
    </div>
    
    <div class="view-content">
      <!-- 加载状态 -->
      <div v-if="loading" class="loading-container">
        <el-skeleton :rows="4" animated />
      </div>
      
      <!-- 错误提示 -->
      <el-alert
        v-if="error"
        :title="error"
        type="error"
        show-icon
        :closable="false"
        class="error-alert"
      />
      
      <!-- 文章列表 -->
      <div v-if="!loading && !error">
        <div v-if="articles.length === 0" class="empty-container">
          <el-empty description="暂无文章" />
        </div>
        
        <div v-else class="article-list">
          <div 
            v-for="article in articles" 
            :key="article.id" 
            class="article-item"
            @click="goToArticleDetail(article.id)"
          >
            <h3 class="article-title">{{ article.title }}</h3>
            <p class="article-summary">{{ article.summary }}</p>
            
            <div class="article-meta">
              <div class="meta-item">
                <i class="el-icon-view"></i>
                <span>{{ article.viewCount }}</span>
              </div>
              <div class="meta-item">
                <i class="el-icon-thumb-up"></i>
                <span>{{ article.likeCount }}</span>
              </div>
              <div class="meta-item">
                <i class="el-icon-chat-dot-round"></i>
                <span>{{ article.commentCount }}</span>
              </div>
              <div class="meta-item">
                <i class="el-icon-time"></i>
                <span>{{ formatDate(article.createTime) }}</span>
              </div>
            </div>
          </div>
        </div>
        
        <!-- 分页 -->
        <div class="pagination-container" v-if="articles.length > 0">
          <el-pagination
            background
            layout="prev, pager, next"
            :total="total"
            :current-page="currentPage"
            :page-size="pageSize"
            @current-change="handlePageChange"
          />
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElSkeleton, ElEmpty, ElAlert, ElPagination } from 'element-plus'
import { getUserArticles } from '@/api/article'

// 路由信息
const route = useRoute()
const router = useRouter()

// 文章数据
const articles = ref([])
const loading = ref(true)
const error = ref('')
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 获取文章列表
const fetchArticles = async () => {
  try {
    loading.value = true
    error.value = ''
    
    const userId = route.params.id
    const response = await getUserArticles(userId, currentPage.value, pageSize.value)
    
    if (response && response.data) {
      articles.value = response.data.records || []
      total.value = response.data.total || 0
    }
  } catch (err) {
    error.value = '获取文章列表失败，请稍后再试'
    console.error('Error fetching articles:', err)
  } finally {
    loading.value = false
  }
}

// 处理页码变化
const handlePageChange = (page) => {
  currentPage.value = page
  fetchArticles()
}

// 格式化日期
const formatDate = (dateString) => {
  if (!dateString) return ''
  const date = new Date(dateString)
  return date.toLocaleDateString('zh-CN', {
    year: 'numeric',
    month: 'long',
    day: 'numeric'
  })
}

// 跳转到文章详情页
const goToArticleDetail = (articleId) => {
  router.push(`/app/article/${articleId}`)
}

// 监听路由变化，重新获取数据
watch(() => route.params.id, () => {
  fetchArticles()
})

// 组件挂载时获取数据
onMounted(() => {
  fetchArticles()
})
</script>

<style scoped>
.articles-view {
  padding: 20px;
}

.view-header {
  margin-bottom: 20px;
}

.view-content {
  background: #ffffff;
  border-radius: 8px;
  padding: 20px;
  min-height: 400px;
}

.loading-container {
  padding: 20px;
}

.error-alert {
  margin-bottom: 20px;
}

.empty-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

.article-list {
  margin-bottom: 20px;
}

.article-item {
  border-bottom: 1px solid #f0f0f0;
  padding: 20px 0;
  cursor: pointer;
  transition: background-color 0.3s;
}

.article-item:hover {
  background-color: #fafafa;
}

.article-item:last-child {
  border-bottom: none;
}

.article-title {
  font-size: 18px;
  font-weight: 600;
  margin-bottom: 10px;
  color: #333;
}

.article-summary {
  color: #666;
  margin-bottom: 15px;
  line-height: 1.6;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
  display: flex;
  color: #999;
  font-size: 14px;
}

.meta-item {
  margin-right: 20px;
  display: flex;
  align-items: center;
}

.meta-item i {
  margin-right: 5px;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>