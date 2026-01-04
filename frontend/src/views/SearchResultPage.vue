<template>
  <div class="search-results-container">
    <div class="search-header">
      <h2>搜索结果</h2>
      <p v-if="keyword">关键词： "{{ keyword }}"</p>
      <p v-else>请输入关键词进行搜索</p>
    </div>

    <div v-if="loading" class="loading">
      <el-skeleton :rows="4" animated />
    </div>

    <div v-else-if="results.length === 0" class="no-results">
      <el-empty description="没有找到相关结果" />
    </div>

    <div v-else class="results-list">
      <el-card 
        v-for="item in results" 
        :key="item.id"
        class="result-item"
        @click="goToDetail(item)"
      >
        <div class="result-content">
          <h3 class="result-title">{{ item.title }}</h3>
          <p class="result-summary">{{ item.summary }}</p>
          <div class="result-meta">
            <span class="result-type">{{ item.type === 'topic' ? '话题' : '文章' }}</span>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { search } from '@/api/search'

const route = useRoute()
const router = useRouter()

const keyword = ref('')
const results = ref([])
const loading = ref(false)

// 监听路由参数变化
watch(
  () => route.query.keyword,
  (newKeyword) => {
    keyword.value = newKeyword || ''
    if (keyword.value) {
      performSearch()
    } else {
      results.value = []
    }
  },
  { immediate: true }
)

// 执行搜索
const performSearch = async () => {
  if (!keyword.value.trim()) {
    results.value = []
    return
  }

  loading.value = true
  try {
    const response = await search(keyword.value)
    results.value = response.data || []
  } catch (error) {
    console.error('搜索失败:', error)
    results.value = []
  } finally {
    loading.value = false
  }
}

// 跳转到详情页
const goToDetail = (item) => {
  if (item.type === 'topic') {
    router.push(`/app/topics/${item.id}`)
  } else {
    // 假设文章详情页路径为 /app/articles/:id
    router.push(`/app/articles/${item.id}`)
  }
}
</script>

<style scoped>
.search-results-container {
  max-width: 800px;
  margin: 0 auto;
  padding: 20px;
}

.search-header {
  margin-bottom: 30px;
  text-align: center;
}

.search-header h2 {
  color: var(--campus-text-primary);
  margin-bottom: 10px;
}

.search-header p {
  color: var(--campus-text-secondary);
}

.loading {
  margin: 20px 0;
}

.no-results {
  text-align: center;
  padding: 60px 0;
}

.result-item {
  margin-bottom: 15px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.result-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.result-content {
  padding: 10px 0;
}

.result-title {
  margin: 0 0 10px 0;
  font-size: 18px;
  font-weight: 600;
  color: var(--campus-text-primary);
  line-height: 1.4;
}

.result-summary {
  margin: 0 0 15px 0;
  color: var(--campus-text-secondary);
  line-height: 1.5;
  font-size: 14px;
}

.result-meta {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.result-type {
  font-size: 12px;
  color: var(--campus-primary);
  background: rgba(64, 158, 255, 0.1);
  padding: 2px 8px;
  border-radius: 12px;
}
</style>