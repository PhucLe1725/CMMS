import { defineStore } from 'pinia';
import { ref } from 'vue';
import api from "../api";
import type { Supplier } from '../types';

export const useSupplierStore = defineStore('supplier', () => {
  const suppliers = ref<Supplier[]>([]);
  const supplier = ref<Supplier>();
  const loading = ref(false);
  const error = ref<string | null>(null);

  const fetchSuppliers = async (skipCount = 0, maxResultCount = 10, sortBy?: string, sortDirection?: string) => {
    loading.value = true;
    try {
      const requestParams: any = { skipCount, maxResultCount };
      if (sortBy) {
        requestParams.sortBy = sortBy;
        requestParams.sortDirection = sortDirection || 'asc';
      }
      
      const response = await api.get(`/suppliers/getAll`, {
        params: requestParams
      });
      
      suppliers.value = response.data?.result?.data || [];

      const returnValue = {
        data: suppliers.value,
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

  const fetchSupplierById = async (id: any) => {
    loading.value = true;
    try {
      const response = await api.get(`/suppliers/${id}`);
      supplier.value = response.data?.result;
    } catch (err) {
      error.value = (err as Error).message;
    } finally {
      loading.value = false;
    }
  };

  const createSupplier = async (supplier: Omit<Supplier, 'supplierId' | 'createdAt' | 'updatedAt'>) => {
    try {
      const response = await api.post(`/suppliers/create`, supplier);
      suppliers.value.push(response.data);
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    }
  };

  const updateSupplier = async (id: number, supplier: Partial<Supplier>) => {
    try {
      const response = await api.put(`/suppliers/update/${id}`, supplier);
      const index = suppliers.value.findIndex((s) => s.supplierId === id);
      if (index !== -1) suppliers.value[index] = response.data;
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    }
  };

  const deleteSupplier = async (id: number) => {
    try {
      await api.delete(`/suppliers/delete/${id}`);
      suppliers.value = suppliers.value.filter((s) => s.supplierId !== id);
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    }
  };

  return { 
    supplier, suppliers, loading, error, 
    fetchSuppliers, fetchSupplierById,
    createSupplier, updateSupplier, deleteSupplier
  };
});
