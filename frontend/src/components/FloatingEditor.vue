<template>
  <!-- 浮动编辑器容器 -->
  <div 
    class="floating-editor" 
    :class="{ minimized: mode === 'minimized', expanded: mode === 'expanded' }"
  >
    <!-- 最小化状态 -->
    <div v-if="mode === 'minimized'" class="editor-minimized" @click="expandEditor">
      <el-button type="primary" circle class="minimized-btn">
        <el-icon><Edit /></el-icon>
      </el-button>
      <span class="minimized-text">写点什么...</span>
    </div>

    <!-- 展开状态 -->
    <div v-else class="editor-expanded">
      <!-- 编辑器头部 -->
      <div class="editor-header">
        <div class="editor-tabs">
          <el-radio-group v-model="editorType" size="small">
            <el-radio-button label="article">写文章</el-radio-button>
            <el-radio-button label="question">提问题</el-radio-button>
            <el-radio-button label="comment">写评论</el-radio-button>
          </el-radio-group>
        </div>
        <div class="editor-actions">
          <el-button link @click="minimizeEditor" :icon="Minus" title="最小化" />
          <el-button link @click="closeEditor" :icon="Close" title="关闭" />
        </div>
      </div>

      <!-- 编辑器内容 -->
      <div class="editor-content">
        <!-- 话题选择（需要话题时显示） -->
        <div v-if="isNeedTopic" class="topic-selector">
          <el-select 
            v-model="currentTopicId" 
            placeholder="选择话题"
            size="small"
            style="width: 100%"
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
        <div v-if="showTitle" class="title-input">
          <el-input 
            v-model="editorContent.title" 
            :placeholder="config.placeholder"
            maxlength="100"
            show-word-limit
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
              <el-button 
                size="small" 
                @click="clearContent"
                :icon="Delete"
                title="清空内容"
              />
            </div>
          </div>

          <!-- 编辑区域 -->
          <div 
            ref="editorRef"
            class="editor-area"
            contenteditable="true"
            @input="onContentChange"
            @paste="handlePaste"
            :placeholder="getContentPlaceholder()"
          ></div>

          <!-- 字数统计 -->
          <div class="editor-footer">
            <span class="char-count">{{ contentLength }} 字</span>
            <el-button 
              type="primary" 
              size="small" 
              @click="submitContent"
              :loading="submitting"
              :disabled="!canSubmit"
            >
              {{ config.submitText }}
            </el-button>
          </div>
        </div>
      </div>
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
          
          <div class="emoji-categories">
            <el-radio-group v-model="emojiCategory" size="small">
              <el-radio-button v-for="category in emojiCategories" 
                              :key="category" 
                              :label="category">
                {{ category }}
              </el-radio-button>
            </el-radio-group>
          </div>
          
          <div class="emoji-grid">
            <span 
              v-for="emoji in filteredEmojis" 
              :key="emoji.code"
              class="emoji-item"
              @click="insertEmoji(emoji.code)"
              :title="emoji.name"
            >
              {{ emoji.code }}
            </span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, watch, nextTick, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { 
  Edit, Minus, Close, Document, Delete, Plus 
} from '@element-plus/icons-vue'
import type { EditorType, EditorMode, EditorContent, EditorConfig } from '@/types/editor'
import type { Topic } from '@/types/topic'
import { mockTopics } from '@/mock/topics'

// Props
interface Props {
  mode?: EditorMode
  defaultType?: EditorType
  currentTopicId?: number
}

const props = withDefaults(defineProps<Props>(), {
  mode: 'minimized',
  defaultType: 'comment',
  currentTopicId: undefined
})

// Emits
const emit = defineEmits<{
  submit: [content: EditorContent]
  close: []
  modeChange: [mode: EditorMode]
}>()

// 响应式数据
const editorType = ref<EditorType>(props.defaultType)
const mode = ref<EditorMode>(props.mode)
const currentTopicId = ref<number | undefined>(props.currentTopicId)
const editorContent = ref<EditorContent>({
  type: props.defaultType,
  content: '',
  title: ''
})

const editorRef = ref<HTMLElement>()
const submitting = ref(false)
const showImageUpload = ref(false)
const showEmojiPicker = ref(false)
const imageUrl = ref('')
const emojiCategory = ref('表情')

