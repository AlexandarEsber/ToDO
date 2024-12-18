<script setup lang="ts">
import {RouterView} from 'vue-router'
import Toaster from '@/components/ui/toast/Toaster.vue'
</script>

<template>
  <div id="app">
    <div class="main">
      <RouterView />
    </div>
    <Toaster />
  </div>
</template>
