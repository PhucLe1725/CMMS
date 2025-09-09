<template>
  <div class="inventory-form-container">
    <h1 class="mb-4">{{ isEdit ? 'Chỉnh sửa phụ tùng' : 'Thêm phụ tùng' }}</h1>
    <form @submit.prevent="submitForm" class="needs-validation" novalidate>
      <div class="row">
        <div class="col-md-6 mb-3">
          <label class="form-label">Tên phụ tùng <span class="text-danger">*</span></label>
          <input v-model="form.partName" class="form-control" required />
          <div class="invalid-feedback">Tên phụ tùng là bắt buộc</div>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Mã phụ tùng <span class="text-danger">*</span></label>
          <input v-model="form.partNumber" class="form-control" required />
          <div class="invalid-feedback">Mã phụ tùng là bắt buộc</div>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Số lượng tồn kho <span class="text-danger">*</span></label>
          <input v-model.number="form.quantity" type="number" class="form-control" min="0" required />
          <div class="invalid-feedback">Số lượng tồn kho là bắt buộc</div>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Số lượng tối thiểu <span class="text-danger">*</span></label>
          <input v-model.number="form.minQuantity" type="number" class="form-control" min="0" required />
          <div class="invalid-feedback">Số lượng tối thiểu là bắt buộc</div>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Vị trí <span class="text-danger">*</span></label>
          <select v-model="form.locationId" class="form-select" required>
            <option value="">Chọn vị trí</option>
            <option v-for="location in inventoryStore.locations" :key="location.locationId" :value="location.locationId">
              {{ location.locationName }}
            </option>
          </select>
          <div class="invalid-feedback">Vui lòng chọn vị trí</div>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Giá đơn vị <span class="text-danger">*</span></label>
          <input v-model.number="form.unitCost" type="number" class="form-control" min="0" step="0.01" required />
          <div class="invalid-feedback">Giá đơn vị là bắt buộc</div>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Supplier ID <span class="text-danger">*</span></label>
          <input v-model.number="form.supplierId" type="number" class="form-control" min="1" required />
          <div class="invalid-feedback">Supplier ID là bắt buộc</div>
        </div>
      </div>
      <div class="mt-4">
        <button type="submit" class="btn btn-primary me-2">{{ isEdit ? 'Cập nhật' : 'Tạo mới' }}</button>
        <router-link to="/inventories" class="btn btn-secondary">Hủy</router-link>
      </div>
    </form>
  </div>
</template>

<script lang="ts" setup>
import { useInventoryStore } from '../stores/inventory';
import { useAuthStore } from '../stores/auth';
import { toast } from 'vue3-toastify';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import type { Inventory } from '../types';

const inventoryStore = useInventoryStore();
const authStore = useAuthStore();
const route = useRoute();
const router = useRouter();

const isEdit = ref(false);
const form = ref<Partial<Inventory>>({
  partName: '',
  partNumber: '',
  quantity: 0,
  minQuantity: 0,
  locationId: 0,
  unitCost: 0,
  supplierId: 0,
  tenantId: authStore.user?.tenantId as number,
});

onMounted(async () => {
  await inventoryStore.fetchLocations();
  
  if (route.params.id) {
    isEdit.value = true;
    try {
      await inventoryStore.fetchInventoryById(route.params.id);
      const inv = inventoryStore.inventory;
      if (inv) {
        form.value = {
          partName: inv.partName,
          partNumber: inv.partNumber,
          quantity: inv.quantity,
          minQuantity: inv.minQuantity,
          locationId: inv.locationId,
          unitCost: inv.unitCost,
          supplierId: inv.supplierId,
          tenantId: inv.tenantId,
        };
      }
    } catch {
      toast.error('Không thể tải thông tin phụ tùng');
      router.push('/inventories');
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
    
    // Đảm bảo các số được convert đúng
    formData.quantity = Number(formData.quantity);
    formData.minQuantity = Number(formData.minQuantity);
    formData.locationId = Number(formData.locationId);
    formData.unitCost = Number(formData.unitCost);
    formData.supplierId = Number(formData.supplierId);
    formData.tenantId = Number(authStore.user?.tenantId as number);

    if (isEdit.value) {
      await inventoryStore.updateInventory(Number(route.params.id), formData);
      toast.success('Cập nhật phụ tùng thành công');
    } else {
      await inventoryStore.createInventory(formData as Omit<Inventory, 'partId' | 'createdAt' | 'updatedAt'>);
      toast.success('Tạo phụ tùng thành công');
    }
    
    router.push('/inventories');
  } catch (error: any) {
    console.error('InventoryForm submitForm error:', error);
    
    if (error.response?.status === 403) {
      toast.error('Bạn không có quyền thực hiện hành động này');
    } else if (error.response?.status === 401) {
      toast.error('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại');
      authStore.logout();
    } else if (error.response?.status === 400) {
      toast.error('Dữ liệu không hợp lệ. Vui lòng kiểm tra lại');
    } else {
      toast.error('Có lỗi xảy ra khi lưu phụ tùng');
    }
  }
};
</script>

<style scoped>
.inventory-form-container {
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
</style>
