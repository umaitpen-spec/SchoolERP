import { Platform } from 'react-native';
import * as SecureStore from 'expo-secure-store';

const canUseLocalStorage = () =>
  Platform.OS === 'web' && typeof window !== 'undefined' && !!window.localStorage;

export const storage = {
  async getItem(key: string): Promise<string | null> {
    if (canUseLocalStorage()) {
      return window.localStorage.getItem(key);
    }

    if (Platform.OS === 'web') {
      return null;
    }

    return SecureStore.getItemAsync(key);
  },

  async setItem(key: string, value: string): Promise<void> {
    if (canUseLocalStorage()) {
      window.localStorage.setItem(key, value);
      return;
    }

    if (Platform.OS === 'web') {
      return;
    }

    await SecureStore.setItemAsync(key, value);
  },

  async deleteItem(key: string): Promise<void> {
    if (canUseLocalStorage()) {
      window.localStorage.removeItem(key);
      return;
    }

    if (Platform.OS === 'web') {
      return;
    }

    await SecureStore.deleteItemAsync(key);
  },
};
