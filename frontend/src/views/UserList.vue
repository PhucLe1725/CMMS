<template>
  <div>
    <div class="d-flex justify-content-between align-items-center mb-3">
      <h2>Quản lý nhân sự</h2>
      <router-link class="btn btn-primary" to="/users/new">
        <i class="bi bi-plus-lg"></i> Thêm nhân sự
      </router-link>
    </div>
    <table class="table table-hover">
      <thead>
        <tr>
          <th>Tên</th>
          <th>Email</th>
          <th>Vai trò</th>
          <th>Trạng thái</th>
          <th>Hành động</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="user in users" :key="user.userId">
          <td>
            <a href="javascript:void(0)" @click="viewUserDetail(user)" class="text-decoration-none">
              {{ user.fullName }}
            </a>
          </td>
          <td>{{ user.email }}</td>
          <td>{{ user.role }}</td>
          <td>
            <span :class="user.isActive === 1 ? 'text-success' : 'text-danger'">
              {{ user.isActive === 1 ? 'ACTIVE' : 'INACTIVE' }}
            </span>
          </td>
          <td>
            <router-link :to="`/users/${user.userId}/edit`" class="btn btn-sm btn-outline-secondary me-2">
              <i class="bi bi-pencil"></i> Sửa
            </router-link>
            <button class="btn btn-sm btn-outline-danger" @click="deleteUser(user.userId)">
              <i class="bi bi-trash"></i> Xóa
            </button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- User Detail Modal -->
    <div class="modal fade" :class="{ show: showModal }" :style="{ display: showModal ? 'block' : 'none' }" 
         id="userDetailModal" tabindex="-1" aria-labelledby="userDetailModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="userDetailModalLabel">Thông tin chi tiết nhân sự</h5>
            <button type="button" class="btn-close" @click="closeModal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div v-if="selectedUser" class="row">
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">User ID</label>
                <div class="form-control-plaintext">{{ selectedUser.userId }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Tenant ID</label>
                <div class="form-control-plaintext">{{ selectedUser.tenantId }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Username</label>
                <div class="form-control-plaintext">{{ selectedUser.username }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Họ tên</label>
                <div class="form-control-plaintext">{{ selectedUser.fullName }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Email</label>
                <div class="form-control-plaintext">{{ selectedUser.email }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Vai trò</label>
                <div class="form-control-plaintext">
                  <span class="badge bg-primary">{{ selectedUser.role }}</span>
                </div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Trạng thái</label>
                <div class="form-control-plaintext">
                  <span :class="selectedUser.isActive === 1 ? 'badge bg-success' : 'badge bg-danger'">
                    {{ selectedUser.isActive === 1 ? 'ACTIVE' : 'INACTIVE' }}
                  </span>
                </div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Ngày tạo</label>
                <div class="form-control-plaintext">{{ formatDate(selectedUser.createdAt) }}</div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <router-link :to="`/users/${selectedUser?.userId}/edit`" class="btn btn-primary" @click="closeModal">
              <i class="bi bi-pencil"></i> Chỉnh sửa
            </router-link>
            <button type="button" class="btn btn-secondary" @click="closeModal">Đóng</button>
          </div>
        </div>
      </div>
    </div>

    <!-- Modal backdrop -->
    <div v-if="showModal" class="modal-backdrop fade show" @click="closeModal"></div>
  </div>
</template>

<script setup lang="ts">
import { onMounted, computed, ref } from 'vue';
import { useUserStore } from '../stores/user';
import type { User } from '../types';

const userStore = useUserStore();
const selectedUser = ref<User | null>(null);
const showModal = ref(false);

onMounted(() => {
  userStore.fetchAllUsers();
});

const users = computed(() => userStore.users);

const viewUserDetail = (user: User) => {
  selectedUser.value = user;
  showModal.value = true;
  document.body.classList.add('modal-open');
};

const closeModal = () => {
  showModal.value = false;
  selectedUser.value = null;
  document.body.classList.remove('modal-open');
};

const deleteUser = async (id: number) => {
  if (confirm('Bạn có chắc chắn muốn xóa người dùng này?')) {
    try {
      await userStore.deleteUser(id);
    } catch (e) {
      alert('Xóa người dùng thất bại!');
    }
  }
};

const formatDate = (dateString?: string) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleString('vi-VN');
};
</script>

<style scoped>
.form-control-plaintext {
  padding-left: 0;
  border: none;
  background-color: transparent;
}

.table td a:hover {
  color: #0d6efd;
  font-weight: 500;
}

.modal-body .row {
  margin: 0;
}

.modal-body .col-md-6 {
  padding-left: 15px;
  padding-right: 15px;
}

.badge {
  font-size: 0.85em;
}

/* Manual modal styles */
.modal {
  position: fixed;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 1055;
}

.modal.show {
  display: block !important;
}

.modal-backdrop {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  background-color: rgba(0, 0, 0, 0.5);
  z-index: 1050;
}

.modal-dialog {
  position: relative;
  margin: 1.75rem auto;
  max-width: 800px;
  z-index: 1056;
}
</style>

