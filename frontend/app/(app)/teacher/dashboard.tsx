import React from 'react';
import { View, Text, ScrollView, FlatList } from 'react-native';
import { useQuery } from '@tanstack/react-query';
import { useAuthStore } from '@/store/authStore';
import { attendanceService } from '@/services/attendanceService';
import { format } from 'date-fns';

export default function TeacherDashboard() {
  const { user } = useAuthStore();
  const today = format(new Date(), 'yyyy-MM-dd');
  const teacherId = 1; // resolve from user profile in production

  return (
    <ScrollView className="flex-1 bg-gray-50">
      <View className="bg-blue-800 px-6 pt-12 pb-8">
        <Text className="text-blue-200 text-sm">Teacher Portal</Text>
        <Text className="text-white text-2xl font-bold mt-1">{user?.fullName ?? 'Teacher'}</Text>
        <Text className="text-blue-200 text-sm mt-1">Today: {today}</Text>
      </View>

      <View className="px-4 mt-4">
        {/* Quick Stats */}
        <View className="flex-row gap-3 mb-6">
          {[
            { label: 'My Classes', value: '5', color: '#2563eb' },
            { label: 'Students', value: '142', color: '#7c3aed' },
            { label: 'Subjects', value: '3', color: '#059669' },
          ].map((s) => (
            <View key={s.label} className="flex-1 rounded-2xl p-4" style={{ backgroundColor: s.color }}>
              <Text className="text-white text-2xl font-bold">{s.value}</Text>
              <Text className="text-white text-xs mt-1 opacity-90">{s.label}</Text>
            </View>
          ))}
        </View>

        {/* Timetable Today Hint */}
        <Text className="text-gray-700 text-lg font-bold mb-3">Today's Schedule</Text>
        <View className="bg-white rounded-2xl p-5 shadow-sm mb-4">
          <Text className="text-gray-500 text-sm">View your full timetable in the Timetable section.</Text>
        </View>

        {/* Mark Attendance CTA */}
        <Text className="text-gray-700 text-lg font-bold mb-3">Quick Actions</Text>
        <View className="flex-row gap-3">
          {[
            { label: 'Mark Attendance', icon: '📋' },
            { label: 'Enter Marks', icon: '📝' },
            { label: 'Timetable', icon: '🗓️' },
          ].map((action) => (
            <View key={action.label} className="flex-1 bg-white rounded-2xl p-4 items-center shadow-sm">
              <Text className="text-3xl mb-2">{action.icon}</Text>
              <Text className="text-gray-600 text-xs text-center font-medium">{action.label}</Text>
            </View>
          ))}
        </View>
      </View>
    </ScrollView>
  );
}
