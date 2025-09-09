import { createRouter, createWebHistory } from 'vue-router';
import Home from '../views/Home.vue';
import Login from '../views/Login.vue';
import AssetList from '../views/AssetList.vue';
import AssetForm from '../views/AssetForm.vue';
import UserProfile from '../views/UserProfile.vue';
import LocationForm from '../views/LocationForm.vue';
import AssetTypeForm from '../views/AssetTypeForm.vue';
import { useAuthStore } from '../stores/auth';
import UserList from '../views/UserList.vue';
import UserForm from '../views/UserForm.vue';
import WorkRequestList from '../views/WorkRequestList.vue';
import WorkRequestForm from '../views/WorkRequestForm.vue';
import InventoryList from '../views/InventoryList.vue';
import InventoryForm from '../views/InventoryForm.vue';
import SupplierList from '../views/SupplierList.vue';
import SupplierForm from '../views/SupplierForm.vue';
import TagHistoryList from '../views/TagHistoryList.vue';

const routes = [
  { path: '/', component: Home },
  { path: '/home', component: Home },
  { path: '/login', component: Login },
  { path: '/assets', component: AssetList, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'TECHNICIAN', 'MANAGER', 'VIEWER'] } },
  { path: '/assets/new', component: AssetForm, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'MANAGER'] } },
  { path: '/assets/:id/edit', component: AssetForm, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'MANAGER'] } },
  { path: '/profile', component: UserProfile },
  { path: '/location', component: LocationForm, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'MANAGER'] } },
  { path: '/asset-types', component: AssetTypeForm, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'MANAGER'] } },
  {
    path: '/users',
    name: 'UserList',
    component: UserList,
    meta: { requiresAuth: true }
  },
  {
    path: '/users/new',
    name: 'UserCreate',
    component: UserForm,
    meta: { requiresAuth: true }
  },
  {
    path: '/users/:id/edit',
    name: 'UserEdit',
    component: UserForm,
    meta: { requiresAuth: true }
  },
  { path: '/workrequests', component: WorkRequestList, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'TECHNICIAN', 'MANAGER', 'VIEWER'] } },
  { path: '/workrequests/new', component: WorkRequestForm, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'MANAGER'] } },
  { path: '/workrequests/:id/edit', component: WorkRequestForm, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'MANAGER'] } },
  { path: '/inventories', component: InventoryList, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'TECHNICIAN', 'MANAGER', 'VIEWER'] } },
  { path: '/inventories/new', component: InventoryForm, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'MANAGER'] } },
  { path: '/inventories/:id/edit', component: InventoryForm, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'MANAGER'] } },
  { path: '/suppliers', component: SupplierList, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'MANAGER', 'VIEWER'] } },
  { path: '/suppliers/new', component: SupplierForm, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'MANAGER'] } },
  { path: '/suppliers/:id/edit', component: SupplierForm, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'MANAGER'] } },
  { path: '/tag-history', component: TagHistoryList, meta: { requiresAuth: true, allowedRoles: ['ADMIN', 'TECHNICIAN', 'MANAGER', 'VIEWER'] } },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
  scrollBehavior(to, _from, savedPosition) {
    
    if (savedPosition) {
      return savedPosition;
    }
    
    if (to.hash) {
      return { el: to.hash };
    }

    return { top: 0 };
  }
});

router.beforeEach((to, from, next) => {

  document.body.classList.add('page-loading');
  
  const authStore = useAuthStore();
  console.log(`Navigating from ${from.path} to ${to.path}, isAuthenticated: ${authStore.isAuthenticated}, role: ${authStore.role}`);
  if (to.meta.requiresAuth && !authStore.isAuthenticated) {
    console.log('Redirecting to /login due to unauthenticated access');
    return next('/login');
  }
  if (to.meta.role && authStore.role !== to.meta.role) {
    console.log(`Redirecting to /login due to role mismatch, expected: ${to.meta.role}, actual: ${authStore.role}`);
    return next('/login');
  }
  next();
});

router.afterEach(() => {
  // Xóa loading class sau khi navigate xong
  setTimeout(() => {
    document.body.classList.remove('page-loading');
  }, 100);
});

export default router;