<template>
  <div class="article-detail-page">
    <div class="article-container">
      <!-- 文章内容区域 -->
      <div class="article-content">
        <el-card class="article-card">
          <!-- 文章头部 -->
          <div class="article-header">
            <h1 class="article-title">{{ articleDetail.title }}</h1>
            <div class="article-meta">
              <div class="author-info">
                <el-avatar :size="32" :src="articleDetail.author?.avatar || defaultAvatar" />
                <div class="author-details">
                  <div class="author-name">{{ articleDetail.author?.nickname || '匿名用户' }}</div>
                  <div class="article-time">{{ formatTime(articleDetail.createTime) }}</div>
                </div>
              </div>
              <div class="article-stats">
                <div class="stat-item">
                  <el-icon><View /></el-icon>
                  <span>{{ articleDetail.viewCount || 0 }} 浏览</span>
                </div>
                <div class="stat-item">
                  <el-icon><ChatLineRound /></el-icon>
                  <span>{{ commentCount }} 评论</span>
                </div>
                <div class="stat-item">
                  <el-icon><Star /></el-icon>
                  <span>{{ articleDetail.likeCount || 0 }} 收藏</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 话题标签 -->
          <div v-if="articleDetail.topics && articleDetail.topics.length > 0" class="article-topics">
            <el-tag 
              v-for="topic in articleDetail.topics" 
              :key="topic.id" 
              size="small" 
              type="info"
              class="topic-tag"
            >
              {{ topic.name }}
            </el-tag>
          </div>

          <!-- 文章正文 -->
          <div class="article-body">
            <div class="article-text" v-html="articleDetail.content"></div>
          </div>

          <!-- 文章操作 -->
          <div class="article-actions">
            <el-button 
              :type="isLiked ? 'primary' : 'default'" 
              :icon="Star" 
              @click="toggleLike"
            >
              {{ isLiked ? '已收藏' : '收藏' }} {{ articleDetail.likeCount || 0 }}
            </el-button>
            <el-button 
              :type="isStared ? 'warning' : 'default'" 
              :icon="ChatLineRound"
              @click="focusComment"
            >
              评论 {{ commentCount }}
            </el-button>
            <el-button 
              :type="isShared ? 'success' : 'default'" 
              :icon="Share"
              @click="shareArticle"
            >
              分享
            </el-button>
          </div>
        </el-card>
      </div>

      <!-- 侧边栏 -->
      <div class="sidebar">
        <!-- 作者信息卡片 -->
        <el-card class="author-card">
          <div class="author-card-content">
            <el-avatar :size="60" :src="articleDetail.author?.avatar || defaultAvatar" />
            <div class="author-card-info">
              <div class="author-card-name">{{ articleDetail.author?.nickname || '匿名用户' }}</div>
              <div class="author-card-stats">
                <div class="stat">
                  <span class="stat-value">12</span>
                  <span class="stat-label">文章</span>
                </div>
                <div class="stat">
                  <span class="stat-value">36</span>
                  <span class="stat-label">粉丝</span>
                </div>
              </div>
            </div>
            <el-button type="primary" size="small">关注</el-button>
          </div>
        </el-card>

        <!-- 相关推荐 -->
        <el-card class="recommend-card">
          <template #header>
            <div class="card-header">
              <span>相关推荐</span>
            </div>
          </template>
          <div class="recommend-list">
            <div 
              v-for="item in relatedArticles" 
              :key="item.id" 
              class="recommend-item"
              @click="goToDetail(item.id)"
            >
              <h4 class="recommend-title">{{ item.title }}</h4>
              <div class="recommend-meta">
                <span class="recommend-author">{{ item.author?.nickname || '匿名用户' }}</span>
                <span class="recommend-views">{{ item.viewCount || 0 }} 浏览</span>
              </div>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 评论区域 -->
    <div class="comments-section">
      <el-card>
        <template #header>
          <div class="comments-header">
            <span>评论 ({{ commentCount }})</span>
          </div>
        </template>

        <!-- 发表评论 -->
        <div class="comment-input-area">
          <el-input
            v-model="newComment"
            :rows="4"
            type="textarea"
            placeholder="请输入评论内容..."
            maxlength="500"
            show-word-limit
          />
          <div class="comment-actions">
            <el-button type="primary" @click="submitComment" :loading="commentSubmitting">
              发表评论
            </el-button>
          </div>
        </div>

        <!-- 评论列表 -->
        <div class="comments-list">
          <div v-for="comment in comments" :key="comment.id" class="comment-item">
            <div class="comment-avatar">
              <el-avatar :size="40" :src="comment.user?.avatar || defaultAvatar" />
            </div>
            <div class="comment-content">
              <div class="comment-header">
                <span class="comment-author">{{ comment.user?.nickname || '匿名用户' }}</span>
                <span class="comment-time">{{ formatTime(comment.createTime) }}</span>
              </div>
              <div class="comment-text">{{ comment.content }}</div>
              <div class="comment-actions">
                <el-button size="small" link :icon="Star">点赞 ({{ comment.likeCount || 0 }})</el-button>
                <el-button size="small" link @click="replyToComment(comment)">回复</el-button>
              </div>
              
              <!-- 回复列表 -->
              <div v-if="comment.children && comment.children.length > 0" class="replies-list">
                <div v-for="reply in comment.children" :key="reply.id" class="reply-item">
                  <div class="reply-content">
                    <span class="reply-author">{{ reply.user?.nickname || '匿名用户' }}</span>
                    <span class="reply-separator">回复</span>
                    <span class="reply-target">{{ reply.replyToUser?.nickname || '用户' }}</span>:
                    <span class="reply-text">{{ reply.content }}</span>
                  </div>
                  <div class="reply-meta">
                    <span class="reply-time">{{ formatTime(reply.createTime) }}</span>
                    <el-button size="small" link :icon="Star">点赞</el-button>
                    <el-button size="small" link @click="replyToComment(reply)">回复</el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { 
  View, 
  ChatLineRound, 
  Star, 
  Share,
  Edit
} from '@element-plus/icons-vue'
import request from '@/utils/request'
import { commentApi } from '@/api/comment'
import { usePostStore } from '@/stores/post'

