<!-- filepath: c:\Users\Admin\OneDriveHUST\Desktop\VSII\frontend\views\LocationTypeForm.vue -->
<template>
  <div>
    <h2>Quản lý Location Types</h2>
    <input v-model="input" placeholder="Tên Location Type" />
    <button v-if="editingId === null" @click="addType">Thêm</button>
    <button v-else @click="updateType">Cập nhật</button>
    <ul>
      <li v-for="type in locationTypes" :key="type.id">
        {{ type.name }}
        <button @click="editType(type)">Sửa</button>
        <button @click="deleteType(type.id)">Xóa</button>
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref } from 'vue';

const locationTypes = ref([]);
const input = ref('');
const editingId = ref(null);

function addType() {
  if (!input.value.trim()) return;
  locationTypes.value.push({
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
  const idx = locationTypes.value.findIndex(t => t.id === editingId.value);
  if (idx !== -1) {
    locationTypes.value[idx].name = input.value.trim();
  }
  input.value = '';
  editingId.value = null;
}

function deleteType(id) {
  locationTypes.value = locationTypes.value.filter(t => t.id !== id);
}
</script>
