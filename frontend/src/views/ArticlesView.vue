<template>
  <div class="articles-container">
    <div class="articles-header">
      <h1>文章</h1>
    </div>
    
    <div class="articles-list">
      <el-skeleton 
        v-if="loading" 
        :rows="4" 
        animated 
        class="article-item-skeleton"
      />
      
      <div v-else>
        <div 
          v-for="article in articles" 
          :key="article.id" 
          class="article-item"
        >
          <div class="article-content">
            <h3 class="article-title">
              <router-link :to="`/app/articles/${article.id}`">
                {{ article.title }}
              </router-link>
            </h3>
            <p class="article-summary" v-html="article.summary || article.content"></p>
            
            <div class="article-meta">
              <div class="author-info">
                <el-avatar 
                  :size="24" 
                  :src="article.author?.avatar || defaultAvatar" 
                  class="author-avatar"
                />
                <span class="author-name">{{ article.author?.nickname || article.author || '匿名用户' }}</span>
              </div>
              
              <div class="article-stats">
                <span class="stat-item">
                  <i class="el-icon-chat-line-round"></i>
                  {{ article.commentCount || 0 }} 评论
                </span>
                <span class="stat-item">
                  <i class="el-icon-view"></i>
                  {{ article.viewCount || 0 }} 浏览
                </span>
              </div>
            </div>
          </div>
        </div>
        
        <div v-if="articles.length === 0" class="no-data">
          暂无文章内容
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
import { getArticles } from '@/api/post'
import { ElMessage } from 'element-plus'
import { usePostStore, type Post } from '@/stores/post'

const articles = ref<Post[]>([])
const loading = ref(false)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

const postStore = usePostStore()

// 生成随机文章列表的函数
const generateRandomArticles = () => {
  const titles = [
    '校园生活指南',
    '大学学习经验分享',
    '如何平衡学习与社团活动？',
    '校园美食地图',
    '新生入学必知事项',
    '期末考试复习攻略',
    '校园兼职经验谈',
    '宿舍生活小贴士',
    '校园社团选择指南',
    '大学生时间管理技巧'
  ]
  
  const contents = [
    '作为一名即将毕业的学长，我想分享一些校园生活的经验...',
    '大学学习和高中有很大的不同，以下是我的一些学习经验...',
    '在大学中，很多同学都会面临学习与社团活动的平衡问题...',
    '校园里有很多隐藏的美食，今天我就来为大家介绍一下...',
    '作为新生，刚入学时可能会感到迷茫，以下是一些必知事项...',
    '期末考试即将来临，如何高效复习是每个同学都关心的问题...',
    '在大学期间做兼职不仅可以赚零花钱，还能积累经验...',
    '宿舍是我们在学校的家，如何让宿舍生活更舒适？...',
    '校园里有各种各样的社团，如何选择适合自己的社团？...',
    '时间管理是大学生必备的技能，以下是一些实用的技巧...'
  ]
  
  const userNames = ['校园达人', '学长学姐', '学习委员', '社团主席', '美食爱好者', '时间管理大师', '兼职达人', '宿舍长']
  
  const randomArticles = []
  const startId = (currentPage.value - 1) * pageSize.value + 1
  
  for (let i = 0; i < pageSize.value; i++) {
    const id = startId + i
    randomArticles.push({
      id,
      title: titles[i % titles.length] + ` - ${Math.floor(Math.random() * 1000)}`,
      content: contents[i % contents.length],
      summary: contents[i % contents.length].substring(0, 100) + '...',
      author: {
        nickname: userNames[Math.floor(Math.random() * userNames.length)],
        avatar: defaultAvatar
      },
      commentCount: Math.floor(Math.random() * 50),
      viewCount: Math.floor(Math.random() * 1000),
      createTime: new Date(Date.now() - Math.random() * 7 * 24 * 60 * 60 * 1000).toISOString()
    })
  }
  
  return randomArticles
}

const fetchArticles = async () => {
  try {
    loading.value = true
    const response = await getArticles({
      pageNum: currentPage.value,
      pageSize: pageSize.value
    })
    
    // 检查返回数据结构是否符合预期
    if (response.data && Array.isArray(response.data.list)) {
      articles.value = response.data.list
      total.value = response.data.total || 0
      
      // 缓存帖子列表
      postStore.cachePosts(articles.value)
    } else {
      // 数据结构不符合预期，使用随机数据
      throw new Error('返回数据结构不符合预期')
    }
  } catch (error) {
    console.error('获取文章列表失败:', error)
    ElMessage.error('获取文章列表失败，显示随机内容')
    
    // 使用随机数据作为降级方案
    const randomArticles = generateRandomArticles()
    articles.value = randomArticles
    total.value = 100 // 设置一个合理的总条数
    
    // 缓存随机数据
    postStore.cachePosts(randomArticles)
  } finally {
    loading.value = false
  }
}

const handleSizeChange = (size: number) => {
  pageSize.value = size
  currentPage.value = 1
  fetchArticles()
}

const handleCurrentChange = (page: number) => {
  currentPage.value = page
  fetchArticles()
}

onMounted(() => {
  fetchArticles()
})
</script>

<style scoped>
.articles-container {
  padding: 20px;
  background: white;
  border-radius: 16px !important;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.articles-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.articles-header h1 {
  margin: 0;
  color: #303133;
  font-size: 24px;
  font-weight: 600;
}

.article-item {
  padding: 15px 0;
  border-bottom: 1px solid #f4f4f5;
}

.article-item:last-child {
  border-bottom: none;
}

.article-title {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
}

.article-title a {
  color: #303133;
  text-decoration: none;
}

.article-title a:hover {
  color: #409eff;
}

.article-summary {
  margin: 0 0 12px 0;
  color: #606266;
  font-size: 14px;
  line-height: 1.5;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.article-meta {
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

.article-stats {
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

.article-item-skeleton {
  margin-bottom: 20px;
}
</style>