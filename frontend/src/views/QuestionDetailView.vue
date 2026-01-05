<template>
  <div class="question-detail-page">
    <div class="question-container">
      <!-- 问题内容区域 -->
      <div class="question-content">
        <el-card class="question-card">
          <!-- 问题头部 -->
          <div class="question-header">
            <h1 class="question-title">{{ questionDetail.title }}</h1>
            <div class="question-meta">
              <div class="author-info">
                <el-avatar :size="32" :src="questionDetail.author?.avatar || defaultAvatar" />
                <div class="author-details">
                  <div class="author-name">{{ questionDetail.author?.nickname || '匿名用户' }}</div>
                  <div class="question-time">{{ formatTime(questionDetail.createTime) }}</div>
                </div>
              </div>
              <div class="question-stats">
                <div class="stat-item">
                  <el-icon><View /></el-icon>
                  <span>{{ questionDetail.viewCount || 0 }} 浏览</span>
                </div>
                <div class="stat-item">
                  <el-icon><ChatLineRound /></el-icon>
                  <span>{{ commentCount }} 评论</span>
                </div>
                <div class="stat-item">
                  <el-icon><Star /></el-icon>
                  <span>{{ questionDetail.likeCount || 0 }} 收藏</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 话题标签 -->
          <div v-if="questionDetail.topics && questionDetail.topics.length > 0" class="question-topics">
            <el-tag 
              v-for="topic in questionDetail.topics" 
              :key="topic.id" 
              size="small" 
              type="info"
              class="topic-tag"
            >
              {{ topic.name }}
            </el-tag>
          </div>

          <!-- 问题正文 -->
          <div class="question-body">
            <div class="question-text" v-html="questionDetail.content"></div>
          </div>

          <!-- 问题操作 -->
          <div class="question-actions">
            <el-button 
              :type="isLiked ? 'primary' : 'default'" 
              :icon="Star" 
              @click="toggleLike"
            >
              {{ isLiked ? '已收藏' : '收藏' }} {{ questionDetail.likeCount || 0 }}
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
              @click="shareQuestion"
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
            <el-avatar :size="60" :src="questionDetail.author?.avatar || defaultAvatar" />
            <div class="author-card-info">
              <div class="author-card-name">{{ questionDetail.author?.nickname || '匿名用户' }}</div>
              <div class="author-card-stats">
                <div class="stat">
                  <span class="stat-value">12</span>
                  <span class="stat-label">问题</span>
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
              v-for="item in relatedQuestions" 
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
import { commentApi } from '@/api/comment'
import { topicApi } from '@/api/topic'
import request from '@/utils/request'
import { usePostStore } from '@/stores/post'

// 路由
const route = useRoute()
const router = useRouter()

// 响应式数据
const questionId = ref(route.params.id)
const questionDetail = ref({})
const commentCount = ref(0)
const comments = ref([])
const relatedQuestions = ref([])
const newComment = ref('')
const commentSubmitting = ref(false)
const isLiked = ref(false)
const isStared = ref(false)
const isShared = ref(false)
const defaultAvatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'

// Store
const postStore = usePostStore()

