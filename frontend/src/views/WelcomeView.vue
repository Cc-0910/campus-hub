<template>
  <div class="welcome-container">
    <div class="welcome-content">
      <div class="logo">
        <h1 class="logo-text">中南论坛</h1>
      </div>
      <div class="center-content">
        <h2 class="greeting">你好啊，CSUer！</h2>
        <div class="login-register-text">
          <span>欢迎</span>
          <span class="login-text" @click="openLoginModal('login')">登录</span>
          <span>/</span>
          <span class="register-text" @click="openLoginModal('register')">注册</span>
        </div>
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
  height: 100vh;
  background-image: url('@/assets/welcome-bg.jpg');
  background-size: cover;
  background-position: center;
  position: relative;
}

.welcome-content {
  position: relative;
  z-index: 1;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  padding: 20px;
}

.logo h1 {
  color: white;
  font-size: 24px;
  font-weight: bold;
  margin: 0;
  text-shadow: 0 2px 10px rgba(0,0,0,0.5);
}

.center-content {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  flex: 1;
  text-align: center;
}

.greeting {
  color: white;
  font-size: 48px;
  font-weight: 300;
  margin-bottom: 30px;
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  text-shadow: 0 2px 10px rgba(0,0,0,0.5);
}

.login-register-text {
  color: white;
  font-size: 20px;
  display: flex;
  gap: 5px;
  text-shadow: 0 2px 10px rgba(0,0,0,0.5);
}

.login-text, .register-text {
  cursor: pointer;
  text-decoration: underline;
  text-decoration-color: rgba(255, 255, 255, 0.7);
}

.login-text:hover, .register-text:hover {
  text-decoration-color: white;
  text-shadow: 0 2px 15px rgba(255,255,255,0.8);
}
</style>