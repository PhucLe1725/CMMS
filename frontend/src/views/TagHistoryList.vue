<template>
  <div v-cloak>
    <div class="d-flex justify-content-between align-items-center mb-4">
      <h1>Tag History - Real-time Data</h1>
      <div class="d-flex align-items-center gap-3">
        <div class="connection-status">
          <span class="badge" :class="isConnected ? 'bg-success' : 'bg-danger'">
            <i class="bi" :class="isConnected ? 'bi-wifi' : 'bi-wifi-off'"></i>
            {{ isConnected ? 'Connected' : 'Disconnected' }}
          </span>
        </div>
        <button @click="refreshData" class="btn btn-outline-primary" :disabled="tagHistoryStore.loading">
          <i class="bi bi-arrow-clockwise me-2"></i>Refresh
        </button>
      </div>
    </div>

    <!-- Control Panel -->
    <div class="card mb-4">
      <div class="card-header">
        <h5 class="mb-0">Control Panel</h5>
      </div>
      <div class="card-body">
        <div class="row">
          <div class="col-md-8">
            <form @submit.prevent="sendSingleData" class="row g-3">
              <div class="col-md-3">
                <label class="form-label">Tag Value 1</label>
                <input v-model.number="newData.tagValue1" type="number" step="0.01" class="form-control" required />
              </div>
              <div class="col-md-3">
                <label class="form-label">Tag Value 2</label>
                <input v-model.number="newData.tagValue2" type="number" step="0.01" class="form-control" required />
              </div>
              <div class="col-md-4">
                <label class="form-label">String Value</label>
                <input v-model="newData.stringValue" type="text" class="form-control" required />
              </div>
              <div class="col-md-2 d-flex align-items-end">
                <button type="submit" class="btn btn-primary w-100">
                  <i class="bi bi-send me-2"></i>Send
                </button>
              </div>
            </form>
          </div>
          <div class="col-md-4 d-flex align-items-end">
            <button @click="generateBatchData" class="btn btn-warning w-100">
              <i class="bi bi-database me-2"></i>Generate Batch Data
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- Data Display -->
    <div class="card">
      <div class="card-header d-flex justify-content-between align-items-center">
        <h5 class="mb-0">Real-time Data Stream</h5>
        <small class="text-muted">Last {{ tagHistoryStore.tagHistories.length }} records</small>
      </div>
      <div class="card-body">
        <!-- Loading state -->
        <div v-if="tagHistoryStore.loading && tagHistoryStore.tagHistories.length === 0" class="text-center py-4">
          <div class="spinner-border text-primary" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
          <p class="mt-2">Loading data...</p>
        </div>

        <!-- Error state -->
        <div v-else-if="tagHistoryStore.error" class="alert alert-danger">
          <i class="bi bi-exclamation-triangle me-2"></i>
          {{ tagHistoryStore.error }}
          <button @click="connectWebSocket" class="btn btn-sm btn-outline-danger ms-2">
            Retry Connection
          </button>
        </div>

        <!-- Data table -->
        <div v-else class="table-responsive" style="max-height: 600px; overflow-y: auto;">
          <table class="table table-sm table-hover">
            <thead class="table-dark sticky-top">
              <tr>
                <th>ID</th>
                <th>Tag Value 1</th>
                <th>Tag Value 2</th>
                <th>String Value</th>
                <th>Timestamp</th>
                <th>Age</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(item, index) in tagHistoryStore.tagHistories" 
                  :key="item.tagHistoryNdx" 
                  :class="{ 'table-success': index < newItemsCount }"
                  class="data-row">
                <td>{{ item.tagHistoryNdx }}</td>
                <td>
                  <span class="badge bg-primary">{{ formatNumber(item.tagValue1) }}</span>
                </td>
                <td>
                  <span class="badge bg-info">{{ formatNumber(item.tagValue2) }}</span>
                </td>
                <td>
                  <span class="text-monospace">{{ item.stringValue }}</span>
                </td>
                <td>
                  <small>{{ formatTimestamp(item.tStamp) }}</small>
                </td>
                <td>
                  <small class="text-muted">{{ getTimeAgo(item.tStamp) }}</small>
                </td>
              </tr>
            </tbody>
          </table>
          
          <div v-if="tagHistoryStore.tagHistories.length === 0" class="text-center py-4 text-muted">
            <i class="bi bi-inbox display-4"></i>
            <p class="mt-2">No data available. Send some data to see real-time updates.</p>
          </div>
        </div>
      </div>
    </div>

    <!-- Statistics -->
    <div class="row mt-4">
      <div class="col-md-3">
        <div class="card text-center">
          <div class="card-body">
            <h5 class="card-title">Total Records</h5>
            <h3 class="text-primary">{{ tagHistoryStore.tagHistories.length }}</h3>
          </div>
        </div>
      </div>
      <div class="col-md-3">
        <div class="card text-center">
          <div class="card-body">
            <h5 class="card-title">Avg Tag Value 1</h5>
            <h3 class="text-success">{{ averageTagValue1 }}</h3>
          </div>
        </div>
      </div>
      <div class="col-md-3">
        <div class="card text-center">
          <div class="card-body">
            <h5 class="card-title">Avg Tag Value 2</h5>
            <h3 class="text-info">{{ averageTagValue2 }}</h3>
          </div>
        </div>
      </div>
      <div class="col-md-3">
        <div class="card text-center">
          <div class="card-body">
            <h5 class="card-title">Connection</h5>
            <h3 :class="isConnected ? 'text-success' : 'text-danger'">
              {{ isConnected ? 'LIVE' : 'OFFLINE' }}
            </h3>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script lang="ts" setup>
