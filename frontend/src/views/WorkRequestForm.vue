<template>
  <div class="work-request-form-container">
    <h1 class="mb-4">{{ isEdit ? 'Chỉnh sửa yêu cầu bảo dưỡng' : 'Tạo yêu cầu bảo dưỡng' }}</h1>
    <form @submit.prevent="submitForm" class="needs-validation" novalidate>
      <div class="row">
        <div class="col-md-6 mb-3">
          <label class="form-label">Tiêu đề <span class="text-danger">*</span></label>
          <input v-model="form.title" class="form-control" required />
          <div class="invalid-feedback">Tiêu đề là bắt buộc</div>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Tài sản <span class="text-danger">*</span></label>
          <select v-model="form.assetId" class="form-select" required>
            <option value="">Chọn tài sản</option>
            <option v-for="asset in workOrderStore.assets" :key="asset.assetId" :value="asset.assetId">
              {{ asset.assetName }}
            </option>
          </select>
          <div class="invalid-feedback">Vui lòng chọn tài sản</div>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Trạng thái</label>
          <select v-model="form.status" class="form-select" required>
            <option value="Open">MỞ</option>
            <option value="In Progress">ĐANG THỰC HIỆN</option>
            <option value="Completed">HOÀN THÀNH</option>
            <option value="Canceled">ĐÃ HỦY</option>
          </select>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Độ ưu tiên</label>
          <select v-model="form.priority" class="form-select" required>
            <option value="Low">THẤP</option>
            <option value="Medium">TRUNG BÌNH</option>
            <option value="High">CAO</option>
            <option value="Urgent">KHẨN CẤP</option>
          </select>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Người được giao</label>
          <select v-model="form.assignedTo" class="form-select">
            <option value="">Chưa giao</option>
            <option v-for="user in workOrderStore.users" :key="user.userId" :value="user.userId">
              {{ user.fullName }}
            </option>
          </select>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Ngày lên lịch</label>
          <input v-model="form.scheduledDate" type="date" class="form-control" />
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Giờ ước tính</label>
          <input v-model.number="form.estimatedHours" type="number" class="form-control" min="0" step="0.5" />
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Giờ thực tế</label>
          <input v-model.number="form.actualHours" type="number" class="form-control" min="0" step="0.5" />
        </div>
        <!-- Chỉ hiển thị completedAt khi edit -->
        <div v-if="isEdit" class="col-md-6 mb-3">
          <label class="form-label">Ngày hoàn thành</label>
          <input v-model="form.completedAt" type="datetime-local" class="form-control" />
        </div>
        <div class="col-12 mb-3">
          <label class="form-label">Mô tả</label>
          <textarea v-model="form.description" class="form-control" rows="4" placeholder="Mô tả chi tiết yêu cầu bảo dưỡng..."></textarea>
        </div>
      </div>
      <div class="mt-4">
        <button type="submit" class="btn btn-primary me-2">{{ isEdit ? 'Cập nhật' : 'Tạo mới' }}</button>
        <router-link to="/workrequests" class="btn btn-secondary">Hủy</router-link>
      </div>
    </form>
  </div>
</template>

<script lang="ts" setup>
import { useWorkOrderStore } from '../stores/workOrder';
import { useAuthStore } from '../stores/auth';
import { toast } from 'vue3-toastify';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import type { WorkOrder } from '../types';

const workOrderStore = useWorkOrderStore();
const authStore = useAuthStore();
const route = useRoute();
const router = useRouter();

const isEdit = ref(false);
const form = ref<Partial<WorkOrder>>({
  title: '',
  assetId: 0,
  description: '',
  status: 'Open',
  priority: 'Medium',
  assignedTo: undefined,
  scheduledDate: '',
  estimatedHours: 0,
  actualHours: 0,
  completedAt: '',
  tenantId: authStore.user?.tenantId as number,
  createdBy: authStore.user?.userId as number,
});

