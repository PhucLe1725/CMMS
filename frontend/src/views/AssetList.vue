<template>
  <div v-cloak>
    <h1 class="mb-4">Danh sách máy</h1>
    
    <!-- Loading state mượt hơn -->
    <div v-if="assetStore.loading || isInitialLoad" class="skeleton-container">
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
        <div class="skeleton-item"></div>
      </div>
    </div>
    
    <div v-else-if="assetStore.error" class="alert alert-danger">{{ assetStore.error }}</div>
    
    <!-- Main content với fade in -->
    <div v-else class="fade-in"> 
      <div class="d-flex justify-content-between mb-3">
        <router-link to="/assets/new" class="btn btn-primary">
          <i class="bi bi-plus-circle me-2"></i>Thêm
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
      
      <!-- Table với min-height để tránh layout shift -->
      <div style="min-height: 400px;">
        <table class="table table-responsive">
          <thead>
            <tr>
              <th class="sortable" @click="sort('assetName')">
                Name
                <i class="bi ms-1" :class="getSortIcon('assetName')"></i>
              </th>
              <th>Type</th>
              <th class="sortable" @click="sort('serialNumber')">
                Serial Number
                <i class="bi ms-1" :class="getSortIcon('serialNumber')"></i>
              </th>
              <th>Location</th>
              <th class="sortable" @click="sort('status')">
                Status
                <i class="bi ms-1" :class="getSortIcon('status')"></i>
              </th>
              <th class="sortable" @click="sort('warrantyExpiry')">
                Warranty Expiry
                <i class="bi ms-1" :class="getSortIcon('warrantyExpiry')"></i>
              </th>
              <th>Actions</th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="asset in assetStore.assets" :key="asset.assetId">
              <td>
                <a href="javascript:void(0)" @click="viewAssetDetail(asset)" class="text-decoration-none">
                  {{ asset.assetName }}
                </a>
              </td>
              <td>{{ assetStore.getAssetTypeName(asset.assetTypeId) }}</td>
              <td>{{ asset.serialNumber }}</td>
              <td>{{ assetStore.getLocationName(asset.locationId) }}</td>
              <td>{{ asset.status }}</td>
              <td>{{ formatDate(asset.warrantyExpiry) || 'Chưa có' }}</td>
              <td>
                <router-link :to="`/assets/${asset.assetId}/edit`" class="btn btn-sm btn-warning me-2">Edit</router-link>
                <button @click="deleteAsset(asset.assetId)" class="btn btn-sm btn-danger">Delete</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      
      <!-- Pagination controls -->
      <nav v-if="totalCount > pageSize">
        <ul class="pagination justify-content-center">
          <li class="page-item" :class="{ disabled: currentPage === 1 }">
            <button class="page-link" @click="goToPage(currentPage - 1)" :disabled="currentPage === 1">
              Trước
            </button>
          </li>
          <li class="page-item" v-for="page in visiblePages" :key="page" :class="{ active: page === currentPage }">
            <button class="page-link" @click="goToPage(page)">{{ page }}</button>
          </li>
          <li class="page-item" :class="{ disabled: currentPage === totalPages }">
            <button class="page-link" @click="goToPage(currentPage + 1)" :disabled="currentPage === totalPages">
              Sau
            </button>
          </li>
        </ul>
      </nav>
    </div>

    <!-- Asset Detail Modal -->
    <div class="modal fade" :class="{ show: showModal }" :style="{ display: showModal ? 'block' : 'none' }" 
         id="assetDetailModal" tabindex="-1" aria-labelledby="assetDetailModalLabel" aria-hidden="true">
      <div class="modal-dialog modal-xl">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="assetDetailModalLabel">Thông tin chi tiết tài sản</h5>
            <button type="button" class="btn-close" @click="closeModal" aria-label="Close"></button>
          </div>
          <div class="modal-body">
            <div v-if="selectedAsset" class="row">
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Asset ID</label>
                <div class="form-control-plaintext">{{ selectedAsset.assetId }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Tên tài sản</label>
                <div class="form-control-plaintext">{{ selectedAsset.assetName }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Loại tài sản</label>
                <div class="form-control-plaintext">
                  <span class="badge bg-info">{{ assetStore.getAssetTypeName(selectedAsset.assetTypeId) }}</span>
                </div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Serial Number</label>
                <div class="form-control-plaintext">{{ selectedAsset.serialNumber }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Vị trí</label>
                <div class="form-control-plaintext">
                  <span class="badge bg-secondary">{{ assetStore.getLocationName(selectedAsset.locationId) }}</span>
                </div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Trạng thái</label>
                <div class="form-control-plaintext">
                  <span :class="getStatusBadgeClass(selectedAsset.status)">
                    {{ selectedAsset.status }}
                  </span>
                </div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Ngày mua</label>
                <div class="form-control-plaintext">{{ formatDate(selectedAsset.purchaseDate) || 'Chưa có' }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Hết bảo hành</label>
                <div class="form-control-plaintext">{{ formatDate(selectedAsset.warrantyExpiry) || 'Chưa có' }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Ngày tạo</label>
                <div class="form-control-plaintext">{{ formatDate(selectedAsset.createdAt) }}</div>
              </div>
              <div class="col-md-6 mb-3">
                <label class="form-label fw-bold">Cập nhật lần cuối</label>
                <div class="form-control-plaintext">{{ formatDate(selectedAsset.updatedAt) || 'Chưa có' }}</div>
              </div>
            </div>
          </div>
          <div class="modal-footer">
            <router-link :to="`/assets/${selectedAsset?.assetId}/edit`" class="btn btn-primary" @click="closeModal">
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
import { useAssetStore } from '../stores/asset';
import { toast } from 'vue3-toastify';
import { onMounted, ref, computed } from 'vue';
import { useRoute } from 'vue-router';
import type { Asset } from '../types';

const assetStore = useAssetStore();
const route = useRoute();
const currentPage = ref(1);
const pageSize = ref(10);
const totalCount = ref(0);
const sortField = ref<string>('');
const sortOrder = ref<'asc' | 'desc'>('asc');
const isInitialLoad = ref(true);
const selectedAsset = ref<Asset | null>(null);
const showModal = ref(false);

// Ghi nhớ trạng thái phân trang và sort
const STORAGE_KEY = 'assetList_state';

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
    assetStore.fetchAssetTypes(),
    assetStore.fetchLocations(),
    fetchAssets()
  ]);
  
  // Delay ngắn để tránh flash
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
  
  // Reset về trang 1 khi sort
  currentPage.value = 1;
  saveState();
  fetchAssets();
};

const getSortIcon = (field: string) => {
  if (sortField.value !== field) return 'bi-chevron-expand';
  return sortOrder.value === 'asc' ? 'bi-chevron-up' : 'bi-chevron-down';
};

const fetchAssets = async () => {
  const skipCount = (currentPage.value - 1) * pageSize.value;
  const response = await assetStore.fetchAssets(
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
  fetchAssets();
};

const goToPage = (page: number) => {
  if (page >= 1 && page <= totalPages.value) {
    currentPage.value = page;
    saveState();
    fetchAssets();
  }
};

const deleteAsset = async (id: number) => {
  if (confirm('Are you sure you want to delete this asset?')) {
    try {
      await assetStore.deleteAsset(id);
      toast.success('Asset deleted successfully');
      fetchAssets();
    } catch {
      toast.error('Failed to delete asset');
    }
  }
};

// Thêm hàm format date
const formatDate = (dateString?: string) => {
  if (!dateString) return '';
  return new Date(dateString).toLocaleDateString('vi-VN');
};

const viewAssetDetail = (asset: Asset) => {
  selectedAsset.value = asset;
  showModal.value = true;
  document.body.classList.add('modal-open');
};

const closeModal = () => {
  showModal.value = false;
  selectedAsset.value = null;
  document.body.classList.remove('modal-open');
};

const getStatusBadgeClass = (status: string) => {
  switch (status?.toLowerCase()) {
    case 'active':
      return 'badge bg-success';
    case 'inactive':
      return 'badge bg-danger';
    case 'under maintenance':
    case 'under_maintenance':
      return 'badge bg-warning';
    default:
      return 'badge bg-secondary';
  }
};
</script>

<style scoped>
/* Cải thiện skeleton animation */
.skeleton-item {
  height: 40px;
  background: linear-gradient(90deg, #f0f0f0 25%, #e0e0e0 50%, #f0f0f0 75%);
  background-size: 200px 100%;
  animation: skeleton-loading 1.2s ease-in-out infinite;
  border-radius: 4px;
  flex: 1;
}

.fade-in {
  animation: fadeIn 0.3s ease-in;
}

@keyframes fadeIn {
  from { opacity: 0; transform: translateY(10px); }
  to { opacity: 1; transform: translateY(0); }
}

/* Cải thiện sortable interaction */
.sortable {
  cursor: pointer;
  user-select: none;
  transition: background-color 0.2s ease;
}

.sortable:hover {
  background-color: #f8f9fa;
}

/* Smooth transitions cho tất cả elements */
* {
  transition: all 0.2s ease;
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
  max-width: 1140px;
  z-index: 1056;
}

.modal-xl {
  max-width: 1140px;
}
</style>
