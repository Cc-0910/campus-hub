// 编辑器类型
export type EditorType = 'article' | 'question' | 'comment'

// 编辑器模式
export type EditorMode = 'minimized' | 'expanded'

// 编辑器内容接口
export interface EditorContent {
  type: EditorType
  title?: string
  content: string
  topicId?: number
  parentId?: number // 用于评论的父级ID
}

// 编辑器配置接口
export interface EditorConfig {
  type: EditorType
  isNeedTopic: boolean
  showTitle: boolean
  placeholder: string
  submitText: string
}

// 表情数据接口
export interface Emoji {
  name: string
  code: string
  category: string
}

// 图片上传响应接口
export interface ImageUploadResponse {
  url: string
  alt?: string
  width?: number
  height?: number
}