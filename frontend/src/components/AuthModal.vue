<template>
  <el-dialog
    v-model="userStore.showLoginModal"
    title="用户认证"
    width="480px"
    :close-on-click-modal="true"
    :close-on-press-escape="true"
    class="auth-modal"
    align-center
  >
    <div class="modal-header">
      <div class="modal-logo">
        <div class="logo-icon">🎓</div>
        <h3 class="modal-title">校园Hub</h3>
      </div>
      <p class="modal-subtitle">欢迎加入我们的校园社区</p>
    </div>
    
    <el-tabs v-model="activeTab" class="auth-tabs" stretch>
      <el-tab-pane label="登录" name="login">
        <div class="tab-content">
          <el-form 
            :model="loginForm" 
            :rules="formRules" 
            ref="loginFormRef"
            @keyup.enter="handleLogin"
            class="auth-form"
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
        </div>
      </el-tab-pane>
      
      <el-tab-pane label="注册" name="register">
        <div class="tab-content">
          <el-form 
            :model="registerForm" 
            :rules="formRules" 
            ref="registerFormRef"
            @keyup.enter="handleRegister"
            class="auth-form"
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
        </div>
      </el-tab-pane>
    </el-tabs>
  </el-dialog>
</template>

<script setup>
import { ref, reactive, watch } from 'vue'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/user'

const router = useRouter()
const userStore = useUserStore()

const activeTab = ref('login')
const loading = ref(false)
const rememberMe = ref(false)
const agreeTerms = ref(false)

// 监听 store 中的 loginModalTab 变化
watch(() => userStore.loginModalTab, (newTab) => {
  activeTab.value = newTab
}, { immediate: true })

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
    userStore.closeLoginModal()
    router.push('/app')
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
    ElMessage.success('注册成功并已自动登录')
    userStore.closeLoginModal()
    router.push('/app')
  } catch (error) {
    console.error('注册失败:', error)
    ElMessage.error(error.message || '注册失败')
  } finally {
    loading.value = false
  }
}
</script>

<style scoped>
.auth-modal :deep(.el-dialog) {
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 25px 50px rgba(0, 0, 0, 0.2);
}

.auth-modal :deep(.el-dialog__header) {
  display: none;
}

.auth-modal :deep(.el-dialog__body) {
  padding: 0;
}

.modal-header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  padding: 30px;
  text-align: center;
}

.modal-logo {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin-bottom: 10px;
}

.logo-icon {
  font-size: 2.5rem;
  filter: drop-shadow(0 2px 4px rgba(0, 0, 0, 0.3));
}

.modal-title {
  font-size: 1.8rem;
  font-weight: 700;
  margin: 0;
  text-shadow: 0 2px 4px rgba(0, 0, 0, 0.3);
}

.modal-subtitle {
  font-size: 0.95rem;
  opacity: 0.9;
  margin: 0;
}

.auth-tabs {
  padding: 0 30px 30px;
}

.auth-tabs :deep(.el-tabs__header) {
  margin-bottom: 25px;
}

.auth-tabs :deep(.el-tabs__nav-wrap::after) {
  display: none;
}

.auth-tabs :deep(.el-tabs__item) {
  font-size: 1.1rem;
  font-weight: 500;
  padding: 0 20px;
  height: 50px;
  line-height: 50px;
  color: #666;
}

.auth-tabs :deep(.el-tabs__item.is-active) {
  color: #667eea;
}

.auth-tabs :deep(.el-tabs__active-bar) {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  height: 3px;
}

.tab-content {
  animation: fadeIn 0.3s ease;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.auth-form {
  margin-bottom: 0;
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
  margin: 25px 0;
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
  .auth-modal :deep(.el-dialog) {
    width: 90vw !important;
    max-width: 400px;
  }
  
  .modal-header {
    padding: 20px;
  }
  
  .auth-tabs {
    padding: 0 20px 20px;
  }
  
  .social-login {
    flex-direction: column;
  }
}
</style>