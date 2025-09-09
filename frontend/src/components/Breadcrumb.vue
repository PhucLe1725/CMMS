<template>
  <nav aria-label="breadcrumb">
    <ol class="breadcrumb">
      <li class="breadcrumb-item">
        <router-link to="/">
          <i class="bi bi-house-door me-1"></i>Home
        </router-link>
      </li>
      <li v-for="(item, index) in items" :key="index" 
          class="breadcrumb-item" 
          :class="{ active: index === items.length - 1 }">
        <router-link v-if="item.to && index < items.length - 1" :to="item.to">
          {{ item.text }}
        </router-link>
        <span v-else>{{ item.text }}</span>
      </li>
    </ol>
  </nav>
</template>

<script setup lang="ts">
interface BreadcrumbItem {
  text: string;
  to?: string;
}

defineProps<{
  items: BreadcrumbItem[];
}>();
</script>
