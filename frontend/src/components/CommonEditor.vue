<template>
  <div class="common-editor">
    <!-- 编辑器头部 -->
    <div class="editor-header" v-if="showHeader">
      <div class="editor-tabs" v-if="showTypeSelection">
        <el-radio-group v-model="editorType" size="small">
          <el-radio-button value="article">写文章</el-radio-button>
          <el-radio-button value="question">提问题</el-radio-button>
          <el-radio-button value="comment" v-if="showCommentType">写评论</el-radio-button>
        </el-radio-group>
      </div>
      
      <div class="editor-actions">
        <slot name="header-actions"></slot>
        <el-button link @click="clearContent" :icon="Delete" title="清空内容" />
      </div>
    </div>

    <!-- 话题选择（需要话题时显示） -->
    <div v-if="showTopicSelector" class="topic-selector">
      <el-select 
        v-model="currentTopicId" 
        placeholder="选择话题"
        size="default"
        style="width: 100%"
        clearable
        filterable
      >
        <el-option 
          v-for="topic in availableTopics" 
          :key="topic.id"
          :label="topic.name" 
          :value="topic.id"
        />
      </el-select>
    </div>

    <!-- 标题输入（需要标题时显示） -->
    <div v-if="showTitleInput" class="title-input">
      <el-input 
        v-model="editorContent.title" 
        :placeholder="titlePlaceholder"
        maxlength="100"
        show-word-limit
        size="large"
      />
    </div>

    <!-- 富文本编辑器 -->
    <div class="rich-editor">
      <!-- 工具栏 -->
      <div class="editor-toolbar">
        <div class="toolbar-left">
          <el-button-group>
            <el-button 
              size="small" 
              :type="isBold ? 'primary' : 'default'"
              @click="toggleBold"
              title="加粗"
            >
              <span style="font-weight: bold;">B</span>
            </el-button>
            <el-button 
              size="small" 
              :type="isItalic ? 'primary' : 'default'"
              @click="toggleItalic"
              title="斜体"
            >
              <span style="font-style: italic;">I</span>
            </el-button>
            <el-button 
              size="small" 
              @click="insertLink"
              title="插入链接"
            >
              🔗
            </el-button>
          </el-button-group>
          
          <el-button-group>
            <el-button 
              size="small" 
              @click="showImageUpload = true"
              title="插入图片"
            >
              🖼️
            </el-button>
            <el-button 
              size="small" 
              @click="showEmojiPicker = true"
              title="插入表情"
            >
              😊
            </el-button>
            <el-button 
              size="small" 
              @click="insertCode"
              :icon="Document"
              title="插入代码"
            />
          </el-button-group>
        </div>
        
        <div class="toolbar-right">
          <slot name="toolbar-actions"></slot>
        </div>
      </div>

      <!-- 编辑区域 -->
      <div 
        ref="editorRef"
        class="editor-area"
        contenteditable="true"
        @input="onContentChange"
        @paste="handlePaste"
        :placeholder="contentPlaceholder"
      ></div>

      <!-- 字数统计和提交按钮 -->
      <div class="editor-footer">
        <div class="footer-left">
          <span class="char-count">{{ contentLength }} 字</span>
          <el-button 
            type="text" 
            size="small"
            @click="showPreview = !showPreview"
          >
            {{ showPreview ? '编辑' : '预览' }}
          </el-button>
        </div>
        
        <div class="footer-right">
          <el-button 
            @click="emit('cancel')"
            v-if="showCancel"
          >
            取消
          </el-button>
          <el-button 
            type="primary" 
            @click="submitContent"
            :loading="submitting"
            :disabled="!canSubmit"
          >
            {{ submitButtonText }}
          </el-button>
        </div>
      </div>
    </div>

    <!-- 预览模式 -->
    <div v-if="showPreview" class="preview-area">
      <h3>预览</h3>
      <div class="preview-content" v-html="editorContent.content"></div>
    </div>

    <!-- 图片上传对话框 -->
    <el-dialog 
      v-model="showImageUpload" 
      title="插入图片" 
      width="400px"
      :close-on-click-modal="false"
    >
      <div class="image-upload-dialog">
        <el-upload
          class="upload-demo"
          action="#"
          :auto-upload="false"
          :show-file-list="false"
          :on-change="handleImageUpload"
        >
          <el-button type="primary" :icon="Upload">选择图片</el-button>
          <template #tip>
            <div class="el-upload__tip">支持 JPG、PNG 格式，最大 5MB</div>
          </template>
        </el-upload>
        
        <div class="image-url-input" style="margin-top: 16px;">
          <el-input 
            v-model="imageUrl" 
            placeholder="或输入图片URL"
            @keyup.enter="insertImageFromUrl"
          >
            <template #append>
              <el-button @click="insertImageFromUrl" :icon="Plus">插入</el-button>
            </template>
          </el-input>
        </div>
      </div>
    </el-dialog>

    <!-- 表情选择器 -->
    <div v-if="showEmojiPicker" class="emoji-picker-overlay" @click="showEmojiPicker = false">
      <div class="emoji-picker-container" @click.stop>
        <div class="emoji-picker">
          <div class="emoji-header">
            <span class="emoji-title">选择表情</span>
            <el-button 
              link 
              @click="showEmojiPicker = false" 
              class="emoji-close"
              size="small"
            >
              ×
            </el-button>
          </div>
          <div class="emoji-grid">
            <span 
              v-for="emoji in commonEmojis" 
              :key="emoji" 
              class="emoji-item"
              @click="insertEmoji(emoji)"
            >
              {{ emoji }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted, nextTick, computed, watch } from 'vue'
