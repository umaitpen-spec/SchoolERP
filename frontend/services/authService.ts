import apiClient from './apiClient';
import * as SecureStore from 'expo-secure-store';
import { STORAGE_KEYS } from '@/constants';
import { ApiResponse, AuthResponse, User } from '@/types';

export const authService = {
  async refreshToken(refreshToken: string): Promise<AuthResponse> {
    const { data } = await apiClient.post<ApiResponse<AuthResponse>>('/auth/refresh', null, {
      params: { refreshToken },
    });
    return data.data;
  },

  async getCurrentUser(): Promise<User | null> {
    const userJson = await SecureStore.getItemAsync(STORAGE_KEYS.USER);
    return userJson ? JSON.parse(userJson) : null;
  },

  async saveAuthData(auth: AuthResponse): Promise<void> {
    await SecureStore.setItemAsync(STORAGE_KEYS.ACCESS_TOKEN, auth.accessToken);
    await SecureStore.setItemAsync(STORAGE_KEYS.REFRESH_TOKEN, auth.refreshToken);
    if (auth.user) {
      await SecureStore.setItemAsync(STORAGE_KEYS.USER, JSON.stringify(auth.user));
    }
  },

  async logout(): Promise<void> {
    await SecureStore.deleteItemAsync(STORAGE_KEYS.ACCESS_TOKEN);
    await SecureStore.deleteItemAsync(STORAGE_KEYS.REFRESH_TOKEN);
    await SecureStore.deleteItemAsync(STORAGE_KEYS.USER);
  },

  async isLoggedIn(): Promise<boolean> {
    const token = await SecureStore.getItemAsync(STORAGE_KEYS.ACCESS_TOKEN);
    return !!token;
  },
};
