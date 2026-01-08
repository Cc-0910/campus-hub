<template>
  <!-- 粒子效果将通过JavaScript动态创建canvas元素 -->
</template>

<script setup>
import { onMounted, onUnmounted } from 'vue'

let canvas = null;
let ctx = null;
let particles = [];
let particleIndex = 0;
let particleLifetime = 120;
let currentLifetime = particleLifetime;
let isRunning = false;
let animationId = null;

// 创建canvas元素
const createCanvas = () => {
  canvas = document.createElement('canvas');
  canvas.width = window.innerWidth;
  canvas.height = window.innerHeight;
  canvas.style.cssText = 'position:fixed;top:0;left:0;pointer-events:none;z-index:999999';
  document.body.appendChild(canvas);
  ctx = canvas.getContext('2d');
};

// 更新canvas大小
const updateCanvasSize = () => {
  if (!canvas) return;
  canvas.width = window.innerWidth;
  canvas.height = window.innerHeight;
};

// 随机数生成
const random = (min, max) => {
  return Math.random() * (max - min) + min;
};

// 生成随机彩色（鲜艳）
const getRandomColor = () => {
  const hue = Math.random() * 360;
  return `hsl(${hue}, 100%, 50%)`; // 使用HSL而不是HSLA，确保完全不透明
};

// 获取输入元素的光标位置和颜色
const getInputInfo = () => {
  const input = document.querySelector('.search-box .el-input__inner');
  if (input) {
    const rect = input.getBoundingClientRect();
    
    // 对于输入框，使用selectionStart来获取光标位置
    if (input.selectionStart !== undefined) {
      // 创建一个临时元素来测量文本宽度
      const tempSpan = document.createElement('span');
      tempSpan.style.cssText = 'position:absolute;top:-9999px;left:0;white-space:pre;';
      tempSpan.textContent = input.value.substring(0, input.selectionStart);
      
      // 复制输入框的字体样式
      const computedStyle = window.getComputedStyle(input);
      tempSpan.style.font = computedStyle.font;
      tempSpan.style.fontSize = computedStyle.fontSize;
      tempSpan.style.fontFamily = computedStyle.fontFamily;
      tempSpan.style.padding = computedStyle.padding;
      tempSpan.style.border = computedStyle.border;
      
      document.body.appendChild(tempSpan);
      const cursorWidth = tempSpan.offsetWidth;
      document.body.removeChild(tempSpan);
      
      // 计算光标位置
      return {
        x: rect.left + cursorWidth + 5, // 考虑padding-left
        y: rect.top - 5, // 光标位置上方5px
        color: getRandomColor()
      };
    } else {
      // 回退到搜索框中心
      return {
        x: rect.left + rect.width / 2,
        y: rect.top + rect.height / 2,
        color: getRandomColor()
      };
    }
  }
  return { x: 0, y: 0, color: getRandomColor() };
};

// 创建粒子
const createParticles = () => {
  const inputInfo = getInputInfo();
  const particleCount = 5 + Math.round(Math.random() * 10);
  
  for (let i = 0; i < particleCount; i++) {
    particles[particleIndex] = {
      x: inputInfo.x,
      y: inputInfo.y,
      alpha: 1,
      color: getRandomColor(), // 每个粒子都使用独立的随机颜色
      velocity: {
        x: -1 + Math.random() * 2,
        y: -3.5 + Math.random() * 2
      }
    };
    particleIndex = (particleIndex + 1) % 500;
  }
  
  currentLifetime = particleLifetime;
  if (!isRunning) {
    animate();
  }
};

// 动画循环
const animate = () => {
  if (currentLifetime > 0) {
    animationId = requestAnimationFrame(animate);
    currentLifetime--;
    isRunning = true;
  } else {
    isRunning = false;
  }
  
  ctx.clearRect(0, 0, canvas.width, canvas.height);
  
  for (let i = 0; i < particles.length; i++) {
    const particle = particles[i];
    if (!particle || particle.alpha <= 0.1) continue;
    
    particle.velocity.y += 0.075;
    particle.x += particle.velocity.x;
    particle.y += particle.velocity.y;
    particle.alpha *= 0.96;
    
    ctx.globalAlpha = particle.alpha;
    ctx.fillStyle = particle.color;
    ctx.fillRect(Math.round(particle.x - 1.5), Math.round(particle.y - 1.5), 3, 3);
  }
};

// 处理输入事件
const handleInput = () => {
  createParticles();
};

onMounted(() => {
  // 创建canvas元素
  createCanvas();
  
  // 监听窗口大小变化
  window.addEventListener('resize', updateCanvasSize);
  
  // 监听搜索框输入事件
  const input = document.querySelector('.search-box .el-input__inner');
  if (input) {
    input.addEventListener('input', handleInput);
  }
  
  // 初始化动画循环
  animate();
});

onUnmounted(() => {
  // 移除事件监听
  window.removeEventListener('resize', updateCanvasSize);
  
  const input = document.querySelector('.search-box .el-input__inner');
  if (input) {
    input.removeEventListener('input', handleInput);
  }
  
  // 取消动画
  if (animationId) {
    cancelAnimationFrame(animationId);
  }
  
  // 移除canvas元素
  if (canvas && canvas.parentNode) {
    canvas.parentNode.removeChild(canvas);
  }
});
</script>