// 编辑器状态
const isBold = ref(false)
const isItalic = ref(false)
const contentLength = ref(0)

// 表情数据
const emojis = ref([
  { name: '笑脸', code: '😊', category: '表情' },
  { name: '大笑', code: '😂', category: '表情' },
  { name: '爱心', code: '❤️', category: '表情' },
  { name: '点赞', code: '👍', category: '手势' },
  { name: '胜利', code: '✌️', category: '手势' },
  { name: '太阳', code: '☀️', category: '自然' },
  { name: '月亮', code: '🌙', category: '自然' },
  { name: '星星', code: '⭐', category: '自然' },
  { name: '咖啡', code: '☕', category: '食物' },
  { name: '蛋糕', code: '🍰', category: '食物' },
  { name: '音乐', code: '🎵', category: '活动' },
  { name: '足球', code: '⚽', category: '活动' }
])

// 计算属性
const availableTopics = computed(() => mockTopics)

const config = computed<EditorConfig>(() => {
  const configs = {
    article: {
      isNeedTopic: true,
      showTitle: true,
      placeholder: '请输入文章标题...',
      submitText: '发布文章'
    },
    question: {
      isNeedTopic: true,
      showTitle: true,
      placeholder: '请输入问题标题...',
      submitText: '发布问题'
    },
    comment: {
      isNeedTopic: false,
      showTitle: false,
      placeholder: '写点评论...',
      submitText: '发表评论'
    }
  }
  return configs[editorType.value]
})

const isNeedTopic = computed(() => config.value.isNeedTopic)
const showTitle = computed(() => config.value.showTitle)

const canSubmit = computed(() => {
  const hasContent = editorContent.value.content.trim().length > 0
  const hasTitle = !showTitle.value || (editorContent.value.title && editorContent.value.title.trim().length > 0)
  const hasTopic = !isNeedTopic.value || currentTopicId.value
  
  return hasContent && hasTitle && hasTopic
})

const emojiCategories = computed(() => {
  return [...new Set(emojis.value.map(e => e.category))]
})

const filteredEmojis = computed(() => {
  return emojis.value.filter(e => e.category === emojiCategory.value)
})

// 方法
const expandEditor = () => {
  mode.value = 'expanded'
  emit('modeChange', 'expanded')
}

const minimizeEditor = () => {
  mode.value = 'minimized'
  emit('modeChange', 'minimized')
}

const closeEditor = () => {
  mode.value = 'minimized'
  emit('close')
}

const onContentChange = () => {
  if (editorRef.value) {
    editorContent.value.content = editorRef.value.innerHTML
    contentLength.value = editorRef.value.textContent?.length || 0
  }
}

const getContentPlaceholder = () => {
  const placeholders = {
    article: '开始撰写您的文章内容...',
    question: '详细描述您的问题...',
    comment: '写下您的评论...'
  }
  return placeholders[editorType.value]
}

// 格式化方法
const toggleBold = () => {
  document.execCommand('bold')
  isBold.value = document.queryCommandState('bold')
  focusEditor()
}

const toggleItalic = () => {
  document.execCommand('italic')
  isItalic.value = document.queryCommandState('italic')
  focusEditor()
}

const insertLink = () => {
  const url = prompt('请输入链接地址:')
  if (url) {
    document.execCommand('createLink', false, url)
    focusEditor()
  }
}

const insertCode = () => {
  const code = '<div style="background: #f6f8fa; border: 1px solid #e1e4e8; border-radius: 6px; padding: 16px; margin: 8px 0; font-family: \'Courier New\', monospace; font-size: 14px; line-height: 1.5;">' +
               '<div style="color: #6a737d; margin-bottom: 8px;">// 在这里输入代码</div>' +
               '<div style="color: #24292e;">// 你的代码内容...</div>' +
               '</div>'
  insertTextAtCursor(code)
}

const insertEmoji = (emoji: string) => {
  insertTextAtCursor(emoji)
  showEmojiPicker.value = false
}