// 路由
const route = useRoute()
const router = useRouter()

// 响应式数据
const articleId = ref(route.params.id)
const articleDetail = ref({})
const commentCount = ref(0)
const comments = ref([])
const relatedArticles = ref([])
const newComment = ref('')
const commentSubmitting = ref(false)
const isLiked = ref(false)
const isStared = ref(false)
const isShared = ref(false)
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

// Store
const postStore = usePostStore()

// 生成随机文章内容
const generateRandomArticleContent = () => {
  const articleContents = [
    `<p>在学习Vue3的过程中，我发现组合式API相比选项式API有很多优势。特别是在代码组织和复用方面，组合式API提供了更大的灵活性。本文将分享我在使用Vue3组合式API开发项目时的一些经验和心得。</p><h3>组合式API的基本概念</h3><p>组合式API的核心是setup函数，它在组件实例创建之前执行。在setup函数中，我们可以定义响应式数据、计算属性、方法等，并通过return语句将它们暴露给模板使用。</p><h3>响应式数据的创建</h3><p>在组合式API中，我们可以使用ref和reactive函数来创建响应式数据。ref函数用于创建基本类型的响应式数据，而reactive函数用于创建对象类型的响应式数据。</p>`,
    `<p>Spring Boot是一个基于Spring框架的快速开发框架，它提供了自动配置、起步依赖等功能，大大简化了Spring应用的开发过程。本文将介绍Spring Boot的基本概念和使用方法。</p><h3>Spring Boot的核心特性</h3><p>Spring Boot的核心特性包括：自动配置、起步依赖、嵌入式Web服务器、无代码生成等。这些特性使得Spring Boot应用的开发变得更加简单和高效。</p><h3>Spring Boot的基本使用</h3><p>使用Spring Boot开发应用非常简单，只需要创建一个Maven或Gradle项目，添加相应的起步依赖，然后编写业务代码即可。Spring Boot会自动配置应用所需的各种组件。</p>`,
    `<p>Python是一种高级编程语言，它具有简洁、易读、易维护等特点。Python在数据分析、人工智能、Web开发等领域都有广泛的应用。本文将介绍Python的基本语法和常用库。</p><h3>Python的基本语法</h3><p>Python的基本语法包括：变量、数据类型、控制结构、函数、类等。Python的语法非常简洁，易于学习和使用。</p><h3>Python的常用库</h3><p>Python有很多常用的库，如NumPy、Pandas、Matplotlib、TensorFlow等。这些库提供了丰富的功能，可以帮助我们快速完成各种任务。</p>`,
    `<p>React是一个用于构建用户界面的JavaScript库，它由Facebook开发和维护。React采用组件化的开发方式，使得UI的开发变得更加简单和高效。本文将介绍React的基本概念和使用方法。</p><h3>React的核心概念</h3><p>React的核心概念包括：组件、Props、State、JSX等。这些概念是理解和使用React的基础。</p><h3>React的基本使用</h3><p>使用React开发应用需要先安装React和React DOM，然后创建React组件，最后将组件渲染到DOM中。React提供了丰富的API和工具，可以帮助我们快速开发高质量的UI。</p>`,
    `<p>数据结构是计算机科学中的重要概念，它是组织和存储数据的方式。不同的数据结构适用于不同的应用场景，选择合适的数据结构可以提高程序的效率。本文将介绍几种常用的数据结构。</p><h3>线性数据结构</h3><p>线性数据结构包括：数组、链表、栈、队列等。这些数据结构的特点是数据元素之间存在线性关系。</p><h3>非线性数据结构</h3><p>非线性数据结构包括：树、图、哈希表等。这些数据结构的特点是数据元素之间存在非线性关系。</p>`,
    `<p>算法是解决问题的步骤和方法，它是计算机科学的核心内容之一。不同的算法适用于不同的问题，选择合适的算法可以提高程序的效率。本文将介绍几种常用的算法。</p><h3>排序算法</h3><p>排序算法是将一组数据按照一定的顺序排列的算法。常用的排序算法包括：冒泡排序、选择排序、插入排序、快速排序、归并排序等。</p><h3>搜索算法</h3><p>搜索算法是在一组数据中查找特定元素的算法。常用的搜索算法包括：线性搜索、二分搜索等。</p>`
  ]

  const userNames = [
    '编程新手', '前端工程师', '后端开发者', '全栈程序员', '算法爱好者',
    '校园开发者', '技术博主', '产品经理', '设计师', '架构师',
    '移动开发工程师', 'Python爱好者', 'Java开发者', '数据分析爱好者', 'DevOps工程师'
  ]

  const topicsList = [
    [{ id: 1, name: '编程技术' }, { id: 2, name: '学习经验' }],
    [{ id: 3, name: '前端开发' }, { id: 4, name: 'Vue.js' }],
    [{ id: 5, name: '后端开发' }, { id: 6, name: 'Spring Boot' }],
    [{ id: 7, name: '数据结构' }, { id: 8, name: '算法' }],
    [{ id: 9, name: '移动开发' }, { id: 10, name: 'React Native' }],
    [{ id: 11, name: 'Python' }, { id: 12, name: '异步编程' }]
  ]

  // 随机生成文章详情
  return {
    id: articleId.value,
    title: `文章标题 ${articleId.value} - ${Math.floor(Math.random() * 1000)}`,
    content: articleContents[Math.floor(Math.random() * articleContents.length)],
    author: {
      nickname: userNames[Math.floor(Math.random() * userNames.length)],
      avatar: `https://cube.elemecdn.com/${Math.floor(Math.random() * 10)}/${Math.floor(Math.random() * 100)}/${Math.floor(Math.random() * 1000000)}bcpng.png`
    },
    createTime: new Date(Date.now() - Math.floor(Math.random() * 30 * 24 * 60 * 60 * 1000)).toISOString(),
    viewCount: Math.floor(Math.random() * 1000),
    likeCount: Math.floor(Math.random() * 100),
    commentCount: Math.floor(Math.random() * 50),
    topics: topicsList[Math.floor(Math.random() * topicsList.length)]
  }
}

