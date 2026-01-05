<template>
  <div class="layout-wrapper">
    <!-- 固定背景层 -->
    <div class="fixed-background"></div>
    
    <!-- 固定透明导航栏 -->
    <el-header :class="['fixed-header', { 'header-scrolled': isScrolled }]">
      <div class="header-content">
        <!-- 最左侧：Logo -->
        <div class="logo">
          <h3>中南论坛</h3>
        </div>
        
        <!-- Logo右侧：动态标题 -->
        <div class="page-title-section">
          <span class="separator">|</span>
          <span class="page-title">{{ getPageTitle() }}</span>
        </div>
        
        <!-- 导航菜单区 -->
        <div class="nav-menu">
          <router-link to="/app" class="nav-item" :class="{ 'nav-item-active': route.path === '/app' || route.path === '/app/' }">首页</router-link>
          <router-link to="/app/topics" class="nav-item" :class="{ 'nav-item-active': route.path.includes('/app/topics') }">话题</router-link>
          <router-link to="/app/qa" class="nav-item" :class="{ 'nav-item-active': route.path.includes('/app/qa') }">问答</router-link>
          <router-link to="/app/articles" class="nav-item" :class="{ 'nav-item-active': route.path.includes('/app/articles') }">文章</router-link>
        </div>
        

        
        <!-- 弹性占位，把后面的元素推向右边 -->
        <div style="flex: 1"></div>
        
        <!-- 最右侧：通知图标、用户头像 -->
        <div class="header-right">
          <!-- 未登录状态 -->
          <el-button 
            v-if="!userStore.token" 
            type="primary" 
            @click="userStore.openLoginModal"
          >
            登录/注册
          </el-button>
          
          <!-- 已登录状态 -->
          <div v-else class="user-info">
            <el-button type="default" :icon="Bell" circle class="notification-btn" />
            <el-dropdown @command="handleCommand">
              <div class="user-avatar">
                <el-avatar :size="40" :src="userStore.userInfo.avatar || defaultAvatar">
                  {{ userStore.userInfo.nickname ? userStore.userInfo.nickname.charAt(0) : 'U' }}
                </el-avatar>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人资料</el-dropdown-item>
                  <el-dropdown-item 
                    v-if="userStore.userInfo.role === 'admin'" 
                    command="admin-topics"
                  >
                    话题管理
                  </el-dropdown-item>
                  <el-dropdown-item command="logout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </div>
    </el-header>

    <section id="banner" class="banner-section">
      <div class="banner-container">
        <div class="banner-content text-center">
          <h1 class="banner-title">你好啊，CSUer</h1>
          <p class="banner-subtitle">欢迎访问中南论坛</p>
          <div class="search-box">
            <SearchBar />
          </div>
        </div>
      </div>
      <div class="scroll-down-arrow" @click="scrollToContent">
        <i class="fa fa-angle-down" aria-hidden="true"></i>
        <svg class="fallback-arrow" viewBox="0 0 1024 1024" xmlns="http://www.w3.org/2000/svg" width="32" height="32" fill="currentColor"><path d="M831.872 340.864 512 652.672 192.128 340.864a30.592 30.592 0 0 0-42.752 0 29.12 29.12 0 0 0 0 41.6L489.664 714.24a32 32 0 0 0 44.672 0l340.288-331.712a29.12 29.12 0 0 0 0-41.728 30.592 30.592 0 0 0-42.752 0z"></path></svg>
      </div>
    </section>

    <!-- 内容包裹器 -->
    <div class="content-wrapper" ref="contentWrapperRef">
      <!-- 左侧悬浮资料卡 -->
      <div class="side-profile-card">
        <SideProfileCard />
      </div>

      <!-- 主内容区 -->
      <el-container class="main-container">
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
      
      <!-- 临时溢出占位元素，用于测试滚动条 -->
      <div style="height: 500vh; background: transparent; pointer-events: none;"></div>
    </div>
    
    <!-- 粒子效果容器 -->
    <div class="effects-container">
      <CursorBoom />
      <ClickExplosion />
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'
import { useRouter, useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { ref, onMounted, onUnmounted } from 'vue'
import SideProfileCard from '@/components/SideProfileCard.vue'
import CursorBoom from '@/components/Effects/CursorBoom.vue'
import ClickExplosion from '@/components/Effects/ClickExplosion.vue'
import SearchBar from '@/components/SearchBar.vue'
import {
  House,
  ChatDotRound,
  ChatLineRound,
  Document,
  User,
  InfoFilled,
  Bell,
  Lightning,
  Search
} from '@element-plus/icons-vue'

const userStore = useUserStore()
const router = useRouter()
const route = useRoute()

// Define the ref to match the template
const contentWrapperRef = ref(null)

// Scroll function (Translated from User's jQuery snippet)
const scrollToContent = () => {
  if (contentWrapperRef.value) {
    const top = contentWrapperRef.value.offsetTop - 80; // Respecting the original "-80" offset
    window.scrollTo({
      top: top,
      behavior: 'smooth' // Native smooth scrolling
    });
  }
}

// 定义滚动状态变量
const isScrolled = ref(false)

// 监听滚动 event 的函数
const handleScroll = () => {
  // 获取全局窗口的滚动距离 
  const scrollTop = window.scrollY || document.documentElement.scrollTop; 
  // 阈值设为 50px 
  isScrolled.value = scrollTop > 50; 
}



// 组件挂载时绑定 event
onMounted(() => {
  // 核心修正：监听 window 全局对象，而不是某个 DOM 元素 
  window.addEventListener('scroll', handleScroll);
})

// 组件卸载时移除 event
onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll);
})

