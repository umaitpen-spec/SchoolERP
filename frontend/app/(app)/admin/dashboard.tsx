import React from 'react';
import { View, Text, ScrollView, TouchableOpacity } from 'react-native';
import { useQuery } from '@tanstack/react-query';
import { studentService } from '@/services/studentService';
import { useAuthStore } from '@/store/authStore';

const StatCard = ({ label, value, color }: { label: string; value: string | number; color: string }) => (
  <View className={`flex-1 rounded-2xl p-4 mx-1 min-w-36`} style={{ backgroundColor: color }}>
    <Text className="text-white text-3xl font-bold">{value}</Text>
    <Text className="text-white text-sm mt-1 opacity-90">{label}</Text>
  </View>
);

export default function AdminDashboard() {
  const { user } = useAuthStore();
  const { data: students } = useQuery({
    queryKey: ['students'],
    queryFn: () => studentService.getStudents(0, 1),
  });

  const stats = [
    { label: 'Total Students', value: students?.totalElements ?? '...', color: '#2563eb' },
    { label: 'Teachers', value: '...', color: '#7c3aed' },
    { label: 'Classes', value: '...', color: '#059669' },
    { label: 'Pending Fees', value: '...', color: '#dc2626' },
  ];

  return (
    <ScrollView className="flex-1 bg-gray-50">
      {/* Header */}
      <View className="bg-blue-800 px-6 pt-12 pb-8">
        <Text className="text-blue-200 text-sm">Good Morning,</Text>
        <Text className="text-white text-2xl font-bold mt-1">{user?.fullName ?? 'Admin'}</Text>
        <Text className="text-blue-200 text-sm mt-1">School ERP Dashboard</Text>
      </View>

      <View className="px-4 -mt-4">
        {/* Stats Row */}
        <ScrollView horizontal showsHorizontalScrollIndicator={false} className="mb-6">
          {stats.map((s) => (
            <StatCard key={s.label} {...s} />
          ))}
        </ScrollView>

        {/* Quick Actions */}
        <Text className="text-gray-700 text-lg font-bold mb-4">Quick Actions</Text>
        <View className="flex-row flex-wrap gap-3 mb-6">
          {[
            { label: 'Add Student', icon: '👤', href: '/(app)/admin/students' },
            { label: 'Attendance', icon: '📋', href: '/(app)/attendance' },
            { label: 'Exams', icon: '📝', href: '/(app)/exams' },
            { label: 'Fee Reports', icon: '💰', href: '/(app)/fees' },
            { label: 'Timetable', icon: '🗓️', href: '/(app)/timetable' },
            { label: 'Notifications', icon: '🔔', href: '/(app)/notifications' },
          ].map((action) => (
            <TouchableOpacity
              key={action.label}
              className="bg-white rounded-2xl p-4 items-center shadow-sm border border-gray-100"
              style={{ width: '30%' }}
            >
              <Text className="text-3xl mb-2">{action.icon}</Text>
              <Text className="text-gray-700 text-xs text-center font-medium">{action.label}</Text>
            </TouchableOpacity>
          ))}
        </View>

        {/* Recent Students */}
        <Text className="text-gray-700 text-lg font-bold mb-4">Recent Students</Text>
        {students?.content?.slice(0, 5).map((student) => (
          <View key={student.id} className="bg-white rounded-xl p-4 mb-3 shadow-sm flex-row items-center">
            <View className="w-10 h-10 bg-blue-100 rounded-full items-center justify-center mr-3">
              <Text className="text-blue-700 font-bold">{student.fullName.charAt(0)}</Text>
            </View>
            <View className="flex-1">
              <Text className="text-gray-800 font-semibold">{student.fullName}</Text>
              <Text className="text-gray-500 text-sm">{student.rollNumber} • {student.className}</Text>
            </View>
          </View>
        ))}
      </View>
    </ScrollView>
  );
}
