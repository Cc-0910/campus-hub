import { defineStore } from 'pinia'
import request from '@/utils/request'
import { ref } from 'vue'
import router from '@/router'
import { mockLogin, mockRegister } from '@/mock/auth'

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
      // 首先尝试真实API，如果失败则使用模拟登录
      try {
        const res = await request.post('/api/auth/login', {
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
        
        await router.push('/app')
        return res
      } catch (error) {
        // 如果真实API失败，尝试模拟登录
        try {
          const res = await mockLogin(username, password)
          
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
        } catch (mockError) {
          throw new Error(mockError.message || error.message || '登录失败')
        }
      }
    }

    const register = async (username, password, email) => {
      // 首先尝试真实API，如果失败则使用模拟注册
      try {
        const res = await request.post('/api/auth/register', {
          username,
          password,
          email
        })
        
        if (res.data && res.data.userInfo) {
          token.value = res.data.token
          userInfo.value = {
            id: res.data.userInfo.id,
            username: username,
            nickname: res.data.userInfo.nickname,
            avatar: res.data.userInfo.avatar,
            role: res.data.userInfo.role
          }
          
          await router.push('/app')
        }
        
        return res
      } catch (error) {
        // 如果真实API失败，尝试模拟注册
        try {
          const res = await mockRegister(username, password, email)
          
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
        } catch (mockError) {
          throw new Error(mockError.message || error.message || '注册失败')
        }
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