<template>
  <div class="admin-topics-container">
    <!-- 页面标题和操作按钮 -->
    <div class="admin-header">
      <h1 class="page-title">话题管理</h1>
      <el-button 
        type="primary" 
        @click="showCreateDialog = true"
        :icon="Plus"
        size="large"
      >
        添加新话题
      </el-button>
    </div>

    <!-- 话题列表 -->
    <div class="topics-list">
      <el-table :data="topics" style="width: 100%" v-loading="loading">
        <el-table-column prop="id" label="ID" width="80" />
        <el-table-column label="话题图片" width="120">
          <template #default="scope">
            <img 
              :src="scope.row.coverImage || '/src/assets/logo.svg'" 
              :alt="scope.row.name"
              class="topic-image"
              @error="handleImageError"
            />
          </template>
        </el-table-column>
        <el-table-column prop="name" label="话题名称" />
        <el-table-column prop="description" label="话题描述" />
        <el-table-column prop="followerCount" label="关注人数" width="100" />
        <el-table-column prop="createdAt" label="创建时间" width="180">
          <template #default="scope">
            {{ formatTime(scope.row.createdAt) }}
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button 
              type="primary" 
              link 
              @click="editTopic(scope.row)"
              :icon="Edit"
            >
              编辑
            </el-button>
            <el-button 
              type="danger" 
              link 
              @click="deleteTopic(scope.row.id)"
              :icon="Delete"
            >
              删除
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 空状态 -->
    <div class="empty-state" v-if="!loading && topics.length === 0">
      <el-empty description="暂无话题" />
    </div>

    <!-- 创建/编辑话题对话框 -->
    <el-dialog 
      v-model="showCreateDialog" 
      :title="editingTopic ? '编辑话题' : '添加新话题'"
      width="600px"
      :close-on-click-modal="false"
    >
      <el-form 
        :model="topicForm" 
        :rules="formRules" 
        ref="topicFormRef"
        label-width="100px"
      >
        <el-form-item label="话题名称" prop="name">
          <el-input 
            v-model="topicForm.name" 
            placeholder="请输入话题名称"
            maxlength="50"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="话题描述" prop="description">
          <el-input 
            v-model="topicForm.description" 
            type="textarea"
            :rows="4"
            placeholder="请输入话题描述"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
        
        <el-form-item label="话题图片">
          <div class="image-upload">
            <div 
              v-if="topicForm.coverImage" 
              class="image-preview"
            >
              <img :src="topicForm.coverImage" alt="话题图片" />
              <div class="image-actions">
                <el-button 
                  type="danger" 
                  size="small" 
                  @click="removeImage"
                  :icon="Delete"
                >
                  移除
                </el-button>
              </div>
            </div>
            <el-upload
              v-else
              class="upload-demo"
              action="#"
              :auto-upload="false"
              :show-file-list="false"
              :on-change="handleImageChange"
            >
              <el-button :icon="Plus">选择图片</el-button>
              <template #tip>
                <div class="el-upload__tip">
                  支持 JPG、PNG 格式，建议尺寸 400x300
                </div>
              </template>
            </el-upload>
          </div>
        </el-form-item>
      </el-form>
      
      <template #footer>
        <el-button @click="showCreateDialog = false">取消</el-button>
        <el-button 
          type="primary" 
          @click="submitTopic"
          :loading="submitting"
        >
          {{ editingTopic ? '更新' : '创建' }}
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, reactive } from 'vue'
import { ElMessage, ElMessageBox, type FormInstance, type FormRules } from 'element-plus'
import { Plus, Edit, Delete } from '@element-plus/icons-vue'
import { topicApi, type CreateTopicRequest } from '@/api/topic'
import type { Topic } from '@/types/topic'
import { useUserStore } from '@/stores/user'

const userStore = useUserStore()

// 响应式数据
const topics = ref<Topic[]>([])
const loading = ref(false)
const showCreateDialog = ref(false)
const editingTopic = ref<Topic | null>(null)
const submitting = ref(false)
const topicFormRef = ref<FormInstance>()

const topicForm = reactive({
  name: '',
  description: '',
  coverImage: ''
})

