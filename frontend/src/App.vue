<!-- App.vue -->
<template>
  <div class="d-flex flex-column min-vh-100">
    <!-- Navbar -->
    <nav class="navbar navbar-expand-lg" :class="{ 'navbar-light bg-light': theme === 'light', 'navbar-dark bg-dark': theme === 'dark' }">
      <div class="container-fluid">
        <a class="navbar-brand" href="/">CMMS</a>
        <button
          class="navbar-toggler"
          type="button"
          data-bs-toggle="collapse"
          data-bs-target="#navbarNav"
          aria-controls="navbarNav"
          aria-expanded="false"
          aria-label="Toggle navigation"
        >
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarNav">
          <div class="navbar-nav me-auto">
            <router-link class="nav-link" to="/home">
              <i class="bi bi-house-door me-2"></i>Home
            </router-link>
            <router-link v-if="authStore.isAuthenticated" class="nav-link" to="/assets">
              <i class="bi bi-box-seam me-2"></i>Máy móc
            </router-link>
            <router-link v-if="authStore.isAuthenticated" class="nav-link" to="/workrequests">
              <i class="bi bi-tools me-2"></i>Bảo dưỡng
            </router-link>
            <router-link v-if="authStore.isAuthenticated" class="nav-link" to="/inventories">
              <i class="bi bi-gear me-2"></i>Phụ tùng
            </router-link>
            <router-link v-if="authStore.isAuthenticated" class="nav-link" to="/tag-history">
              <i class="bi bi-activity me-2"></i>Tag History
            </router-link>
            <router-link v-if="authStore.isAuthenticated" class="nav-link" to="/opc-dashboard">
              <i class="bi bi-diagram-3 me-2"></i>OPC UA Dashboard
            </router-link>
            <!-- Thêm Quản lý nhân sự -->
            <router-link v-if="authStore.isAuthenticated && authStore.user?.role === 'ADMIN'" class="nav-link" to="/users">
              <i class="bi bi-people me-2"></i>Quản lý nhân sự
            </router-link>
          </div>
          <div class="d-flex align-items-center">
            <!-- User Profile Link -->
            <div v-if="authStore.isAuthenticated" class="dropdown me-2">
              <button
                class="btn btn-outline-secondary"
                type="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
                aria-label="User menu"
              >
                <i class="bi bi-person"></i>
              </button>
              <ul class="dropdown-menu dropdown-menu-end" :class="{ 'bg-dark text-white': theme === 'dark', 'bg-light': theme === 'light' }">
                <li>
                  <router-link class="dropdown-item" to="/profile">
                    <i class="bi bi-person-circle me-2"></i>View Profile
                  </router-link>
                </li>
                <li>
                  <button class="dropdown-item" @click="authStore.logout">
                    <i class="bi bi-box-arrow-right me-2"></i>Logout
                  </button>
                </li>
              </ul>
            </div>
            <div v-else class="navbar-nav">
              <router-link class="nav-link" to="/login">
                <i class="bi bi-box-arrow-in-right me-2"></i>Login
              </router-link>
            </div>
            <!-- Notification Dropdown -->
            <div class="dropdown me-2" v-if="authStore.isAuthenticated">
              <button
                class="btn btn-outline-secondary position-relative"
                type="button"
                data-bs-toggle="dropdown"
                aria-expanded="false"
                aria-label="Notifications"
              >
                <i class="bi bi-bell"></i>
                <span v-if="notificationStore.unreadCount > 0" class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
                  {{ notificationStore.unreadCount }}
                  <span class="visually-hidden">unread notifications</span>
                </span>
              </button>
              <ul class="dropdown-menu dropdown-menu-end notifications">
                <li v-if="notificationStore.notifications.length === 0" class="dropdown-item text-center">
                  No notifications
                </li>
                <li v-for="notification in notificationStore.notifications" :key="notification.id" class="notification-item">
                  <div class="notification-card" :class="{ 'notification-read': notification.read }">
                    <div class="notification-content">
                      <span class="notification-message">{{ notification.message }}</span>
                      <span class="notification-timestamp">{{ formatDate(notification.created_at) }}</span>
                    </div>
                    <button
                      v-if="!notification.read"
                      class="btn btn-link notification-action p-0"
                      @click="notificationStore.markAsRead(notification.id)"
                      aria-label="Mark as read"
                    >
                      <i class="bi bi-check-circle"></i>
                    </button>
                  </div>
                </li>
                <li v-if="notificationStore.notifications.length > 0" class="dropdown-item text-center">
                  <button class="btn btn-link" @click="notificationStore.markAllAsRead">Mark all as read</button>
                </li>
              </ul>
            </div>
            <button
              class="theme-toggle btn btn-outline-secondary ms-2"
              @click="toggleTheme"
              :aria-label="theme === 'light' ? 'Switch to dark mode' : 'Switch to light mode'"
            >
              <i :class="theme === 'light' ? 'bi bi-moon-stars' : 'bi bi-sun'"></i>
            </button>
          </div>
        </div>
      </div>
    </nav>

    <!-- Main Content -->
    <main class="flex-grow-1 container-fluid py-4">
      <router-view v-slot="{ Component }">
        <transition name="page" mode="out-in">
          <component :is="Component" />
        </transition>
      </router-view>
    </main>

    <!-- Footer -->
    <footer class="footer mt-auto py-3" :class="{ 'bg-light': theme === 'light', 'bg-dark text-white': theme === 'dark' }">
      <div class="container">
        <p class="footer-text mb-0">
          © {{ currentYear }} CMMS. All rights reserved.
          <router-link to="/terms" class="footer-link ms-2">Terms & Conditions</router-link>
        </p>
      </div>
    </footer>
  </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, computed, watch } from 'vue';
