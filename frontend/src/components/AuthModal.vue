<template>
  <el-dialog
    v-model="userStore.showLoginModal"
    title="用户认证"
    width="400px"
    :close-on-click-modal="false"
    :close-on-press-escape="false"
  >
    <el-tabs v-model="activeTab" class="auth-tabs">
      <el-tab-pane label="登录" name="login">
        <el-form 
          :model="loginForm" 
          :rules="formRules" 
          ref="loginFormRef"
          @keyup.enter="handleLogin"
        >
          <el-form-item prop="username">
            <el-input 
              v-model="loginForm.username" 
              placeholder="用户名" 
              prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input 
              v-model="loginForm.password" 
              type="password" 
              placeholder="密码" 
              prefix-icon="Lock"
            />
          </el-form-item>
          <el-form-item>
            <el-button 
              type="primary" 
              @click="handleLogin" 
              :loading="loading"
              style="width: 100%"
            >
              登录
            </el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      
      <el-tab-pane label="注册" name="register">
        <el-form 
          :model="registerForm" 
          :rules="formRules" 
          ref="registerFormRef"
          @keyup.enter="handleRegister"
        >
          <el-form-item prop="username">
            <el-input 
              v-model="registerForm.username" 
              placeholder="用户名" 
              prefix-icon="User"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input 
              v-model="registerForm.password" 
              type="password" 
              placeholder="密码" 
              prefix-icon="Lock"
            />
          </el-form-item>
          <el-form-item prop="email">
            <el-input 
              v-model="registerForm.email" 
              type="email" 
              placeholder="邮箱" 
              prefix-icon="Message"
            />
          </el-form-item>
          <el-form-item>
            <el-button 
              type="success" 
              @click="handleRegister" 
              :loading="loading"
              style="width: 100%"
            >
              注册
            </el-button>
          </el-form-item>
        </el-form>
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

// 监听 store 中的 loginModalTab 变化
watch(() => userStore.loginModalTab, (newTab) => {
  activeTab.value = newTab
}, { immediate: true })
const loading = ref(false)

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
    { min: 3, max: 20, message: '用户名长度应在3-20个字符之间', trigger: 'blur' }
  ],
  password: [
    { required: true, message: '请输入密码', trigger: 'blur' },
    { min: 6, max: 20, message: '密码长度应在6-20个字符之间', trigger: 'blur' }
  ],
  email: [
    { required: true, message: '请输入邮箱', trigger: 'blur' },
    { type: 'email', message: '请输入正确的邮箱格式', trigger: 'blur' }
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
.auth-tabs {
  margin-top: 10px;
}
</style>