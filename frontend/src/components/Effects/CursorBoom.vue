<template>
  <canvas ref="canvasRef" class="cursor-boom-canvas"></canvas>
</template>

<script setup>
import { ref, onMounted, onUnmounted } from 'vue'

const canvasRef = ref(null);
let computerCanvas = null;
let computerContext = null;
let renderContext = null;

let globalWidth = window.innerWidth;
let globalHeight = window.innerHeight;

let booms = [];
let running = false;
let animationId = null;

// --- Original Source Code Classes (Adapted for Vue) ---

class Circle {
  constructor({ origin, speed, color, angle, context }) {
    this.origin = origin;
    this.position = { ...this.origin };
    this.color = color;
    this.speed = speed;
    this.angle = angle;
    this.context = context;
    this.renderCount = 0;
  }

  draw() {
    this.context.fillStyle = this.color;
    this.context.beginPath();
    this.context.arc(this.position.x, this.position.y, 2, 0, Math.PI * 2);
    this.context.fill();
  }

  move() {
    // The user's specific gravity logic
    this.position.x = (Math.sin(this.angle) * this.speed) + this.position.x;
    this.position.y = (Math.cos(this.angle) * this.speed) + this.position.y + (this.renderCount * 0.3);
    this.renderCount++;
  }
}

class Boom {
  constructor({ origin, context, circleCount = 10, area }) {
    this.origin = origin;
    this.context = context;
    this.circleCount = circleCount;
    this.area = area;
    this.stop = false;
    this.circles = [];
  }

  randomArray(range) {
    const length = range.length;
    const randomIndex = Math.floor(length * Math.random());
    return range[randomIndex];
  }

  randomColor() {
    // The user's specific color logic (Bright colors 8-F)
    const range = ['8', '9', 'A', 'B', 'C', 'D', 'E', 'F'];
    return '#' + this.randomArray(range) + this.randomArray(range) + this.randomArray(range) + this.randomArray(range) + this.randomArray(range) + this.randomArray(range);
  }

  randomRange(start, end) {
    return (end - start) * Math.random() + start;
  }

  init() {
    for (let i = 0; i < this.circleCount; i++) {
      const circle = new Circle({
        context: this.context,
        origin: this.origin,
        color: this.randomColor(),
        angle: this.randomRange(Math.PI - 1, Math.PI + 1),
        speed: this.randomRange(1, 6)
      });
      this.circles.push(circle);
    }
  }

  move() {
    this.circles.forEach((circle, index) => {
      if (circle.position.x > this.area.width || circle.position.y > this.area.height) {
        this.circles.splice(index, 1); // Remove if off screen
      } else {
        circle.move();
      }
    });
    if (this.circles.length == 0) {
      this.stop = true;
    }
  }

  draw() {
    this.circles.forEach(circle => circle.draw());
  }
}

// --- Main Logic ---

const run = () => {
  running = true;
  if (booms.length == 0) {
    running = false;
    return;
  }

  animationId = requestAnimationFrame(run);

  if (!computerContext || !renderContext) return;

  computerContext.clearRect(0, 0, globalWidth, globalHeight);
  renderContext.clearRect(0, 0, globalWidth, globalHeight);

  booms.forEach((boom, index) => {
    if (boom.stop) {
      booms.splice(index, 1); // Adjust index after splice
      // Note: in a simple loop usually reverse iteration is safer,
      // but keeping strictly to logic provided.
    } else {
      boom.move();
      boom.draw();
    }
  });

  renderContext.drawImage(computerCanvas, 0, 0, globalWidth, globalHeight);
};

const handleMouseDown = (e) => {
  const boom = new Boom({
    origin: { x: e.clientX, y: e.clientY },
    context: computerContext,
    circleCount: 5, // CHANGED: Set to 50% (5 particles)
    area: { width: globalWidth, height: globalHeight }
  });
  boom.init();
  booms.push(boom);
  if (!running) run();
};

const updateSize = () => {
  if (!canvasRef.value) return;
  globalWidth = window.innerWidth;
  globalHeight = window.innerHeight;

  canvasRef.value.width = globalWidth;
  canvasRef.value.height = globalHeight;

  if (computerCanvas) {
    computerCanvas.width = globalWidth;
    computerCanvas.height = globalHeight;
  }
};

onMounted(() => {
  if (canvasRef.value) {
    // Setup Render Canvas
    renderContext = canvasRef.value.getContext('2d');

    // Setup Computer Canvas (Off-screen buffer as per source)
    computerCanvas = document.createElement('canvas');
    computerContext = computerCanvas.getContext('2d');
    updateSize();

    window.addEventListener('resize', updateSize);
    window.addEventListener('mousedown', handleMouseDown);


  }
});
onUnmounted(() => {
  window.removeEventListener('resize', updateSize);
  window.removeEventListener('mousedown', handleMouseDown);
  if (animationId) cancelAnimationFrame(animationId);
});
</script>

<style scoped>
.cursor-boom-canvas {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none;
  z-index: 9999;
}
</style>