import { useAuthStore } from './stores/auth';
import { useNotificationStore } from './stores/notificationStore';
import { useRoute } from 'vue-router';

const authStore = useAuthStore();
const notificationStore = useNotificationStore();
const theme = ref<'light' | 'dark'>('light');
const currentYear = computed(() => new Date().getFullYear());
const route = useRoute();

const formatDate = (dateString: string) => {
  const date = new Date(dateString);
  return date.toLocaleString('fr-FR', { dateStyle: 'short', timeStyle: 'short' });
};

onMounted(() => {
  const savedTheme = localStorage.getItem('theme') as 'light' | 'dark' | null;
  theme.value = savedTheme || 'light';
  document.documentElement.setAttribute('data-theme', theme.value);
  notificationStore.fetchNotifications();
});

const toggleTheme = () => {
  theme.value = theme.value === 'light' ? 'dark' : 'light';
  document.documentElement.setAttribute('data-theme', theme.value);
  localStorage.setItem('theme', theme.value);
};

// Watch route changes for debugging
watch(
  () => route.fullPath,
  (newPath, oldPath) => {
    console.log(`Route changed from ${oldPath} to ${newPath}`);
  }
);
</script>

<style scoped>
.navbar {
  box-shadow: var(--card-shadow);
}
.navbar-dark .nav-link,
.navbar-dark .btn-link,
.navbar-dark .dropdown-item {
  color: var(--text-primary);
}
.navbar-dark .nav-link:hover,
.navbar-dark .btn-link:hover,
.navbar-dark .dropdown-item:hover {
  color: var(--link-hover);
}
.navbar-light .nav-link,
.navbar-light .btn-link,
.navbar-light .dropdown-item {
  color: var(--text-primary);
}
.navbar-light .nav-link:hover,
.navbar-light .btn-link:hover,
.navbar-light .dropdown-item:hover {
  color: var(--link-hover);
}
.theme-toggle {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 40px;
  height: 40px;
  border-radius: 50%;
}
.navbar-brand i,
.nav-link i,
.btn-link i,
.btn-outline-secondary i,
.dropdown-item i {
  font-size: 1.2rem;
  vertical-align: middle;
}
.dropdown-menu {
  min-width: 200px;
  border-radius: 8px;
  box-shadow: var(--card-shadow);
}
.dropdown-menu.notifications {
  min-width: 300px;
  max-height: 400px;
  overflow-y: auto;
}
.notification-item {
  padding: 0;
}
.notification-card {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  margin: 8px;
  background: var(--bg-secondary);
  border: 1px solid var(--border-color);
  border-radius: 8px;
  transition: background-color 0.3s ease, transform 0.2s ease, box-shadow 0.3s ease;
}
.notification-card:hover {
  background-color: var(--input-bg);
  transform: scale(1.02);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}
.notification-read {
  opacity: 0.7;
}
.notification-content {
  flex-grow: 1;
}
.notification-message {
  display: block;
  font-size: 0.95rem;
  font-weight: 500;
  color: var(--text-primary);
}
.notification-timestamp {
  display: block;
  font-size: 0.8rem;
  color: var(--text-secondary);
}
.notification-action i {
  color: var(--text-primary);
  transition: color 0.3s ease;
}
.notification-action:hover i {
  color: var(--accent-primary);
}
.dropdown-item.text-center {
  padding: 10px 15px;
}
@media (max-width: 767.98px) {
  .navbar-nav {
    padding: 1rem;
  }
  .nav-link i,
  .btn-link i,
  .btn-outline-secondary i,
  .dropdown-item i {
    margin-right: 0.5rem;
    font-size: 1.1rem;
  }
  .theme-toggle,
  .dropdown button {
    margin-top: 0.5rem;
    width: 100%;
    justify-content: start;
    padding: 0.5rem 1rem;
    text-align: left;
  }
  .notification-card {
    margin: 4px;
    padding: 10px 12px;
  }
}
</style>

<style>
/* Thêm vào phần global styles (không scoped) */
.page-enter-active, .page-leave-active {
  transition: opacity 0.2s ease;
}
.page-enter-from, .page-leave-to {
  opacity: 0;
}

/* Prevent layout shift */
body {
  overflow-x: hidden;
}

/* Smooth scrolling */
html {
  scroll-behavior: smooth;
}

/* Prevent flash of unstyled content */
[v-cloak] {
  display: none !important;
}

/* Loading skeleton có animation mượt hơn */
@keyframes skeleton-loading {
  0% {
    background-position: -200px 0;
  }
  100% {
    background-position: calc(200px + 100%) 0;
  }
}
</style>