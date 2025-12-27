<template>
  <div class="profile-card">
    <div v-if="userStore.token" class="profile-content">
      <!-- 上部：头像和昵称 -->
      <div class="profile-header">
        <el-avatar :size="64" :src="userStore.userInfo.avatar || defaultAvatar" class="avatar" />
        <div class="user-info">
          <div class="nickname">{{ userStore.userInfo.nickname || userStore.userInfo.username || '用户' }}</div>
        </div>
      </div>

      <!-- 中部：数据统计 -->
      <div class="stats">
        <div class="stat-item">
          <div class="stat-number">12</div>
          <div class="stat-label">文章</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">156</div>
          <div class="stat-label">关注</div>
        </div>
        <div class="stat-item">
          <div class="stat-number">243</div>
          <div class="stat-label">粉丝</div>
        </div>
      </div>

      <!-- 下部：收藏夹入口 -->
      <div class="collections">
        <div class="collection-item">
          <el-icon><Star /></el-icon>
          <span>收藏</span>
        </div>
        <div class="collection-item">
          <el-icon><Document /></el-icon>
          <span>文章</span>
        </div>
        <div class="collection-item">
          <el-icon><ChatDotRound /></el-icon>
          <span>话题</span>
        </div>
      </div>
    </div>
    
    <div v-else class="login-prompt">
      <p>登录后体验完整功能</p>
      <el-button type="primary" size="small" @click="userStore.openLoginModal('login')">
        立即登录
      </el-button>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { Star, Document, ChatDotRound } from '@element-plus/icons-vue'

const userStore = useUserStore()

const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
</script>

<style scoped>
.profile-card {
  background: #ffffff;
  border-radius: 8px;
  padding: 20px;
  margin: 0 12px 20px;
  border: 1px solid #e6e6e6;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.profile-header {
  text-align: center;
  margin-bottom: 16px;
}

.avatar {
  margin-bottom: 12px;
}

.user-info {
  text-align: center;
}

.nickname {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin-bottom: 4px;
}

.stats {
  display: flex;
  justify-content: space-around;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 1px solid #e6e6e6;
}

.stat-item {
  text-align: center;
}

.stat-number {
  font-size: 18px;
  font-weight: bold;
  color: #409EFF; /* 蓝色数字 */
  margin-bottom: 2px;
}

.stat-label {
  font-size: 12px;
  color: #666;
}

.collections {
  display: flex;
  justify-content: space-around;
}

.collection-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8px;
  border-radius: 8px;
  cursor: pointer;
  transition: background-color 0.3s;
  flex: 1;
  text-align: center;
}

.collection-item:hover {
  background-color: #f5f7fa;
}

.collection-item i, .collection-item span {
  font-size: 12px;
  color: #666;
}

.login-prompt {
  text-align: center;
  padding: 20px 0;
}

.login-prompt p {
  margin: 0 0 15px;
  font-size: 14px;
  color: #666;
}
</style>