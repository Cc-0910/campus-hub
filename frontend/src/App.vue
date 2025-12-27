<script setup>
import { RouterView } from 'vue-router'
import AuthModal from '@/components/AuthModal.vue'
import CursorBoomEffect from '@/components/Effects/CursorBoomEffect.vue'
import ClickExplosion from '@/components/Effects/ClickExplosion.vue'
</script>

<template>
  <div id="app">
    <RouterView />
    <CursorBoomEffect />
    <ClickExplosion />
    <AuthModal />
  </div>
</template>

<style>
html, body, #app {
  height: 100%;
  margin: 0;
  padding: 0;
}
</style>