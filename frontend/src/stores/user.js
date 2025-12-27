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
          id: res.data.userInfo.id,
          username: username,
          nickname: res.data.userInfo.nickname,
          avatar: res.data.userInfo.avatar,
          role: res.data.userInfo.role
        }
        
        // 登录成功后跳转到 /app
        await router.push('/app')
        
        return res
      } catch (error) {
        // 优化错误信息显示
        if (error.response && error.response.data) {
          throw new Error(error.response.data.message || '登录失败')
        }
        throw new Error('网络错误，请检查网络连接')
      }
    }

    const register = async (username, password, email) => {
      try {
        const res = await request.post('/auth/register', {
          username,
          password,
          email
        })
        
        // 注册成功后直接使用返回的用户信息
        if (res.data && res.data.userInfo) {
          token.value = res.data.token
          userInfo.value = {
            id: res.data.userInfo.id,
            username: username,
            nickname: res.data.userInfo.nickname,
            avatar: res.data.userInfo.avatar,
            role: res.data.userInfo.role
          }
          
          // 注册成功后跳转到 /app
          await router.push('/app')
        }
        
        return res
      } catch (error) {
        // 优化错误信息显示
        if (error.response && error.response.data) {
          throw new Error(error.response.data.message || '注册失败')
        }
        throw new Error('网络错误，请检查网络连接')
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