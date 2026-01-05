<template>
  <div class="article-list">
    <el-empty v-if="!articles || articles.length === 0" description="暂无文章"></el-empty>
    <div v-else class="article-items">
      <article-card 
        v-for="article in articles" 
        :key="article.id" 
        :article="article" 
      />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import request from '@/utils/request'

const props = defineProps({
  articles: {
    type: Array,
    default: () => []
  }
})
</script>

<style scoped>
.article-list {
  padding: 10px;
}

.article-items {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
}

@media (max-width: 768px) {
  .article-items {
    grid-template-columns: 1fr;
  }
}
</style>