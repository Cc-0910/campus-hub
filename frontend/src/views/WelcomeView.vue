<template>
  <div class="welcome-container">
    <!-- 背景装饰元素 -->
    <div class="background-decoration">
      <div class="floating-circle circle-1"></div>
      <div class="floating-circle circle-2"></div>
      <div class="floating-circle circle-3"></div>
      <div class="floating-circle circle-4"></div>
    </div>
    
    <div class="welcome-content">
      <!-- 顶部导航栏 -->
      <div class="welcome-header">
        <div class="logo">
          <div class="logo-icon">🎓</div>
          <h1 class="logo-text">校园Hub</h1>
        </div>
        <div class="header-actions">
          <el-button type="primary" size="large" @click="openLoginModal('login')" class="login-btn">
            立即登录
          </el-button>
        </div>
      </div>
      
      <!-- 主内容区 -->
      <div class="center-content">
        <div class="hero-section">
          <h2 class="greeting">你好啊，CSUer！</h2>
          <p class="welcome-subtitle">欢迎来到中南大学校园社区</p>
          
          <div class="feature-cards">
            <div class="feature-card">
              <div class="feature-icon">📚</div>
              <h3>学习资源</h3>
              <p>丰富的课程资料和学习经验分享</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">👥</div>
              <h3>校园社区</h3>
              <p>与同学交流互动，结识新朋友</p>
            </div>
            <div class="feature-card">
              <div class="feature-icon">💬</div>
              <h3>实时交流</h3>
              <p>随时随地参与话题讨论</p>
            </div>
          </div>
          
          <div class="action-buttons">
            <el-button type="primary" size="large" @click="openLoginModal('login')" class="primary-action">
              <span class="btn-icon">🚀</span>
              开始探索
            </el-button>
            <el-button type="success" size="large" @click="openLoginModal('register')" class="secondary-action">
              <span class="btn-icon">✨</span>
              立即注册
            </el-button>
          </div>
          
          <div class="quick-access">
            <span class="quick-text">或直接</span>
            <span class="login-link" @click="openLoginModal('login')">登录</span>
            <span class="divider">/</span>
            <span class="register-link" @click="openLoginModal('register')">注册</span>
          </div>
        </div>
      </div>
      
      <!-- 底部信息 -->
      <div class="welcome-footer">
        <p class="footer-text">© 2024 校园Hub - 连接校园，分享知识，创造价值</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

const openLoginModal = (tabName) => {
  userStore.openLoginModal(tabName)
}
</script>

<style scoped>
.welcome-container {
  width: 100vw;
  min-height: 100vh;
  background-image: url('@/assets/welcome-bg.jpg');
  background-size: cover;
  background-position: center;
  position: relative;
  overflow-x: hidden;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
}

.background-decoration {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
}

.floating-circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 8s ease-in-out infinite;
  filter: blur(1px);
}

.circle-1 {
  width: 120px;
  height: 120px;
  top: 10%;
  left: 5%;
  animation-delay: 0s;
}

.circle-2 {
  width: 80px;
  height: 80px;
  top: 70%;
  right: 10%;
  animation-delay: 2s;
}

.circle-3 {
  width: 60px;
  height: 60px;
  bottom: 20%;
  left: 15%;
  animation-delay: 4s;
}

.circle-4 {
  width: 100px;
  height: 100px;
  top: 30%;
  right: 5%;
  animation-delay: 6s;
}

@keyframes float {
  0%, 100% { 
    transform: translateY(0px) rotate(0deg) scale(1);
    opacity: 0.3;
  }
  50% { 
    transform: translateY(-30px) rotate(180deg) scale(1.1);
    opacity: 0.6;
  }
}

.welcome-content {
  position: relative;
  z-index: 1;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 0;
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(10px);
}

.welcome-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px 40px;
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(15px);
  border-bottom: 1px solid rgba(255, 255, 255, 0.2);
}

.logo {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 2rem;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.logo-text {
  color: white;
  font-size: 1.8rem;
  font-weight: 700;
  margin: 0;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.4);
}

.login-btn {
  background: rgba(255, 255, 255, 0.2);
  border: 2px solid rgba(255, 255, 255, 0.3);
  color: white;
  font-weight: 600;
  border-radius: 12px;
  transition: all 0.3s ease;
}

.login-btn:hover {
  background: rgba(255, 255, 255, 0.3);
  border-color: rgba(255, 255, 255, 0.5);
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(255, 255, 255, 0.2);
}

.center-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  flex: 1;
  text-align: center;
  padding: 0 20px;
}