const insertTextAtCursor = (text: string) => {
  if (editorRef.value) {
    const selection = window.getSelection()
    
    // 确保编辑器获得焦点
    focusEditor()
    
    if (selection && selection.rangeCount > 0) {
      const range = selection.getRangeAt(0)
      
      // 检查range是否在编辑器内部
      if (!editorRef.value.contains(range.commonAncestorContainer)) {
        // 如果不在编辑器内部，在编辑器末尾插入
        const range = document.createRange()
        range.selectNodeContents(editorRef.value)
        range.collapse(false) // 移动到末尾
        selection.removeAllRanges()
        selection.addRange(range)
      }
      
      range.deleteContents()
      
      // 使用innerHTML插入内容，确保正确显示
      const tempDiv = document.createElement('div')
      tempDiv.innerHTML = text
      
      const fragment = document.createDocumentFragment()
      while (tempDiv.firstChild) {
        fragment.appendChild(tempDiv.firstChild)
      }
      
      range.insertNode(fragment)
      range.collapse(false)
      selection.removeAllRanges()
      selection.addRange(range)
    } else {
      // 如果没有选中区域，在编辑器末尾插入
      const range = document.createRange()
      const selection = window.getSelection()
      
      range.selectNodeContents(editorRef.value)
      range.collapse(false) // 移动到末尾
      
      selection?.removeAllRanges()
      selection?.addRange(range)
      
      // 插入内容
      const tempDiv = document.createElement('div')
      tempDiv.innerHTML = text
      
      const fragment = document.createDocumentFragment()
      while (tempDiv.firstChild) {
        fragment.appendChild(tempDiv.firstChild)
      }
      
      range.insertNode(fragment)
      range.collapse(false)
    }
    
    onContentChange()
    focusEditor()
  }
}

const focusEditor = () => {
  if (editorRef.value) {
    editorRef.value.focus()
  }
}

const clearContent = () => {
  if (editorRef.value) {
    editorRef.value.innerHTML = ''
    onContentChange()
  }
  editorContent.value.title = ''
}

const handlePaste = (event: ClipboardEvent) => {
  const items = event.clipboardData?.items
  if (items) {
    for (let i = 0; i < items.length; i++) {
      if (items[i].type.indexOf('image') !== -1) {
        event.preventDefault()
        const file = items[i].getAsFile()
        if (file) {
          handleImageUpload({ raw: file })
        }
        return
      }
    }
  }
}

const handleImageUpload = (file: any) => {
  const reader = new FileReader()
  reader.onload = (e) => {
    const img = document.createElement('img')
    img.src = e.target?.result as string
    img.style.maxWidth = '100%'
    insertElementAtCursor(img)
    showImageUpload.value = false
  }
  reader.readAsDataURL(file.raw)
}

const insertImageFromUrl = () => {
  if (imageUrl.value) {
    const img = document.createElement('img')
    img.src = imageUrl.value
    img.style.maxWidth = '100%'
    img.onerror = () => ElMessage.error('图片加载失败')
    insertElementAtCursor(img)
    imageUrl.value = ''
    showImageUpload.value = false
  }
}

const insertElementAtCursor = (element: HTMLElement) => {
  if (editorRef.value) {
    const selection = window.getSelection()
    if (selection && selection.rangeCount > 0) {
      const range = selection.getRangeAt(0)
      range.deleteContents()
      range.insertNode(element)
      range.collapse(false)
      selection.removeAllRanges()
      selection.addRange(range)
    }
    onContentChange()
  }
}

const submitContent = async () => {
  if (!canSubmit.value) return
  
  submitting.value = true
  
  try {
    const content: EditorContent = {
      type: editorType.value,
      content: editorContent.value.content,
      title: editorContent.value.title,
      topicId: currentTopicId.value
    }
    
    emit('submit', content)
    
    // 清空内容
    clearContent()
    
    ElMessage.success('发布成功!')
    minimizeEditor()
  } catch (error) {
    ElMessage.error('发布失败，请重试')
  } finally {
    submitting.value = false
  }
}

// 监听器
watch(editorType, (newType) => {
  editorContent.value.type = newType
  // 如果是评论模式，清空话题选择
  if (newType === 'comment') {
    currentTopicId.value = undefined
  }
})

