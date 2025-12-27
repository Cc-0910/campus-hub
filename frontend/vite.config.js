import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url))
    }
  },
  server: {
    proxy: {
      '/api': {
        target: 'http://localhost:9090', // 这里指向你的 Spring Boot 后端端口
        changeOrigin: true,
        // 下面这行通常建议注释掉。
        // 因为你的后端接口如果是 @RequestMapping("/api/auth")，前端发 /api/auth，后端收到的也应该是 /api/auth。
        // rewrite: (path) => path.replace(/^\/api/, '') 
      }
    }
  }
})