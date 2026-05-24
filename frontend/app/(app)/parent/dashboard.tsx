import React from 'react';
import { View, Text, ScrollView } from 'react-native';
import { useAuthStore } from '@/store/authStore';

export default function ParentDashboard() {
  const { user } = useAuthStore();

  return (
    <ScrollView className="flex-1 bg-gray-50">
      <View className="bg-blue-800 px-6 pt-12 pb-8">
        <Text className="text-blue-200 text-sm">Parent Portal</Text>
        <Text className="text-white text-2xl font-bold mt-1">{user?.fullName ?? 'Parent'}</Text>
      </View>

      <View className="px-4 mt-4">
        <Text className="text-gray-700 text-lg font-bold mb-4">My Children</Text>

        {/* Placeholder child card */}
        <View className="bg-white rounded-2xl p-5 shadow-sm mb-4">
          <Text className="text-gray-500 text-sm">No children linked. Please contact the administrator.</Text>
        </View>

        <Text className="text-gray-700 text-lg font-bold mb-3">Quick Access</Text>
        <View className="flex-row gap-3">
          {[
            { label: 'Attendance', icon: '📋' },
            { label: 'Exam Results', icon: '🏆' },
            { label: 'Fee Status', icon: '💰' },
            { label: 'Timetable', icon: '🗓️' },
          ].map((action) => (
            <View key={action.label} className="flex-1 bg-white rounded-2xl p-3 items-center shadow-sm">
              <Text className="text-2xl mb-1">{action.icon}</Text>
              <Text className="text-gray-600 text-xs text-center">{action.label}</Text>
            </View>
          ))}
        </View>
      </View>
    </ScrollView>
  );
}