const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

const getPageTitle = () => {
  return route.meta.title || '首页'
}

const handleCommand = (command) => {
  if (command === 'profile') {
    const userId = userStore.userInfo?.id || userStore.userInfo?.userId;
    console.log('尝试跳转，当前用户ID:', userId);

    if (userId) {
      router.push(`/user/${userId}`);
    } else {
      ElMessage.error('无法跳转：未找到用户ID，请重新登录');
      console.error('用户信息缺失:', userStore.userInfo);
    }
  } else if (command === 'admin-topics') {
    router.push('/app/admin/topics');
  } else if (command === 'logout') {
    logout();
  }
}

const logout = async () => {
  try {
    await ElMessageBox.confirm(
      '确定要退出登录吗？',
      '提示',
      {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning',
      }
    )
    userStore.logout()
    ElMessage.success('已退出登录')
  } catch {
    // 用户取消操作
  }
}
</script>

<style scoped>
/* 强制重置祖先元素的 overflow，激活 sticky */
:global(body), :global(#app) {
  overflow: visible !important;
  height: auto !important;
}

/* 固定背景层 */
.fixed-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1; /* 确保永远在最底下 */
  background-image: url('@/assets/main-bg.jpg');
  background-size: cover;
  background-position: center;
  pointer-events: none; /* 不拦截点击 */
}

/* 根容器 - 现在只负责布局，不再有背景 */
.layout-wrapper {
  /* 绝对禁止使用height: 100vh，避免高度被固定 */
  height: auto !important; 
  min-height: 100vh; /* 保持最小高度为一屏 */
  overflow: visible !important; /* 确保内容可完整显示 */
  position: relative; /* 确保 z-index 生效 */
  z-index: 1; /* 确保内容在背景之上 */
}

/* 固定透明导航栏 */
.fixed-header {
  padding: 0;
  background-color: transparent !important; /* 初始 state: 透明 */
  height: 64px;
  box-shadow: none; /* 默认状态无阴影 */
  transition: all 0.4s ease !important; /* 平滑过渡效果 */
  color: #ffffff; /* 确保在透明状态下文字可见 */
  position: fixed;
  top: 0;
  width: 100%;
  z-index: 100; /* 确保在最顶层 */
  backdrop-filter: none;
}

