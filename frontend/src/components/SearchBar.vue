<template>
  <form @submit.prevent="handleSearch">
    <el-input
      v-model="keyword"
      placeholder="Search..."
      :suffix-icon="Search"
      size="default"
      clearable
    />
  </form>
</template>

<script setup>
import { ref } from 'vue';
import { Search } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const keyword = ref('');

const handleSearch = () => {
  if (keyword.value.trim()) {
    router.push(`/search?keyword=${encodeURIComponent(keyword.value.trim())}`);
  }
};
</script>

<style scoped>
form {
  width: 100%;
  max-width: 400px;
}

.el-input {
  width: 100%;
}
</style>