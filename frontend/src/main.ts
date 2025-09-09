import { createApp } from 'vue';
import { createPinia } from 'pinia';
import piniaPluginPersistedstate from "pinia-plugin-persistedstate";
import App from './App.vue';
import router from './router';
import 'bootstrap/dist/css/bootstrap.min.css';
import 'bootstrap-icons/font/bootstrap-icons.css';
import 'bootstrap';
import 'vue3-toastify/dist/index.css';
import Vue3Toastify from 'vue3-toastify';
import './assets/styles.css';

const app = createApp(App);
const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);
app.use(pinia);
app.use(router);
app.use(Vue3Toastify, { autoClose: 3000 });

app.mount('#app');