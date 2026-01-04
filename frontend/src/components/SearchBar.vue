<template>
  <div class="search-bar">
    <el-input 
      v-model="keyword" 
      placeholder="搜索话题、文章或用户..." 
      prefix-icon="Search" 
      @keyup.enter="handleSearch" 
      @focus="showSuggestions = true" 
      @blur="setTimeout(() => { showSuggestions = false }, 200)" 
    />
    <button @click="handleSearch" class="search-btn">搜索</button>

    <!-- 搜索建议（可选） -->
    <div v-if="showSuggestions && keyword" class="suggestions">
      <div class="suggestion-item" @click="selectSuggestion('图书馆')">图书馆</div>
      <div class="suggestion-item" @click="selectSuggestion('C++')">C++</div>
      <div class="suggestion-item" @click="selectSuggestion('社团活动')">社团活动</div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const keyword = ref('')
const showSuggestions = ref(false)

// 处理搜索
const handleSearch = () => {
  if (!keyword.value.trim()) return
  router.push({ path: '/app/search', query: { q: keyword.value } })
}

// 选择建议项
const selectSuggestion = (text) => {
  keyword.value = text
  handleSearch()
}
</script>

<style scoped>
.search-bar {
  display: flex;
  align-items: center;
  gap: 10px;
  position: relative;
}

.el-input {
  width: 300px;
}

.search-btn {
  padding: 8px 16px;
  background: #1976d2;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.suggestions {
  position: absolute;
  top: 100%;
  left: 0;
  background: white;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
  z-index: 100;
  max-height: 200px;
  overflow-y: auto;
}

.suggestion-item {
  padding: 8px 12px;
  cursor: pointer;
  border-bottom: 1px solid #eee;
}

.suggestion-item:hover {
  background: #f0f0f0;
}
</style>