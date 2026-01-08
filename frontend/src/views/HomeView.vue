<template>
  <div class="home-view">
    <!-- 顶部蓝色欢迎横幅 -->
    <el-card class="banner-card">
      <div class="banner-content">
        <h1>欢迎来到校园Hub</h1>
        <p>发现校园生活的精彩话题与问答</p>
      </div>
    </el-card>

    <!-- 内容区域 -->
    <el-row :gutter="20" class="content-row">
      <!-- 左侧：热门话题 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>热门话题</span>
            </div>
          </template>
          <div class="card-content">
            <p>这里将展示热门话题内容</p>
            <ul>
              <li>话题一：校园生活分享</li>
              <li>话题二：学习经验交流</li>
              <li>话题三：社团活动讨论</li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：最新问答 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>最新问答</span>
            </div>
          </template>
          <div class="card-content">
            <el-skeleton v-if="loading" :rows="3" animated />
            <ul v-else-if="latestQuestions.length > 0">
              <li v-for="question in latestQuestions" :key="question.id">
                <router-link :to="`/app/qa/${question.id}`" class="post-link">
                  {{ question.title }}
                </router-link>
              </li>
            </ul>
            <p v-else>暂无最新问答</p>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 中间内容区域 -->
    <el-row :gutter="20" class="content-row">
      <!-- 最热问答 -->
      <el-col :xs="24" :sm="12" :md="8" :lg="8">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>最热问答</span>
            </div>
          </template>
          <div class="card-content">
            <el-skeleton v-if="loading" :rows="3" animated />
            <ul v-else-if="hotQuestions.length > 0">
              <li v-for="question in hotQuestions" :key="question.id">
                <router-link :to="`/app/qa/${question.id}`" class="post-link">
                  {{ question.title }}
                </router-link>
              </li>
            </ul>
            <p v-else>暂无最热问答</p>
          </div>
        </el-card>
      </el-col>
      
      <!-- 最新文章 -->
      <el-col :xs="24" :sm="12" :md="8" :lg="8">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>最新文章</span>
            </div>
          </template>
          <div class="card-content">
            <el-skeleton v-if="loading" :rows="3" animated />
            <ul v-else-if="latestArticles.length > 0">
              <li v-for="article in latestArticles" :key="article.id">
                <router-link :to="`/app/article/${article.id}`" class="post-link">
                  {{ article.title }}
                </router-link>
              </li>
            </ul>
            <p v-else>暂无最新文章</p>
          </div>
        </el-card>
      </el-col>
      
      <!-- 最热文章 -->
      <el-col :xs="24" :sm="24" :md="8" :lg="8">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>最热文章</span>
            </div>
          </template>
          <div class="card-content">
            <el-skeleton v-if="loading" :rows="3" animated />
            <ul v-else-if="hotArticles.length > 0">
              <li v-for="article in hotArticles" :key="article.id">
                <router-link :to="`/app/article/${article.id}`" class="post-link">
                  {{ article.title }}
                </router-link>
              </li>
            </ul>
            <p v-else>暂无最热文章</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { getPosts } from '@/api/post'
import { ElMessage } from 'element-plus'
import type { Post } from '@/stores/post'

// 定义数据数组
const latestQuestions = ref<Post[]>([])
const hotQuestions = ref<Post[]>([])
const latestArticles = ref<Post[]>([])
const hotArticles = ref<Post[]>([])

// 加载状态
const loading = ref<boolean>(false)

// 获取最新问答
const fetchLatestQuestions = async () => {
  try {
    const response = await getPosts({
      pageNum: 1,
      pageSize: 5,
      type: 'question',
      sort: 'new'
    })
    if (response && response.data && response.data.list) {
      latestQuestions.value = response.data.list
    }
  } catch (error) {
    console.error('获取最新问答失败:', error)
    ElMessage.error('获取最新问答失败')
  }
}

// 获取最热问答
const fetchHotQuestions = async () => {
  try {
    const response = await getPosts({
      pageNum: 1,
      pageSize: 5,
      type: 'question',
      sort: 'hot'
    })
    if (response && response.data && response.data.list) {
      hotQuestions.value = response.data.list
    }
  } catch (error) {
    console.error('获取最热问答失败:', error)
    ElMessage.error('获取最热问答失败')
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
    ElMessage.error('获取最新文章失败')
  }
}

// 获取最热文章
const fetchHotArticles = async () => {
  try {
    const response = await getPosts({
      pageNum: 1,
      pageSize: 5,
      type: 'article',
      sort: 'hot'
    })
    if (response && response.data && response.data.list) {
      hotArticles.value = response.data.list
    }
  } catch (error) {
    console.error('获取最热文章失败:', error)
    ElMessage.error('获取最热文章失败')
  }
}

// 页面加载时获取数据
onMounted(async () => {
  loading.value = true
  // 并行调用四个API
  await Promise.all([
    fetchLatestQuestions(),
    fetchHotQuestions(),
    fetchLatestArticles(),
    fetchHotArticles()
  ])
  loading.value = false
})
</script>

<style scoped>
.home-view {
  background-color: #f0f2f5;
  padding: 20px;
  min-height: calc(100vh - 40px);
}

.banner-card {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #409EFF, #66b1ff);
  color: white;
  border: none;
  border-radius: 16px !important; /* 统一圆角标准 */
  overflow: hidden; /* 防止内部元素溢出圆角 */
}

.banner-content {
  text-align: center;
  padding: 20px; /* 确保内容不贴边 */
}

.banner-content h1 {
  margin: 0 0 10px 0;
  font-size: 28px;
}

.banner-content p {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.content-row {
  margin-bottom: 20px;
}

.content-card {
  margin-bottom: 20px;
  height: 100%;
  border-radius: 16px !important; /* 统一圆角标准 */
  overflow: hidden; /* 防止内部元素溢出圆角 */
}

.card-header {
  font-weight: bold;
  font-size: 16px;
  border-radius: 15px 15px 0 0; /* 头部圆角，底部由卡片整体控制 */
}

.card-content {
  min-height: 120px;
  border-radius: 0 0 15px 15px; /* 底部圆角，顶部由头部或卡片控制 */
}

.card-content ul {
  padding-left: 20px;
  margin: 10px 0;
}

.card-content li {
  margin-bottom: 8px;
  border-radius: 8px; /* 列表项也使用小圆角 */
}
</style>