/* 滚动状态下的样式 */
.header-scrolled {
  background-color: rgba(80, 135, 164, 0.95) !important; /* #5087A4 with slight opacity */
  backdrop-filter: blur(10px); /* Glassmorphism effect */
  box-shadow: 0 4px 10px rgba(0,0,0,0.1); /* Subtle shadow */
}

.header-content {
  display: flex;
  align-items: center;
  height: 100%;
  padding: 0 20px;
  color: #ffffff; /* 白色文字 */
}

/* 确保透明header下文字保持白色 */
.fixed-header :deep(.header-content) {
  color: #ffffff;
}

.logo h3 {
  color: #ffffff; /* 白色Logo */
  margin: 0;
  font-size: 18px;
  font-weight: bold;
}

.page-title-section {
  display: flex;
  align-items: center;
  margin: 0 20px;
}

.separator {
  margin: 0 10px;
  color: rgba(255, 255, 255, 0.7);
}

.page-title {
  color: #ffffff; /* 白色标题 */
  font-size: 16px;
  font-weight: 500;
}

/* 导航菜单区 */
.nav-menu {
  display: flex;
  align-items: center;
  margin-left: 40px;
}

.nav-item {
  color: #ffffff;
  text-decoration: none;
  font-size: 16px;
  margin-right: 24px;
  padding-bottom: 2px;
}

.nav-item:hover {
  text-decoration: none;
}

.nav-item-active {
  color: #ffffff;
  font-weight: bold;
  text-decoration: none;
  border-bottom: 2px solid #ffffff;
}

.effect-switch-wrapper {
  display: flex;
  align-items: center;
  gap: 8px;
}

.effect-label {
  color: #ffffff;
  font-size: 14px;
}

.effect-switch {
  margin-left: 0;
}

.search-box {
  width: 100%;
  max-width: 580px;
  margin: 40px auto 0;
}

.search-input :deep(.el-input__wrapper) {
  background-color: rgba(255, 255, 255, 0.15); /* 半透明白色背景 */
  border: 1px solid rgba(255, 255, 255, 0.3); /* 极细半透明边框 */
  box-shadow: none;
  border-radius: 30px; /* 完全圆润的胶囊状 */
  height: 50px; /* 增加高度 */
}

.search-input :deep(.el-input__wrapper):hover {
  background-color: rgba(255, 255, 255, 0.25); /* 悬停时背景稍亮 */
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.1); /* 轻微发光阴影 */
}

.search-input :deep(.el-input__wrapper):focus {
  background-color: rgba(255, 255, 255, 0.25); /* 聚焦时背景稍亮 */
  box-shadow: 0 0 10px rgba(255, 255, 255, 0.1); /* 轻微发光阴影 */
}

.search-input :deep(.el-input__inner) {
  color: #ffffff;
  background-color: transparent;
  height: 48px; /* 调整内部高度以适应新的容器 */
}

.search-input :deep(.el-input__inner::placeholder) {
  color: rgba(255, 255, 255, 0.7); /* 浅白色占位符 */
}

.search-input :deep(.el-input__prefix) {
  color: rgba(255, 255, 255, 0.8);
  margin-left: 15px;
}

.search-input :deep(.el-input__suffix) {
  margin-right: 15px;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 15px;
}

.notification-btn {
  color: #ffffff !important; /* 白色通知图标 */
  background-color: transparent !important;
  border-color: rgba(255, 255, 255, 0.3) !important;
}

.notification-btn :deep(i) {
  color: #ffffff !important;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 15px;
}

.user-avatar {
  cursor: pointer;
}

