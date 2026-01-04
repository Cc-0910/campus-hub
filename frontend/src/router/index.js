import { createRouter, createWebHistory } from 'vue-router'
import BasicLayout from '../layout/BasicLayout.vue'
import HomeDashboard from '../views/HomeDashboard.vue'
import WelcomeView from '../views/WelcomeView.vue'
import UserProfilePage from '../views/UserProfilePage.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    // 欢迎页 - 未登录用户访问
    {
      path: '/',
      name: 'welcome',
      component: WelcomeView,
    },
    // 用户资料页面 - 直接访问
    {
      path: '/user/:id',
      name: 'user-profile',
      component: UserProfilePage,
      meta: { title: '用户详情' }
    },
    // 应用主路径 - 登录后访问
    {
      path: '/app',
      component: BasicLayout,
      children: [
        {
          path: '',
          name: 'home',
          component: HomeDashboard,
          meta: { title: '首页' }
        },
        {
          path: 'topics',
          name: 'topics',
          component: () => import('../views/TopicsView.vue'),
          meta: { title: '话题' }
        },
        {
          path: 'qa',
          name: 'qa',
          component: () => import('../views/QAView.vue'),
          meta: { title: '问答' }
        },
        {
          path: 'articles',
          name: 'articles',
          component: () => import('../views/ArticlesView.vue'),
          meta: { title: '文章' }
        },
        {
          path: 'members',
          name: 'members',
          component: () => import('../views/MembersView.vue'),
          meta: { title: '成员' }
        },
        {
          path: 'user/:id',
          name: 'user-profile-app',
          component: () => import('../views/UserProfilePage.vue'),
          meta: { title: '用户详情' }
        },
        {
          path: 'user/:id/articles',
          name: 'user-articles',
          component: () => import('../views/ArticlesView.vue'),
          meta: { title: '用户文章' }
        },
        {
          path: 'user/:id/following',
          name: 'user-following',
          component: () => import('../views/FollowingView.vue'),
          meta: { title: '用户关注' }
        },
        {
          path: 'user/:id/followers',
          name: 'user-followers',
          component: () => import('../views/FollowersView.vue'),
          meta: { title: '用户粉丝' }
        },
        {
          path: 'search',
          name: 'search',
          component: () => import('../views/SearchResultPage.vue'),
          meta: { title: '搜索结果' }
        },
        {
          path: 'about',
          name: 'about',
          // route level code-splitting
          // this generates a separate chunk (About.[hash].js) for this route
          // which is lazy-loaded when the route is visited.
          component: () => import('../views/AboutView.vue'),
          meta: { title: '关于' }
        },
      ],
    },
  ],
})

// 全局路由守卫
router.beforeEach(async (to, from, next) => {
  // 动态导入 store 以避免循环依赖
  const { useUserStore } = await import('@/stores/user');
  const userStore = useUserStore();
  
  // 如果用户已登录且访问根路径，则重定向到 /app
  if (userStore.token && to.path === '/') {
    next('/app');
  }
  // 如果用户未登录且访问 /app 下的页面，则重定向到根路径
  else if (!userStore.token && to.path.startsWith('/app')) {
    next('/');
  }
  // 其他情况正常导航
  else {
    next();
  }
});

export default router
