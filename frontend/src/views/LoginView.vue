<template>
  <div class="login-container">
    <!-- 背景装饰元素 -->
    <div class="background-decoration">
      <div class="circle circle-1"></div>
      <div class="circle circle-2"></div>
      <div class="circle circle-3"></div>
    </div>
    
    <div class="login-content">
      <!-- 左侧品牌展示区 -->
      <div class="brand-section">
        <div class="brand-logo">
          <div class="logo-icon">🎓</div>
          <h1 class="brand-name">校园Hub</h1>
        </div>
        <p class="brand-slogan">连接校园，分享知识，创造价值</p>
        <div class="feature-list">
          <div class="feature-item">
            <span class="feature-icon">📚</span>
            <span>丰富的学习资源</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon">👥</span>
            <span>活跃的校园社区</span>
          </div>
          <div class="feature-item">
            <span class="feature-icon">💬</span>
            <span>实时的交流互动</span>
          </div>
        </div>
      </div>
      
      <!-- 右侧登录表单区 -->
      <div class="form-section">
        <el-card class="login-card" :body-style="{ padding: '0' }">
          <div class="card-header">
            <h2 class="form-title">欢迎回来</h2>
            <p class="form-subtitle">请登录您的账户</p>
          </div>
          
          <div class="card-body">
            <el-tabs v-model="activeTab" class="login-tabs" stretch>
              <el-tab-pane label="登录" name="login">
                <el-form 
                  :model="loginForm" 
                  :rules="formRules" 
                  ref="loginFormRef"
                  @keyup.enter="handleLogin"
                  class="login-form"
                >
                  <el-form-item prop="username">
                    <el-input 
                      v-model="loginForm.username" 
                      placeholder="请输入用户名" 
                      prefix-icon="User"
                      size="large"
                      class="custom-input"
                    />
                  </el-form-item>
                  <el-form-item prop="password">
                    <el-input 
                      v-model="loginForm.password" 
                      type="password" 
                      placeholder="请输入密码" 
                      prefix-icon="Lock"
                      size="large"
                      class="custom-input"
                      show-password
                    />
                  </el-form-item>
                  
                  <div class="form-options">
                    <el-checkbox v-model="rememberMe">记住我</el-checkbox>
                    <a href="#" class="forgot-password">忘记密码？</a>
                  </div>
                  
                  <el-form-item>
                    <el-button 
                      type="primary" 
                      @click="handleLogin" 
                      :loading="loading"
                      size="large"
                      class="submit-button"
                    >
                      {{ loading ? '登录中...' : '登录' }}
                    </el-button>
                  </el-form-item>
                </el-form>
                
                <div class="divider">
                  <span>或者</span>
                </div>
                
                <div class="social-login">
                  <el-button class="social-button wechat" size="large">
                    <span class="social-icon">💬</span>
                    微信登录
                  </el-button>
                  <el-button class="social-button qq" size="large">
                    <span class="social-icon">🐧</span>
                    QQ登录
                  </el-button>
                </div>
              </el-tab-pane>
              
              <el-tab-pane label="注册" name="register">
                <el-form 
                  :model="registerForm" 
                  :rules="formRules" 
                  ref="registerFormRef"
                  @keyup.enter="handleRegister"
                  class="register-form"
                >
                  <el-form-item prop="username">
                    <el-input 
                      v-model="registerForm.username" 
                      placeholder="请输入用户名" 
                      prefix-icon="User"
                      size="large"
                      class="custom-input"
                    />
                  </el-form-item>
                  <el-form-item prop="email">
                    <el-input 
                      v-model="registerForm.email" 
                      type="email" 
                      placeholder="请输入邮箱" 
                      prefix-icon="Message"
                      size="large"
                      class="custom-input"
                    />
                  </el-form-item>
                  <el-form-item prop="password">
                    <el-input 
                      v-model="registerForm.password" 
                      type="password" 
                      placeholder="请输入密码" 
                      prefix-icon="Lock"
                      size="large"
                      class="custom-input"
                      show-password
                    />
                  </el-form-item>
                  
                  <el-form-item>
                    <el-button 
                      type="success" 
                      @click="handleRegister" 
                      :loading="loading"
                      size="large"
                      class="submit-button"
                    >
                      {{ loading ? '注册中...' : '注册' }}
                    </el-button>
                  </el-form-item>
                </el-form>
                
                <div class="agreement">
                  <el-checkbox v-model="agreeTerms">
                    我已阅读并同意 <a href="#" class="agreement-link">《用户协议》</a> 和 <a href="#" class="agreement-link">《隐私政策》</a>
                  </el-checkbox>
                </div>
              </el-tab-pane>
            </el-tabs>
          </div>
        </el-card>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

