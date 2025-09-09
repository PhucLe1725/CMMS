import { defineStore } from "pinia";
import { ref, computed } from "vue";
import api from "../api";
import { useRouter } from "vue-router";

export const useAuthStore = defineStore(
  "auth",
  () => {
    const accessToken = ref<string | null>(null);
    const refreshToken = ref<string | null>(null);
    const role = ref<string | null>(null);
    const user = ref<{ tenantId: Number, userId: number; username:string, email: string; fullName: string; role: string } | null>(null);
    const router = useRouter();

    const isAuthenticated = computed(() => !!accessToken.value);
    const isAdmin = computed(() => role.value === "ADMIN");

    const addUser = async (username: string, password: string, email: string, fullName: string, isAdmin: string) => {
      try {
        const response = await api.post("/auth/add", { username, email, password, fullName, isAdmin });
        accessToken.value = response.data.accessToken;
        refreshToken.value = response.data.refreshToken;
        role.value = response.data.userResponse.role || "USER";
        user.value = response.data.userResponse;
        router.push("/home");
      } catch (error: any) {
        throw new Error(error.response?.data?.message || "Add new user failed");
      }
    };

    const login = async (username: string, password: string) => {
      try {
        const response = await api.post("/auth/login", { username, password });
        accessToken.value = response.data.accessToken;
        refreshToken.value = response.data.refreshToken;
        role.value = response.data.userResponse.role || "USER";
        user.value = response.data.userResponse;
        console.log(JSON.stringify(user.value));
        await router.push("/");
      } catch (error: any) {
        if (error.response?.status === 401) {
          throw new Error("Tên đăng nhập hoặc mật khẩu không chính xác.");
        }
        throw new Error(error.response?.data?.message || "Login failed");
      }
    };

    const refreshAccessToken = async () => {
      try {
        const response = await api.post("/auth/refresh", { refreshToken: refreshToken.value });
        accessToken.value = response.data.accessToken;
      } catch (error) {
        logout();
        throw error;
      }
    };

    const logout = async () => {
      try {
        await api.post("/auth/logout", { refreshToken: refreshToken.value });
      } catch (error) {
        console.error("Logout failed:", error);
      }
      accessToken.value = null;
      refreshToken.value = null;
      role.value = null;
      user.value = null;
      router.push("/login");
    };

    return { accessToken, refreshToken, role, user, isAuthenticated, isAdmin, addUser, login, refreshAccessToken, logout };
  },
  {
    persist: {
      storage: localStorage,
    },
  }
);