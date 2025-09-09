import { defineStore } from 'pinia';
import { ref, onUnmounted } from 'vue';
import api from "../api";
import type { TagHistory, TagHistoryCreateRequest } from '../types';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';

export const useTagHistoryStore = defineStore('tagHistory', () => {
  const tagHistories = ref<TagHistory[]>([]);
  const loading = ref(false);
  const error = ref<string | null>(null);
  const isConnected = ref(false);
  
  let stompClient: Client | null = null;
  let reconnectAttempts = 0;
  const maxReconnectAttempts = 5;

  // WebSocket connection
  const connectWebSocket = () => {
    try {
      // Check if API URL is defined
      const apiUrl = import.meta.env.VITE_API_URL;
      if (!apiUrl) {
        console.error('VITE_API_URL is not defined');
        error.value = 'WebSocket URL not configured';
        return;
      }

      // Extract base URL (remove /api suffix if present)
      const baseUrl = apiUrl.replace('/api', '');
      const wsUrl = `${baseUrl}/ws`;
      console.log('Connecting to WebSocket:', wsUrl);
      
      const socket = new SockJS(wsUrl);
      stompClient = new Client({
        webSocketFactory: () => socket,
        debug: (str) => console.log('STOMP:', str),
        onConnect: onConnected,
        onDisconnect: onDisconnected,
        onStompError: onError,
        onWebSocketError: (event) => {
          console.error('WebSocket error:', event);
          error.value = `WebSocket connection failed: ${event.type}`;
        },
        onWebSocketClose: (event) => {
          console.log('WebSocket closed:', event.code, event.reason);
          isConnected.value = false;
        },
        reconnectDelay: 5000,
        heartbeatIncoming: 4000,
        heartbeatOutgoing: 4000,
        connectionTimeout: 10000, // 10 seconds timeout
      });
      
      stompClient.activate();
    } catch (err) {
      console.error('Failed to connect WebSocket:', err);
      error.value = `Failed to connect to WebSocket server: ${err}`;
      isConnected.value = false;
    }
  };

  const onConnected = () => {
    console.log('Connected to WebSocket');
    isConnected.value = true;
    reconnectAttempts = 0;
    error.value = null;

    if (!stompClient) return;

    // Subscribe to real-time data
    stompClient.subscribe('/topic/tag-data', (message) => {
      try {
        const data = JSON.parse(message.body);
        console.log('Received real-time data:', data);
        
        // Add new data to the beginning of array
        tagHistories.value.unshift(data);
        
        // Keep only last 100 records for performance
        if (tagHistories.value.length > 100) {
          tagHistories.value = tagHistories.value.slice(0, 100);
        }
      } catch (err) {
        console.error('Error parsing real-time data:', err);
      }
    });

    // Subscribe to batch data
    stompClient.subscribe('/topic/tag-data-batch', (message) => {
      try {
        const batchData = JSON.parse(message.body);
        console.log('Received batch data:', batchData);
        
        // Add batch data
        if (Array.isArray(batchData)) {
          tagHistories.value.unshift(...batchData);
        } else {
          tagHistories.value.unshift(batchData);
        }
        
        // Keep only last 100 records
        if (tagHistories.value.length > 100) {
          tagHistories.value = tagHistories.value.slice(0, 100);
        }
      } catch (err) {
        console.error('Error parsing batch data:', err);
      }
    });

    // Subscribe to heartbeat
    stompClient.subscribe('/topic/heartbeat', (message) => {
      console.log('Heartbeat:', message.body);
    });
  };

  const onDisconnected = () => {
    console.log('Disconnected from WebSocket');
    isConnected.value = false;
    
    // Auto-reconnect
    if (reconnectAttempts < maxReconnectAttempts) {
      reconnectAttempts++;
      console.log(`Reconnecting... Attempt ${reconnectAttempts}`);
      setTimeout(connectWebSocket, 3000 * reconnectAttempts);
    } else {
      error.value = 'WebSocket connection lost. Please refresh the page.';
    }
  };

  const onError = (error: any) => {
    console.error('STOMP error:', error);
    console.error('Error details:', error.headers, error.body);
    isConnected.value = false;
    
    // Handle specific errors
    if (error.headers && error.headers.message) {
      error.value = `WebSocket error: ${error.headers.message}`;
    } else {
      error.value = 'WebSocket connection error. Please check server status.';
    }
    
    // Try to reconnect after error
    if (reconnectAttempts < maxReconnectAttempts) {
      reconnectAttempts++;
      console.log(`Reconnecting after error... Attempt ${reconnectAttempts}`);
      setTimeout(connectWebSocket, 3000 * reconnectAttempts);
    }
  };

  const disconnectWebSocket = () => {
    if (stompClient) {
      stompClient.deactivate();
      stompClient = null;
    }
    isConnected.value = false;
  };

  // API calls
  const fetchLatestData = async (limit = 10) => {
    loading.value = true;
    try {
      const response = await api.get(`/tag-history/latest`, {
        params: { limit }
      });
      
      const data = response.data?.result || response.data || [];
      tagHistories.value = Array.isArray(data) ? data : [];
      
      return tagHistories.value;
    } catch (err) {
      error.value = (err as Error).message;
      return [];
    } finally {
      loading.value = false;
    }
  };

  const fetchAllData = async () => {
    loading.value = true;
    try {
      const response = await api.get(`/tag-history/all`);
      
      const data = response.data?.result || response.data || [];
      tagHistories.value = Array.isArray(data) ? data : [];
      
      return tagHistories.value;
    } catch (err) {
      error.value = (err as Error).message;
      return [];
    } finally {
      loading.value = false;
    }
  };

  const sendData = async (data: TagHistoryCreateRequest) => {
    try {
      const response = await api.post('/tag-history/send-data', data);
      return response.data;
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    }
  };

  const sendBatchData = async (dataList: TagHistoryCreateRequest[]) => {
    try {
      const response = await api.post('/tag-history/send-batch', dataList);
      return response.data;
    } catch (err) {
      error.value = (err as Error).message;
      throw err;
    }
  };

  // Cleanup on unmount
  onUnmounted(() => {
    disconnectWebSocket();
  });

  return {
    tagHistories,
    loading,
    error,
    isConnected,
    connectWebSocket,
    disconnectWebSocket,
    fetchLatestData,
    fetchAllData,
    sendData,
    sendBatchData
  };
});