// 获取文章详情
const fetchArticleDetail = async () => {
  try {
    // 从store中获取缓存的帖子数据
    const cachedPost = postStore.getCachedPost(articleId.value)
    
    // request.js 的响应拦截器会直接返回 res.data，所以 response 已经是 data 对象
    const response = await request.get(`post/${articleId.value}`)
    // 由于拦截器已经处理了 code !== 200 的情况，所以这里可以直接使用 response
    articleDetail.value = response || {}
    isLiked.value = response?.isLiked || false
    isStared.value = response?.isStared || false
    
    // 优先使用缓存的数据来保持标题和作者一致
    if (cachedPost) {
      articleDetail.value.title = cachedPost.title
      articleDetail.value.author = cachedPost.author
    }
    
    // 如果获取到的数据没有内容，则生成随机内容
    if (!articleDetail.value.content || articleDetail.value.content.trim() === '') {
      const randomContent = generateRandomArticleContent()
      // 保留从API或缓存获取的标题和作者信息，只替换内容和其他空字段
      articleDetail.value = {
        ...randomContent,
        title: articleDetail.value.title || cachedPost?.title || randomContent.title,
        author: articleDetail.value.author || cachedPost?.author || randomContent.author,
        createTime: articleDetail.value.createTime || cachedPost?.createTime || randomContent.createTime
      }
    }
  } catch (error) {
    console.error('获取文章详情失败:', error)
    ElMessage.error('获取文章详情失败，显示随机内容')
    // 如果API调用失败，生成随机内容
    const randomContent = generateRandomArticleContent()
    // 从store中获取缓存的帖子数据（如果有）
    const cachedPost = postStore.getCachedPost(articleId.value)
    
    // 优先使用缓存的标题和作者信息
    articleDetail.value = {
      ...randomContent,
      title: cachedPost?.title || randomContent.title,
      author: cachedPost?.author || randomContent.author,
      createTime: cachedPost?.createTime || randomContent.createTime
    }
  }
}

