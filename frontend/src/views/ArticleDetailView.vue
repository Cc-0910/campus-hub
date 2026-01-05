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
                  <el-icon><Star /></el-icon>
                  <span>{{ articleDetail.likeCount || 0 }} 点赞</span>
                </div>
                <div class="stat-item">
                  <el-icon><ChatLineRound /></el-icon>
                  <span>{{ commentCount }} 评论</span>
                </div>
              </div>
            </div>
          </div>

          <!-- 文章标签 -->
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
              {{ isLiked ? '已点赞' : '点赞' }} {{ articleDetail.likeCount || 0 }}
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
                    <span class="reply-target">{{ reply.replyToUser?.nickname || '用户' }}:</span>
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

<script setup lang="ts">
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
    `<p>在大学校园里，生活的方方面面都需要我们自己去管理和规划。从学习到生活，从社交到个人成长，每一个环节都至关重要。</p><p>首先，学习是大学生活的核心。我们需要合理安排时间，制定学习计划，充分利用图书馆和自习室等资源。同时，也要积极参与课堂讨论，与老师和同学保持良好的沟通。</p><p>其次，生活方面也不能忽视。保持健康的饮食习惯，定期进行体育锻炼，保证充足的睡眠，这些都是保持良好状态的基础。此外，学会理财也是一项重要的技能，可以帮助我们更好地管理自己的生活费用。</p><p>最后，社交和个人成长也同样重要。参加社团活动，结交志同道合的朋友，培养自己的兴趣爱好，这些都有助于我们拓展视野，提高自己的综合素质。</p><p>总之，大学生活是一段宝贵的经历，我们应该珍惜这段时光，努力让自己变得更加优秀。</p>`,
    `<p>随着互联网技术的不断发展，新媒体已经成为我们生活中不可或缺的一部分。从社交媒体到短视频平台，从博客到直播，新媒体的形式多种多样，影响着我们的生活方式和思维方式。</p><p>新媒体的出现为我们提供了更多的信息获取渠道和表达方式。我们可以通过社交媒体了解时事新闻，通过短视频学习新知识，通过博客分享自己的观点和经验。同时，新媒体也为企业和个人提供了更多的营销和推广渠道。</p><p>然而，新媒体的发展也带来了一些问题。信息过载、虚假信息、隐私泄露等问题日益突出，需要我们保持警惕。此外，过度依赖新媒体也可能影响我们的现实社交能力和专注力。</p><p>因此，我们应该理性看待新媒体，充分利用其优势，同时也要注意避免其带来的负面影响。只有这样，我们才能更好地适应新媒体时代的发展。</p>`,
    `<p>环境保护是当今世界面临的重要挑战之一。随着工业化和城市化的加速发展，环境污染、资源短缺、气候变化等问题日益严重，威胁着人类的生存和发展。</p><p>为了应对这些挑战，我们需要采取一系列措施。首先，政府应该加强环境立法和执法力度，制定严格的环境标准，限制污染物的排放。其次，企业应该承担起社会责任，采用环保技术和生产方式，减少对环境的破坏。</p><p>此外，个人的力量也不可忽视。我们可以从身边的小事做起，比如节约水电、减少使用一次性用品、垃圾分类等。同时，也要积极参与环保宣传和公益活动，提高公众的环保意识。</p><p>环境保护是一项长期而艰巨的任务，需要政府、企业和个人的共同努力。只有这样，我们才能保护好我们的地球家园，为子孙后代创造一个美好的未来。</p>`
  ]

  const userNames = [
    '校园作家', '生活达人', '科技爱好者', '环保志愿者', '文艺青年',
    '旅行博主', '美食家', '健身教练', '摄影爱好者', '历史迷'
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
    topics: [
      { id: 3, name: '校园生活' },
      { id: 4, name: '生活感悟' }
    ]
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
    '这篇文章写得非常好，受益匪浅！',
    '感谢分享，学到了很多新知识。',
    '内容很有深度，值得仔细阅读。',
    '写得很详细，对我帮助很大。',
    '观点很独特，让我有了新的思考。',
    '例子很生动，容易理解。',
    '期待更多类似的文章！',
    '分析得很到位，赞一个！',
    '内容很实用，谢谢分享。',
    '很棒的文章，已收藏。'
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

// 生成随机推荐文章
const generateRandomRelatedArticles = () => {
  const recommendTitles = [
    '校园生活指南',
    '大学生学习方法分享',
    '如何平衡学习与社团活动',
    '校园美食地图',
    '大学生理财入门',
    '实习经验分享',
    '校园活动策划指南',
    '如何提高英语口语',
    '大学生心理健康小贴士',
    '毕业季就业准备'
  ]
  
  const userNames = [
    '校园作家', '生活达人', '学习委员', '社团主席', '实习达人'
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
    const response = await request.get('posts', {
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

// 切换点赞状态
const toggleLike = () => {
  isLiked.value = !isLiked.value
  if (isLiked.value) {
    articleDetail.value.likeCount = (articleDetail.value.likeCount || 0) + 1
    ElMessage.success('已点赞')
  } else {
    articleDetail.value.likeCount = Math.max(0, (articleDetail.value.likeCount || 0) - 1)
    ElMessage.info('已取消点赞')
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
  router.push(`/app/articles/${id}`)
}

// 初始化数据
onMounted(async () => {
  await fetchArticleDetail()
  await fetchComments()
  await fetchRelatedArticles()
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