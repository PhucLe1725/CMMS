import { defineStore } from 'pinia';
import { ref } from 'vue';
import api from '../api';
import type { User } from '../types';


export const useUserStore = defineStore('userStore', () => {
  const users = ref<User[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);
  const user = ref<User | null>(null);

  async function fetchAllUsers() {
    loading.value = true;
    error.value = null;
    try {
      const res = await api.get('/users/getAll');
      const data = res.data;
      if (data.success && data.result && Array.isArray(data.result.data)) {
        users.value = data.result.data;
      } else {
        users.value = [];
        error.value = 'Không lấy được dữ liệu người dùng';
      }
    } catch (e: any) {
      users.value = [];
      error.value = e?.message || 'Lỗi không xác định';
    } finally {
      loading.value = false;
    }
  }

  async function createUser(payload: Partial<User> & { password: string }) {
    loading.value = true;
    error.value = null;
    try {
      const now = new Date();
      const createdAt = now.toISOString();
      const res = await api.post('/users/create', {
        ...payload,
        isActive: String(payload.isActive ?? 1),
        createdAt,
      });
      await fetchAllUsers();
      return res.data;
    } catch (e: any) {
      error.value = e?.message || 'Lỗi tạo người dùng';
      throw e;
    } finally {
      loading.value = false;
    }
  }

  async function fetchUserById(id: number) {
    loading.value = true;
    error.value = null;
    try {
      const res = await api.get(`/users/${id}`);
      // API trả về user trong res.data.result
      user.value = res.data?.result || null;
    } catch (e: any) {
      user.value = null;
      error.value = e?.message || 'Không lấy được thông tin người dùng';
      throw e;
    } finally {
      loading.value = false;
    }
  }

  async function updateUser(id: number, payload: Partial<User>) {
    loading.value = true;
    error.value = null;
    try {
      const res = await api.put(`/users/update/${id}`, payload);
      await fetchAllUsers();
      return res.data;
    } catch (e: any) {
      error.value = e?.message || 'Lỗi cập nhật người dùng';
      throw e;
    } finally {
      loading.value = false;
    }
  }

  async function deleteUser(id: number) {
    loading.value = true;
    error.value = null;
    try {
      await api.delete(`/users/delete/${id}`);
      await fetchAllUsers();
    } catch (e: any) {
      error.value = e?.message || 'Lỗi xóa người dùng';
      throw e;
    } finally {
      loading.value = false;
    }
  }

  return { users, user, loading, error, fetchAllUsers, createUser, fetchUserById, updateUser, deleteUser };
});
