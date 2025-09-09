import { defineStore } from 'pinia';
import { ref } from 'vue';
import api from "../api";
import type { Inventory, Location } from '../types';
import { useAuthStore } from './auth';

export const useInventoryStore = defineStore('inventory', () => {
  const inventories = ref<Inventory[]>([]);
  const inventory = ref<Inventory>();
  const locations = ref<Location[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);

  const fetchInventories = async (skipCount = 0, maxResultCount = 10, sortBy?: string, sortDirection?: string) => {
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
      
      const response = await api.get(`/inventories/getAllByTenantId`, {
        params: requestParams
      });
      
      inventories.value = response.data?.result?.data || [];

      const returnValue = {
        data: inventories.value,
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

  const fetchInventoryById = async (id: any) => {
    loading.value = true;
    try {
      const response = await api.get(`/inventories/${id}`);
      inventory.value = response.data?.result;
    } catch (err) {
      error.value = (err as Error).message;
    } finally {
      loading.value = false;
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
      locations.value = response.data?.result?.data || [];
    } catch (err) {
      error.value = (err as Error).message;
    }
  };

  const createInventory = async (inventory: Omit<Inventory, 'partId' | 'createdAt' | 'updatedAt'>) => {
    try {
      const authStore = useAuthStore();
      const tenantId = authStore.user?.tenantId;
      if (!tenantId) throw new Error('No tenantId found');
      
      const response = await api.post(`/inventories/create`, inventory, {
        params: { tenantId }
      });
      inventories.value.push(response.data);
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    }
  };

  const updateInventory = async (id: number, inventory: Partial<Inventory>) => {
    try {
      const response = await api.put(`/inventories/update/${id}`, inventory);
      const index = inventories.value.findIndex((i) => i.partId === id);
      if (index !== -1) inventories.value[index] = response.data;
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    }
  };

  const deleteInventory = async (id: number) => {
    try {
      await api.delete(`/inventories/delete/${id}`);
      inventories.value = inventories.value.filter((i) => i.partId !== id);
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    }
  };

  const fetchLowStock = async (skipCount = 0, maxResultCount = 10, sortBy?: string, sortDirection?: string) => {
    try {
      const authStore = useAuthStore();
      const tenantId = authStore.user?.tenantId;
      if (!tenantId) throw new Error('No tenantId found');
      
      const requestParams: any = { tenantId, skipCount, maxResultCount };
      if (sortBy) {
        requestParams.sortBy = sortBy;
        requestParams.sortDirection = sortDirection || 'asc';
      }
      
      const response = await api.get(`/inventories/low-stock`, {
        params: requestParams
      });
      
      return {
        data: response.data?.result?.data || [],
        totalCount: response.data?.result?.totalRecords || 0
      };
    } catch (err) {
      error.value = (err as Error).message;
      return { data: [], totalCount: 0 };
    }
  };

  function getLocationName(locationId: number) {
    const location = locations.value.find(l => l.locationId === locationId);
    return location ? location.locationName : `Location #${locationId}`;
  }

  function getStockStatus(quantity: number, minQuantity: number) {
    if (quantity <= 0) return 'OUT_OF_STOCK';
    if (quantity <= minQuantity) return 'LOW_STOCK';
    return 'IN_STOCK';
  }

  return { 
    inventory, inventories, locations, loading, error, 
    fetchInventories, fetchInventoryById, fetchLocations, fetchLowStock,
    createInventory, updateInventory, deleteInventory,
    getLocationName, getStockStatus
  };
});
