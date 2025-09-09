<template>
  <div v-cloak>
    <h1 class="mb-4">Danh sách nhà cung cấp</h1>
    
    <!-- Loading state -->
    <div v-if="supplierStore.loading || isInitialLoad" class="skeleton-container">
      <div class="d-flex justify-content-between mb-3">
        <div class="skeleton-item" style="width: 100px; height: 40px;"></div>
        <div class="skeleton-item" style="width: 150px; height: 40px;"></div>
      </div>
      <div v-for="n in pageSize" :key="n" class="skeleton-row">
        <div class="skeleton-item"></div>
        <div class="skeleton-item"></div>
        <div class="skeleton-item"></div>
      </div>
    </div>
    
    <div v-else-if="supplierStore.error" class="alert alert-danger">{{ supplierStore.error }}</div>
    
    <!-- Main content -->
    <div v-else class="fade-in"> 
      <div class="d-flex justify-content-between mb-3">
        <router-link to="/suppliers/new" class="btn btn-primary">
          <i class="bi bi-plus-circle me-2"></i>Thêm nhà cung cấp
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
              <th class="sortable" @click="sort('supplierName')">
                Tên nhà cung cấp
                <i class="bi ms-1" :class="getSortIcon('supplierName')"></i>
              </th>
              <th class="sortable" @click="sort('contactInfo')">
                Thông tin liên hệ
                <i class="bi ms-1" :class="getSortIcon('contactInfo')"></i>
              </th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="supplier in supplierStore.suppliers" :key="supplier.supplierId">
              <td>
                <a href="javascript:void(0)" @click="viewSupplierDetail(supplier)" class="text-decoration-none">
                  {{ supplier.supplierName }}
                </a>
              </td>
              <td>{{ supplier.contactInfo }}</td>
              <td>
                <router-link :to="`/suppliers/${supplier.supplierId}/edit`" class="btn btn-sm btn-warning me-2">Edit</router-link>
                <button @click="deleteSupplier(supplier.supplierId)" class="btn btn-sm btn-danger">Delete</button>
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

    <!-- Supplier Detail Modal -->
    <div class="modal fade" :class="{ show: showModal }" :style="{ display: showModal ? 'block' : 'none' }">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Thông tin chi tiết nhà cung cấp</h5>
            <button type="button" class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <div v-if="selectedSupplier" class="row">
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Supplier ID</label>
                <div class="form-control-plaintext">{{ selectedSupplier.supplierId }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Tên nhà cung cấp</label>
                <div class="form-control-plaintext">{{ selectedSupplier.supplierName }}</div>
              </div>
              <div class="col-md-12 mb-3">
                <label class="form-label fw-bold">Thông tin liên hệ</label>
                <div class="form-control-plaintext">{{ selectedSupplier.contactInfo }}</div>
              </div>
              <div class="col-md-6 mb-3" v-if="selectedSupplier.createdAt">
                <label class="form-label fw-bold">Ngày tạo</label>
                <div class="form-control-plaintext">{{ formatDate(selectedSupplier.createdAt) }}</div>
              </div>
              <div class="col-md-6 mb-3" v-if="selectedSupplier.updatedAt">
                <label class="form-label fw-bold">Cập nhật lần cuối</label>
                <div class="form-control-plaintext">{{ formatDate(selectedSupplier.updatedAt) }}</div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <router-link :to="`/suppliers/${selectedSupplier?.supplierId}/edit`" class="btn btn-primary" @click="closeModal">
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
import { useSupplierStore } from '../stores/supplier';
import { toast } from 'vue3-toastify';
import { onMounted, ref, computed } from 'vue';
import type { Supplier } from '../types';

const supplierStore = useSupplierStore();
const currentPage = ref(1);
const pageSize = ref(10);
const totalCount = ref(0);
const sortField = ref<string>('');
const sortOrder = ref<'asc' | 'desc'>('asc');
const isInitialLoad = ref(true);
const selectedSupplier = ref<Supplier | null>(null);
const showModal = ref(false);

const STORAGE_KEY = 'supplierList_state';

onMounted(async () => {
  const savedState = localStorage.getItem(STORAGE_KEY);
  if (savedState) {
    const state = JSON.parse(savedState);
    currentPage.value = state.currentPage || 1;
    pageSize.value = state.pageSize || 10;
    sortField.value = state.sortField || '';
    sortOrder.value = state.sortOrder || 'asc';
  }

  await fetchSuppliers();
  
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
  fetchSuppliers();
};

const getSortIcon = (field: string) => {
  if (sortField.value !== field) return 'bi-chevron-expand';
  return sortOrder.value === 'asc' ? 'bi-chevron-up' : 'bi-chevron-down';
};

const fetchSuppliers = async () => {
  const skipCount = (currentPage.value - 1) * pageSize.value;
  const response = await supplierStore.fetchSuppliers(
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
  fetchSuppliers();
};

const goToPage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
    saveState();
    fetchSuppliers();
  }
};

const deleteSupplier = async (id: number) => {
  if (confirm('Bạn có chắc chắn muốn xóa nhà cung cấp này?')) {
    try {
      await supplierStore.deleteSupplier(id);
      toast.success('Xóa nhà cung cấp thành công');
      fetchSuppliers();
    } catch {
      toast.error('Xóa nhà cung cấp thất bại');
    }
  }
};

const formatDate = (dateString?: string) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleDateString('vi-VN');
};

const viewSupplierDetail = (supplier: Supplier) => {
  selectedSupplier.value = supplier;
  showModal.value = true;
  document.body.classList.add('modal-open');
};

const closeModal = () => {
  showModal.value = false;
  selectedSupplier.value = null;
  document.body.classList.remove('modal-open');
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

.modal-body .col-md-6, .modal-body .col-md-12 {
  padding-left: 15px;
  padding-right: 15px;
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
  max-width: 800px;
  z-index: 1056;
}
</style>
