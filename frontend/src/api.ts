import axios, { type AxiosInstance, AxiosError } from "axios";
import { useAuthStore } from "./stores/auth";

const api: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL,
});

api.interceptors.request.use((config) => {
  const authStore = useAuthStore();
  if (authStore.accessToken) {
    config.headers.Authorization = `Bearer ${authStore.accessToken}`;
  }
  return config;
});

api.interceptors.response.use(
  (response) => {
    return response;
  },
  async (error: AxiosError) => {
    const originalRequest = error.config as any;
    if (error.response?.status === 401
        && originalRequest.url !== '/auth/login'
        && !originalRequest._retry
    ) {
      originalRequest._retry = true;
      const authStore = useAuthStore();
      try {
        await authStore.refreshAccessToken();
        originalRequest.headers.Authorization = `Bearer ${authStore.accessToken}`;
        return api(originalRequest);
      } catch (refreshError) {
        authStore.logout();
        return Promise.reject(refreshError);
      }
    }
    return Promise.reject(error);
  }
);

export default api;