watch(currentTopicId, (newTopicId) => {
  editorContent.value.topicId = newTopicId
})

// 生命周期
onMounted(() => {
  nextTick(() => {
    if (editorRef.value) {
      editorRef.value.innerHTML = editorContent.value.content
    }
  })
})
</script>

<style scoped>
.floating-editor {
  position: fixed;
  bottom: 24px;
  right: 24px;
  z-index: 1000;
  transition: all 0.3s ease;
}

.floating-editor.minimized {
  width: auto;
  height: auto;
}

.floating-editor.expanded {
  width: 500px;
  max-width: 90vw;
  max-height: 80vh;
  background: white;
  border-radius: 12px;
  box-shadow: 0 8px 32px rgba(0, 0, 0, 0.15);
  border: 1px solid #e5e7eb;
  overflow: hidden;
}

.editor-minimized {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background: #3b82f6;
  color: white;
  border-radius: 24px;
  cursor: pointer;
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
  transition: all 0.3s ease;
}

.editor-minimized:hover {
  background: #2563eb;
  transform: translateY(-2px);
}

.minimized-btn {
  background: white !important;
  color: #3b82f6 !important;
}

.minimized-text {
  font-size: 14px;
  font-weight: 500;
}

.editor-expanded {
  display: flex;
  flex-direction: column;
  height: 500px;
  max-height: 80vh;
}

.editor-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  flex-shrink: 0;
}

.editor-tabs {
  flex: 1;
}

.editor-actions {
  display: flex;
  gap: 4px;
}

.editor-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 16px;
  gap: 12px;
  overflow: hidden;
}

.topic-selector,
.title-input {
  margin-bottom: 8px;
}

.rich-editor {
  flex: 1;
  display: flex;
  flex-direction: column;
  border: 1px solid #e5e7eb;
  border-radius: 6px;
  overflow: hidden;
}

.editor-toolbar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background: #f9fafb;
  border-bottom: 1px solid #e5e7eb;
  flex-shrink: 0;
}

.toolbar-left {
  display: flex;
  gap: 8px;
}

.editor-area {
  flex: 1;
  padding: 12px;
  min-height: 200px;
  max-height: none;
  overflow-y: auto;
  outline: none;
  line-height: 1.6;
}

.editor-area img {
  max-width: 100%;
  height: auto;
  display: block;
  margin: 8px 0;
}

.editor-area:empty:before {
  content: attr(placeholder);
  color: #9ca3af;
  pointer-events: none;
}

.editor-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 12px;
  background: #f9fafb;
  border-top: 1px solid #e5e7eb;
}

.char-count {
  font-size: 12px;
  color: #6b7280;
}

.image-upload-dialog {
  text-align: center;
}

.emoji-picker-overlay {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1001;
  background: rgba(0, 0, 0, 0.1);
}

.emoji-picker-container {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  z-index: 1002;
}

.emoji-picker {
  width: 300px;
  max-height: 300px;
  background: white;
  border: 1px solid #e5e7eb;
  border-radius: 8px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}

.emoji-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 12px 16px;
  border-bottom: 1px solid #f0f0f0;
  background: #f9fafb;
}

.emoji-title {
  font-weight: 600;
  color: #1f2937;
}

.emoji-close {
  font-size: 18px;
  font-weight: bold;
  color: #6b7280;
}

.emoji-categories {
  padding: 12px 16px 0;
  border-bottom: 1px solid #f0f0f0;
}

.emoji-grid {
  display: grid;
  grid-template-columns: repeat(8, 1fr);
  gap: 4px;
  padding: 12px;
  max-height: 200px;
  overflow-y: auto;
}

.emoji-item {
  font-size: 20px;
  cursor: pointer;
  text-align: center;
  padding: 6px;
  border-radius: 4px;
  transition: background-color 0.2s;
  user-select: none;
}

.emoji-item:hover {
  background-color: #3b82f6;
  color: white;
}

@media (max-width: 768px) {
  .floating-editor.expanded {
    width: calc(100vw - 32px);
    right: 16px;
    bottom: 16px;
  }
  
  .editor-expanded {
    height: 400px;
  }
  
  .emoji-grid {
    grid-template-columns: repeat(6, 1fr);
  }
}
</style>