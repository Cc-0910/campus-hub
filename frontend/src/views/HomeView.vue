<template>
  <div class="home-view">
    <!-- 顶部蓝色欢迎横幅 -->
    <el-card class="banner-card">
      <div class="banner-content">
        <h1>欢迎来到校园Hub</h1>
        <p>发现校园生活的精彩话题与问答</p>
      </div>
    </el-card>

    <!-- 内容区域 -->
    <el-row :gutter="20" class="content-row">
      <!-- 左侧：热门话题 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>热门话题</span>
            </div>
          </template>
          <div class="card-content">
            <p>这里将展示热门话题内容</p>
            <ul>
              <li>话题一：校园生活分享</li>
              <li>话题二：学习经验交流</li>
              <li>话题三：社团活动讨论</li>
            </ul>
          </div>
        </el-card>
      </el-col>

      <!-- 右侧：最新问答 -->
      <el-col :xs="24" :sm="24" :md="12" :lg="12">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>最新问答</span>
            </div>
          </template>
          <div class="card-content">
            <p>这里将展示最新问答内容</p>
            <ul>
              <li>问题一：如何选课最合理？</li>
              <li>问题二：食堂哪家窗口最好吃？</li>
              <li>问题三：图书馆座位预约技巧</li>
            </ul>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 底部内容区域（可选） -->
    <el-row :gutter="20" class="content-row">
      <el-col :xs="24" :sm="12" :md="8" :lg="8">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>校园公告</span>
            </div>
          </template>
          <div class="card-content">
            <p>这里是校园公告内容</p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="12" :md="8" :lg="8">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>活动预告</span>
            </div>
          </template>
          <div class="card-content">
            <p>这里是活动预告内容</p>
          </div>
        </el-card>
      </el-col>
      <el-col :xs="24" :sm="24" :md="8" :lg="8">
        <el-card class="content-card">
          <template #header>
            <div class="card-header">
              <span>热门资源</span>
            </div>
          </template>
          <div class="card-content">
            <p>这里是热门资源内容</p>
          </div>
        </el-card>
      </el-col>
    </el-row>
  </div>
</template>

<script>
export default {
  name: 'HomeView',
};
</script>

<style scoped>
.home-view {
  background-color: #f0f2f5;
  padding: 20px;
  min-height: calc(100vh - 40px);
}

.banner-card {
  margin-bottom: 20px;
  background: linear-gradient(135deg, #409EFF, #66b1ff);
  color: white;
  border: none;
  border-radius: 16px !important; /* 统一圆角标准 */
  overflow: hidden; /* 防止内部元素溢出圆角 */
}

.banner-content {
  text-align: center;
  padding: 20px; /* 确保内容不贴边 */
}

.banner-content h1 {
  margin: 0 0 10px 0;
  font-size: 28px;
}

.banner-content p {
  margin: 0;
  font-size: 16px;
  opacity: 0.9;
}

.content-row {
  margin-bottom: 20px;
}

.content-card {
  margin-bottom: 20px;
  height: 100%;
  border-radius: 16px !important; /* 统一圆角标准 */
  overflow: hidden; /* 防止内部元素溢出圆角 */
}

.card-header {
  font-weight: bold;
  font-size: 16px;
  border-radius: 15px 15px 0 0; /* 头部圆角，底部由卡片整体控制 */
}

.card-content {
  min-height: 120px;
  border-radius: 0 0 15px 15px; /* 底部圆角，顶部由头部或卡片控制 */
}

.card-content ul {
  padding-left: 20px;
  margin: 10px 0;
}

.card-content li {
  margin-bottom: 8px;
  border-radius: 8px; /* 列表项也使用小圆角 */
}
</style>