<template>
  <canvas ref="canvasRef" class="explosion-canvas"></canvas>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const canvasRef = ref(null);
let ctx = null;
let width = 0;
let height = 0;

// State
let balls = [];
let longPressed = false;
let longPressTimer;
let multiplier = 0;
let animationFrameId;

// Config: Colorful Palette
const colours = ['#F73859', '#14FFEC', '#00E0FF', '#FF99FE', '#FAF15D'];
const randBetween = (min, max) => Math.floor(Math.random() * max) + min;

class Ball {
  constructor(x, y) {
    this.x = x;
    this.y = y;
    this.angle = Math.PI * 2 * Math.random();

    // Logic: Long press = bigger explosion
    if (longPressed) {
      this.multiplier = randBetween(14 + multiplier, 15 + multiplier);
    } else {
      this.multiplier = randBetween(6, 12);
    }
    this.vx = (this.multiplier + Math.random() * 0.5) * Math.cos(this.angle);
    this.vy = (this.multiplier + Math.random() * 0.5) * Math.sin(this.angle);
    this.r = randBetween(5, 10) + 3 * Math.random();
    this.color = colours[Math.floor(Math.random() * colours.length)];
  }

  update() {
    this.x += this.vx;
    this.y += this.vy;
    this.r -= 0.3;
    this.vx *= 0.9;
    this.vy *= 0.9;
  }
}

const pushBalls = (count, x, y) => {
  for (let i = 0; i < count; i++) {
    balls.push(new Ball(x, y));
  }
};

const loop = () => {
  if (!ctx || !canvasRef.value) return;
  ctx.clearRect(0, 0, width, height);

  for (let i = 0; i < balls.length; i++) {
    let b = balls[i];
    if (b.r < 0) continue;
    ctx.fillStyle = b.color;
    ctx.beginPath();
    ctx.arc(b.x, b.y, b.r, 0, Math.PI * 2, false);
    ctx.fill();
    b.update();
  }

  // Cleanup
  for (let i = 0; i < balls.length; i++) {
    if (balls[i].r < 0) balls.splice(i, 1);
  }

  if (longPressed) {
    multiplier += multiplier <= 10 ? 0.2 : 0.0;
  } else if (!longPressed && multiplier >= 0) {
    multiplier -= 0.4;
  }

  animationFrameId = requestAnimationFrame(loop);
};

const updateSize = () => {
  if (!canvasRef.value) return;
  width = window.innerWidth;
  height = window.innerHeight;
  canvasRef.value.width = width;
  canvasRef.value.height = height;
};

// --- EVENT HANDLERS ---

const handleMouseDown = (e) => {
      // Trigger explosion immediately on click
      // CHANGED: Current 6-12 -> Now 5-10 (~50% of original)
      pushBalls(randBetween(5, 10), e.clientX, e.clientY);

  longPressTimer = setTimeout(() => {
    longPressed = true;
  }, 750);
};

const handleMouseUp = (e) => {
  clearTimeout(longPressTimer);
  if (longPressed) {
            // Only trigger superblast if it WAS a long press
            // CHANGED: Current 30-60 -> Now 25-50 (~50% of original)
            pushBalls(randBetween(25 + Math.ceil(multiplier), 50 + Math.ceil(multiplier)), e.clientX, e.clientY);
            longPressed = false;
          }
  // If it was a short click, DO NOTHING (No ghost circle!)
  multiplier = 0;
};

onMounted(() => {
  if (canvasRef.value) {
    ctx = canvasRef.value.getContext('2d');
    updateSize();
    loop();
    window.addEventListener('resize', updateSize);
    window.addEventListener('mousedown', handleMouseDown);
    window.addEventListener('mouseup', handleMouseUp);
  }
});

onUnmounted(() => {
  window.removeEventListener('resize', updateSize);
  window.removeEventListener('mousedown', handleMouseDown);
  window.removeEventListener('mouseup', handleMouseUp);
  cancelAnimationFrame(animationFrameId);
});
</script>

<style scoped>
.explosion-canvas {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 9997; /* Slightly behind CursorBoom for better visual layering */
}
</style>