// 默认显示登录界面
const activeTab = ref('login')
const loading = ref(false)
const rememberMe = ref(false)
const agreeTerms = ref(false)

const loginForm = reactive({
  username: '',
  password: ''
})

const registerForm = reactive({
  username: '',
  password: '',
  email: ''
})

const formRules = {
  username: [
    { required: true, message: '请输入用户名', trigger: 'blur' },
    { 
      pattern: /^[a-zA-Z0-9]{3,20}$/, 
      message: '用户名必须是3-20位字母或数字', 
      trigger: 'blur' 
    }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { 
      min: 6, 
      message: '密码长度至少6位', 
      trigger: 'blur' 
    }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { 
      type: 'email', 
      message: '请输入正确的邮箱格式', 
      trigger: 'blur' 
    }
  ]
}

const handleLogin = async () => {
  const form = loginForm
  if (!form.username || !form.password) {
    ElMessage.error('请填写完整的登录信息')
    return
  }
  
  try {
    loading.value = true
    await userStore.login(form.username, form.password)
    ElMessage.success('登录成功')
    router.push('/')
  } catch (error) {
    console.error('登录失败:', error)
    ElMessage.error(error.message || '登录失败')
  } finally {
    loading.value = false
  }
}

const handleRegister = async () => {
  const form = registerForm
  if (!form.username || !form.password || !form.email) {
    ElMessage.error('请填写完整的注册信息')
    return
  }
  
  try {
    loading.value = true
    await userStore.register(form.username, form.password, form.email)
    ElMessage.success('注册成功，请登录')
    activeTab.value = 'login' // 切换到登录tab
  } catch (error) {
    console.error('注册失败:', error)
    ElMessage.error(error.message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 0;
  position: relative;
  overflow: hidden;
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

.circle {
  position: absolute;
  border-radius: 50%;
  background: rgba(255, 255, 255, 0.1);
  animation: float 6s ease-in-out infinite;
}

.circle-1 {
  width: 200px;
  height: 200px;
  top: 10%;
  left: 10%;
  animation-delay: 0s;
}

.circle-2 {
  width: 150px;
  height: 150px;
  top: 60%;
  right: 10%;
  animation-delay: 2s;
}

.circle-3 {
  width: 100px;
  height: 100px;
  bottom: 20%;
  left: 20%;
  animation-delay: 4s;
}

@keyframes float {
  0%, 100% { transform: translateY(0px) rotate(0deg); }
  50% { transform: translateY(-20px) rotate(180deg); }
}

.login-content {
  display: flex;
  width: 100%;
  max-width: 1200px;
  height: 100vh;
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 0;
  overflow: hidden;
  box-shadow: 0 20px 40px rgba(0, 0, 0, 0.1);
}

.brand-section {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 60px 40px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  position: relative;
}

.brand-logo {
  text-align: center;
  margin-bottom: 30px;
}

.logo-icon {
  font-size: 4rem;
  margin-bottom: 10px;
  filter: drop-shadow(0 4px 8px rgba(0, 0, 0, 0.2));
}

.brand-name {
  font-size: 2.5rem;
  font-weight: 700;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.brand-slogan {
  font-size: 1.2rem;
  text-align: center;
  margin-bottom: 40px;
  opacity: 0.9;
  line-height: 1.6;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 15px;
  font-size: 1.1rem;
  opacity: 0.9;
  transition: opacity 0.3s ease;
}

.feature-item:hover {
  opacity: 1;
}

.feature-icon {
  font-size: 1.5rem;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.2));
}

.form-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 40px;
  background: white;
}

.login-card {
  width: 100%;
  max-width: 450px;
  border: none;
  border-radius: 20px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.08);
  overflow: hidden;
}

