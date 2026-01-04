<template>
  <div class="followers-view">
    <div class="view-header">
      <h2>粉丝列表</h2>
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
      
      <!-- 用户列表 -->
      <div v-if="!loading && !error">
        <div v-if="followersList.length === 0" class="empty-container">
          <el-empty description="暂无粉丝" />
        </div>
        
        <div v-else class="user-list">
          <div 
            v-for="user in followersList" 
            :key="user.id" 
            class="user-item"
          >
            <div class="user-avatar">
              <img 
                :src="user.avatar || defaultAvatar" 
                @click="goToUserProfile(user.id)"
                alt="用户头像"
              />
            </div>
            
            <div class="user-info">
              <h3 class="user-nickname">{{ user.nickname || user.username }}</h3>
              <p class="user-signature">{{ user.signature || '这个用户很懒，什么都没有留下' }}</p>
            </div>
            
            <div class="user-actions">
              <!-- 如果当前用户不是自己，显示关注/取消关注按钮 -->
              <el-button 
                v-if="!isCurrentUser(user.id)" 
                :type="user.isFollowing ? 'info' : 'primary'" 
                size="small"
                @click="toggleFollow(user)"
              >
                {{ user.isFollowing ? '已关注' : '关注' }}
              </el-button>
              
              <!-- 如果是自己，显示编辑资料按钮 -->
              <el-button 
                v-else 
                size="small"
                @click="goToUserProfile(user.id)"
              >
                查看资料
              </el-button>
            </div>
          </div>
        </div>
        
        <!-- 分页 -->
        <div class="pagination-container" v-if="followersList.length > 0">
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
import { ElSkeleton, ElEmpty, ElAlert, ElPagination, ElButton } from 'element-plus'
import { getUserFollowers, followUser, unfollowUser } from '@/api/user'

// 默认头像
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

// 路由信息
const route = useRoute()
const router = useRouter()

// 粉丝列表数据
const followersList = ref([])
const loading = ref(true)
const error = ref('')
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 获取粉丝列表
const fetchFollowersList = async () => {
  try {
    loading.value = true
    error.value = ''
    
    const userId = route.params.id
    const response = await getUserFollowers(userId, currentPage.value, pageSize.value)
    
    if (response && response.data) {
      // 获取用户数据，并判断是否被当前用户关注
      followersList.value = (response.data.records || []).map(user => ({
        ...user,
        isFollowing: user.isFollowing || false // 关注状态可能需要从后端获取
      }))
      total.value = response.data.total || 0
    }
  } catch (err) {
    error.value = '获取粉丝列表失败，请稍后再试'
    console.error('Error fetching followers list:', err)
  } finally {
    loading.value = false
  }
}

// 处理页码变化
const handlePageChange = (page) => {
  currentPage.value = page
  fetchFollowersList()
}

// 切换关注状态
const toggleFollow = async (user) => {
  try {
    if (user.isFollowing) {
      // 取消关注
      await unfollowUser(user.id)
      user.isFollowing = false
    } else {
      // 关注
      await followUser(user.id)
      user.isFollowing = true
    }
  } catch (err) {
    console.error('Error toggling follow status:', err)
    // 如果操作失败，恢复状态
    user.isFollowing = !user.isFollowing
  }
}

// 检查是否是当前用户
const isCurrentUser = (userId) => {
  // 假设当前用户ID为1，实际项目中应该从用户状态或JWT token中获取
  return userId === 1
}

// 跳转到用户资料页
const goToUserProfile = (userId) => {
  router.push(`/app/user/${userId}`)
}

// 监听路由变化，重新获取数据
watch(() => route.params.id, () => {
  fetchFollowersList()
})

// 组件挂载时获取数据
onMounted(() => {
  fetchFollowersList()
})
</script>

<style scoped>
.followers-view {
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
  padding: 40px 0;
  text-align: center;
}

.user-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.user-item {
  display: flex;
  align-items: center;
  padding: 16px;
  border-radius: 8px;
  border: 1px solid #f0f0f0;
  transition: all 0.3s;
}

.user-item:hover {
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.05);
  border-color: #e4e7ed;
}

.user-avatar {
  width: 50px;
  height: 50px;
  margin-right: 12px;
  flex-shrink: 0;
}

.user-avatar img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  cursor: pointer;
}

.user-info {
  flex: 1;
  min-width: 0;
}

.user-nickname {
  font-size: 16px;
  margin: 0 0 6px 0;
  color: #303133;
  font-weight: 500;
}

.user-signature {
  font-size: 13px;
  margin: 0;
  color: #909399;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.user-actions {
  margin-left: 12px;
}

.pagination-container {
  margin-top: 20px;
  display: flex;
  justify-content: center;
}
</style>