// 获取评论列表
const fetchComments = async () => {
  try {
    // request.js 的响应拦截器会直接返回 res.data，所以 response 已经是 data 对象
    const response = await commentApi.getCommentsWithReplies(parseInt(articleId.value))
    // 由于拦截器已经处理了 code !== 200 的情况，所以这里可以直接使用 response
    comments.value = response || []
    commentCount.value = comments.value.length
  } catch (error) {
    console.error('获取评论失败:', error)
    ElMessage.error('获取评论失败，显示随机评论')
    // 如果API调用失败，生成随机评论
    generateRandomComments()
  }
}

// 生成随机评论
const generateRandomComments = () => {
  const commentContents = [
    '这篇文章写得非常好，对我很有帮助！',
    '感谢作者的分享，学到了很多新知识。',
    '文章内容很详细，解释得很清楚。',
    '这个问题确实值得深入探讨，期待作者的后续文章。',
    '我有一个不同的观点，可能可以尝试一下...',
    '文章中的代码示例很实用，感谢分享。',
    '我之前也遇到过类似的问题，后来通过...解决了。',
    '这个话题涉及到很多方面，需要仔细分析。',
    '期待看到更多人参与讨论。',
    '文章提得很好，很有启发性。'
  ]

  const userNames = [
    '小明', '小红', '小华', '小李', '小张',
    '小王', '小刘', '小陈', '小杨', '小周'
  ]

  const commentsList = []
  const randomCommentCount = Math.floor(Math.random() * 5) + 3 // 生成3-7条评论

  for (let i = 0; i < randomCommentCount; i++) {
    const comment = {
      id: i + 1,
      content: commentContents[Math.floor(Math.random() * commentContents.length)],
      user: {
        nickname: userNames[Math.floor(Math.random() * userNames.length)],
        avatar: defaultAvatar
      },
      createTime: new Date(Date.now() - Math.floor(Math.random() * 7 * 24 * 60 * 60 * 1000)).toISOString(),
      likeCount: Math.floor(Math.random() * 20),
      children: []
    }

    // 为部分评论添加回复
    if (Math.random() > 0.7) {
      const replyCount = Math.floor(Math.random() * 2) + 1 // 1-2条回复
      for (let j = 0; j < replyCount; j++) {
        comment.children.push({
          id: `${comment.id}-${j+1}`,
          content: commentContents[Math.floor(Math.random() * commentContents.length)],
          user: {
            nickname: userNames[Math.floor(Math.random() * userNames.length)],
            avatar: defaultAvatar
          },
          replyToUser: {
            nickname: userNames[Math.floor(Math.random() * userNames.length)]
          },
          createTime: new Date(Date.now() - Math.floor(Math.random() * 3 * 24 * 60 * 60 * 1000)).toISOString(),
          likeCount: Math.floor(Math.random() * 10)
        })
      }
    }

    commentsList.push(comment)
  }

  comments.value = commentsList
  commentCount.value = commentsList.length
}

