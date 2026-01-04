<template>
  <div class="following-view">
    <div class="view-header">
      <h2>关注列表</h2>
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
        <div v-if="followingList.length === 0" class="empty-container">
          <el-empty description="暂无关注" />
        </div>
        
        <div v-else class="user-list">
          <div 
            v-for="user in followingList" 
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
        <div class="pagination-container" v-if="followingList.length > 0">
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
import { getUserFollowing, followUser, unfollowUser } from '@/api/user'

// 默认头像
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

// 路由信息
const route = useRoute()
const router = useRouter()

// 关注列表数据
const followingList = ref([])
const loading = ref(true)
const error = ref('')
const total = ref(0)
const currentPage = ref(1)
const pageSize = ref(10)

// 获取关注列表
const fetchFollowingList = async () => {
  try {
    loading.value = true
    error.value = ''
    
    const userId = route.params.id
    const response = await getUserFollowing(userId, currentPage.value, pageSize.value)
    
    if (response && response.data) {
      // 如果是当前用户自己，则设置关注状态
      followingList.value = (response.data.records || []).map(user => ({
        ...user,
        isFollowing: true // 关注列表中的人都是当前用户已关注的
      }))
      total.value = response.data.total || 0
    }
  } catch (err) {
    error.value = '获取关注列表失败，请稍后再试'
    console.error('Error fetching following list:', err)
  } finally {
    loading.value = false
  }
}

// 处理页码变化
const handlePageChange = (page) => {
  currentPage.value = page
  fetchFollowingList()
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
  fetchFollowingList()
})

// 组件挂载时获取数据
onMounted(() => {
  fetchFollowingList()
})
</script>

<style scoped>
.following-view {
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
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 300px;
}

.user-list {
  margin-bottom: 20px;
}

.user-item {
  display: flex;
  align-items: center;
  border-bottom: 1px solid #f0f0f0;
  padding: 15px 0;
}

.user-item:last-child {
  border-bottom: none;
}

.user-avatar {
  margin-right: 15px;
}

.user-avatar img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  cursor: pointer;
  object-fit: cover;
}

.user-info {
  flex: 1;
}

.user-nickname {
  font-size: 16px;
  font-weight: 600;
  margin-bottom: 5px;
  color: #333;
}

.user-signature {
  font-size: 14px;
  color: #666;
  margin: 0;
  overflow: hidden;
  text-overflow: ellipsis;
  display: -webkit-box;
  -webkit-line-clamp: 1;
  -webkit-box-orient: vertical;
}

.user-actions {
  display: flex;
  align-items: center;
}

.pagination-container {
  display: flex;
  justify-content: center;
  margin-top: 20px;
}
</style>