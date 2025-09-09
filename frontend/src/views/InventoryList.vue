<template>
  <div v-cloak>
    <h1 class="mb-4">Danh sách phụ tùng</h1>
    
    <!-- Loading state -->
    <div v-if="inventoryStore.loading || isInitialLoad" class="skeleton-container">
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
    
    <div v-else-if="inventoryStore.error" class="alert alert-danger">{{ inventoryStore.error }}</div>
    
    <!-- Main content -->
    <div v-else class="fade-in"> 
      <div class="d-flex justify-content-between mb-3">
        <div class="d-flex gap-2">
          <router-link to="/inventories/new" class="btn btn-primary">
            <i class="bi bi-plus-circle me-2"></i>Thêm phụ tùng
          </router-link>
          <button @click="showLowStock" class="btn btn-warning">
            <i class="bi bi-exclamation-triangle me-2"></i>Tồn kho thấp
          </button>
        </div>
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
              <th class="sortable" @click="sort('partName')">
                Tên phụ tùng
                <i class="bi ms-1" :class="getSortIcon('partName')"></i>
              </th>
              <th class="sortable" @click="sort('partNumber')">
                Mã phụ tùng
                <i class="bi ms-1" :class="getSortIcon('partNumber')"></i>
              </th>
              <th class="sortable" @click="sort('quantity')">
                Tồn kho
                <i class="bi ms-1" :class="getSortIcon('quantity')"></i>
              </th>
              <th class="sortable" @click="sort('minQuantity')">
                Tối thiểu
                <i class="bi ms-1" :class="getSortIcon('minQuantity')"></i>
              </th>
              <th>Vị trí</th>
              <th class="sortable" @click="sort('unitCost')">
                Giá đơn vị
                <i class="bi ms-1" :class="getSortIcon('unitCost')"></i>
              </th>
              <th>Trạng thái</th>
              <th>Hành động</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="inventory in inventoryStore.inventories" :key="inventory.partId">
              <td>
                <a href="javascript:void(0)" @click="viewInventoryDetail(inventory)" class="text-decoration-none">
                  {{ inventory.partName }}
                </a>
              </td>
              <td>{{ inventory.partNumber }}</td>
              <td>{{ inventory.quantity }}</td>
              <td>{{ inventory.minQuantity }}</td>
              <td>{{ inventoryStore.getLocationName(inventory.locationId) }}</td>
              <td>{{ formatCurrency(inventory.unitCost) }}</td>
              <td>
                <span :class="getStockStatusBadgeClass(inventory.quantity, inventory.minQuantity)">
                  {{ getStockStatusText(inventory.quantity, inventory.minQuantity) }}
                </span>
              </td>
              <td>
                <router-link :to="`/inventories/${inventory.partId}/edit`" class="btn btn-sm btn-warning me-2">Edit</router-link>
                <button @click="deleteInventory(inventory.partId)" class="btn btn-sm btn-danger">Delete</button>
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

    <!-- Inventory Detail Modal -->
    <div class="modal fade" :class="{ show: showModal }" :style="{ display: showModal ? 'block' : 'none' }">
      <div class="modal-dialog modal-lg">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title">Thông tin chi tiết phụ tùng</h5>
            <button type="button" class="btn-close" @click="closeModal"></button>
          </div>
          <div class="modal-body">
            <div v-if="selectedInventory" class="row">
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Part ID</label>
                <div class="form-control-plaintext">{{ selectedInventory.partId }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Tên phụ tùng</label>
                <div class="form-control-plaintext">{{ selectedInventory.partName }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Mã phụ tùng</label>
                <div class="form-control-plaintext">{{ selectedInventory.partNumber }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Số lượng tồn kho</label>
                <div class="form-control-plaintext">{{ selectedInventory.quantity }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Số lượng tối thiểu</label>
                <div class="form-control-plaintext">{{ selectedInventory.minQuantity }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Vị trí</label>
                <div class="form-control-plaintext">
                  <span class="badge bg-secondary">{{ inventoryStore.getLocationName(selectedInventory.locationId) }}</span>
                </div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Giá đơn vị</label>
                <div class="form-control-plaintext">{{ formatCurrency(selectedInventory.unitCost) }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Supplier ID</label>
                <div class="form-control-plaintext">{{ selectedInventory.supplierId }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Trạng thái tồn kho</label>
                <div class="form-control-plaintext">
                  <span :class="getStockStatusBadgeClass(selectedInventory.quantity, selectedInventory.minQuantity)">
                    {{ getStockStatusText(selectedInventory.quantity, selectedInventory.minQuantity) }}
                  </span>
                </div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <router-link :to="`/inventories/${selectedInventory?.partId}/edit`" class="btn btn-primary" @click="closeModal">
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
import { useInventoryStore } from '../stores/inventory';
import { toast } from 'vue3-toastify';
import { onMounted, ref, computed } from 'vue';
import type { Inventory } from '../types';

const inventoryStore = useInventoryStore();
const currentPage = ref(1);
const pageSize = ref(10);
const totalCount = ref(0);
const sortField = ref<string>('');
const sortOrder = ref<'asc' | 'desc'>('asc');
const isInitialLoad = ref(true);
const selectedInventory = ref<Inventory | null>(null);
const showModal = ref(false);
const isLowStockMode = ref(false);

const STORAGE_KEY = 'inventoryList_state';

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
    inventoryStore.fetchLocations(),
    fetchInventories()
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
  fetchInventories();
};

const getSortIcon = (field: string) => {
  if (sortField.value !== field) return 'bi-chevron-expand';
  return sortOrder.value === 'asc' ? 'bi-chevron-up' : 'bi-chevron-down';
};

const fetchInventories = async () => {
  const skipCount = (currentPage.value - 1) * pageSize.value;
  let response;
  
  if (isLowStockMode.value) {
    response = await inventoryStore.fetchLowStock(
      skipCount, 
      pageSize.value,
      sortField.value || undefined,
      sortOrder.value
    );
  } else {
    response = await inventoryStore.fetchInventories(
      skipCount, 
      pageSize.value,
      sortField.value || undefined,
      sortOrder.value
    );
  }
  
  totalCount.value = response?.totalCount || 0;
};

const showLowStock = () => {
  isLowStockMode.value = !isLowStockMode.value;
  currentPage.value = 1;
  fetchInventories();
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
  fetchInventories();
};

const goToPage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
    saveState();
    fetchInventories();
  }
};

const deleteInventory = async (id: number) => {
  if (confirm('Bạn có chắc chắn muốn xóa phụ tùng này?')) {
    try {
      await inventoryStore.deleteInventory(id);
      toast.success('Xóa phụ tùng thành công');
      fetchInventories();
    } catch {
      toast.error('Xóa phụ tùng thất bại');
    }
  }
};

const formatCurrency = (amount: number) => {
  return new Intl.NumberFormat('vi-VN', {
    style: 'currency',
    currency: 'VND'
  }).format(amount);
};

const viewInventoryDetail = (inventory: Inventory) => {
  selectedInventory.value = inventory;
  showModal.value = true;
  document.body.classList.add('modal-open');
};

const closeModal = () => {
  showModal.value = false;
  selectedInventory.value = null;
  document.body.classList.remove('modal-open');
};

const getStockStatusBadgeClass = (quantity: number, minQuantity: number) => {
  const status = inventoryStore.getStockStatus(quantity, minQuantity);
  switch (status) {
    case 'OUT_OF_STOCK':
      return 'badge bg-danger';
    case 'LOW_STOCK':
      return 'badge bg-warning';
    case 'IN_STOCK':
      return 'badge bg-success';
    default:
      return 'badge bg-secondary';
  }
};

const getStockStatusText = (quantity: number, minQuantity: number) => {
  const status = inventoryStore.getStockStatus(quantity, minQuantity);
  switch (status) {
    case 'OUT_OF_STOCK':
      return 'HẾT HÀNG';
    case 'LOW_STOCK':
      return 'TỒN KHO THẤP';
    case 'IN_STOCK':
      return 'CÒN HÀNG';
    default:
      return 'KHÔNG XÁC ĐỊNH';
  }
};
</script>

<style scoped>
/* ...existing skeleton and animation styles... */
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
  max-width: 800px;
  z-index: 1056;
}
</style>
