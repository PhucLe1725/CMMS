<template>
  <div class="container mt-5">
    <div class="card">
      <h2>Đăng nhập</h2>
      <form @submit.prevent="login">
        <div class="mb-3">
          <label for="email" class="form-label">Tên đăng nhập</label>
          <input v-model="form.username" type="text" class="form-control" id="username" required />
        </div>
        <div class="mb-3">
          <label for="password" class="form-label">Mật khẩu</label>
          <input v-model="form.password" type="password" class="form-control" id="password" required />
        </div>
        <button type="submit" class="btn btn-primary">Login</button>
        <p v-if="error" class="text-danger mt-2">{{ error }}</p>
      </form>
      <!-- <p class="mt-3">
        Don't have an account? <router-link to="/register">Register</router-link>
      </p> -->
    </div>
  </div>
</template>

<script lang="ts" setup>
import { ref } from "vue";
import { useAuthStore } from "../stores/auth";

const authStore = useAuthStore();
const form = ref({ username: "", password: "" });
const error = ref("");

const login = async () => {
  try {
    await authStore.login(form.value.username, form.value.password);
    error.value = "";
  } catch (err: any) {
    error.value = err.message || "Login failed";
  }
};
</script>