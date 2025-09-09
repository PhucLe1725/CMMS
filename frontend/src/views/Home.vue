<template>
  <div>
    <h1 class="mb-4">Dashboard</h1>
    <div class="row">
      <div class="col-md-4">
        <div class="card">
          <div class="card-body">
            <router-link class="nav-link" to="/locations">
              <h5 class="card-title"><i class="bi bi-globe-asia-australia me-2"></i>Vị trí</h5>
              <p class="card-text">{{ assetStore.locations.length }}</p>
            </router-link>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card">
          <div class="card-body">
            <router-link class="nav-link" to="/suppliers">
              <h5 class="card-title"><i class="bi bi-shop me-2"></i>Nhà phân phối</h5>
              <p class="card-text">{{ totalSupplierCount }}</p>
            </router-link>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card">
          <div class="card-body">
            <router-link class="nav-link" to="/assets">
              <h5 class="card-title"><i class="bi bi-box-seam me-2"></i>Máy móc</h5>
              <p class="card-text">{{ totalAssetCount }}</p>
            </router-link>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card">
          <div class="card-body">
            <router-link class="nav-link" to="/workrequests">
              <h5 class="card-title"><i class="bi bi-tools me-2"></i>Bảo dưỡng</h5>
              <p class="card-text">{{ totalWorkOrderCount }}</p>
            </router-link>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card">
          <div class="card-body">
            <router-link class="nav-link" to="/inventories">
              <h5 class="card-title"><i class="bi bi-gear me-2"></i>Phụ tùng</h5>
              <p class="card-text">{{ totalInventoryCount }}</p>
            </router-link>
          </div>
        </div>
      </div>
      <div class="col-md-4">
        <div class="card">
          <div class="card-body">
            <router-link class="nav-link" to="/reports">
              <h5 class="card-title"><i class="bi bi-file-earmark-text me-2"></i>Báo cáo</h5>
              <p class="card-text">0</p>
            </router-link>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useAssetStore } from '../stores/asset';
import { useInventoryStore } from '../stores/inventory';
import { useWorkOrderStore } from '../stores/workOrder';
import { useSupplierStore } from '../stores/supplier';
import { onMounted, ref } from 'vue';

const assetStore = useAssetStore();
const inventoryStore = useInventoryStore();
const workOrderStore = useWorkOrderStore();
const supplierStore = useSupplierStore();
const totalAssetCount = ref(0);
const totalInventoryCount = ref(0);
const totalWorkOrderCount = ref(0);
const totalSupplierCount = ref(0);

onMounted(async () => {
  try {
    // Fetch locations để hiển thị số lượng locations
    await assetStore.fetchLocations();
    
    // Fetch assets để lấy totalCount
    const assetResult = await assetStore.fetchAssets(0, 1);
    totalAssetCount.value = assetResult.totalCount;
    
    // Fetch inventories để lấy totalCount
    const inventoryResult = await inventoryStore.fetchInventories(0, 1);
    totalInventoryCount.value = inventoryResult.totalCount;
    
    // Fetch work orders để lấy totalCount
    const workOrderResult = await workOrderStore.fetchWorkOrders(0, 1);
    totalWorkOrderCount.value = workOrderResult.totalCount;
    
    // Fetch suppliers để lấy totalCount
    const supplierResult = await supplierStore.fetchSuppliers(0, 1);
    totalSupplierCount.value = supplierResult.totalCount;
  } catch (error) {
    console.error('Lỗi khi fetch dữ liệu:', error);
  }
});
</script>
