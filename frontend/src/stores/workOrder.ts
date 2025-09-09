import { defineStore } from 'pinia';
import { ref } from 'vue';
import api from "../api";
import type { WorkOrder, Asset, User } from '../types';
import { useAuthStore } from './auth';

export const useWorkOrderStore = defineStore('workOrder', () => {
  const workOrders = ref<WorkOrder[]>([]);
  const workOrder = ref<WorkOrder>();
  const assets = ref<Asset[]>([]);
  const users = ref<User[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);

  const fetchWorkOrders = async (skipCount = 0, maxResultCount = 10, sortBy?: string, sortDirection?: string) => {
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
      
      const response = await api.get(`/work-orders/getAllByTenant`, {
        params: requestParams
      });
      
      workOrders.value = response.data?.result?.data || [];

      const returnValue = {
        data: workOrders.value,
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

  const fetchWorkOrderById = async (id: any) => {
    loading.value = true;
    try {
      const response = await api.get(`/work-orders/${id}`);
      workOrder.value = response.data?.result;
    } catch (err) {
      error.value = (err as Error).message;
    } finally {
      loading.value = false;
    }
  };

  const fetchAssets = async () => {
    try {
      const authStore = useAuthStore();
      const tenantId = authStore.user?.tenantId;
      if (!tenantId) throw new Error('No tenantId found');
      const response = await api.get(`/assets/getAllByTenantId`, {
        params: { tenantId, skipCount: 0, maxResultCount: 1000 }
      });
      assets.value = response.data?.result?.data || [];
    } catch (err) {
      error.value = (err as Error).message;
    }
  };

  const fetchUsers = async () => {
    try {
      const response = await api.get(`/users/getAll`);
      users.value = response.data?.result?.data || [];
    } catch (err) {
      error.value = (err as Error).message;
    }
  };

  const createWorkOrder = async (workOrder: Omit<WorkOrder, 'workOrderId' | 'createdAt' | 'completedAt'>) => {
    try {
      const response = await api.post(`/work-orders/create`, workOrder);
      workOrders.value.push(response.data);
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    }
  };

  const updateWorkOrder = async (id: number, workOrder: Partial<WorkOrder>) => {
    try {
      const response = await api.put(`/work-orders/update/${id}`, workOrder);
      const index = workOrders.value.findIndex((w) => w.workOrderId === id);
      if (index !== -1) workOrders.value[index] = response.data;
    } catch (err: any) {
      error.value = err.message;
      throw err;
    }
  };

  const deleteWorkOrder = async (id: number) => {
    try {
      await api.delete(`/work-orders/delete/${id}`);
      workOrders.value = workOrders.value.filter((w) => w.workOrderId !== id);
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    }
  };

  function getAssetName(assetId: number) {
    const asset = assets.value.find(a => a.assetId === assetId);
    return asset ? asset.assetName : `Asset #${assetId}`;
  }

  function getUserName(userId?: number) {
    if (!userId) return '';
    const user = users.value.find(u => u.userId === userId);
    return user ? user.fullName : `User #${userId}`;
  }

  return { 
    workOrder, workOrders, assets, users, loading, error, 
    fetchWorkOrders, fetchWorkOrderById, fetchAssets, fetchUsers,
    createWorkOrder, updateWorkOrder, deleteWorkOrder,
    getAssetName, getUserName
  };
});