/* 2. Parent Wrapper: Enable Sticky Context */
.content-wrapper {
  /* Ensure layout is side-by-side */
  display: flex;
  justify-content: center;
  /* CRITICAL: align-items must be flex-start.
     If default (stretch), the sidebar becomes as tall as the content
     and sticky positioning won't work. */
  align-items: flex-start;
  gap: 20px;
  
  width: 100%;
  max-width: 1200px;
  margin: 0 auto;
  padding-bottom: 50px;
  position: relative;
  z-index: 1;
  overflow: visible !important; /* CRITICAL: Must not hide overflow */
}

/* 3. Main Content: Takes remaining space */
.main-container {
  flex: 1; /* Grow to fill space */
  min-width: 0; /* Prevent flex overflow */
  background-color: transparent;
}

/* 4. Sidebar: Activate Sticky */
.side-profile-card {
  /* Enable Sticky */
  position: -webkit-sticky;
  position: sticky !important;
  
  /* KEY FIX: Offset by Header Height (64px) + Padding (20px) */
  top: 84px !important;
  
  width: 260px;
  height: fit-content; /* Ensure height allows movement */
  flex-shrink: 0;
  z-index: 99; /* Ensure it stays above main content but below header (100) */
}

.main {
  padding: 0;
  margin: 0; /* Reset any previous margins */
  background: transparent;
  overflow: visible; /* Ensure scroll isn't trapped here */
}

/* Mobile: Disable sticky on small screens */
@media (max-width: 768px) {
  .content-wrapper {
    flex-direction: column;
    align-items: center;
  }
  .side-profile-card {
    position: relative;
    top: 0;
    width: 100%;
    margin-bottom: 20px;
  }
}



/* 适配小屏幕 */
@media (max-width: 768px) {
  .aside {
    width: 60px !important;
  }
  
  .logo h3, 
  .el-menu-item span,
  .effect-switch-wrapper span {
    display: none;
  }
  
  .el-menu-item {
    justify-content: center;
  }
  
  .main {
    margin-left: 60px;
  }
}

.effects-container {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 9997; /* Slightly below the particle effects to ensure they're visible */
  overflow: hidden;
}

/* --- Transparent Hero Styles --- */
.banner-section {
  position: relative;
  width: 100%;
  height: 100vh; /* Occupy full screen */
  display: flex;
  align-items: center;
  justify-content: center;
  background: transparent; /* KEY: No purple gradient! */
  z-index: 1;
}
.banner-container { position: relative; z-index: 2; text-align: center; color: #fff; text-shadow: 0 2px 10px rgba(0,0,0,0.3); /* Shadow for readability on image */ padding: 0 20px; }

.banner-title { 
  /* 使用楷体字栈 */ 
  font-family: 'KaiTi', 'STKaiti', '楷体', serif; 
  /* 保持原有大小 */ 
  font-size: 2.8rem; 
  /* 关键：不加粗，保持修长 */ 
  font-weight: normal; 
  margin-bottom: 1rem; 
  /* 增加字间距，更舒展 */ 
  letter-spacing: 4px; 
  /* 精致的文字阴影，增强立体感但不过分厚重 */ 
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.6); 
} 

.banner-subtitle { 
  /* 使用楷体字栈 */ 
  font-family: 'KaiTi', 'STKaiti', '楷体', serif; 
  font-size: 1.2rem; 
  /* 关键：不加粗 */ 
  font-weight: normal; 
  opacity: 0.95; /* 稍微提高一点不透明度提升清晰度 */ 
  /* 增加字间距，更舒展 */ 
  letter-spacing: 2px; 
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.6); 
}

/* Arrow Animation */
.scroll-down-arrow { position: absolute; bottom: 40px; left: 50%; transform: translateX(-50%); color: #fff; font-size: 2rem; cursor: pointer; }

.fallback-arrow { display: block; margin: 0 auto; }



/* Responsive */
@media (max-width: 768px) {
  .banner-title { font-size: 2rem; }
  .banner-subtitle { font-size: 1.2rem; }
}
</style>