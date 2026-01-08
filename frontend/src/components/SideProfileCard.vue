<template>
  <div class="user-card">
    <div class="avatar-wrapper">
      <img :src="user?.avatar || defaultAvatar" alt="Avatar" class="avatar" @click="goToProfile">
    </div>

    <div class="username">{{ user?.nickname || '用户' }}</div>
    <div class="user-id" v-if="user?.id > 0">ID: {{ user.id }}</div>

    <div class="stats">
      <div class="stat-item" @click="goToArticles">
        <span class="count">0</span>
        <span class="label">文章</span>
      </div>
      <div class="stat-item" @click="goToFollowing">
        <span class="count">{{ user?.followingCount || 0 }}</span>
        <span class="label">关注</span>
      </div>
      <div class="stat-item" @click="goToFollowers">
        <span class="count">{{ user?.followerCount || 0 }}</span>
        <span class="label">粉丝</span>
      </div>
    </div>

    <div class="actions" v-if="user?.id > 0">
      <button class="btn" @click="goToArticles">文章</button>
      <button class="btn" @click="goToCollections">收藏</button>
      <button class="btn" @click="goToTopics">话题</button>
    </div>
  </div>
</template>

<script setup>
import { useRouter } from 'vue-router'

const props = defineProps({
  user: {
    type: Object,
    default: () => ({})
  }
})

const router = useRouter()
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

const goToProfile = () => {
  if (props.user?.id) {
    router.push(`/app/user/${props.user.id}`)
  }
}

const goToArticles = () => {
  if (props.user?.id) {
    router.push(`/app/user/${props.user.id}/articles`)
  }
}

const goToCollections = () => {
  if (props.user?.id) {
    router.push(`/app/user/${props.user.id}/collections`)
  }
}

const goToTopics = () => {
  if (props.user?.id) {
    router.push(`/app/user/${props.user.id}/topics`)
  }
}

const goToFollowing = () => {
  if (props.user?.id) {
    router.push(`/app/user/${props.user.id}/following`)
  }
}

const goToFollowers = () => {
  if (props.user?.id) {
    router.push(`/app/user/${props.user.id}/followers`)
  }
}
</script>

<style scoped>
.user-card {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: var(--campus-border-radius);
  padding: 20px;
  box-shadow: var(--campus-shadow);
  text-align: center;
  transition: all 0.3s ease;
}

.user-card:hover {
  box-shadow: 0 8px 24px rgba(64, 158, 255, 0.15);
  transform: translateY(-2px);
}

.avatar-wrapper {
  margin-bottom: 12px;
}

.avatar {
  width: 60px;
  height: 60px;
  border-radius: 50%;
  border: 3px solid var(--campus-primary);
  cursor: pointer;
  transition: transform 0.3s ease;
}

.avatar:hover {
  transform: scale(1.05);
}

.username {
  font-size: 18px;
  font-weight: 600;
  color: var(--campus-text-primary);
  margin-bottom: 4px;
}

.user-id {
  font-size: 12px;
  color: var(--campus-text-secondary);
  margin-bottom: 16px;
}

.stats {
  display: flex;
  justify-content: space-around;
  margin: 15px 0;
  padding: 12px 0;
  border-top: 1px solid #f0f0f0;
  border-bottom: 1px solid #f0f0f0;
}

.stat-item {
  text-align: center;
  cursor: pointer;
  transition: transform 0.2s ease;
  padding: 4px 0;
}

.stat-item:hover {
  transform: translateY(-2px);
}

.count {
  font-size: 1.2em;
  color: var(--campus-primary);
  font-weight: 600;
  display: block;
  margin-bottom: 4px;
}

.label {
  font-size: 0.8em;
  color: var(--campus-text-secondary);
}

.actions {
  margin-top: 16px;
  display: flex;
  gap: 8px;
  justify-content: center;
}

.btn {
  padding: 6px 12px;
  border: none;
  border-radius: var(--campus-border-radius);
  cursor: pointer;
  background: var(--campus-primary);
  color: white;
  font-size: 12px;
  transition: all 0.3s ease;
}

.btn:hover {
  background: var(--campus-primary-light);
  transform: translateY(-1px);
}
</style>