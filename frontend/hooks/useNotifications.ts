import { useAuthStore } from '@/store/authStore';
import { notificationService } from '@/services/notificationService';
import { useQuery } from '@tanstack/react-query';

export function useNotifications() {
  const { isAuthenticated } = useAuthStore();

  const { data: unreadCount = 0 } = useQuery({
    queryKey: ['unread-notifications'],
    queryFn: notificationService.getUnreadCount,
    enabled: isAuthenticated,
    refetchInterval: 30000, // poll every 30s
  });

  return { unreadCount };
}
