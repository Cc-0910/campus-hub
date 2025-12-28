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
      try {
        // 使用模拟登录（在没有后端服务时）
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
      } catch (error) {
        // 如果模拟登录失败，尝试真实API（用于开发环境切换）
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
          
          await router.push('/app')
          return res
        } catch (realError) {
          throw new Error(error.message || '登录失败')
        }
      }
    }

    const register = async (username, password, email) => {
      try {
        // 使用模拟注册（在没有后端服务时）
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
      } catch (error) {
        // 如果模拟注册失败，尝试真实API（用于开发环境切换）
        try {
          const res = await request.post('/auth/register', {
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
        } catch (realError) {
          throw new Error(error.message || '注册失败')
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