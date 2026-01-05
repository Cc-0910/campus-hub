<template>
  <div class="user-profile-page">
    <!-- Loading 状态 -->
    <el-skeleton v-if="loading" :rows="5" animated class="loading-panel" />

    <!-- 错误提示 -->
    <el-result
      v-else-if="error"
      icon="error"
      title="加载失败"
      :sub-title="error"
      style="margin: 48px 0"
    />

    <!-- 主体内容（仅数据正确时） -->
    <div v-else class="user-profile-container">
      <!-- 顶部资料卡 -->
      <el-card class="user-card">
        <!-- 封面图 -->
        <div
          class="cover"
          :style="{ backgroundImage: `url(${user.coverImage || defaultCover})` }"
        >
          <div class="cover-overlay" @click="handleCoverClick"></div>
        </div>

        <!-- 用户信息 -->
        <div class="info-section">
          <!-- 头像，视觉重点压住封面下方 -->
          <div class="avatar-wrapper">
            <el-avatar
              :size="120"
              :src="user.avatar || defaultAvatar"
              class="user-avatar"
            >
              {{ user.nickname ? user.nickname.charAt(0).toUpperCase() : '' }}
            </el-avatar>
          </div>

          <!-- 用户名/简介与操作 -->
          <div class="user-info">
            <div class="username">
              {{ user.nickname || '未命名用户' }}
              <span v-if="user.id" class="uid">#{{ user.id }}</span>
            </div>
            <div class="bio">
              {{ user.bio || '这个人很懒，什么都没写' }}
            </div>
            <div class="actions">
              <el-button type="primary" :icon="Edit" @click="handleEditProfile" size="default">
                编辑资料
              </el-button>
              <el-dropdown>
                <el-button type="default" :icon="More" size="default">
                  更多
                </el-button>
                <template #dropdown>
                  <el-dropdown-menu>
                    <el-dropdown-item @click="handleSendMessage">发送消息</el-dropdown-item>
                    <el-dropdown-item @click="handleReport">举报用户</el-dropdown-item>
                  </el-dropdown-menu>
                </template>
              </el-dropdown>
            </div>
            <!-- 关注与粉丝 -->
            <div class="follow-stats">
              <div class="follow-item">
                <span class="follow-count">{{ user.followingCount ?? 0 }}</span>
                <span class="follow-label">关注</span>
              </div>
              <div class="follow-divider">|</div>
              <div class="follow-item">
                <span class="follow-count">{{ user.followerCount ?? 0 }}</span>
                <span class="follow-label">粉丝</span>
              </div>
            </div>
          </div>
        </div>
      </el-card>

      <!-- 内容 Tabs 区 -->
      <el-card class="content-card">
        <el-tabs v-model="activeTab" class="user-tabs">
          <el-tab-pane label="提问" name="questions">
            <el-empty
              v-if="questions.length === 0"
              description="暂无提问"
              style="margin-top: 30px"
            />
            <div v-else class="content-list">
              <div v-for="question in questions" :key="question.id" class="content-item">
                <h4>{{ question.title }}</h4>
                <p class="content-preview">{{ question.content?.slice(0, 100) }}...</p>
                <div class="content-meta">
                  <span>点赞: {{ question.likes ?? 0 }}</span>
                  <span>回复: {{ question.replies ?? 0 }}</span>
                  <span>{{ question.createTime }}</span>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="回答" name="answers">
            <el-empty
              v-if="answers.length === 0"
              description="暂无回答"
              style="margin-top: 30px"
            />
            <div v-else class="content-list">
              <div v-for="answer in answers" :key="answer.id" class="content-item">
                <h4>{{ answer.questionTitle }}</h4>
                <p class="content-preview">{{ answer.content?.slice(0, 100) }}...</p>
                <div class="content-meta">
                  <span>点赞: {{ answer.likes ?? 0 }}</span>
                  <span>{{ answer.createTime }}</span>
                </div>
              </div>
            </div>
          </el-tab-pane>
          <el-tab-pane label="文章" name="articles">
            <el-empty
              v-if="articles.length === 0"
              description="暂无文章"
              style="margin-top: 30px"
            />
            <div v-else class="content-list">
              <div v-for="article in articles" :key="article.id" class="content-item">
                <h4>{{ article.title }}</h4>
                <p class="content-preview">
                  {{ article.summary || article.content?.slice(0, 100) }}...
                </p>
                <div class="content-meta">
                  <span>点赞: {{ article.likes ?? 0 }}</span>
                  <span>浏览: {{ article.views ?? 0 }}</span>
                  <span>{{ article.createTime }}</span>
                </div>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { Edit, More } from '@element-plus/icons-vue'
import { useUserStore } from '@/stores/user'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'

// --- 状态 & 依赖 ---
const userStore = useUserStore()
const route = useRoute()

const loading = ref(false)
const error = ref('')
const activeTab = ref('questions')
const user = ref({})
const questions = ref([])
const answers = ref([])
const articles = ref([])

const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a55bcpng.png'
const defaultCover = 'https://cdn.pixabay.com/photo/1506744038136-46273834b3fb?auto=compress&w=800&q=80'