import { 
  Delete, 
  Document, 
  Upload, 
  Plus 
} from '@element-plus/icons-vue'

// 定义属性
const props = defineProps({
  // 显示选项
  showHeader: {
    type: Boolean,
    default: true
  },
  showTypeSelection: {
    type: Boolean,
    default: true
  },
  showCommentType: {
    type: Boolean,
    default: false
  },
  showTopicSelector: {
    type: Boolean,
    default: false
  },
  showTitleInput: {
    type: Boolean,
    default: false
  },
  showCancel: {
    type: Boolean,
    default: true
  },
  initialType: {
    type: String,
    default: 'article'
  },
  titlePlaceholder: {
    type: String,
    default: '请输入标题'
  },
  contentPlaceholder: {
    type: String,
    default: '请输入内容...'
  },
  submitButtonText: {
    type: String,
    default: '提交'
  },
  initialContent: {
    type: Object,
    default: () => ({
      title: '',
      content: ''
    })
  },
  topics: {
    type: Array,
    default: () => []
  }
})

// 定义事件
const emit = defineEmits(['submit', 'cancel', 'content-change'])

// 响应式数据
const editorType = ref(props.initialType)
const currentTopicId = ref(null)
const editorContent = ref({ ...props.initialContent })
const editorRef = ref(null)
const isBold = ref(false)
const isItalic = ref(false)
const showImageUpload = ref(false)
const showEmojiPicker = ref(false)
const imageUrl = ref('')
const showPreview = ref(false)
const submitting = ref(false)
const availableTopics = ref(props.topics)

// 计算属性
const contentLength = computed(() => {
  return editorContent.value.content ? editorContent.value.content.length : 0
})

const canSubmit = computed(() => {
  const hasTitle = !props.showTitleInput || (editorContent.value.title && editorContent.value.title.trim())
  const hasContent = editorContent.value.content && editorContent.value.content.trim()
  return hasTitle && hasContent
})

// 常用表情
const commonEmojis = [
  '😀', '😃', '😄', '😁', '😆', '😅', '😂', '🤣', '😊', '😇',
  '🙂', '🙃', '😉', '😌', '😍', '🥰', '😘', '😗', '😙', '😚',
  '😋', '😛', '😝', '😜', '🤪', '🤨', '🧐', '🤓', '😎', '🤩',
  '🥳', '😏', '😒', '😞', '😔', '😟', '😕', '🙁', '☹️', '😣'
]

// 方法
const toggleBold = () => {
  isBold.value = !isBold.value
  document.execCommand('bold', false)
  editorRef.value?.focus()
}

const toggleItalic = () => {
  isItalic.value = !isItalic.value
  document.execCommand('italic', false)
  editorRef.value?.focus()
}

const insertLink = () => {
  const url = prompt('请输入链接地址:')
  if (url) {
    document.execCommand('createLink', false, url)
  }
  editorRef.value?.focus()
}

const insertCode = () => {
  const code = prompt('请输入代码:')
  if (code) {
    document.execCommand('insertHTML', false, `<code>${code}</code>`)
  }
  editorRef.value?.focus()
}

