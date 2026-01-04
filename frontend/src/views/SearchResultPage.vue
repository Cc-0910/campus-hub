<template>
  <div class="search-results">
    <h2>Search Results for "{{ keyword }}"</h2>
    
    <div v-if="loading" class="loading">
      <el-skeleton :rows="4" />
    </div>
    
    <div v-else-if="results.length === 0" class="no-results">
      <p>No results found for "{{ keyword }}"</p>
    </div>
    
    <div v-else class="results-container">
      <el-card
        v-for="result in results"
        :key="result.id"
        class="result-card"
        @click="goToResult(result)"
      >
        <template #header>
          <div class="card-header">
            <span class="result-type">{{ result.type }}</span>
            <span class="result-title">{{ result.title }}</span>
          </div>
        </template>
        <div class="result-summary">
          {{ result.summary }}
        </div>
      </el-card>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, watch } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { searchAPI } from '@/api/search';

const route = useRoute();
const router = useRouter();
const results = ref([]);
const loading = ref(false);
const keyword = ref('');

// Watch for route changes
watch(
  () => route.query.keyword,
  (newKeyword) => {
    keyword.value = newKeyword || '';
    if (keyword.value) {
      performSearch();
    }
  },
  { immediate: true }
);

const performSearch = async () => {
  if (!keyword.value.trim()) return;
  
  loading.value = true;
  try {
    const response = await searchAPI.search(keyword.value);
    results.value = response.data || [];
  } catch (error) {
    console.error('Search error:', error);
    results.value = [];
  } finally {
    loading.value = false;
  }
};

const goToResult = (result) => {
  if (result.type === 'topic') {
    router.push(`/topic/${result.id}`);
  } else if (result.type === 'post') {
    router.push(`/post/${result.id}`);
  }
};
</script>

<style scoped>
.search-results {
  padding: 20px;
  max-width: 800px;
  margin: 0 auto;
}

.results-container {
  margin-top: 20px;
}

.result-card {
  margin-bottom: 15px;
  cursor: pointer;
  transition: box-shadow 0.3s;
}

.result-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.card-header {
  display: flex;
  align-items: center;
}

.result-type {
  display: inline-block;
  padding: 2px 8px;
  background-color: #f0f9ff;
  color: #409eff;
  border-radius: 4px;
  font-size: 12px;
  margin-right: 10px;
  font-weight: 500;
}

.result-title {
  font-weight: 600;
  color: #303133;
}

.result-summary {
  color: #606266;
  line-height: 1.5;
}

.loading {
  margin-top: 20px;
}

.no-results {
  text-align: center;
  padding: 40px 0;
  color: #909399;
  font-size: 16px;
}
</style>