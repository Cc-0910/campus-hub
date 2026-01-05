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
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { 
  Collection, 
  ChatLineRound, 
  Document, 
  HotWater,
  Trophy
} from '@element-plus/icons-vue'

const router = useRouter()

const goToDetail = (type, id) => { // 简单的路由跳转逻辑，后续可根据实际路由调整
  if (type === 'topic') {
    router.push(`/app/topics/${id}`)
  } else {
    router.push(`/app/${type}/${id}`)
  }
}

// --- 模拟数据 (Mock Data) - API Contract Compliant ---
const recommendedTopics = ref([
  { id: 1, title: '校园生活' },
  { id: 2, title: '学术交流' },
  { id: 3, title: '求职招聘' },
  { id: 4, title: '美食分享' },
  { id: 5, title: '体育健身' },
  { id: 6, title: '二手交易' }
])

const latestQA = ref([
  { id: 101, title: '新校区的图书馆几点闭馆？', createTime: '10分钟前', type: 'question' },
  { id: 102, title: '求推荐C++的高质量网课资源', createTime: '30分钟前', type: 'question' },
  { id: 103, title: '有没有周末一起去爬岳麓山的？', createTime: '1小时前', type: 'question' },
  { id: 104, title: '请问奖学金评定的综测分怎么算？', createTime: '2小时前', type: 'question' },
  { id: 105, title: '校园网怎么又断了？？？', createTime: '3小时前', type: 'question' }
])

const hottestQA = ref([
  { id: 201, title: '关于近期食堂涨价的讨论', viewCount: 5230, type: 'question' },
  { id: 202, title: '如何看待今年的保研政策变化？', viewCount: 4102, type: 'question' },
  { id: 203, title: '大一新生入坑指南，学长血泪史', viewCount: 3890, type: 'question' },
  { id: 204, title: '那个窗口的阿姨手真的不抖！', viewCount: 2560, type: 'question' },
  { id: 205, title: '有没有人捡到一个粉色的AirPods？', viewCount: 1200, type: 'question' }
])

const latestArticles = ref([
  { id: 301, title: 'Web前端开发学习路线图(2025版)', createTime: '今天', type: 'article' },
  { id: 302, title: '深度解析 DeepSeek 的技术原理', createTime: '昨天', type: 'article' },
  { id: 303, title: '我的大学四年回顾：迷茫与成长', createTime: '2天前', type: 'article' },
  { id: 304, title: '摄影社春季外拍活动精选照片', createTime: '3天前', type: 'article' },
  { id: 305, title: '校辩论队夺冠战报！', createTime: '1周前', type: 'article' }
])

const hottestArticles = ref([
  { id: 401, title: 'ACM金牌选手的算法笔记分享', viewCount: 9999, type: 'article' },
  { id: 402, title: '中南大学周边美食地图（附攻略）', viewCount: 8848, type: 'article' },
  { id: 403, title: '教你如何优雅地使用校园VPN', viewCount: 6666, type: 'article' },
  { id: 404, title: '那些年我们在图书馆占过的座', viewCount: 5520, type: 'article' },
  { id: 405, title: '毕业季二手物品清理清单', viewCount: 4300, type: 'article' }
])
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