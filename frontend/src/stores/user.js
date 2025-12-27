import { defineStore } from 'pinia'
import request from '@/utils/request'
import { ref } from 'vue'
import router from '@/router'

export const useUserStore = defineStore(
  'user',
  () => {
    // State
    const token = ref('')
    const userInfo = ref({})
    const showLoginModal = ref(false)
    const loginModalTab = ref('login')
    const enableClickEffect = ref(true)

    // Actions
    const login = async (username, password) => {
      try {
        const res = await request.post('/auth/login', {
          username,
          password
        })
        token.value = res.data.token
        userInfo.value = {
          id: res.data.id,
          username: res.data.username,
          nickname: res.data.nickname,
          avatar: res.data.avatar
        }
        
        // 登录成功后跳转到 /app
        await router.push('/app')
        
        return res
      } catch (error) {
        throw error
      }
    }

    const register = async (username, password, email) => {
      try {
        const res = await request.post('/auth/register', {
          username,
          password,
          email
        })
        
        // 注册成功后自动登录
        await login(username, password)
        
        return res
      } catch (error) {
        throw error
      }
    }

    const logout = async () => {
      token.value = ''
      userInfo.value = {}
      
      // 退出登录后跳转到首页
      await router.push('/')
    }

    const openLoginModal = (tabName = 'login') => {
      loginModalTab.value = tabName
      showLoginModal.value = true
    }

    const closeLoginModal = () => {
      showLoginModal.value = false
    }

    return {
      token,
      userInfo,
      showLoginModal,
      enableClickEffect,
      login,
      register,
      logout,
      openLoginModal,
      closeLoginModal
    }
  },
  {
    persist: true
  }
)