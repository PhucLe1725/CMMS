<template>
  <div v-cloak>
    <h1 class="mb-4">Danh sách yêu cầu bảo dưỡng</h1>
    
    <!-- Loading state -->
    <div v-if="workOrderStore.loading || isInitialLoad" class="skeleton-container">
      <div class="d-flex justify-content-between mb-3">
        <div class="skeleton-item" style="width: 100px; height: 40px;"></div>
        <div class="skeleton-item" style="width: 150px; height: 40px;"></div>
      </div>
      <div v-for="n in pageSize" :key="n" class="skeleton-row">
        <div class="skeleton-item"></div>
        <div class="skeleton-item"></div>
        <div class="skeleton-item"></div>
        <div class="skeleton-item"></div>
        <div class="skeleton-item"></div>
        <div class="skeleton-item"></div>
      </div>
    </div>
    
    <div v-else-if="workOrderStore.error" class="alert alert-danger">{{ workOrderStore.error }}</div>
    
    <!-- Main content -->
    <div v-else class="fade-in"> 
      <div class="d-flex justify-content-between mb-3">
        <router-link to="/workrequests/new" class="btn btn-primary">
          <i class="bi bi-plus-circle me-2"></i>Thêm yêu cầu
        </router-link>
        <div class="d-flex align-items-center gap-3">
          <label>Hiển thị:</label>
          <select v-model="pageSize" @change="onPageSizeChange" class="form-select" style="width: auto;">
            <option value="10">10</option>
            <option value="20">20</option>
            <option value="50">50</option>
          </select>
          <span>/ trang</span>
        </div>
      </div>
      
      <div style="min-height: 400px;">
        <table class="table table-responsive">
          <thead>
            <tr>
              <th class="sortable" @click="sort('title')">
                Tiêu đề
                <i class="bi ms-1" :class="getSortIcon('title')"></i>
              </th>
              <th>Tài sản</th>
              <th class="sortable" @click="sort('status')">
                Trạng thái
                <i class="bi ms-1" :class="getSortIcon('status')"></i>
              </th>
              <th class="sortable" @click="sort('priority')">
                Độ ưu tiên
                <i class="bi ms-1" :class="getSortIcon('priority')"></i>
              </th>
              <th class="sortable" @click="sort('scheduledDate')">
                Ngày lên lịch
                <i class="bi ms-1" :class="getSortIcon('scheduledDate')"></i>
              </th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="workOrder in workOrderStore.workOrders" :key="workOrder.workOrderId">
              <td>
                <a href="javascript:void(0)" @click="viewWorkOrderDetail(workOrder)" class="text-decoration-none">
                  {{ workOrder.title }}
                </a>
              </td>
              <td>{{ workOrderStore.getAssetName(workOrder.assetId) }}</td>
              <td>
                <span :class="getStatusBadgeClass(workOrder.status)">
                  {{ workOrder.status.toUpperCase() }}
                </span>
              </td>
              <td>
                <span :class="getPriorityBadgeClass(workOrder.priority)">
                  {{ workOrder.priority.toUpperCase() }}
                </span>
              </td>
              <td>{{ formatDate(workOrder.scheduledDate) || 'Chưa có' }}</td>
              <td>
                <router-link :to="`/workrequests/${workOrder.workOrderId}/edit`" class="btn btn-sm btn-warning me-2">Edit</router-link>
                <button @click="deleteWorkOrder(workOrder.workOrderId)" class="btn btn-sm btn-danger">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <!-- Pagination -->
      <nav v-if="totalCount > pageSize">
        <ul class="pagination justify-content-center">
          <li class="page-item" :class="{ disabled: currentPage === 1 }">
            <button class="page-link" @click="goToPage(currentPage - 1)" :disabled="currentPage === 1">Trước</button>
          </li>
          <li class="page-item" v-for="page in visiblePages" :key="page" :class="{ active: page === currentPage }">
            <button class="page-link" @click="goToPage(page)">{{ page }}</button>
          </li>
          <li class="page-item" :class="{ disabled: currentPage === totalPages }">
            <button class="page-link" @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages">Sau</button>
          </li>
        </ul>
      </nav>
    </div>

    <!-- Work Order Detail Modal -->
    <div class="modal fade" :class="{ show: showModal }" :style="{ display: showModal ? 'block' : 'none' }">
      <div class="modal-dialog modal-xl">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Thông tin chi tiết yêu cầu bảo dưỡng</h5>
            <button type="button" class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <div v-if="selectedWorkOrder" class="row">
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Work Order ID</label>
                <div class="form-control-plaintext">{{ selectedWorkOrder.workOrderId }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Tiêu đề</label>
                <div class="form-control-plaintext">{{ selectedWorkOrder.title }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Tài sản</label>
                <div class="form-control-plaintext">
                  <span class="badge bg-info">{{ workOrderStore.getAssetName(selectedWorkOrder.assetId) }}</span>
                </div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Trạng thái</label>
                <div class="form-control-plaintext">
                  <span :class="getStatusBadgeClass(selectedWorkOrder.status)">{{ selectedWorkOrder.status.toUpperCase() }}</span>
                </div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Độ ưu tiên</label>
                <div class="form-control-plaintext">
                  <span :class="getPriorityBadgeClass(selectedWorkOrder.priority)">{{ selectedWorkOrder.priority.toUpperCase() }}</span>
                </div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Người được giao</label>
                <div class="form-control-plaintext">{{ workOrderStore.getUserName(selectedWorkOrder.assignedTo) || 'Chưa giao' }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Người tạo</label>
                <div class="form-control-plaintext">{{ workOrderStore.getUserName(selectedWorkOrder.createdBy) }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Ngày tạo</label>
                <div class="form-control-plaintext">{{ formatDate(selectedWorkOrder.createdAt) }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Ngày lên lịch</label>
                <div class="form-control-plaintext">{{ formatDate(selectedWorkOrder.scheduledDate) || 'Chưa có' }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Ngày hoàn thành</label>
                <div class="form-control-plaintext">{{ formatDate(selectedWorkOrder.completedAt) || 'Chưa có' }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Giờ ước tính</label>
                <div class="form-control-plaintext">{{ selectedWorkOrder.estimatedHours || 'Chưa có' }} giờ</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Giờ thực tế</label>
                <div class="form-control-plaintext">{{ selectedWorkOrder.actualHours || 'Chưa có' }} giờ</div>
              </div>
              <div class="col-12 mb-3">
                <label class="form-label fw-bold">Mô tả</label>
                <div class="form-control-plaintext">{{ selectedWorkOrder.description || 'Không có mô tả' }}</div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <router-link :to="`/workrequests/${selectedWorkOrder?.workOrderId}/edit`" class="btn btn-primary" @click="closeModal">
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

<script lang="ts" setup>
import { useWorkOrderStore } from '../stores/workOrder';
import { toast } from 'vue3-toastify';
import { onMounted, ref, computed } from 'vue';
import type { WorkOrder } from '../types';

const workOrderStore = useWorkOrderStore();
const currentPage = ref(1);
const pageSize = ref(10);
const totalCount = ref(0);
const sortField = ref<string>('');
const sortOrder = ref<'asc' | 'desc'>('asc');
const isInitialLoad = ref(true);
const selectedWorkOrder = ref<WorkOrder | null>(null);
const showModal = ref(false);

const STORAGE_KEY = 'workRequestList_state';

onMounted(async () => {
  const savedState = localStorage.getItem(STORAGE_KEY);
  if (savedState) {
    const state = JSON.parse(savedState);
    currentPage.value = state.currentPage || 1;
    pageSize.value = state.pageSize || 10;
    sortField.value = state.sortField || '';
    sortOrder.value = state.sortOrder || 'asc';
  }

  await Promise.all([
    workOrderStore.fetchAssets(),
    fetchWorkOrders()
  ]);
  
  setTimeout(() => {
    isInitialLoad.value = false;
  }, 100);
});

const totalPages = computed(() => Math.ceil(totalCount.value / pageSize.value));
const visiblePages = computed(() => {
  const pages = [];
  const start = Math.max(1, currentPage.value - 2);
  const end = Math.min(totalPages.value, start + 4);
  for (let i = start; i <= end; i++) {
    pages.push(i);
  }
  return pages;
});

const sort = (field: string) => {
  if (sortField.value === field) {
    sortOrder.value = sortOrder.value === 'asc' ? 'desc' : 'asc';
  } else {
    sortField.value = field;
    sortOrder.value = 'asc';
  }
  currentPage.value = 1;
  saveState();
  fetchWorkOrders();
};

const getSortIcon = (field: string) => {
  if (sortField.value !== field) return 'bi-chevron-expand';
  return sortOrder.value === 'asc' ? 'bi-chevron-up' : 'bi-chevron-down';
};

const fetchWorkOrders = async () => {
  const skipCount = (currentPage.value - 1) * pageSize.value;
  const response = await workOrderStore.fetchWorkOrders(
    skipCount, 
    pageSize.value,
    sortField.value || undefined,
    sortOrder.value
  );
  totalCount.value = response?.totalCount || 0;
};

const saveState = () => {
  const state = {
    currentPage: currentPage.value,
    pageSize: pageSize.value,
    sortField: sortField.value,
    sortOrder: sortOrder.value
  };
  localStorage.setItem(STORAGE_KEY, JSON.stringify(state));
};

const onPageSizeChange = () => {
  currentPage.value = 1;
  saveState();
  fetchWorkOrders();
};

const goToPage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
    saveState();
    fetchWorkOrders();
  }
};

const deleteWorkOrder = async (id: number) => {
  if (confirm('Bạn có chắc chắn muốn xóa yêu cầu bảo dưỡng này?')) {
    try {
      await workOrderStore.deleteWorkOrder(id);
      toast.success('Xóa yêu cầu bảo dưỡng thành công');
      fetchWorkOrders();
    } catch {
      toast.error('Xóa yêu cầu bảo dưỡng thất bại');
    }
  }
};

const formatDate = (dateString?: string) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleDateString('vi-VN');
};

const viewWorkOrderDetail = (workOrder: WorkOrder) => {
  selectedWorkOrder.value = workOrder;
  showModal.value = true;
  document.body.classList.add('modal-open');
};

const closeModal = () => {
  showModal.value = false;
  selectedWorkOrder.value = null;
  document.body.classList.remove('modal-open');
};

const getStatusBadgeClass = (status: string) => {
  switch (status?.toLowerCase()) {
    case 'open':
      return 'badge bg-primary';
    case 'in progress':
      return 'badge bg-warning';
    case 'completed':
      return 'badge bg-success';
    case 'canceled':
      return 'badge bg-danger';
    default:
      return 'badge bg-secondary';
  }
};

const getPriorityBadgeClass = (priority: string) => {
  switch (priority?.toLowerCase()) {
    case 'urgent':
      return 'badge bg-danger';
    case 'high':
      return 'badge bg-warning';
    case 'medium':
      return 'badge bg-info';
    case 'low':
      return 'badge bg-success';
    default:
      return 'badge bg-secondary';
  }
};
</script>

<style scoped>
/* Skeleton animation */
.skeleton-item {
  height: 40px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200px 100%;
  animation: skeleton-loading 1.2s ease-in-out infinite;
  border-radius: 4px;
  flex: 1;
}

.skeleton-row {
  display: flex;
  gap: 1rem;
  margin-bottom: 1rem;
}

.fade-in {
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

.sortable {
  cursor: pointer;
  user-select: none;
  transition: background-color 0.2s ease;
}

.sortable:hover {
  background-color: #f8f9fa;
}

.table td a:hover {
  color: #0d6efd;
  font-weight: 500;
}

.form-control-plaintext {
  padding-left: 0;
  border: none;
  background-color: transparent;
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

/* Modal styles */
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
  max-width: 1140px;
  z-index: 1056;
}
</style>
