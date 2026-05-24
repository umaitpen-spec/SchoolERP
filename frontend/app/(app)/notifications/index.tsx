import React from 'react';
import { View, Text, FlatList, TouchableOpacity } from 'react-native';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { notificationService } from '@/services/notificationService';
import type { Notification } from '@/types';

export default function NotificationsScreen() {
  const queryClient = useQueryClient();

  const { data } = useQuery({
    queryKey: ['notifications'],
    queryFn: () => notificationService.getMyNotifications(),
  });

  const markRead = useMutation({
    mutationFn: (id: number) => notificationService.markAsRead(id),
    onSuccess: () => queryClient.invalidateQueries({ queryKey: ['notifications'] }),
  });

  const TYPE_ICONS: Record<string, string> = {
    GENERAL: '📢', ATTENDANCE: '📋', EXAM: '📝',
    FEE: '💰', TIMETABLE: '🗓️', RESULT: '🏆',
  };

  const renderItem = ({ item }: { item: Notification }) => (
    <TouchableOpacity
      onPress={() => !item.read && markRead.mutate(item.id)}
      className={`rounded-xl p-4 mb-3 shadow-sm flex-row items-start ${item.read ? 'bg-white' : 'bg-blue-50 border border-blue-100'}`}
    >
      <Text className="text-2xl mr-3">{TYPE_ICONS[item.type] ?? '🔔'}</Text>
      <View className="flex-1">
        <View className="flex-row items-center justify-between">
          <Text className={`font-semibold flex-1 ${item.read ? 'text-gray-700' : 'text-blue-900'}`}>
            {item.title}
          </Text>
          {!item.read && (
            <View className="w-2 h-2 bg-blue-600 rounded-full ml-2" />
          )}
        </View>
        <Text className="text-gray-500 text-sm mt-1">{item.message}</Text>
        <Text className="text-gray-400 text-xs mt-2">
          {new Date(item.createdAt).toLocaleDateString()}
        </Text>
      </View>
    </TouchableOpacity>
  );

  return (
    <View className="flex-1 bg-gray-50">
      <View className="bg-blue-800 px-6 pt-8 pb-6">
        <Text className="text-white text-xl font-bold">Notifications</Text>
        <Text className="text-blue-200 text-sm mt-1">{data?.totalElements ?? 0} total</Text>
      </View>
      <FlatList
        data={data?.content ?? []}
        keyExtractor={(item) => String(item.id)}
        renderItem={renderItem}
        contentContainerStyle={{ padding: 16 }}
        ListEmptyComponent={
          <View className="items-center py-12">
            <Text className="text-4xl mb-3">🔔</Text>
            <Text className="text-gray-400 text-lg">No notifications</Text>
          </View>
        }
      />
    </View>
  );
}
