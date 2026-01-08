<template>
  <div class="home-dashboard">


    <el-row :gutter="20" class="section-row">
      <el-col :span="24">
        <el-card class="box-card topic-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span><el-icon><Collection /></el-icon> 推荐话题</span>
              <el-button class="button" text @click="router.push('/app/topics')">更多</el-button>
            </div>
          </template>
          <div class="topic-list">
            <el-tag 
              v-for="topic in recommendedTopics" 
              :key="topic.id" 
              class="topic-item" 
              effect="light" 
              round 
              @click="goToDetail('topic', topic.id)"
            >
              # {{ topic.title }}
            </el-tag>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="section-row">
      <el-col :xs="24" :sm="12" :md="12">
        <el-card class="box-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span><el-icon><ChatLineRound /></el-icon> 最新问答</span>
              <el-button class="button" text @click="router.push('/app/qa')">更多</el-button>
            </div>
          </template>
          <ul class="text-list">
            <li v-for="qa in latestQA" :key="qa.id" @click="goToDetail(qa.type, qa.id)">
              <span class="list-title">{{ qa.title }}</span>
              <span class="list-time">{{ qa.createTime }}</span>
            </li>
          </ul>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="12">
        <el-card class="box-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="red-icon"><el-icon><HotWater /></el-icon> 最热问答</span>
              <el-button class="button" text @click="router.push('/app/qa?sort=hot')">更多</el-button>
            </div>
          </template>
          <ul class="text-list">
            <li v-for="qa in hottestQA" :key="qa.id" @click="goToDetail(qa.type, qa.id)">
              <span class="list-title">{{ qa.title }}</span>
              <span class="list-meta">🔥 {{ qa.viewCount }}</span>
            </li>
          </ul>
        </el-card>
      </el-col>
    </el-row>

    <el-row :gutter="20" class="section-row">
      <el-col :xs="24" :sm="12" :md="12">
        <el-card class="box-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span><el-icon><Document /></el-icon> 最新文章</span>
              <el-button class="button" text @click="router.push('/app/articles')">更多</el-button>
            </div>
          </template>
          <ul class="text-list">
            <li v-for="article in latestArticles" :key="article.id" @click="goToDetail(article.type, article.id)">
              <span class="list-title">{{ article.title }}</span>
              <span class="list-time">{{ article.createTime }}</span>
            </li>
          </ul>
        </el-card>
      </el-col>

      <el-col :xs="24" :sm="12" :md="12">
        <el-card class="box-card" shadow="hover">
          <template #header>
            <div class="card-header">
              <span class="red-icon"><el-icon><Trophy /></el-icon> 最热文章</span>
              <el-button class="button" text @click="router.push('/app/articles?sort=hot')">更多</el-button>
            </div>
          </template>
          <ul class="text-list">
            <li v-for="article in hottestArticles" :key="article.id" @click="goToDetail(article.type, article.id)">
              <span class="list-title">{{ article.title }}</span>
              <span class="list-meta">🔥 {{ article.viewCount }}</span>
            </li>
          </ul>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { 
  Collection, 
  ChatLineRound, 
  Document, 
  HotWater,
  Trophy
} from '@element-plus/icons-vue'
import { getPosts } from '@/api/post'
import { ElMessage } from 'element-plus'

const router = useRouter()

const goToDetail = (type, id) => { // 简单的路由跳转逻辑，后续可根据实际路由调整
  if (type === 'topic') {
    router.push(`/app/topics/${id}`)
  } else if (type === 'question') {
    router.push(`/app/qa/${id}`)
  } else {
    router.push(`/app/${type}/${id}`)
  }
}

// 初始化数据数组
const recommendedTopics = ref([
  { id: 1, title: '校园生活' },
  { id: 2, title: '学术交流' },
  { id: 3, title: '求职招聘' },
  { id: 4, title: '美食分享' },
  { id: 5, title: '体育健身' },
  { id: 6, title: '二手交易' }
])

const latestQA = ref([])
const hottestQA = ref([])
const latestArticles = ref([])
const hottestArticles = ref([])

// 获取最新问答
const fetchLatestQA = async () => {
  try {
    const response = await getPosts({
      pageNum: 1,
      pageSize: 5,
      type: 'question',
      sort: 'new'
    })
    if (response && response.data && response.data.list) {
      latestQA.value = response.data.list
    }
  } catch (error) {
    console.error('获取最新问答失败:', error)
  }
}

// 获取最热问答
const fetchHottestQA = async () => {
  try {
    const response = await getPosts({
      pageNum: 1,
      pageSize: 5,
      type: 'question',
      sort: 'hot'
    })
    if (response && response.data && response.data.list) {
      hottestQA.value = response.data.list
    }
  } catch (error) {
    console.error('获取最热问答失败:', error)
  }
}

// 获取最新文章
const fetchLatestArticles = async () => {
  try {
    const response = await getPosts({
      pageNum: 1,
      pageSize: 5,
      type: 'article',
      sort: 'new'
    })
    if (response && response.data && response.data.list) {
      latestArticles.value = response.data.list
    }
  } catch (error) {
    console.error('获取最新文章失败:', error)
  }
}

// 获取最热文章
const fetchHottestArticles = async () => {
  try {
    const response = await getPosts({
      pageNum: 1,
      pageSize: 5,
      type: 'article',
      sort: 'hot'
    })
    if (response && response.data && response.data.list) {
      hottestArticles.value = response.data.list
    }
  } catch (error) {
    console.error('获取最热文章失败:', error)
  }
}

// 页面加载时获取数据
onMounted(async () => {
  // 并行调用四个API
  await Promise.all([
    fetchLatestQA(),
    fetchHottestQA(),
    fetchLatestArticles(),
    fetchHottestArticles()
  ])
})
</script>

<style scoped>
.welcome-banner {
  background: linear-gradient(135deg, #409EFF 0%, #3a8ee6 100%);
  color: white;
  padding: 30px 40px;
  border-radius: 8px;
  margin-bottom: 20px;
  text-align: center;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}
.welcome-banner h2 {
  margin: 0 0 10px 0;
  font-size: 28px;
}
.welcome-banner p {
  margin: 0;
  opacity: 0.9;
  font-size: 16px;
}

.section-row {
  margin-bottom: 20px;
}

.box-card {
  height: 100%;
  border-radius: 8px;
  border: none; /* 半透明白色背景，配合毛玻璃效果 */
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-header span {
  font-weight: 600;
  color: #303133;
}

.card-header .red-icon {
  color: #f56c6c;
}

.topic-list {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
}

.topic-item {
  cursor: pointer;
  transition: all 0.3s ease;
}

.topic-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 8px rgba(0,0,0,0.1);
}

.text-list {
  list-style: none;
  padding: 0;
  margin: 0;
}

.text-list li {
  padding: 8px 0;
  border-bottom: 1px solid #eee;
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s ease;
}

.text-list li:last-child {
  border-bottom: none;
}

.text-list li:hover {
  background-color: #f5f7fa;
  border-radius: 4px;
  padding-left: 8px;
  padding-right: 8px;
}

.list-title {
  flex: 1;
  color: #303133;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.list-time, .list-meta {
  color: #909399;
  font-size: 14px;
  white-space: nowrap;
}

.topic-card {
  margin-bottom: 20px;
}
</style>