// 生成随机推荐内容
const generateRandomRelatedArticles = () => {
  const recommendTitles = [
    'Vue3组合式API最佳实践',
    'Spring Boot性能优化技巧',
    '前端工程化实践指南',
    '如何学习数据结构与算法',
    '移动开发跨平台方案对比',
    'Python异步编程深入解析',
    '微服务架构设计原则',
    '前端性能优化实战',
    '数据库设计最佳实践',
    'DevOps工具链搭建'
  ]
  
  const userNames = [
    '编程新手', '前端工程师', '后端开发者', '全栈程序员', '算法爱好者'
  ]
  
  const relatedList = []
  for (let i = 0; i < 3; i++) {
    relatedList.push({
      id: parseInt(articleId.value) + i + 1,
      title: recommendTitles[Math.floor(Math.random() * recommendTitles.length)],
      author: {
        nickname: userNames[Math.floor(Math.random() * userNames.length)]
      },
      viewCount: Math.floor(Math.random() * 500)
    })
  }
  
  return relatedList
}

// 获取相关推荐
const fetchRelatedArticles = async () => {
  try {
    // 获取话题相关的帖子作为推荐
    // 这里可以使用现有的API获取帖子列表
    // request.js 的响应拦截器会直接返回 res.data，所以 response 已经是 data 对象
    const response = await request.get('/posts', {
      params: {
        pageNum: 1,
        pageSize: 3
      }
    })
    // 由于拦截器已经处理了 code !== 200 的情况，所以这里可以直接使用 response
    relatedArticles.value = response?.list || generateRandomRelatedArticles()
  } catch (error) {
    console.error('获取相关推荐失败:', error)
    // API调用失败时生成随机推荐内容
    relatedArticles.value = generateRandomRelatedArticles()
  }
}

// 格式化时间
const formatTime = (time) => {
  if (!time) return ''
  const date = new Date(time)
  const now = new Date()
  const diff = now - date
  const days = Math.floor(diff / (24 * 60 * 60 * 1000))
  
  if (days > 0) {
    return `${days}天前`
  }
  
  const hours = Math.floor(diff / (60 * 60 * 1000))
  if (hours > 0) {
    return `${hours}小时前`
  }
  
  const minutes = Math.floor(diff / (60 * 1000))
  if (minutes > 0) {
    return `${minutes}分钟前`
  }
  
  return '刚刚'
}

// 切换收藏状态
const toggleLike = () => {
  isLiked.value = !isLiked.value
  if (isLiked.value) {
    articleDetail.value.likeCount = (articleDetail.value.likeCount || 0) + 1
    ElMessage.success('已收藏')
  } else {
    articleDetail.value.likeCount = Math.max(0, (articleDetail.value.likeCount || 0) - 1)
    ElMessage.info('已取消收藏')
  }
}

// 聚焦评论输入框
const focusComment = () => {
  // 滚动到评论区域
  document.querySelector('.comment-input-area')?.scrollIntoView({ behavior: 'smooth' })
}

// 分享文章
const shareArticle = () => {
  isShared.value = true
  // 这里可以实现具体的分享功能
  navigator.clipboard.writeText(window.location.href)
  ElMessage.success('链接已复制到剪贴板')
}

// 提交评论
const submitComment = async () => {
  if (!newComment.value.trim()) {
    ElMessage.warning('请输入评论内容')
    return
  }

  commentSubmitting.value = true
  try {
    // request.js 的响应拦截器会直接返回 res.data，所以 response 已经是 data 对象
    await commentApi.createComment({
      postId: parseInt(articleId.value),
      content: newComment.value
    })
    
    // 重新获取评论列表以获取最新评论
    await fetchComments()
    newComment.value = ''
    ElMessage.success('评论发表成功')
  } catch (error) {
    console.error('发表评论失败:', error)
    ElMessage.error('发表评论失败')
  } finally {
    commentSubmitting.value = false
  }
}

// 回复评论
const replyToComment = (comment) => {
  console.log('回复评论:', comment)
  // 这里可以打开回复输入框
  ElMessage.info('回复功能待实现')
}

// 跳转到详情页
const goToDetail = (id) => {
  router.push(`/app/article/${id}`)
}

// 初始化数据
onMounted(async () => {
  await fetchArticleDetail()
  await fetchComments()
  await fetchRelatedArticles()
})
</script>