// --- 方法 ---
const handleCoverClick = () => {
  ElMessage.info('点击封面图，可后续实现更换')
}
const handleEditProfile = () => {
  ElMessage.info('编辑资料，待实现')
}
const handleSendMessage = () => {
  ElMessage.info('发送消息，待实现')
}
const handleReport = () => {
  ElMessage.info('举报用户，待实现')
}

const loadUserData = async () => {
  loading.value = true
  error.value = ''
  // 改成真实 API 请求时请处理 try-catch 和数据结构
  try {
    // ==== MOCK START ====
    const userId = route.params.id
    if (!userId) throw new Error('用户ID缺失')
    await new Promise(r => setTimeout(r, 400))
    user.value = {
      id: userId,
      nickname: 'Cc哈哈哈',
      avatar: userStore.userInfo?.avatar || '',
      coverImage: '',
      followingCount: 15,
      followerCount: 42,
      bio: '这个人很懒，什么都没写'
    }
    questions.value = [
      { id: 1, title: '如何学习前端开发？', content: '我想学习前端开发，但是不知道从哪里开始，有什么好的建议吗？', likes: 5, replies: 3, createTime: '2024-01-15' },
      { id: 2, title: 'Vue3和React哪个更好？', content: '在当前的技术环境下，Vue3和React哪个更适合初学者？', likes: 8, replies: 12, createTime: '2024-01-10' }
    ]
    answers.value = [
      { id: 1, questionTitle: '如何学习前端开发？', content: '建议先从HTML、CSS和JavaScript基础开始，然后学习一个框架...', likes: 15, createTime: '2024-01-16' }
    ]
    articles.value = [
      { id: 1, title: '前端开发入门指南', summary: '本文详细介绍了前端开发的学习路径和必备技能...', likes: 45, views: 120, createTime: '2024-01-20' },
    ]
    // ==== MOCK END ====
  } catch (e) {
    error.value = e?.message || '加载失败'
    user.value = {}
    questions.value = []
    answers.value = []
    articles.value = []
  } finally {
    loading.value = false
  }
}

// 路由变化时重新加载（防页面缓存/切换用户无刷新）
onMounted(loadUserData)
watch(() => route.params.id, loadUserData)
</script>

<style scoped>
.user-profile-page {
  min-height: 100vh;
  background: #f7f8fa;
  padding-top: 30px;
}
.loading-panel {
  max-width: 800px;
  margin: 64px auto 0 auto;
}
.user-profile-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
  padding-top: 0;
}
.user-card {
  position: relative;
  border-radius: 8px;
  overflow: visible; /* 使头像溢出可见 */
  margin-bottom: 20px;
  box-shadow: 0 4px 24px #0001;
}

/* 封面 */
.cover {
  height: 240px;
  background-size: cover;
  background-position: center;
  position: relative;
  cursor: pointer;
}
.cover-overlay {
  position: absolute;
  top: 0; left: 0; right: 0; bottom: 0;
  background: rgba(0,0,0,0.1);
  transition: background 0.3s;
}
.cover-overlay:hover { background: rgba(0,0,0,0.18); }

/* 头像压封面 */
.avatar-wrapper {
  position: absolute;
  top: 180px;  /* 向上覆盖一半 */
  left: 32px;
  z-index: 10;
}
.user-avatar {
  border: 4px solid #fff;
  box-shadow: 0 2px 12px rgba(0,0,0,0.16);
  background: #fdfdfd;
}

/* 右侧信息 */
.info-section {
  position: relative;
  min-height: 110px;
  padding: 32px 32px 24px 0px;
  padding-left: 180px; /* 头像宽+留白 */
}
.user-info {
  padding-top: 12px;
}
.username {
  font-size: 24px;
  font-weight: 600;
  color: #222;
  margin-bottom: 8px;
}
.username .uid {
  font-size: 14px;
  color: #a6a8b3;
  margin-left: 8px;
}
.bio {
  color: #6b778c;
  font-size: 14px;
  margin-bottom: 14px;
}
.actions {
  display: flex;
  gap: 16px;
  margin-bottom: 13px;
}
.follow-stats {
  display: flex;
  align-items: center;
  gap: 22px;
}
.follow-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}
.follow-count {
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}
.follow-label {
  font-size: 14px;
  color: #909399;
  margin-top: 2px;
}
.follow-divider {
  color: #dcdfe6;
  font-weight: 600;
}

/* tab区 */
.content-card {
  border-radius: 8px;
  margin-top: 8px;
}
.user-tabs :deep(.el-tabs__item) {
  font-size: 16px; font-weight: 500;
}
.content-list { padding: 20px 0; }
.content-item {
  padding: 16px 0;
  border-bottom: 1px solid #ebeef5;
}
.content-item:last-child { border-bottom: none; }
.content-item h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
  line-height: 1.4;
}
.content-preview {
  margin: 0 0 8px 0;
  color: #606266;
  line-height: 1.5;
  font-size: 14px;
}
.content-meta {
  display: flex;
  gap: 16px;
  color: #909399;
  font-size: 12px;
}

/* 响应式优化 */
@media (max-width: 768px) {
  .user-profile-container { padding: 10px; }
  .info-section { padding-left: 0; padding-top: 100px; text-align: center; }
  .avatar-wrapper { left: 50%; transform: translateX(-50%); top: 160px; }
  .actions { justify-content: center; }
  .follow-stats { justify-content: center; }
}
</style>
