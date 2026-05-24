import apiClient from './apiClient';
import { ApiResponse, PageResponse, Notification } from '@/types';

export const notificationService = {
  async getMyNotifications(page = 0): Promise<PageResponse<Notification>> {
    const { data } = await apiClient.get<ApiResponse<PageResponse<Notification>>>('/notifications', {
      params: { page, size: 20 },
    });
    return data.data;
  },

  async getUnreadCount(): Promise<number> {
    const { data } = await apiClient.get<ApiResponse<{ unreadCount: number }>>('/notifications/unread-count');
    return data.data.unreadCount;
  },

  async markAsRead(id: number): Promise<void> {
    await apiClient.patch(`/notifications/${id}/read`);
  },
};
