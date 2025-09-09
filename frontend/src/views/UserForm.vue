<template>
  <div class="user-form-container">
    <h2>{{ isEdit ? 'Chỉnh sửa nhân sự' : 'Thêm nhân sự' }}</h2>
    <form @submit.prevent="submitForm" class="mt-3 needs-validation" novalidate>
      <div class="row">
        <div class="mb-3 col-md-6">
          <label class="form-label">Tenant ID</label>
          <input v-model.number="form.tenantId" type="number" class="form-control" required min="1" />
          <div class="invalid-feedback">Tenant ID là bắt buộc và phải lớn hơn 0.</div>
        </div>
        <div class="mb-3 col-md-6">
          <label class="form-label">Username</label>
          <input v-model.trim="form.username" type="text" class="form-control" :readonly="isEdit" required pattern="^[a-zA-Z0-9_]{3,}$" />
          <div class="invalid-feedback">Username tối thiểu 3 ký tự, không dấu cách.</div>
        </div>
      </div>
      <div class="row">
        <div class="mb-3 col-md-6" v-if="!isEdit">
          <label class="form-label">Mật khẩu</label>
          <input v-model="form.password" type="password" class="form-control" required minlength="6" />
          <div class="invalid-feedback">Mật khẩu tối thiểu 6 ký tự.</div>
        </div>
        <div class="mb-3 col-md-6">
          <label class="form-label">Email</label>
          <input v-model.trim="form.email" type="email" class="form-control" required />
          <div class="invalid-feedback">Email hợp lệ là bắt buộc.</div>
        </div>
      </div>
      <div class="row">
        <div class="mb-3 col-md-6">
          <label class="form-label">Họ tên</label>
          <input v-model.trim="form.fullName" type="text" class="form-control" required />
          <div class="invalid-feedback">Họ tên là bắt buộc.</div>
        </div>
        <div class="mb-3 col-md-6">
          <label class="form-label">Vai trò</label>
          <select v-model="form.role" class="form-select" required>
            <option value="" disabled>Chọn vai trò</option>
            <option value="ADMIN">Quản trị</option>
            <option value="MANAGER">Quản lý</option>
            <option value="TECHNICIAN">Kỹ thuật</option>
          </select>
          <div class="invalid-feedback">Vai trò là bắt buộc.</div>
        </div>
      </div>
      <div class="mb-3" v-if="isEdit">
        <label class="form-label">Trạng thái hoạt động</label>
        <select v-model.number="form.isActive" class="form-select" required>
          <option :value="1">Active</option>
          <option :value="0">Inactive</option>
        </select>
      </div>
      <div v-else>
        <input type="hidden" v-model.number="form.isActive" />
      </div>
      <div class="mt-4">
        <button type="submit" class="btn btn-primary me-2">{{ isEdit ? 'Lưu' : 'Tạo mới' }}</button>
        <router-link to="/users" class="btn btn-secondary">Hủy</router-link>
      </div>
    </form>
  </div>
</template>

<script setup lang="ts">
import { onMounted, reactive } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import { useUserStore } from '../stores/user';

const route = useRoute();
const router = useRouter();
const userStore = useUserStore();
const isEdit = !!route.params.id;

const form = reactive({
  tenantId: 1,
  userId: undefined as number | undefined,
  username: '',
  password: '',
  role: '',
  email: '',
  fullName: '',
  isActive: 1
});

onMounted(async () => {
  if (isEdit) {
    try {
      await userStore.fetchUserById(Number(route.params.id));
      const u = userStore.user;
      if (u) {
        form.tenantId = u.tenantId;
        form.userId = u.userId;
        form.username = u.username;
        form.password = '';
        form.role = u.role;
        form.email = u.email;
        form.fullName = u.fullName;
        form.isActive = u.isActive ?? 1;
      }
    } catch (e) {
      console.error('Không lấy được thông tin user:', e);
      alert('Không lấy được thông tin user!');
      router.push('/users');
    }
  }
});

const submitForm = async () => {
  const formEl = document.querySelector('.needs-validation') as HTMLFormElement;
  if (!formEl.checkValidity()) {
    formEl.classList.add('was-validated');
    return;
  }
  try {
    if (!isEdit) {
      await userStore.createUser({
        tenantId: form.tenantId,
        username: form.username,
        password: form.password,
        role: form.role,
        email: form.email,
        fullName: form.fullName,
        isActive: form.isActive,
      });
    } else {
      await userStore.updateUser(Number(form.userId), {
        tenantId: form.tenantId,
        username: form.username,
        role: form.role,
        email: form.email,
        fullName: form.fullName,
        isActive: form.isActive,
      });
    }
    router.push('/users');
  } catch (e) {
    console.error(isEdit ? 'Lỗi khi cập nhật user:' : 'Lỗi khi tạo mới user:', e);
    alert('Có lỗi xảy ra khi lưu user. Vui lòng kiểm tra lại dữ liệu hoặc thử lại sau.');
  }
};
</script>

<style scoped>
.user-form-container {
  max-width: 700px;
  margin: 0 auto;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.07);
  padding: 32px 24px;
}
h2 {
  font-weight: 600;
  margin-bottom: 24px;
}
.form-label {
  font-weight: 500;
}
input[readonly] {
  background: #f5f5f5;
}
.was-validated .form-control:invalid,
.was-validated .form-select:invalid {
  border-color: #dc3545;
}
.was-validated .form-control:valid,
.was-validated .form-select:valid {
  border-color: #198754;
}
</style>
