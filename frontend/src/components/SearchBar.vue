<template>
  <div class="search-bar">
    <el-input
      v-model="searchKeyword"
      class="search-input"
      placeholder="搜索话题或文章..."
      :prefix-icon="Search"
      @keyup.enter="handleSearch"
      clearable
    />
    <div style="color: red; font-weight: bold; margin-top: 5px;">
      实时调试值: [{{ searchKeyword }}] (长度: {{ searchKeyword.length }})
    </div>
  </div>
</template>

<script setup>
import { ref, defineExpose, onMounted } from 'vue'
import { Search } from '@element-plus/icons-vue'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()
const searchKeyword = ref('')

// 组件加载时检查路由参数，回显关键词
onMounted(() => {
  if (route.query.keyword) {
    searchKeyword.value = route.query.keyword
  }
})

// 处理搜索
const handleSearch = () => {
  if (searchKeyword.value.trim()) {
    router.push({ 
      path: '/search', 
      query: { keyword: searchKeyword.value.trim() } 
    })
  }
}

// 暴露变量和方法，以便调试
defineExpose({
  searchKeyword
})
</script>

<style scoped>
.search-bar {
  width: 100%;
}

.search-input {
  width: 100%;
}
</style>