const insertEmoji = (emoji) => {
  document.execCommand('insertHTML', false, emoji)
  showEmojiPicker.value = false
  editorRef.value?.focus()
}

const insertImageFromUrl = () => {
  if (imageUrl.value) {
    document.execCommand('insertImage', false, imageUrl.value)
    imageUrl.value = ''
    showImageUpload.value = false
    editorRef.value?.focus()
  }
}

const handleImageUpload = (file) => {
  // 这里可以处理文件上传逻辑
  console.log('上传文件:', file)
  // 模拟上传并插入图片
  const url = URL.createObjectURL(file.raw)
  document.execCommand('insertImage', false, url)
  showImageUpload.value = false
  editorRef.value?.focus()
}

const onContentChange = (event) => {
  editorContent.value.content = event.target.innerHTML
  emit('content-change', { ...editorContent.value })
}

const handlePaste = (event) => {
  event.preventDefault()
  const text = (event.clipboardData || window.clipboardData).getData('text/plain')
  document.execCommand('insertText', false, text)
}

const clearContent = () => {
  if (editorRef.value) {
    editorRef.value.innerHTML = ''
    editorContent.value.content = ''
  }
  if (props.showTitleInput) {
    editorContent.value.title = ''
  }
}

const submitContent = async () => {
  if (!canSubmit.value) return
  
  submitting.value = true
  try {
    await emit('submit', {
      ...editorContent.value,
      type: editorType.value,
      topicId: currentTopicId.value
    })
  } finally {
    submitting.value = false
  }
}

// 监听props变化
watch(() => props.topics, (newTopics) => {
  availableTopics.value = newTopics
})

// 初始化编辑器内容
onMounted(async () => {
  await nextTick()
  if (editorRef.value && props.initialContent.content) {
    editorRef.value.innerHTML = props.initialContent.content
  }
})
</script>

<style scoped>
.common-editor {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
  background: white;
}

.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
  background-color: #fafafa;
}

.editor-tabs {
  flex: 1;
}

.editor-actions {
  display: flex;
  gap: 8px;
}

.topic-selector {
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
}

.title-input {
  padding: 12px 16px;
  border-bottom: 1px solid #e4e7ed;
}

.rich-editor {
  display: flex;
  flex-direction: column;
}

.editor-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  border-bottom: 1px solid #e4e7ed;
  background-color: #fafafa;
}

.toolbar-left,
.toolbar-right {
  display: flex;
  gap: 4px;
  align-items: center;
}

.editor-area {
  min-height: 200px;
  padding: 16px;
  outline: none;
  line-height: 1.6;
  overflow-y: auto;
}

.editor-area:focus {
  box-shadow: 0 0 0 1px #409eff inset;
}

.editor-area[placeholder]:empty::before {
  content: attr(placeholder);
  color: #c0c4cc;
  pointer-events: none;
}

.editor-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-top: 1px solid #e4e7ed;
  background-color: #fafafa;
}

.footer-left {
  display: flex;
  align-items: center;
  gap: 16px;
}

.footer-right {
  display: flex;
  gap: 8px;
}

.char-count {
  font-size: 12px;
  color: #909399;
}

.preview-area {
  padding: 16px;
  border-top: 1px solid #e4e7ed;
  background-color: #fafafa;
}

.preview-content {
  line-height: 1.6;
  color: #303133;
}

.preview-content :deep(h1), .preview-content :deep(h2), .preview-content :deep(h3) {
  margin: 16px 0 8px 0;
}

.preview-content :deep(p) {
  margin: 8px 0;
}

.preview-content :deep(code) {
  background-color: #f5f5f5;
  padding: 2px 4px;
  border-radius: 3px;
  font-family: monospace;
}

.image-upload-dialog {
  padding: 16px;
}

.emoji-picker-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 2000;
}

.emoji-picker-container {
  background: white;
  border-radius: 8px;
  overflow: hidden;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.emoji-picker {
  width: 300px;
  padding: 16px;
}

.emoji-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.emoji-title {
  font-weight: 500;
  color: #303133;
}

.emoji-grid {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 8px;
}

.emoji-item {
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 20px;
  cursor: pointer;
  padding: 4px;
  border-radius: 4px;
  transition: background-color 0.2s;
}

.emoji-item:hover {
  background-color: #f0f0f0;
}

@media (max-width: 768px) {
  .emoji-grid {
    grid-template-columns: repeat(6, 1fr);
  }
}
</style>