// 生成随机问题内容
const generateRandomQuestionContent = () => {
  const questionContents = [
    `<p>最近在学习Vue3的组合式API，感觉和之前的选项式API有很大不同。特别是在处理响应式数据和生命周期方面，想了解一下大家是如何快速上手的？</p><p>我目前遇到的主要问题是：</p><ul><li>如何更好地组织代码结构？</li><li>组合式API和TypeScript结合使用时有什么最佳实践？</li><li>在大型项目中如何避免代码冗余？</li></ul><p>希望有经验的开发者能够分享一下你们的经验和建议，谢谢！</p>`,
    `<p>正在开发一个校园论坛网站，需要实现帖子的点赞和收藏功能。想了解一下在前后端分离的架构下，如何设计这两个功能的API和数据结构？</p><p>具体来说，我想知道：</p><ul><li>点赞和收藏的数据应该如何存储？</li><li>前端如何处理点赞和收藏的状态管理？</li><li>如何实现点赞和收藏的实时更新？</li></ul><p>期待大家的回答，不胜感激！</p>`,
    `<p>最近在学习数据结构和算法，想找一些适合新手的练习题目和学习资源。请问大家有什么推荐吗？</p><p>我目前的基础是：</p><ul><li>了解基本的数据结构（数组、链表、栈、队列）</li><li>掌握基本的排序和搜索算法</li><li>能够用JavaScript实现简单的算法题目</li></ul><p>希望能够进一步提升自己的算法能力，特别是解决实际问题的能力。</p>`,
    `<p>在学习Spring Boot的过程中，遇到了一些关于依赖注入和事务管理的问题。请问大家有什么好的学习资源或者经验可以分享吗？</p><p>我主要的困惑点在于：</p><ul><li>如何正确使用@Autowired和@Resource注解？</li><li>事务传播行为应该如何选择？</li><li>如何解决事务嵌套问题？</li></ul><p>感谢大家的帮助！</p>`,
    `<p>最近在做一个移动端的项目，需要实现一个高性能的列表组件。请问在React Native中，如何优化长列表的性能？</p><p>我目前遇到的问题是：</p><ul><li>列表滚动时出现卡顿</li><li>大量数据加载时内存占用过高</li><li>图片加载影响性能</li></ul><p>希望有经验的开发者能够分享一下优化方案。</p>`,
    `<p>在学习Python的过程中，想了解一下Python的异步编程。请问asyncio库应该如何使用？有什么好的学习资源吗？</p><p>我主要想知道：</p><ul><li>如何创建和运行协程？</li><li>如何处理异步IO操作？</li><li>异步编程有哪些最佳实践？</li></ul><p>期待大家的回答！</p>`
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

  // 随机生成问题详情
  return {
    id: questionId.value,
    title: `问答标题 ${questionId.value} - ${Math.floor(Math.random() * 1000)}`,
    content: questionContents[Math.floor(Math.random() * questionContents.length)],
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

// 获取问题详情
const fetchQuestionDetail = async () => {
  try {
    // 从store中获取缓存的帖子数据
    const cachedPost = postStore.getCachedPost(questionId.value)
    
    // request.js 的响应拦截器会直接返回 res.data，所以 response 已经是 data 对象
    const response = await request.get(`post/${questionId.value}`)
    // 由于拦截器已经处理了 code !== 200 的情况，所以这里可以直接使用 response
    // 但要注意，response 包含 code、message 和 data 字段
    const postData = response?.data || {}
    questionDetail.value = postData || {}
    isLiked.value = postData?.isLiked || false
    isStared.value = postData?.isStared || false
    
    // 优先使用缓存的数据来保持标题和作者一致
    if (cachedPost) {
      questionDetail.value.title = cachedPost.title
      questionDetail.value.author = cachedPost.author
    }
    
    // 如果获取到的数据没有内容，则生成随机内容
    if (!questionDetail.value.content || questionDetail.value.content.trim() === '') {
      const randomContent = generateRandomQuestionContent()
      // 保留从API或缓存获取的标题和作者信息，只替换内容和其他空字段
      questionDetail.value = {
        ...randomContent,
        title: questionDetail.value.title || cachedPost?.title || randomContent.title,
        author: questionDetail.value.author || cachedPost?.author || randomContent.author,
        createTime: questionDetail.value.createTime || cachedPost?.createTime || randomContent.createTime
      }
    }
  } catch (error) {
    console.error('获取问题详情失败:', error)
    ElMessage.error('获取问题详情失败，显示随机内容')
    // 如果API调用失败，生成随机内容
    const randomContent = generateRandomQuestionContent()
    // 从store中获取缓存的帖子数据（如果有）
    const cachedPost = postStore.getCachedPost(questionId.value)
    
    // 优先使用缓存的标题和作者信息
    questionDetail.value = {
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
    const response = await commentApi.getCommentsWithReplies(parseInt(questionId.value))
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
    '这个问题很有价值，我也遇到过类似的情况。',
    '感谢分享，我学到了很多新知识。',
    '问题描述得很清楚，期待看到更多回答。',
    '我有一个不同的观点，可能可以尝试一下...',
    '这个问题确实很难解决，我也在寻找答案。',
    '回答得非常详细，对我帮助很大。',
    '我之前也遇到过同样的问题，后来通过...解决了。',
    '这个问题涉及到很多方面，需要仔细分析。',
    '期待看到更多人参与讨论。',
    '问题提得很好，很有启发性。'
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
const generateRandomRelatedQuestions = () => {
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
      id: parseInt(questionId.value) + i + 1,
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
const fetchRelatedQuestions = async () => {
  try {
    // 获取话题相关的帖子作为推荐
    // 这里可以使用现有的API获取帖子列表
    // request.js 的响应拦截器会直接返回 res.data，所以 response 已经是 data 对象
    const response = await request.get('posts', {
      params: {
        pageNum: 1,
        pageSize: 3
      }
    })
    // 由于拦截器已经处理了 code !== 200 的情况，所以这里可以直接使用 response
    relatedQuestions.value = response?.list || generateRandomRelatedQuestions()
  } catch (error) {
    console.error('获取相关推荐失败:', error)
    // API调用失败时生成随机推荐内容
    relatedQuestions.value = generateRandomRelatedQuestions()
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
    questionDetail.value.likeCount = (questionDetail.value.likeCount || 0) + 1
    ElMessage.success('已收藏')
  } else {
    questionDetail.value.likeCount = Math.max(0, (questionDetail.value.likeCount || 0) - 1)
    ElMessage.info('已取消收藏')
  }
}

// 聚焦评论输入框
const focusComment = () => {
  // 滚动到评论区域
  document.querySelector('.comment-input-area')?.scrollIntoView({ behavior: 'smooth' })
}

// 分享问题
const shareQuestion = () => {
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
      postId: parseInt(questionId.value),
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
  router.push(`/app/qa/${id}`)
}

// 初始化数据
onMounted(async () => {
  await fetchQuestionDetail()
  await fetchComments()
  await fetchRelatedQuestions()
})

const goBack = () => {
  // 返回上一页或话题详情页
  if (window.history.length > 1) {
    router.back()
  } else {
    router.push('/app/topics')
  }
}
</script>

<style scoped>
.question-detail-page {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.question-container {
  display: flex;
  gap: 20px;
}

.question-content {
  flex: 1;
}

.question-card {
  margin-bottom: 20px;
}

.question-header {
  margin-bottom: 16px;
}

.question-title {
  margin: 0 0 12px 0;
  font-size: 28px;
  font-weight: 600;
  color: #303133;
  line-height: 1.3;
}

.question-meta {
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

.question-time {
  font-size: 13px;
  color: #909399;
}

.question-stats {
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

.question-topics {
  display: flex;
  gap: 8px;
  margin-bottom: 16px;
  flex-wrap: wrap;
}

.topic-tag {
  cursor: pointer;
}

.question-body {
  margin: 24px 0;
}

.question-text {
  line-height: 1.8;
  color: #303133;
}

.question-text :deep(h3) {
  margin: 24px 0 12px 0;
  font-size: 18px;
  font-weight: 600;
}

.question-text :deep(p) {
  margin: 12px 0;
}

.question-text :deep(pre) {
  background-color: #f6f8fa;
  border-radius: 6px;
  padding: 12px;
  overflow-x: auto;
  margin: 12px 0;
}

.question-actions {
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
  .question-container {
    flex-direction: column;
  }
  
  .sidebar {
    width: 100%;
  }
  
  .question-detail-page {
    padding: 10px;
  }
}
</style>