// 表单验证规则
const formRules: FormRules = {
  name: [
    { required: true, message: '请输入话题名称', trigger: 'blur' },
    { min: 2, max: 50, message: '话题名称长度在 2 到 50 个字符', trigger: 'blur' }
  ],
  description: [
    { required: true, message: '请输入话题描述', trigger: 'blur' },
    { min: 10, max: 200, message: '话题描述长度在 10 到 200 个字符', trigger: 'blur' }
  ]
}

// 加载话题列表
const loadTopics = async () => {
  try {
    loading.value = true
    const response = await topicApi.getTopics()
    if (response.code === 200) {
      topics.value = response.data
    }
  } catch (error) {
    console.error('加载话题列表失败:', error)
    ElMessage.error('加载话题列表失败')
  } finally {
    loading.value = false
  }
}

// 创建话题
const submitTopic = async () => {
  if (!topicFormRef.value) return
  
  try {
    const valid = await topicFormRef.value.validate()
    if (!valid) return
    
    submitting.value = true
    
    const requestData: CreateTopicRequest = {
      name: topicForm.name,
      description: topicForm.description,
      coverImage: topicForm.coverImage
    }
    
    if (editingTopic.value) {
      // 编辑话题（这里简化处理，实际应该调用更新API）
      const index = topics.value.findIndex(t => t.id === editingTopic.value!.id)
      if (index !== -1) {
        topics.value[index] = {
          ...topics.value[index],
          ...requestData,
          updatedAt: new Date().toISOString()
        }
      }
      ElMessage.success('话题更新成功')
    } else {
      // 创建新话题
      const newTopic = await topicApi.createTopic(requestData)
      topics.value.unshift(newTopic)
      ElMessage.success('话题创建成功')
    }
    
    showCreateDialog.value = false
    resetForm()
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败')
  } finally {
    submitting.value = false
  }
}

// 编辑话题
const editTopic = (topic: Topic) => {
  editingTopic.value = topic
  topicForm.name = topic.name
  topicForm.description = topic.description
  topicForm.coverImage = topic.coverImage
  showCreateDialog.value = true
}

// 删除话题
const deleteTopic = async (topicId: number) => {
  try {
    await ElMessageBox.confirm('确定要删除这个话题吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    
    // 模拟删除操作
    topics.value = topics.value.filter(topic => topic.id !== topicId)
    ElMessage.success('话题删除成功')
  } catch (error) {
    // 用户取消删除
  }
}

// 处理图片上传
const handleImageChange = (file: any) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    topicForm.coverImage = e.target?.result as string
  }
  reader.readAsDataURL(file.raw)
}

// 移除图片
const removeImage = () => {
  topicForm.coverImage = ''
}

// 重置表单
const resetForm = () => {
  topicForm.name = ''
  topicForm.description = ''
  topicForm.coverImage = ''
  editingTopic.value = null
  topicFormRef.value?.resetFields()
}

// 图片加载失败处理
const handleImageError = (event: Event) => {
  const img = event.target as HTMLImageElement
  img.src = '/src/assets/logo.svg'
}

// 格式化时间
const formatTime = (timeString: string) => {
  const date = new Date(timeString)
  return date.toLocaleString('zh-CN')
}

// 监听对话框关闭
const handleDialogClose = () => {
  resetForm()
}

// 初始化加载
onMounted(() => {
  // 检查管理员权限
  if (userStore.userInfo.role !== 'admin') {
    ElMessage.warning('您没有权限访问此页面')
    return
  }
  
  loadTopics()
})
</script>

<style scoped>
.admin-topics-container {
  padding: 24px;
  background: white;
  border-radius: 16px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
  min-height: 600px;
}

.admin-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
  padding-bottom: 16px;
  border-bottom: 1px solid #f0f0f0;
}

.page-title {
  font-size: 24px;
  font-weight: 600;
  color: #1f2937;
  margin: 0;
}

.topic-image {
  width: 60px;
  height: 45px;
  object-fit: cover;
  border-radius: 4px;
}

.image-upload {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.image-preview {
  position: relative;
  width: 200px;
  height: 150px;
  border: 1px dashed #dcdfe6;
  border-radius: 6px;
  overflow: hidden;
}

.image-preview img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.image-actions {
  position: absolute;
  top: 8px;
  right: 8px;
}

.empty-state {
  padding: 60px 0;
  text-align: center;
}

@media (max-width: 768px) {
  .admin-header {
    flex-direction: column;
    gap: 16px;
    align-items: flex-start;
  }
  
  .admin-topics-container {
    padding: 16px;
  }
}
</style>