.hero-section {
  max-width: 800px;
  margin: 0 auto;
}

.greeting {
  color: white;
  /* 使用楷体字栈 */ 
  font-family: 'KaiTi', 'STKaiti', '楷体', serif;
  font-size: 2.8rem;
  /* 关键：不加粗，保持修长 */
  font-weight: normal;
  margin-bottom: 16px;
  /* 增加字间距，更舒展 */ 
  letter-spacing: 4px;
  /* 精致的文字阴影，增强立体感但不过分厚重 */ 
  text-shadow: 1px 1px 3px rgba(0, 0, 0, 0.6);
  background: linear-gradient(45deg, #ffffff, #e3f2fd);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
  background-clip: text;
}

.welcome-subtitle {
  color: rgba(255, 255, 255, 0.9);
  /* 使用楷体字栈 */ 
  font-family: 'KaiTi', 'STKaiti', '楷体', serif;
  font-size: 1.2rem;
  /* 关键：不加粗 */
  font-weight: normal;
  margin-bottom: 50px;
  /* 增加字间距，更舒展 */
  letter-spacing: 2px;
  text-shadow: 1px 1px 2px rgba(0, 0, 0, 0.6);
}

.feature-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
  gap: 30px;
  margin-bottom: 50px;
}

.feature-card {
  background: rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(15px);
  border: 1px solid rgba(255, 255, 255, 0.2);
  border-radius: 20px;
  padding: 30px 20px;
  text-align: center;
  transition: all 0.3s ease;
  cursor: pointer;
}

.feature-card:hover {
  transform: translateY(-10px);
  background: rgba(255, 255, 255, 0.15);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.2);
}

.feature-icon {
  font-size: 3rem;
  margin-bottom: 15px;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.feature-card h3 {
  color: white;
  font-size: 1.3rem;
  font-weight: 600;
  margin-bottom: 10px;
}

.feature-card p {
  color: rgba(255, 255, 255, 0.8);
  font-size: 0.95rem;
  line-height: 1.5;
}

.action-buttons {
  display: flex;
  gap: 20px;
  justify-content: center;
  margin-bottom: 30px;
  flex-wrap: wrap;
}

.primary-action, .secondary-action {
  padding: 15px 30px;
  font-size: 1.1rem;
  font-weight: 600;
  border-radius: 12px;
  border: none;
  transition: all 0.3s ease;
}

.primary-action {
  background: linear-gradient(135deg, #ff6b6b, #ff8e53);
  color: white;
}

.primary-action:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 25px rgba(255, 107, 107, 0.4);
}

.secondary-action {
  background: rgba(255, 255, 255, 0.2);
  color: white;
  border: 2px solid rgba(255, 255, 255, 0.3);
}

.secondary-action:hover {
  background: rgba(255, 255, 255, 0.3);
  transform: translateY(-3px);
  box-shadow: 0 10px 25px rgba(255, 255, 255, 0.2);
}

.btn-icon {
  margin-right: 8px;
  font-size: 1.2rem;
}

.quick-access {
  color: rgba(255, 255, 255, 0.8);
  font-size: 1rem;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.login-link, .register-link {
  color: rgba(255, 255, 255, 0.9);
  text-decoration: underline;
  cursor: pointer;
  transition: all 0.3s ease;
  font-weight: 500;
}

.login-link:hover, .register-link:hover {
  color: white;
  text-shadow: 0 2px 8px rgba(255, 255, 255, 0.5);
}

.divider {
  color: rgba(255, 255, 255, 0.6);
}

.welcome-footer {
  padding: 20px;
  text-align: center;
  background: rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(10px);
}

.footer-text {
  color: rgba(255, 255, 255, 0.7);
  font-size: 0.9rem;
  margin: 0;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .welcome-header {
    padding: 15px 20px;
    flex-direction: column;
    gap: 15px;
  }
  
  .greeting {
    font-family: 'KaiTi', 'STKaiti', '楷体', serif;
    font-size: 2.5rem;
    letter-spacing: 3px; /* 响应式调整字间距 */
  }
  
  .welcome-subtitle {
    font-family: 'KaiTi', 'STKaiti', '楷体', serif;
    font-size: 1.1rem;
    letter-spacing: 1.5px; /* 响应式调整字间距 */
  }
  
  .feature-cards {
    grid-template-columns: 1fr;
    gap: 20px;
  }
  
  .action-buttons {
    flex-direction: column;
    align-items: center;
  }
  
  .primary-action, .secondary-action {
    width: 100%;
    max-width: 280px;
  }
}
</style>