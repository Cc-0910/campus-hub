<template>
  <div class="qa-container">
    <div class="qa-header">
      <h1>问答</h1>
    </div>
    
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
      title="发布问题"
      width="60%"
      :before-close="handleCloseEditor"
    >
      <CommonEditor
        :show-topic-selector="true"
        :show-title-input="true"
        :show-comment-type="false"
        :topics="topics"
        :title-placeholder="'请输入问题标题'"
        :content-placeholder="'请输入问题详情...'"
        :submit-button-text="'发布问题'"
        @submit="handlePublishWithEditor"
        @cancel="handleCloseEditor"
      />
    </el-dialog>
    
    <div class="qa-list">
      <el-skeleton 
        v-if="loading" 
        :rows="4" 
        animated 
        class="qa-item-skeleton"
      />
      
      <div v-else>
        <div 
          v-for="question in questions" 
          :key="question.id" 
          class="qa-item"
        >
          <div class="qa-content">
            <h3 class="qa-title">
              <router-link :to="`/app/qa/${question.id}`">
                {{ question.title }}
              </router-link>
            </h3>
            <p class="qa-summary">{{ truncateText(question.content || '') }}</p>
            
            <div class="qa-meta">
              <div class="author-info">
                <el-avatar 
                  :size="24" 
                  :src="question.author?.avatar || defaultAvatar" 
                  class="author-avatar"
                />
                <span class="author-name">{{ question.author?.nickname || question.author || '匿名用户' }}</span>
              </div>
              
              <div class="qa-stats">
                <span class="stat-item">
                  <i class="el-icon-chat-line-round"></i>
                  {{ question.commentCount || question.answerCount || 0 }} 回答
                </span>
                <span class="stat-item">
                  <i class="el-icon-view"></i>
                  {{ question.viewCount || 0 }} 浏览
                </span>
              </div>
            </div>
          </div>
        </div>
        
        <div v-if="questions.length === 0" class="no-data">
          暂无问答内容
        </div>
      </div>
    </div>
    
    <!-- 分页 -->
    <el-pagination
      v-if="total > 0"
      class="pagination"
      :current-page="currentPage"
      :page-size="pageSize"
      :total="total"
      :page-sizes="[10, 20, 50]"
      layout="total, sizes, prev, pager, next, jumper"
      @size-change="handleSizeChange"
      @current-change="handleCurrentChange"
    />
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getQuestions } from '@/api/post'
import { ElMessage } from 'element-plus'
import { usePostStore, type Post } from '@/stores/post'
import { useUserStore } from '@/stores/user.js'
import CommonEditor from '@/components/CommonEditor.vue'
import { Edit } from '@element-plus/icons-vue'
import request from '@/utils/request'

// 截取文本前n个字符的函数
const truncateText = (text: string, maxLength: number = 15): string => {
  if (!text) return ''
  // 去除HTML标签
  const plainText = text.replace(/<[^>]*>/g, '')
  // 截取指定长度并添加省略号
  return plainText.length > maxLength ? plainText.substring(0, maxLength) + '...' : plainText
}

const questions = ref<Post[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

const postStore = usePostStore()
const userStore = useUserStore()

// 编辑器相关
const showEditor = ref(false)
const topics = ref<Array<{ id: number; name: string }>>([])

// 获取话题列表
const fetchTopics = async () => {
  try {
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

const fetchQuestions = async () => {
  try {
    loading.value = true
    const response = await getQuestions({
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    
    // 检查返回数据结构是否符合预期
    if (response && response.data && response.data.list && Array.isArray(response.data.list)) {
      questions.value = response.data.list
      total.value = response.data.total || 0
      
      // 缓存帖子列表
      postStore.cachePosts(questions.value)
    } else {
      // 数据结构不符合预期，显示空列表
      questions.value = []
      total.value = 0
      ElMessage.error('获取问答列表失败：数据结构不符合预期')
    }
  } catch (error) {
    console.error('获取问答列表失败:', error)
    ElMessage.error('获取问答列表失败')
    
    // 发生错误时显示空列表
    questions.value = []
    total.value = 0
  } finally {
    loading.value = false
  }
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchQuestions()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchQuestions()
}

// 编辑器相关方法
const handleCloseEditor = () => {
  showEditor.value = false
}

// 使用编辑器发布问题
const handlePublishWithEditor = async (editorData: any) => {
  if (!editorData.title.trim()) {
    ElMessage.warning('请输入问题标题')
    return
  }
  if (!editorData.content.trim()) {
    ElMessage.warning('请输入问题内容')
    return
  }

  try {
    const response = await request.post('/api/post', {
      title: editorData.title,
      content: editorData.content,
      type: 'question',
      topicId: editorData.topicId
    })
    
    if (response && response.code === 200) {
      ElMessage.success('问答发布成功')
      showEditor.value = false
      fetchQuestions() // 刷新列表
    } else {
      ElMessage.error(response?.message || '发布问答失败')
    }
  } catch (error) {
    console.error('发布问题失败:', error)
    ElMessage.error('发布问题失败')
  }
}

onMounted(() => {
  fetchQuestions()
  fetchTopics()
})
</script>

<style scoped>
.qa-container {
  padding: 20px;
  background: white;
  border-radius: 16px !important;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.qa-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.qa-header h1 {
  margin: 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.qa-item {
  padding: 15px 0;
  border-bottom: 1px solid #f4f4f5;
}

.qa-item:last-child {
  border-bottom: none;
}

.qa-title {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
}

.qa-title a {
  color: #303133;
  text-decoration: none;
}

.qa-title a:hover {
  color: #409eff;
}

.qa-summary {
  margin: 0 0 12px 0;
  color: #606266;
  font-size: 14px;
  line-height: 1.5;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.qa-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 12px;
  color: #909399;
}

.author-info {
  display: flex;
  align-items: center;
}

.author-avatar {
  margin-right: 8px;
}

.author-name {
  color: #606266;
}

.qa-stats {
  display: flex;
  gap: 15px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 3px;
}

.no-data {
  text-align: center;
  padding: 40px 0;
  color: #909399;
  font-size: 14px;
}

.pagination {
  margin-top: 20px;
  text-align: center;
}

.qa-item-skeleton {
  margin-bottom: 20px;
}

.fixed-write-button {
  position: fixed;
  bottom: 48px;
  right: 48px;
  z-index: 9999;
  box-shadow: 0 4px 20px rgba(0,0,0,0.2);
  font-size: 16px;
  padding: 0;
}
</style>