import { defineStore } from 'pinia';
import { ref } from 'vue';
import api from "../api";
import type { Asset, AssetType, Location } from '../types';
import { useAuthStore } from './auth';

export const useAssetStore = defineStore('asset', () => {
  const assets = ref<Asset[]>([]);
  const asset = ref<Asset>();
  const assetTypes = ref<AssetType[]>([]);
  const locations = ref<Location[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);

  const fetchAssets = async (skipCount = 0, maxResultCount = 10, sortBy?: string, sortDirection?: string) => {
    loading.value = true;
    try {
      const authStore = useAuthStore();
      
      const tenantId = authStore.user?.tenantId;
      if (!tenantId) {
        throw new Error('No tenantId found');
      }
      
      const requestParams: any = { tenantId, skipCount, maxResultCount };
      if (sortBy) {
        requestParams.sortBy = sortBy;
        requestParams.sortDirection = sortDirection || 'asc';
      }
      
      const response = await api.get(`/assets/getAllByTenantId`, {
        params: requestParams
      });
      
      assets.value = response.data?.result?.data || [];

      const returnValue = {
        data: assets.value,
        totalCount: response.data?.result?.totalRecords || 0
      };

      return returnValue;
    } catch (err) {
      error.value = (err as Error).message;
      return { data: [], totalCount: 0 };
    } finally {
      loading.value = false;
    }
  };

  const fetchAssetById = async (id: any) => {
    loading.value = true;
    try {
      const response = await api.get(`/assets/${id}`);
      asset.value = response.data?.result;
    } catch (err) {
      error.value = (err as Error).message;
    } finally {
      loading.value = false;
    }
  };

  const fetchAssetTypes = async () => {
    try {
      const response = await api.get(`/asset-types/getAll`);
      assetTypes.value = response.data?.result?.data;
    } catch (err) {
      error.value = (err as Error).message;
    }
  };

  const fetchLocations = async () => {
    try {
      const authStore = useAuthStore();
      const tenantId = authStore.user?.tenantId;
      if (!tenantId) throw new Error('No tenantId found');
      const response = await api.get(`/locations/getAllByTenantId`, {
        params: { tenantId }
      });
      locations.value = response.data?.result?.data;
    } catch (err) {
      error.value = (err as Error).message;
    }
  };

  const createAsset = async (asset: Omit<Asset, 'asset_id' | 'created_at' | 'updated_at'>) => {
    try {
      const authStore = useAuthStore();
      const tenantId = authStore.user?.tenantId;
      if (!tenantId) throw new Error('No tenantId found');
      //console.log(JSON.stringify(asset));
      const response = await api.post(`/assets/create`, asset, {
        params: { tenantId }
      });
      assets.value.push(response.data);
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    }
  };

  const updateAsset = async (id: number, asset: Partial<Asset>) => {
    try {
      //console.log(JSON.stringify(asset));
      const response = await api.put(`/assets/update/${id}`, asset);
      const index = assets.value.findIndex((a) => a.assetId === id);
      if (index !== -1) assets.value[index] = response.data;
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    }
  };

  const deleteAsset = async (id: number) => {
    try {
      await api.delete(`/assets/delete/${id}`);
      assets.value = assets.value.filter((a) => a.assetId !== id);
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    }
  };

  function getAssetTypeName(assetTypeId: number) {
    const type = assetTypes.value.find(t => t.assetTypeId === assetTypeId);
    return type ? type.typeName : '';
  }

  function getLocationName(locationId: number) {
    const location = locations.value.find(l => l.locationId === locationId);
    return location ? location.locationName : '';
  }

  return { 
    asset, assets, assetTypes, locations, loading, error, 
    fetchAssets, fetchAssetById, fetchAssetTypes, fetchLocations, 
    createAsset, updateAsset, deleteAsset,
    getAssetTypeName, getLocationName
  };
});