import { useTagHistoryStore } from '../stores/tagHistory';
import { toast } from 'vue3-toastify';
import { onMounted, onUnmounted, ref, computed, watch } from 'vue';
import type { TagHistoryCreateRequest } from '../types';

const tagHistoryStore = useTagHistoryStore();

const newData = ref<TagHistoryCreateRequest>({
  tagValue1: 0,
  tagValue2: 0,
  stringValue: ''
});

const newItemsCount = ref(0);
const previousLength = ref(0);

const isConnected = computed(() => tagHistoryStore.isConnected);

const averageTagValue1 = computed(() => {
  const values = tagHistoryStore.tagHistories.map(item => item.tagValue1);
  const avg = values.length > 0 ? values.reduce((a, b) => a + b, 0) / values.length : 0;
  return avg.toFixed(2);
});

const averageTagValue2 = computed(() => {
  const values = tagHistoryStore.tagHistories.map(item => item.tagValue2);
  const avg = values.length > 0 ? values.reduce((a, b) => a + b, 0) / values.length : 0;
  return avg.toFixed(2);
});

// Watch for new items
watch(() => tagHistoryStore.tagHistories.length, (newLength) => {
  if (newLength > previousLength.value) {
    newItemsCount.value = newLength - previousLength.value;
    
    // Reset highlight after 3 seconds
    setTimeout(() => {
      newItemsCount.value = 0;
      previousLength.value = newLength;
    }, 3000);
  }
});

onMounted(async () => {
  // Connect to WebSocket
  tagHistoryStore.connectWebSocket();
  
  // Load initial data
  await refreshData();
});

onUnmounted(() => {
  tagHistoryStore.disconnectWebSocket();
});

const connectWebSocket = () => {
  tagHistoryStore.connectWebSocket();
};

const refreshData = async () => {
  try {
    await tagHistoryStore.fetchLatestData(50);
    previousLength.value = tagHistoryStore.tagHistories.length;
  } catch (error) {
    toast.error('Failed to refresh data');
  }
};