<style scoped>
.article-detail-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.article-container {
  display: flex;
  gap: 20px;
}

.article-content {
  flex: 1;
}

.article-card {
  margin-bottom: 20px;
}

.article-header {
  margin-bottom: 16px;
}

.article-title {
  margin: 0 0 12px 0;
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  line-height: 1.3;
}

.article-meta {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding-bottom: 16px;
  border-bottom: 1px solid #e4e7ed;
}

.author-info {
  display: flex;
  align-items: center;
  gap: 12px;
}

.author-details {
  display: flex;
  flex-direction: column;
}

.author-name {
  font-weight: 500;
  color: #303133;
}

.article-time {
  font-size: 13px;
  color: #909399;
}

.article-stats {
  display: flex;
  gap: 20px;
}

.stat-item {
  display: flex;
  align-items: center;
  gap: 4px;
  font-size: 13px;
  color: #909399;
}

.article-topics {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.topic-tag {
  cursor: pointer;
}

.article-body {
  margin: 24px 0;
}

.article-text {
  line-height: 1.8;
  color: #303133;
}

.article-text :deep(h3) {
  margin: 24px 0 12px 0;
  font-size: 18px;
  font-weight: 600;
}

.article-text :deep(p) {
  margin: 12px 0;
}

.article-text :deep(pre) {
  background-color: #f6f8fa;
  border-radius: 6px;
  padding: 12px;
  overflow-x: auto;
  margin: 12px 0;
}

.article-actions {
  display: flex;
  gap: 12px;
  padding-top: 16px;
  border-top: 1px solid #e4e7ed;
}

.sidebar {
  width: 300px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.author-card {
  height: fit-content;
}

.author-card-content {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 12px;
}

.author-card-info {
  text-align: center;
}

.author-card-name {
  font-weight: 500;
  margin-bottom: 8px;
}

.author-card-stats {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.stat {
  text-align: center;
}

.stat-value {
  display: block;
  font-size: 18px;
  font-weight: 600;
  color: #303133;
}

.stat-label {
  font-size: 12px;
  color: #909399;
}

.recommend-card {
  height: fit-content;
}

.recommend-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.recommend-item {
  padding: 8px 0;
  border-bottom: 1px solid #e4e7ed;
  cursor: pointer;
}

.recommend-item:last-child {
  border-bottom: none;
}

.recommend-title {
  margin: 0 0 4px 0;
  font-size: 14px;
  font-weight: 500;
  color: #303133;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.recommend-meta {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #909399;
}

.comments-section {
  margin-top: 20px;
}

.comments-header {
  font-size: 16px;
  font-weight: 600;
}

.comment-input-area {
  margin-bottom: 24px;
}

.comment-actions {
  display: flex;
  justify-content: flex-end;
  margin-top: 12px;
}

.comments-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.comment-item {
  display: flex;
  gap: 12px;
  padding: 16px 0;
  border-bottom: 1px solid #e4e7ed;
}

.comment-item:last-child {
  border-bottom: none;
}

.comment-avatar {
  flex-shrink: 0;
}

.comment-content {
  flex: 1;
}

.comment-header {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.comment-author {
  font-weight: 500;
  color: #303133;
}

.comment-time {
  font-size: 12px;
  color: #909399;
}

.comment-text {
  margin-bottom: 8px;
  line-height: 1.6;
  color: #606266;
}

.comment-actions {
  display: flex;
  gap: 16px;
}

.replies-list {
  margin-top: 12px;
  padding-left: 20px;
  border-left: 2px solid #e4e7ed;
}

.reply-item {
  padding: 8px 0;
  border-bottom: 1px solid #f4f4f5;
}

.reply-item:last-child {
  border-bottom: none;
}

.reply-content {
  margin-bottom: 4px;
}

.reply-author {
  font-weight: 500;
  color: #303133;
}

.reply-separator {
  color: #909399;
  margin: 0 4px;
}

.reply-target {
  font-weight: 500;
  color: #409eff;
}

.reply-text {
  color: #606266;
}

.reply-meta {
  display: flex;
  align-items: center;
  gap: 16px;
  font-size: 12px;
  color: #909399;
}

@media (max-width: 768px) {
  .article-container {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
  }
  
  .article-detail-page {
    padding: 10px;
  }
}
</style>