<template>
  <div class="qa-container">
    <div class="qa-header">
      <h1>问答</h1>
    </div>
    
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
            <p class="qa-summary" v-html="question.summary || question.content"></p>
            
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

const questions = ref<Post[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

const postStore = usePostStore()

// 生成随机问答列表的函数
const generateRandomQuestions = () => {
  const titles = [
    'Vue 3组合式API的最佳实践是什么？',
    '如何解决前端跨域问题？',
    'React和Vue的主要区别是什么？',
    '如何优化Vue应用的性能？',
    'TypeScript在前端开发中的优势？',
    '如何实现响应式布局？',
    '什么是单页应用(SPA)？',
    '如何使用Element Plus组件库？',
    '前端路由的原理是什么？',
    '如何实现前端数据持久化？'
  ]
  
  const contents = [
    '我正在学习Vue 3的组合式API，想了解一下在实际项目中的最佳实践有哪些...',
    '在开发前端项目时遇到了跨域问题，请问有哪些解决方案？...',
    '作为一名前端新手，我想知道React和Vue这两个框架的主要区别是什么...',
    '我的Vue应用在数据量大的时候运行比较慢，有什么优化方法吗？...',
    '最近开始学习TypeScript，想了解它在前端开发中有哪些优势...',
    '如何实现一个适配不同屏幕尺寸的响应式布局？...',
    '什么是单页应用(SPA)？它与传统的多页应用有什么区别？...',
    '刚接触Element Plus组件库，想知道如何快速上手使用它...',
    '前端路由的原理是什么？如何实现页面之间的跳转？...',
    '在前端开发中，有哪些方法可以实现数据的持久化存储？...'
  ]
  
  const userNames = ['前端新手', '编程爱好者', '技术探索者', 'Vue开发者', 'React工程师', '全栈开发者', '学生党', '码农一枚']
  
  const randomQuestions = []
  const startId = (currentPage.value - 1) * pageSize.value + 1
  
  for (let i = 0; i < pageSize.value; i++) {
    const id = startId + i
    randomQuestions.push({
      id,
      title: titles[i % titles.length] + ` - ${Math.floor(Math.random() * 1000)}`,
      content: contents[i % contents.length],
      summary: contents[i % contents.length].substring(0, 100) + '...',
      author: {
        nickname: userNames[Math.floor(Math.random() * userNames.length)],
        avatar: defaultAvatar
      },
      commentCount: Math.floor(Math.random() * 50),
      answerCount: Math.floor(Math.random() * 50),
      viewCount: Math.floor(Math.random() * 1000),
      createTime: new Date(Date.now() - Math.random() * 7 * 24 * 60 * 60 * 1000).toISOString()
    })
  }
  
  return randomQuestions
}

const fetchQuestions = async () => {
  try {
    loading.value = true
    const response = await getQuestions({
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    
    // 检查返回数据结构是否符合预期
    if (response.data && Array.isArray(response.data.list)) {
      questions.value = response.data.list
      total.value = response.data.total || 0
      
      // 缓存帖子列表
      postStore.cachePosts(questions.value)
    } else {
      // 数据结构不符合预期，使用随机数据
      throw new Error('返回数据结构不符合预期')
    }
  } catch (error) {
    console.error('获取问答列表失败:', error)
    ElMessage.error('获取问答列表失败，显示随机内容')
    
    // 使用随机数据作为降级方案
    const randomQuestions = generateRandomQuestions()
    questions.value = randomQuestions
    total.value = 100 // 设置一个合理的总条数
    
    // 缓存随机数据
    postStore.cachePosts(randomQuestions)
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

onMounted(() => {
  fetchQuestions()
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
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
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
</style>