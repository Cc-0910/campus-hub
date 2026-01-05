<template>
  <div class="article-page">
    <!-- 页面标题和筛选栏 -->
    <div class="page-header">
      <h2 class="page-title">文章大厅</h2>
      <div class="filter-section">
        <el-select 
          v-model="selectedTopic" 
          placeholder="选择话题" 
          clearable
          @change="handleTopicChange"
          style="width: 200px;"
        >
          <el-option 
            v-for="topic in topics" 
            :key="topic.id" 
            :label="topic.name" 
            :value="topic.id"
          />
        </el-select>
      </div>
    </div>

    <!-- 文章列表 -->
    <div class="article-list">
      <PostList 
        :posts="posts" 
        post-type="article"
        @post-click="handlePostClick"
      />
    </div>

    <!-- 分页 -->
    <div class="pagination-wrapper" v-if="total > 0">
      <el-pagination
        v-model:current-page="currentPage"
        v-model:page-size="pageSize"
        :page-sizes="[10, 20, 30, 50]"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
        @current-change="handlePageChange"
        @size-change="handleSizeChange"
      />
    </div>

    <!-- 相关推荐 -->
    <Recommendations 
      :recommendations="recommendations"
      :current-post-type="'article'"
      @recommendation-click="handleRecommendationClick"
    />

    <!-- 固定定位的写点什么按钮 -->
    <div class="fixed-write-button">
      <el-button type="primary" round size="large" @click="showEditor = true">
        <el-icon><Edit /></el-icon>
        写点什么
      </el-button>
    </div>

    <!-- 编辑器弹窗 -->
    <el-dialog
      v-model="showEditor"
      title="发布文章"
      width="60%"
      :before-close="handleCloseEditor"
    >
      <CommonEditor
        :show-topic-selector="true"
        :show-title-input="true"
        :show-comment-type="false"
        :topics="topics"
        :title-placeholder="'请输入文章标题'"
        :content-placeholder="'请输入文章内容...'"
        :submit-button-text="'发布文章'"
        @submit="handlePublishWithEditor"
        @cancel="handleCloseEditor"
      />
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import PostList from '@/components/PostList.vue'
import CommonEditor from '@/components/CommonEditor.vue'
import Recommendations from '@/components/Recommendations.vue'
import { Edit } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'

// 用户状态
const userStore = useUserStore()

// 响应式数据
const posts = ref([])
const topics = ref([])
const recommendations = ref([])
const selectedTopic = ref('')
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)
const showEditor = ref(false)
const publishing = ref(false)
const editorTitle = ref('')
const editorContent = ref('')
const editorTopicId = ref('')

import request from '@/utils/request'

// 获取文章列表
const fetchArticleList = async () => {
  try {
    const params = {
      pageNum: currentPage.value,
      pageSize: pageSize.value
    }
    
    if (selectedTopic.value) {
      params.topicId = selectedTopic.value
    }

    // 使用按类型获取文章的接口
    const response = await request.get('/posts/type/article', { params })
    
    if (response.code === 200) {
      posts.value = response.data.list || []
      total.value = response.data.total || 0
      // 获取相关推荐
      await fetchRecommendations()
    } else {
      ElMessage.error(response.message || '获取文章列表失败')
      posts.value = []
      total.value = 0
    }
  } catch (error) {
    console.error('获取文章列表失败:', error)
    ElMessage.error(error.message || '获取文章列表失败')
    posts.value = []
    total.value = 0
  }
}

// 获取相关推荐
const fetchRecommendations = async () => {
  try {
    // 获取热门文章作为推荐
    const response = await request.get('/posts/type/article', {
      params: {
        pageNum: 1,
        pageSize: 5
      }
    })
    
    if (response.code === 200) {
      // 过滤掉当前列表中的内容，避免重复
      const currentPostIds = posts.value.map(post => post.id)
      recommendations.value = (response.data.list || [])
        .filter(post => !currentPostIds.includes(post.id))
        .slice(0, 3) // 只显示3条推荐
    } else {
      recommendations.value = []
    }
  } catch (error) {
    console.error('获取推荐失败:', error)
    recommendations.value = []
  }
}

// 获取话题列表
const fetchTopics = async () => {
  try {
    // TODO: 替换为实际的话题API调用
    // const response = await request.get('/topics')
    // if (response.code === 200) {
    //   topics.value = response.data.list
    // } else {
    //   ElMessage.error(response.message || '获取话题列表失败')
    // }
    
    // 临时使用模拟数据，直到话题API实现
    topics.value = [
      { id: 1, name: 'Vue.js' },
      { id: 2, name: 'Spring Boot' },
      { id: 3, name: 'MyBatis-Plus' },
      { id: 4, name: '前端开发' },
      { id: 5, name: '后端开发' }
    ]
  } catch (error) {
    console.error('获取话题列表失败:', error)
    ElMessage.error('获取话题列表失败')
  }
}

// 处理话题筛选变化
const handleTopicChange = () => {
  currentPage.value = 1
  fetchArticleList()
}

// 处理分页变化
const handlePageChange = (page) => {
  currentPage.value = page
  fetchArticleList()
}

// 处理页面大小变化
const handleSizeChange = (size) => {
  pageSize.value = size
  currentPage.value = 1
  fetchArticleList()
}

// 处理文章点击
const handlePostClick = (postId) => {
  console.log('点击文章:', postId)
}

// 处理推荐点击
const handleRecommendationClick = (post) => {
  // 根据类型跳转到对应的详情页面
  if (post.type === 'question') {
    window.open(`/app/qa/${post.id}`, '_blank')
  } else if (post.type === 'article') {
    window.open(`/app/articles/${post.id}`, '_blank')
  }
}

// 显示编辑器
const showPublishEditor = () => {
  if (!userStore.user) {
    ElMessage.warning('请先登录')
    return
  }
  showEditor.value = true
}

// 关闭编辑器
const handleCloseEditor = () => {
  showEditor.value = false
}

// 使用编辑器发布文章
const handlePublishWithEditor = async (editorData) => {
  if (!editorData.title.trim()) {
    ElMessage.warning('请输入文章标题')
    return
  }
  if (!editorData.content.trim()) {
    ElMessage.warning('请输入文章内容')
    return
  }

  publishing.value = true
  try {
    const response = await request.post('/post', {
      title: editorData.title,
      content: editorData.content,
      type: 'article',
      topicId: editorData.topicId
    })
    
    if (response.code === 200) {
      ElMessage.success('文章发布成功')
      showEditor.value = false
      fetchArticleList() // 刷新列表
    } else {
      ElMessage.error(response.message || '发布文章失败')
    }
  } catch (error) {
    console.error('发布文章失败:', error)
    ElMessage.error('发布文章失败')
  } finally {
    publishing.value = false
  }
}

// 初始化数据
onMounted(() => {
  fetchTopics()
  fetchArticleList()
})
</script>

<style scoped>
.article-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  background: #f8f9fa;
  min-height: calc(100vh - 120px);
}

.page-header {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
  margin-bottom: 20px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.page-title {
  margin: 0;
  font-size: 22px;
  font-weight: 600;
  color: #303133;
}

.filter-section {
  display: flex;
  align-items: center;
  gap: 12px;
}

.article-list {
  margin-bottom: 24px;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  margin-top: 24px;
  background: #fff;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.06);
}

.fixed-write-button {
  position: fixed;
  bottom: 48px;
  right: 48px;
  z-index: 1000;
  box-shadow: 0 2px 12px rgba(0,0,0,0.15);
  font-size: 16px;
  padding: 12px 24px;
}
</style>