onMounted(async () => {
  await Promise.all([
    workOrderStore.fetchAssets(),
    workOrderStore.fetchUsers()
  ]);
  
  if (route.params.id) {
    isEdit.value = true;
    try {
      await workOrderStore.fetchWorkOrderById(route.params.id);
      const wo = workOrderStore.workOrder;
      if (wo) {
        // Map status từ backend format sang frontend format
        let mappedStatus = wo.status;
        if ((wo.status as string) === 'IN_PROGRESS') mappedStatus = 'In Progress';
        else if ((wo.status as string) === 'OPEN') mappedStatus = 'Open';
        else if ((wo.status as string) === 'COMPLETED') mappedStatus = 'Completed';
        else if ((wo.status as string) === 'CANCELED') mappedStatus = 'Canceled';

        // Map priority từ backend format sang frontend format  
        let mappedPriority = wo.priority;
        if ((wo.priority as string) === 'LOW') mappedPriority = 'Low';
        else if ((wo.priority as string) === 'MEDIUM') mappedPriority = 'Medium';
        else if ((wo.priority as string) === 'HIGH') mappedPriority = 'High';
        else if ((wo.priority as string) === 'URGENT') mappedPriority = 'Urgent';

        form.value = {
          title: wo.title,
          assetId: wo.assetId,
          description: wo.description,
          status: mappedStatus as any,
          priority: mappedPriority as any,
          assignedTo: wo.assignedTo,
          scheduledDate: wo.scheduledDate ? wo.scheduledDate.split('T')[0] : '',
          estimatedHours: wo.estimatedHours,
          actualHours: wo.actualHours,
          completedAt: wo.completedAt ? wo.completedAt.slice(0, 16) : '', // Format for datetime-local
          tenantId: wo.tenantId,
          createdBy: wo.createdBy,
        };
      }
    } catch {
      toast.error('Không thể tải thông tin yêu cầu bảo dưỡng');
      router.push('/workrequests');
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
    
    // Convert dates to ISO string if provided
    if (formData.scheduledDate) {
      formData.scheduledDate = new Date(formData.scheduledDate).toISOString();
    }
    
    if (formData.completedAt && isEdit.value) {
      formData.completedAt = new Date(formData.completedAt).toISOString();
    } else if (!isEdit.value) {
      delete formData.completedAt;
    }

    // Chuyển đổi số về number nếu cần
    if (formData.estimatedHours) {
      formData.estimatedHours = Number(formData.estimatedHours);
    }
    if (formData.actualHours) {
      formData.actualHours = Number(formData.actualHours);
    }
    
    // Đảm bảo assignedTo có giá trị đúng
    if (!formData.assignedTo || formData.assignedTo === 0) {
      formData.assignedTo = undefined;
    } else {
      formData.assignedTo = Number(formData.assignedTo);
    }
    
    // Đảm bảo có đầy đủ các trường bắt buộc theo format Postman
    formData.workOrderId = Number(route.params.id);
    formData.tenantId = Number(authStore.user?.tenantId as number);
    formData.createdBy = Number(authStore.user?.userId as number);
    formData.assetId = Number(formData.assetId);

    // Đảm bảo status và priority đúng format
    if (formData.status) {
      formData.status = formData.status.toUpperCase().replace(' ', '_') as any;
    }
    if (formData.priority) {
      formData.priority = formData.priority.toUpperCase() as any;
    }

    if (isEdit.value) {
      await workOrderStore.updateWorkOrder(Number(route.params.id), formData);
      toast.success('Cập nhật yêu cầu bảo dưỡng thành công');
    } else {
      await workOrderStore.createWorkOrder(formData as Omit<WorkOrder, 'workOrderId' | 'createdAt' | 'completedAt'>);
      toast.success('Tạo yêu cầu bảo dưỡng thành công');
    }
    
    router.push('/workrequests');
  } catch (error: any) {
    console.error('WorkRequestForm submitForm error:', error);
    
    if (error.response?.status === 403) {
      toast.error('Bạn không có quyền thực hiện hành động này');
    } else if (error.response?.status === 401) {
      toast.error('Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại');
      authStore.logout();
    } else if (error.response?.status === 400) {
      toast.error('Dữ liệu không hợp lệ. Vui lòng kiểm tra lại');
    } else {
      toast.error('Có lỗi xảy ra khi lưu yêu cầu bảo dưỡng');
    }
  }
};
</script>

<style scoped>
.work-request-form-container {
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