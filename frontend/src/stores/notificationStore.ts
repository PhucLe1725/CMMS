// src/stores/notificationStore.ts
import { defineStore } from 'pinia';
import { ref, computed } from 'vue';

export interface Notification {
  id: number;
  message: string;
  read: boolean;
  created_at: string;
  type?: 'success' | 'error' | 'warning' | 'info';
}

export const useNotificationStore = defineStore('notification', () => {
  const notifications = ref<Notification[]>([
    // Mock data
    { id: 1, message: 'New work order assigned: Pump A maintenance', read: false, created_at: '2025-08-12T09:00:00Z' },
    { id: 2, message: 'Asset warranty expiring: Machine B', read: false, created_at: '2025-08-12T08:30:00Z' },
    { id: 3, message: 'Maintenance completed: Pump A', read: true, created_at: '2025-08-11T15:00:00Z' },
  ]);

  const unreadCount = computed(() => notifications.value.filter(n => !n.read).length);

  const fetchNotifications = async () => {
    // Replace with API call to backend, e.g., GET /api/notifications
    try {
      // const response = await axios.get(`${import.meta.env.VITE_API_URL}/notifications`);
      // notifications.value = response.data;
    } catch (error) {
      console.error('Failed to fetch notifications:', error);
    }
  };

  const markAsRead = (id: number) => {
    const notification = notifications.value.find(n => n.id === id);
    if (notification) {
      notification.read = true;
    }
  };

  const markAllAsRead = () => {
    notifications.value.forEach(n => (n.read = true));
  };

  const addNotification = (notification: { type: 'success' | 'error' | 'warning' | 'info'; message: string }) => {
    const newNotification: Notification = {
      id: Date.now(),
      message: notification.message,
      type: notification.type,
      read: false,
      created_at: new Date().toISOString()
    };
    notifications.value.unshift(newNotification);
  };

  return { notifications, unreadCount, fetchNotifications, markAsRead, markAllAsRead, addNotification };
});