.card-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 30px;
  text-align: center;
}

.form-title {
  font-size: 1.8rem;
  font-weight: 600;
  margin: 0 0 8px 0;
}

.form-subtitle {
  font-size: 1rem;
  opacity: 0.9;
  margin: 0;
}

.card-body {
  padding: 30px;
}

.login-tabs {
  margin-top: 10px;
}

.login-tabs :deep(.el-tabs__header) {
  margin-bottom: 30px;
}

.login-tabs :deep(.el-tabs__nav-wrap::after) {
  display: none;
}

.login-tabs :deep(.el-tabs__item) {
  font-size: 1.1rem;
  font-weight: 500;
  padding: 0 20px;
  height: 50px;
  line-height: 50px;
}

.login-tabs :deep(.el-tabs__active-bar) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  height: 3px;
}

.custom-input {
  margin-bottom: 20px;
}

.custom-input :deep(.el-input__wrapper) {
  border-radius: 12px;
  padding: 12px 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  border: 2px solid transparent;
  transition: all 0.3s ease;
}

.custom-input :deep(.el-input__wrapper:hover),
.custom-input :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea;
  box-shadow: 0 4px 12px rgba(102, 126, 234, 0.2);
}

.form-options {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
}

.forgot-password {
  color: #667eea;
  text-decoration: none;
  font-size: 0.9rem;
  transition: color 0.3s ease;
}

.forgot-password:hover {
  color: #764ba2;
  text-decoration: underline;
}

.submit-button {
  width: 100%;
  border-radius: 12px;
  font-weight: 600;
  font-size: 1.1rem;
  padding: 15px;
  margin-top: 10px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s ease;
}

.submit-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.3);
}

.divider {
  text-align: center;
  margin: 30px 0;
  position: relative;
  color: #999;
  font-size: 0.9rem;
}

.divider::before {
  content: '';
  position: absolute;
  top: 50%;
  left: 0;
  right: 0;
  height: 1px;
  background: #e6e6e6;
}

.divider span {
  background: white;
  padding: 0 15px;
  position: relative;
}

.social-login {
  display: flex;
  gap: 15px;
  margin-top: 20px;
}

.social-button {
  flex: 1;
  border-radius: 12px;
  font-weight: 500;
  padding: 12px;
  border: 2px solid #e6e6e6;
  background: white;
  color: #666;
  transition: all 0.3s ease;
}

.social-button.wechat:hover {
  border-color: #07c160;
  color: #07c160;
  transform: translateY(-1px);
}

.social-button.qq:hover {
  border-color: #12b7f5;
  color: #12b7f5;
  transform: translateY(-1px);
}

.social-icon {
  margin-right: 8px;
  font-size: 1.2rem;
}

.agreement {
  margin-top: 20px;
  text-align: center;
}

.agreement-link {
  color: #667eea;
  text-decoration: none;
}

.agreement-link:hover {
  text-decoration: underline;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .login-content {
    flex-direction: column;
    height: auto;
    min-height: 100vh;
  }
  
  .brand-section {
    padding: 40px 20px;
    flex: none;
  }
  
  .form-section {
    padding: 20px;
    flex: none;
  }
  
  .social-login {
    flex-direction: column;
  }
}
</style>