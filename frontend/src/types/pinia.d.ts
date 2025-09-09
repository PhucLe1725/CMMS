// src/types/pinia.d.ts
import { PersistOptions } from 'pinia-plugin-persistedstate';

declare module 'pinia' {
  export interface DefineStoreOptionsBase<S, Store> {
    persist?: PersistOptions<S>;
  }
}