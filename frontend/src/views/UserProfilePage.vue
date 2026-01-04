<template>
  <div class="user-profile-page">
    <!-- 头部背景图 -->
    <div class="header-bg" :style="{ backgroundImage: `url(${userInfo.avatar || '/default-bg.jpg'})` }"></div>

    <div class="profile-container">
      <div class="avatar-section">
        <img :src="userInfo.avatar || defaultAvatar" alt="Avatar" class="avatar">
        <h1>{{ userInfo.nickname }}</h1>
      </div>

      <div class="bio-section">
        <div v-if="userInfo.role" class="tag">{{ userInfo.role }}</div>
        <p>{{ userInfo.bio || userInfo.signature }}</p>
        <p><span>学校：</span>{{ userInfo.school }}</p>
        <p><span>地点：</span>{{ userInfo.location }}</p>
        <p><span>兴趣：</span>{{ userInfo.interests }}</p>
      </div>

      <div class="stats">
        <span>{{ userInfo.followerCount || userInfo.fansCount || 0 }} 粉丝</span>
        <span>{{ userInfo.followingCount || 0 }} 关注</span>
        <span>{{ userInfo.articleCount || 0 }} 文章</span>
      </div>

      <!-- 操作按钮 -->
      <div class="action-buttons">
        <el-button 
          v-if="!userInfo.isMe"
          :type="userInfo.isFollowing ? 'success' : 'primary'"
          @click="handleFollow"
        >
          <el-icon v-if="userInfo.isFollowing"><Check /></el-icon>
          <el-icon v-else><Plus /></el-icon>
          {{ userInfo.isFollowing ? '已关注' : '关注' }}
        </el-button>
        
        <el-button 
          v-else
          type="primary"
          @click="editProfile"
        >
          <el-icon><Edit /></el-icon>
          编辑资料
        </el-button>
      </div>

      <!-- 标签页 -->
      <el-tabs v-model="activeTab" class="profile-tabs">
        <el-tab-pane label="文章" name="articles">
          <div class="tab-content">
            <ArticleList :articles="articles" />
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="问答" name="questions">
          <div class="tab-content">
            <p class="content-placeholder">暂无问答</p>
          </div>
        </el-tab-pane>
        
        <el-tab-pane label="话题" name="topics">
          <div class="tab-content">
            <p class="content-placeholder">暂无话题</p>
          </div>
        </el-tab-pane>
      </el-tabs>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Edit, Plus, Check } from '@element-plus/icons-vue'
import request from '@/utils/request'
import ArticleList from '@/components/ArticleList.vue'

const route = useRoute()
const userId = route.params.id

const userInfo = ref({})
const articles = ref([])
const activeTab = ref('articles')

const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

onMounted(async () => {
  try {
    // 获取用户详情
    const res = await request({
      url: `/api/user/profile/${userId}`,
      method: 'GET'
    })
    
    if (res.code === 200) {
      userInfo.value = res.data
    } else {
      ElMessage.error(res.msg || '获取用户信息失败')
      userInfo.value = null
    }

    // 获取用户文章
    const artRes = await request({
      url: `/api/user/${userId}/articles`,
      method: 'GET'
    })
    
    if (artRes.code === 200) {
      articles.value = artRes.data
    }
  } catch (error) {
    console.error('加载用户数据失败:', error)
    ElMessage.error('加载用户数据失败')
  }
})

// 关注/取消关注
const handleFollow = async () => {
  try {
    if (userInfo.value.isFollowing) {
      // 取消关注
      const response = await request({
        url: `/api/user/follow/${userInfo.value.id}`,
        method: 'DELETE'
      })
      
      if (response.code === 200) {
        ElMessage.success('已取消关注')
        userInfo.value.isFollowing = false
        userInfo.value.followerCount = (userInfo.value.followerCount || userInfo.value.fansCount || 0) - 1
      } else {
        ElMessage.error(response.msg || '取消关注失败')
      }
    } else {
      // 关注
      const response = await request({
        url: `/api/user/follow/${userInfo.value.id}`,
        method: 'POST'
      })
      
      if (response.code === 200) {
        ElMessage.success('关注成功')
        userInfo.value.isFollowing = true
        userInfo.value.followerCount = (userInfo.value.followerCount || userInfo.value.fansCount || 0) + 1
      } else {
        ElMessage.error(response.msg || '关注失败')
      }
    }
  } catch (error) {
    console.error('关注操作失败:', error)
    ElMessage.error('操作失败')
  }
}

// 编辑资料
const editProfile = () => {
  // TODO: 打开编辑资料弹窗或跳转到编辑页面
  ElMessage.info('编辑资料功能暂未实现')
}
</script>

<style scoped>
.user-profile-page {
  background: #f5f7fa;
  min-height: 100vh;
  padding-bottom: 40px;
}

.header-bg {
  height: 300px;
  background-size: cover;
  background-position: center;
  background-color: var(--campus-primary);
  position: relative;
}

.header-bg::after {
  content: '';
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: linear-gradient(135deg, rgba(64, 158, 255, 0.8) 0%, rgba(102, 177, 255, 0.6) 100%);
}

.profile-container {
  max-width: 800px;
  margin: -60px auto 0;
  padding: 0 20px;
  position: relative;
  z-index: 10;
}

.avatar-section {
  text-align: center;
  margin-bottom: 30px;
  background: white;
  border-radius: 16px;
  padding: 30px;
  box-shadow: var(--campus-shadow);
}

.avatar {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  border: 4px solid white;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
  margin-bottom: 16px;
}

.avatar-section h1 {
  font-size: 28px;
  font-weight: 600;
  color: var(--campus-text-primary);
  margin: 0;
  line-height: 1.4;
}

.bio-section {
  background: white;
  border-radius: 16px;
  padding: 24px;
  margin-bottom: 20px;
  box-shadow: var(--campus-shadow);
}

.tag {
  display: inline-block;
  background: var(--campus-primary);
  color: white;
  padding: 4px 12px;
  border-radius: 16px;
  font-size: 12px;
  margin-bottom: 12px;
}

.bio-section p {
  margin: 8px 0;
  color: var(--campus-text-secondary);
  line-height: 1.6;
}

.bio-section span {
  color: var(--campus-text-primary);
  font-weight: 500;
}

.stats {
  background: white;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 20px;
  box-shadow: var(--campus-shadow);
  display: flex;
  justify-content: space-around;
  text-align: center;
}

.stats span {
  font-size: 16px;
  color: var(--campus-text-secondary);
  font-weight: 500;
}

.action-buttons {
  text-align: center;
  margin-bottom: 30px;
}

.profile-tabs {
  background: white;
  border-radius: 16px;
  box-shadow: var(--campus-shadow);
  overflow: hidden;
}

.tab-content {
  padding: 20px;
}

.content-placeholder {
  text-align: center;
  color: #999;
  font-size: 14px;
  margin: 40px 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .profile-container {
    padding: 0 10px;
  }
  
  .avatar-section {
    padding: 20px;
  }
  
  .bio-section {
    padding: 16px;
  }
  
  .stats {
    flex-direction: column;
    gap: 12px;
  }
}
</style>