const sendSingleData = async () => {
  try {
    await tagHistoryStore.sendData(newData.value);
    toast.success('Data sent successfully');
    
    // Reset form
    newData.value = {
      tagValue1: Math.random() * 100,
      tagValue2: Math.random() * 50,
      stringValue: `Test_${Date.now()}`
    };
  } catch (error) {
    toast.error('Failed to send data');
  }
};

const generateBatchData = async () => {
  try {
    const batchData: TagHistoryCreateRequest[] = [];
    for (let i = 0; i < 5; i++) {
      batchData.push({
        tagValue1: Math.random() * 100,
        tagValue2: Math.random() * 50,
        stringValue: `Batch_${i}_${Date.now()}`
      });
    }
    
    await tagHistoryStore.sendBatchData(batchData);
    toast.success('Batch data sent successfully');
  } catch (error) {
    toast.error('Failed to send batch data');
  }
};

const formatNumber = (value: number) => {
  return value.toFixed(2);
};

const formatTimestamp = (timestamp: string) => {
  if (!timestamp) return 'N/A';
  
  try {
    // Handle LocalDateTime format from backend
    // Convert LocalDateTime format to ISO format if needed
    let dateStr = timestamp;
    
    // If timestamp doesn't have timezone info, treat it as local time
    if (!timestamp.includes('T')) {
      // Format: "2024-01-01 12:00:00" -> "2024-01-01T12:00:00"
      dateStr = timestamp.replace(' ', 'T');
    }
    
    // If no timezone info, add local timezone
    if (!dateStr.includes('Z') && !dateStr.includes('+') && !dateStr.includes('-', 10)) {
      // Assume backend sends local time, convert to ISO string
      const date = new Date(dateStr);
      if (isNaN(date.getTime())) {
        return 'Invalid Date';
      }
      return date.toLocaleString('vi-VN');
    }
    
    const date = new Date(dateStr);
    if (isNaN(date.getTime())) {
      return 'Invalid Date';
    }
    
    return date.toLocaleString('vi-VN');
  } catch (error) {
    console.error('Error formatting timestamp:', error, timestamp);
    return 'Invalid Date';
  }
};

const getTimeAgo = (timestamp: string) => {
  if (!timestamp) return 'N/A';
  
  try {
    // Handle LocalDateTime format from backend
    let dateStr = timestamp;
    
    // If timestamp doesn't have timezone info, treat it as local time
    if (!timestamp.includes('T')) {
      dateStr = timestamp.replace(' ', 'T');
    }
    
    const now = new Date();
    const time = new Date(dateStr);
    
    if (isNaN(time.getTime())) {
      return 'Invalid';
    }
    
    const diffInSeconds = Math.floor((now.getTime() - time.getTime()) / 1000);
    
    if (diffInSeconds < 60) return `${diffInSeconds}s ago`;
    if (diffInSeconds < 3600) return `${Math.floor(diffInSeconds / 60)}m ago`;
    if (diffInSeconds < 86400) return `${Math.floor(diffInSeconds / 3600)}h ago`;
    return `${Math.floor(diffInSeconds / 86400)}d ago`;
  } catch (error) {
    console.error('Error calculating time ago:', error, timestamp);
    return 'Invalid';
  }
};
</script>

<style scoped>
.connection-status .badge {
  font-size: 0.8em;
  padding: 0.5em 0.75em;
}

.data-row {
  transition: background-color 0.3s ease;
}

.table-success {
  animation: highlight 3s ease-out;
}

@keyframes highlight {
  0% { background-color: #d4edda; }
  100% { background-color: transparent; }
}

.text-monospace {
  font-family: 'Courier New', monospace;
  font-size: 0.9em;
}

.card {
  border: none;
  box-shadow: 0 2px 8px rgba(0,0,0,0.1);
}

.sticky-top {
  position: sticky;
  top: 0;
  z-index: 10;
}

.spinner-border {
  width: 3rem;
  height: 3rem;
}
</style>
