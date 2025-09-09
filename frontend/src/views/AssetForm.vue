<template>
  <div>
    <h1 class="mb-4">{{ isEdit ? 'Edit Asset' : 'Add Asset' }}</h1>
    <form @submit.prevent="submitForm">
      <div class="row">
        <div class="col-md-6 mb-3">
          <label class="form-label">Asset Name</label>
          <input v-model="form.assetName" class="form-control" required />
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Asset Type</label>
          <select v-model="form.assetTypeId" class="form-select" required>
            <option v-for="type in assetStore.assetTypes" :key="type.assetTypeId" :value="type.assetTypeId">
              {{ type.typeName }}
            </option>
          </select>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Serial Number</label>
          <input v-model="form.serialNumber" class="form-control" required />
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Location</label>
          <select v-model="form.locationId" class="form-select" required>
            <option v-for="location in assetStore.locations" :key="location.locationId" :value="location.locationId">
              {{ location.locationName }}
            </option>
          </select>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Status</label>
          <select v-model="form.status" class="form-select" required>
            <option value="ACTIVE">Active</option>
            <option value="INACTIVE">Inactive</option>
            <option value="UNDER_MAINTENANCE">Under Maintenance</option>
          </select>
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Purchase Date</label>
          <input v-model="form.purchaseDate" type="date" class="form-control" />
        </div>
        <div class="col-md-6 mb-3">
          <label class="form-label">Warranty Expiry</label>
          <input v-model="form.warrantyExpiry" type="date" class="form-control" />
        </div>
      </div>
      <button type="submit" class="btn btn-primary">Save</button>
      <router-link to="/assets" class="btn btn-secondary ms-2">Cancel</router-link>
    </form>
  </div>
</template>

<script lang="ts" setup>
import { useAssetStore } from '../stores/asset';
import { toast } from 'vue3-toastify';
import { ref, onMounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import type { Asset } from '../types';
import moment from 'moment';

const assetStore = useAssetStore();
const route = useRoute();
const router = useRouter();

const isEdit = ref(false);
const form = ref<Partial<Asset>>({
  assetName: '',
  assetTypeId: 0,
  serialNumber: '',
  locationId: 0,
  status: 'Active',
  purchaseDate: undefined,
  warrantyExpiry: undefined,
});

onMounted(async () => {
  await Promise.all([assetStore.fetchAssetTypes(), assetStore.fetchLocations()]);
  if (route.params.id) {
    isEdit.value = true;
    try {
      await assetStore.fetchAssetById(route.params.id);
      console.log(JSON.stringify(assetStore.asset?.assetName));
      form.value.assetName = assetStore.asset?.assetName;
      form.value.assetTypeId = assetStore.asset?.assetTypeId;
      form.value.serialNumber = assetStore.asset?.serialNumber;
      form.value.locationId = assetStore.asset?.locationId;
      form.value.status = assetStore.asset?.status;
      form.value.purchaseDate = moment(assetStore.asset?.purchaseDate).format('YYYY-MM-DD');
      form.value.warrantyExpiry = moment(assetStore.asset?.warrantyExpiry).format('YYYY-MM-DD');
      console.log("form value-------", JSON.stringify(form.value))
    } catch {
      toast.error('Failed to load asset');
    }
  }
});

const toOffsetDateTime = (dateStr?: string) => {
  if (!dateStr) return undefined;
  return dateStr.length === 10 ? `${dateStr}T00:00:00+07:00` : dateStr;
};

const submitForm = async () => {
  console.log('AssetForm submitForm called, isEdit:', isEdit.value);
  
  try {
    // Tạo copy của form để không ảnh hưởng đến form gốc
    const formData = { ...form.value };
    
    // Chuyển đổi ngày về dạng OffsetDateTime cho API
    formData.purchaseDate = toOffsetDateTime(formData.purchaseDate);
    formData.warrantyExpiry = toOffsetDateTime(formData.warrantyExpiry);

    if (isEdit.value) {
      console.log('Updating asset with id:', route.params.id);
      await assetStore.updateAsset(Number(route.params.id), formData);
      toast.success('Asset updated successfully');
    } else {
      console.log('Creating new asset');
      await assetStore.createAsset(formData as Omit<Asset, 'asset_id' | 'created_at' | 'updated_at'>);
      toast.success('Asset created successfully');
    }
    
    console.log('AssetForm - navigating back to /assets');
    router.push('/assets');
  } catch (error) {
    console.error('AssetForm submitForm error:', error);
    toast.error('Failed to save asset');
  }
};
</script>
