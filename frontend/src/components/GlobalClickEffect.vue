<template>
  <div class="global-click-effect" ref="containerRef" v-if="isEnabled" />
</template>

<script>
import { useUserStore } from '@/stores/user';
import { computed, onMounted, onUnmounted, ref } from 'vue';

export default {
  name: 'GlobalClickEffect',
  setup() {
    const containerRef = ref(null);
    const particlesContainer = ref(null);
    const startTime = ref(0);
    
    const userStore = useUserStore();
    
    // 只有在启用时才创建粒子效果
    const isEnabled = computed(() => userStore.enableClickEffect);

    // 初始化粒子引擎
    const initParticles = async () => {
      const canvas = document.createElement('canvas');
      canvas.style.position = 'fixed';
      canvas.style.top = '0';
      canvas.style.left = '0';
      canvas.style.width = '100%';
      canvas.style.height = '100%';
      canvas.style.pointerEvents = 'none';
      canvas.style.zIndex = '9999';
      canvas.style.overflow = 'hidden';
      document.body.appendChild(canvas);

      const ctx = canvas.getContext('2d');
      
      // 监听窗口大小变化
      const handleResize = () => {
        canvas.width = window.innerWidth;
        canvas.height = window.innerHeight;
      };
      
      window.addEventListener('resize', handleResize);
      handleResize(); // 初始化尺寸

      // 粒子类
      class Particle {
        constructor(x, y, color, vx, vy, groupType, isLongPress = false) {
          this.x = x;
          this.y = y;
          this.color = color;
          this.vx = vx;
          this.vy = vy;
          this.groupType = groupType; // 'main_burst' or 'debris_fall'
          
          // 根据技术规范设置精确物理参数
          if (groupType === 'main_burst') {
            // 主爆发粒子 - 高初速、中重力、高衰减 (符合技术规范)
            this.size = Math.random() * 7 + 1; // 1-8px (符合size.value: { min: 1, max: 8 })
            this.gravity = 0.12; // acceleration: 12 (模拟轻微抛物线)
            this.friction = 0.85; // decay: 0.15 (实现"迸发力"，高衰减)
            this.decay = 0.25; // 尺寸收缩速度: 25 (根据技术规范speed: 25)
          } else {
            // 碎屑粒子 - 低初速、超高重力 (符合技术规范)
            this.size = Math.random() * 3 + 1; // 1-4px (符合最小尺寸要求)
            this.gravity = 0.50; // acceleration: 50 (极高重力，快速坠落)
            this.friction = 0.85; // decay: 0.15 (高衰减)
            this.decay = 0.25; // 尺寸收缩速度: 25 (根据技术规范speed: 25)
          }
          
          // 根据按压时长调整速度
          if (isLongPress) {
            if (groupType === 'main_burst') {
              // 长按时主粒子速度提高到上限 { min: 80, max: 120 }
              const speedFactor = 120 / 70; // 比例缩放
              this.vx = vx * speedFactor;
              this.vy = vy * speedFactor;
            }
          }
        }

        update() {
          // 应用高衰减（"迸发力"核心）
          this.vx *= this.friction;
          this.vy *= this.friction;
          
          // 应用重力
          this.vy += this.gravity;
          
          // 更新位置
          this.x += this.vx;
          this.y += this.vy;
          
          // 尺寸收缩动画（实体化坍缩）- 根据技术规范
          // size.animation: { enable: true, speed: 25, startValue: "max", destroy: "min" }
          this.size = Math.max(0, this.size - this.decay);
        }

        draw() {
          if (this.size <= 0) return;
          
          ctx.save();
          ctx.fillStyle = this.color;
          ctx.globalAlpha = 1; // 移除透明度动画: 设置 opacity: 1
          ctx.beginPath();
          ctx.arc(this.x, this.y, this.size, 0, Math.PI * 2);
          ctx.fill();
          ctx.restore();
        }

        isAlive() {
          return this.size > 0 && 
                 this.x > -50 && this.x < canvas.width + 50 && 
                 this.y > -50 && this.y < canvas.height + 50; // 在屏幕外就销毁
        }
      }

      const particles = [];
      let animationFrameId;

      // 动画循环
      const animate = () => {
        ctx.clearRect(0, 0, canvas.width, canvas.height);

        for (let i = particles.length - 1; i >= 0; i--) {
          particles[i].update();
          particles[i].draw();

          if (!particles[i].isAlive()) {
            particles.splice(i, 1);
          }
        }

        animationFrameId = requestAnimationFrame(animate);
      };

      animate();

      // 触发粒子爆炸效果 - 根据双发射器触发逻辑技术规范
      const triggerExplosion = (x, y, isLongPress) => {
        if (!isEnabled.value) return;

        if (isLongPress) {
          // 长按模式 (> 300ms):
          // 唯一发射器: 瞬间发射 100+ 大尺寸粒子
          // 物理覆盖: 移除 debris_fall。将初始速度提高到上限，扩散角度设为全向 (direction: "none")
          const particleCount = 120;
          for (let i = 0; i < particleCount; i++) {
            const angle = Math.random() * Math.PI * 2; // 全向 (direction: "none")
            // 速度范围 { min: 80, max: 120 } 转换为像素/帧
            const particleSpeed = (Math.random() * 40 + 80) * 0.1; // 8.0-12.0 (对应80-120速度)
            const vx = Math.cos(angle) * particleSpeed;
            const vy = Math.sin(angle) * particleSpeed;
            
            const colors = ['#00FFFF', '#FF00FF', '#FFFF00', '#FF0000', '#00FF00', '#0000FF', '#FF7F00']; // CMYK风格颜色
            const color = colors[Math.floor(Math.random() * colors.length)];
            
            // 只发射 main_burst 粒子，移除 debris_fall
            const particle = new Particle(x, y, color, vx, vy, 'main_burst', true);
            particles.push(particle);
          }
        } else {
          // 短按模式 (< 300ms):
          // 发射器 A (主爆破): 瞬间发射 15-20 个粒子，开启 main_burst 物理组（高初速、中重力）
          const mainCount = 18; // 15-20 个粒子
          for (let i = 0; i < mainCount; i++) {
            const angle = Math.random() * Math.PI * 2;
            // 速度范围 { min: 40, max: 70 } 转换为像素/帧
            const particleSpeed = (Math.random() * 30 + 40) * 0.1; // 4.0-7.0 (对应40-70速度)
            const vx = Math.cos(angle) * particleSpeed;
            const vy = Math.sin(angle) * particleSpeed;
            
            const colors = ['#00FFFF', '#FF00FF', '#FFFF00', '#FF0000', '#00FF00', '#0000FF', '#FF7F00']; // CMYK风格颜色
            const color = colors[Math.floor(Math.random() * colors.length)];
            
            const particle = new Particle(x, y, color, vx, vy, 'main_burst');
            particles.push(particle);
          }
          
          // 发射器 B (重力碎屑): 瞬间发射 30-40 个极小粒子，开启 debris_fall 物理组（低初速、超高重力、极速下坠）
          const debrisCount = 35; // 30-40 个粒子
          for (let i = 0; i < debrisCount; i++) {
            const angle = Math.random() * Math.PI * 2;
            // 速度范围 { min: 5, max: 15 } 转换为像素/帧
            const particleSpeed = (Math.random() * 10 + 5) * 0.1; // 0.5-1.5 (对应5-15速度)
            const vx = Math.cos(angle) * particleSpeed;
            const vy = Math.sin(angle) * particleSpeed;
            
            const colors = ['#00FFFF', '#FF00FF', '#FFFF00', '#FF0000', '#00FF00', '#0000FF', '#FF7F00']; // CMYK风格颜色
            const color = colors[Math.floor(Math.random() * colors.length)];
            
            const particle = new Particle(x, y, color, vx, vy, 'debris_fall');
            particles.push(particle);
          }
        }
      };

      // 保存触发函数到实例
      particlesContainer.value = {
        triggerExplosion,
        canvas,
        handleResize,
        animationFrameId
      };

      // 鼠标事件处理
      const handleMouseDown = (event) => {
        if (!isEnabled.value) return;
        startTime.value = Date.now();
      };

      const handleMouseUp = (event) => {
        if (!isEnabled.value) return;
        const endTime = Date.now();
        const pressDuration = endTime - startTime.value;
        
        // 判断是短按还是长按
        const isLongPress = pressDuration > 300;
        
        // 触发爆炸效果
        particlesContainer.value.triggerExplosion(event.clientX, event.clientY, isLongPress);
      };

      // 添加事件监听器
      window.addEventListener('mousedown', handleMouseDown);
      window.addEventListener('mouseup', handleMouseUp);

      // 清理函数
      onUnmounted(() => {
        window.removeEventListener('mousedown', handleMouseDown);
        window.removeEventListener('mouseup', handleMouseUp);
        if (animationFrameId) {
          cancelAnimationFrame(animationFrameId);
        }
        if (canvas.parentNode) {
          canvas.parentNode.removeChild(canvas);
        }
        window.removeEventListener('resize', handleResize);
      });
    };

    onMounted(() => {
      initParticles();
    });

    return {
      containerRef,
      isEnabled
    };
  }
};
</script>

<style scoped>
.global-click-effect {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 9999;
}
</style>