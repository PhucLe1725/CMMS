<template>
  <div class="supplier-form-container">
    <h1 class="mb-4">{{ isEdit ? 'Chỉnh sửa nhà cung cấp' : 'Thêm nhà cung cấp' }}</h1>
    <form @submit.prevent="submitForm" class="needs-validation" novalidate>
      <div class="row">
        <div class="col-md-12 mb-3">
          <label class="form-label">Tên nhà cung cấp <span class="text-danger">*</span></label>
          <input v-model="form.supplierName" class="form-control" required />
          <div class="invalid-feedback">Tên nhà cung cấp là bắt buộc</div>
        </div>
        <div class="col-md-12 mb-3">
          <label class="form-label">Thông tin liên hệ <span class="text-danger">*</span></label>
          <textarea v-model="form.contactInfo" class="form-control" rows="4" required placeholder="Ví dụ: contact@company.com, +84-123-456-789, 123 Đường ABC, Quận XYZ, TP. HCM"></textarea>
          <div class="invalid-feedback">Thông tin liên hệ là bắt buộc</div>
          <div class="form-text">Nhập email, số điện thoại, địa chỉ hoặc các thông tin liên hệ khác</div>
        </div>
      </div>
      <div class="mt-4">
        <button type="submit" class="btn btn-primary me-2">{{ isEdit ? 'Cập nhật' : 'Tạo mới' }}</button>
        <router-link to="/suppliers" class="btn btn-secondary">Hủy</router-link>
      </div>
    </form>
  </div>
</template>

<script lang="ts" setup>
import { useSupplierStore } from '../stores/supplier';
import { toast } from 'vue3-toastify';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import type { Supplier } from '../types';

const supplierStore = useSupplierStore();
const route = useRoute();
const router = useRouter();

const isEdit = ref(false);
const form = ref<Partial<Supplier>>({
  supplierName: '',
  contactInfo: '',
});

onMounted(async () => {
  if (route.params.id) {
    isEdit.value = true;
    try {
      await supplierStore.fetchSupplierById(route.params.id);
      const supplier = supplierStore.supplier;
      if (supplier) {
        form.value = {
          supplierName: supplier.supplierName,
          contactInfo: supplier.contactInfo,
        };
      }
    } catch {
      toast.error('Không thể tải thông tin nhà cung cấp');
      router.push('/suppliers');
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
    const formData = { ...form.value };
    
    // Trim whitespace
    formData.supplierName = formData.supplierName?.trim();
    formData.contactInfo = formData.contactInfo?.trim();

    if (isEdit.value) {
      await supplierStore.updateSupplier(Number(route.params.id), formData);
      toast.success('Cập nhật nhà cung cấp thành công');
    } else {
      await supplierStore.createSupplier(formData as Omit<Supplier, 'supplierId' | 'createdAt' | 'updatedAt'>);
      toast.success('Tạo nhà cung cấp thành công');
    }
    
    router.push('/suppliers');
  } catch (error: any) {
    console.error('SupplierForm submitForm error:', error);
    
    if (error.response?.status === 403) {
      toast.error('Bạn không có quyền thực hiện hành động này');
    } else if (error.response?.status === 401) {
      toast.error('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại');
    } else if (error.response?.status === 400) {
      toast.error('Dữ liệu không hợp lệ. Vui lòng kiểm tra lại');
    } else {
      toast.error('Có lỗi xảy ra khi lưu nhà cung cấp');
    }
  }
};
</script>

<style scoped>
.supplier-form-container {
  max-width: 800px;
  margin: 0 auto;
  background: #fff;
  border-radius: 12px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.07);
  padding: 32px 24px;
}

h1 {
  font-weight: 600;
  margin-bottom: 24px;
}

.form-label {
  font-weight: 500;
}

.text-danger {
  color: #dc3545 !important;
}

.was-validated .form-control:invalid,
.was-validated .form-select:invalid {
  border-color: #dc3545;
}

.was-validated .form-control:valid,
.was-validated .form-select:valid {
  border-color: #198754;
}

.form-text {
  font-size: 0.875rem;
  color: #6c757d;
}
</style>
