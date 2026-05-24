import apiClient from './apiClient';
import { STORAGE_KEYS } from '@/constants';
import { ApiResponse, AuthResponse, User } from '@/types';
import { storage } from './storage';

export const authService = {
  async refreshToken(refreshToken: string): Promise<AuthResponse> {
    const { data } = await apiClient.post<ApiResponse<AuthResponse>>('/auth/refresh', null, {
      params: { refreshToken },
    });
    return data.data;
  },

  async getCurrentUser(): Promise<User | null> {
    const userJson = await storage.getItem(STORAGE_KEYS.USER);
    return userJson ? JSON.parse(userJson) : null;
  },

  async saveAuthData(auth: AuthResponse): Promise<void> {
    await storage.setItem(STORAGE_KEYS.ACCESS_TOKEN, auth.accessToken);
    await storage.setItem(STORAGE_KEYS.REFRESH_TOKEN, auth.refreshToken);
    if (auth.user) {
      await storage.setItem(STORAGE_KEYS.USER, JSON.stringify(auth.user));
    }
  },

  async logout(): Promise<void> {
    await storage.deleteItem(STORAGE_KEYS.ACCESS_TOKEN);
    await storage.deleteItem(STORAGE_KEYS.REFRESH_TOKEN);
    await storage.deleteItem(STORAGE_KEYS.USER);
  },

  async isLoggedIn(): Promise<boolean> {
    const token = await storage.getItem(STORAGE_KEYS.ACCESS_TOKEN);
    return !!token;
  },
};
