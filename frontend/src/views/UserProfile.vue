<!-- src/views/UserProfile.vue -->
<template>
  <div class="container">
    <h1 class="section-title">User Profile</h1>
    <div class="card">
      <div class="card-body">
        <div v-if="authStore.isAuthenticated && authStore.user" class="row">
          <div class="col-md-6 mb-3">
            <label class="form-label">Tenant Id</label>
            <input class="form-control" :value="authStore.user.tenantId" readonly />
          </div>
          <div class="col-md-6 mb-3">
            <label class="form-label">Username</label>
            <input class="form-control" :value="authStore.user.username" readonly />
          </div>
          <div class="col-md-6 mb-3">
            <label class="form-label">Role</label>
            <input class="form-control" :value="authStore.user.role" readonly />
          </div>
          <!-- Add more fields as needed (e.g., email, created_at) -->
          <div class="col-12">
            <button class="btn btn-primary" @click="router.push('/')">Back to Home</button>
          </div>
        </div>
        <div v-else class="alert alert-warning">
          Please log in to view your profile.
          <router-link class="alert-link" to="/login">Login</router-link>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useAuthStore } from '../stores/auth';
import { useRouter } from 'vue-router';

const authStore = useAuthStore();
const router = useRouter();
</script>
