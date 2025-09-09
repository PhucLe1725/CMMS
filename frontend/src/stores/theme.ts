import { defineStore } from 'pinia';
import { ref } from 'vue';

export const useThemeStore = defineStore('theme', () => {
  const isDarkMode = ref(localStorage.getItem('theme') === 'dark');

  const toggleTheme = () => {
    isDarkMode.value = !isDarkMode.value;
    localStorage.setItem('data-theme', isDarkMode.value ? 'dark' : 'light');
    document.body.classList.toggle('dark-mode', isDarkMode.value);
  };
  return { isDarkMode, toggleTheme };
});