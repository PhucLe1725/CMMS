// src/types/index.ts
export interface Asset {
  assetId: number;
  assetName: string;
  assetTypeId: number;
  serialNumber: string;
  locationId: number;
  purchaseDate?: string;
  warrantyExpiry?: string;
  status: 'Active' | 'Inactive' | 'Under Maintenance';
  createdAt?: string;
  updatedAt?: string;
  assetType?: AssetType;
  location?: Location;
}

export interface AssetType {
  assetTypeId: number;
  typeName: string;
  description?: string;
}

export interface Location {
  locationId: number;
  tenantId: number;
  locationName: string;
  description?: string;
  parentLocationId?: number;
}

export interface WorkOrder {
  workOrderId: number;
  assetId: number;
  title: string;
  description?: string;
  status: 'Open' | 'In Progress' | 'Completed' | 'Canceled';
  priority: 'Low' | 'Medium' | 'High' | 'Urgent';
  assignedTo?: number;
  createdBy: number;
  createdAt?: string;
  scheduledDate?: string;
  completedAt?: string;
  estimatedHours?: number;
  actualHours?: number;
  tenantId: number;
  asset?: Asset;
}

export interface User {
  userId: number;
  tenantId: number;
  username: string;
  email: string;
  fullName: string;
  role: string;
  isActive?: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface Inventory {
  partId: number;
  tenantId: number;
  partName: string;
  partNumber: string;
  quantity: number;
  minQuantity: number;
  locationId: number;
  unitCost: number;
  supplierId: number;
  createdAt?: string;
  updatedAt?: string;
}

export interface Supplier {
  supplierId: number;
  supplierName: string;
  contactInfo: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface TagHistory {
  tagHistoryNdx: number;
  tagValue1: number;
  tagValue2: number;
  stringValue: string;
  tStamp: string; // LocalDateTime as ISO string
}

export interface TagHistoryCreateRequest {
  tagValue1: number;
  tagValue2: number;
  stringValue: string;
}

// Add interfaces for other models (Supplier, User, Report, etc.) as needed