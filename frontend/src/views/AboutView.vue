<script setup>
import { ref } from 'vue'
import request from '@/utils/request'
import { ElMessage } from 'element-plus'

const responseMessage = ref('')

const sendTestRequest = async () => {
  try {
    const result = await request.get('/test/hello')
    responseMessage.value = result.data
    ElMessage.success(result.data)
  } catch (error) {
    console.error('请求失败:', error)
    ElMessage.error('请求失败: ' + error.message)
  }
}
</script>

<template>
  <div class="about">
    <h1>This is an about page</h1>
    <div class="test-section">
      <button @click="sendTestRequest" class="test-btn">发送测试请求</button>
      <div v-if="responseMessage" class="response">
        <h3>响应内容:</h3>
        <p>{{ responseMessage }}</p>
      </div>
    </div>
  </div>
</template>

<style scoped>
.test-section {
  margin-top: 20px;
  padding: 20px;
  border: 1px solid #ccc;
  border-radius: 4px;
}

.test-btn {
  padding: 10px 20px;
  background-color: #409eff;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 16px;
}

.test-btn:hover {
  background-color: #66b1ff;
}

.response {
  margin-top: 20px;
  padding: 10px;
  background-color: #f0f9ff;
  border-radius: 4px;
}

@media (min-width: 1024px) {
  .about {
    min-height: 100vh;
    display: flex;
    align-items: center;
  }
}
</style>
