<!-- filepath: c:\Users\Admin\OneDriveHUST\Desktop\VSII\frontend\views\AssetTypeForm.vue -->
<template>
  <div>
    <h2>Quản lý Asset Types</h2>
    <input v-model="input" placeholder="Tên Asset Type" />
    <button v-if="editingId === null" @click="addType">Thêm</button>
    <button v-else @click="updateType">Cập nhật</button>
    <ul>
      <li v-for="type in assetTypes" :key="type.id">
        {{ type.name }}
        <button @click="editType(type)">Sửa</button>
        <button @click="deleteType(type.id)">Xóa</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const assetTypes = ref([]);
const input = ref('');
const editingId = ref(null);

function addType() {
  if (!input.value.trim()) return;
  assetTypes.value.push({
    id: Date.now(),
    name: input.value.trim()
  });
  input.value = '';
}

function editType(type) {
  input.value = type.name;
  editingId.value = type.id;
}

function updateType() {
  const idx = assetTypes.value.findIndex(t => t.id === editingId.value);
  if (idx !== -1) {
    assetTypes.value[idx].name = input.value.trim();
  }
  input.value = '';
  editingId.value = null;
}

function deleteType(id) {
  assetTypes.value = assetTypes.value.filter(t => t.id !== id);
}
</script>