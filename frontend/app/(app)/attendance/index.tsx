import React, { useState } from 'react';
import { View, Text, ScrollView, TouchableOpacity, FlatList } from 'react-native';
import { useQuery } from '@tanstack/react-query';
import { useAuthStore } from '@/store/authStore';
import { attendanceService } from '@/services/attendanceService';
import { format } from 'date-fns';
import { ATTENDANCE_COLORS } from '@/constants';
import type { Attendance } from '@/types';

export default function AttendanceScreen() {
  const { user } = useAuthStore();
  const [selectedTab, setSelectedTab] = useState<'history' | 'mark'>('history');
  const studentId = 1; // In production, resolve from user profile
  const today = format(new Date(), 'yyyy-MM-dd');
  const monthStart = format(new Date(new Date().getFullYear(), new Date().getMonth(), 1), 'yyyy-MM-dd');

  const { data: attendancePage, isLoading } = useQuery({
    queryKey: ['attendance', studentId],
    queryFn: () => attendanceService.getStudentAttendance(studentId),
  });

  const { data: summary } = useQuery({
    queryKey: ['attendance-summary', studentId],
    queryFn: () => attendanceService.getAttendanceSummary(studentId, monthStart, today),
  });

  const renderItem = ({ item }: { item: Attendance }) => (
    <View className="bg-white rounded-xl p-4 mb-3 shadow-sm flex-row items-center justify-between">
      <View>
        <Text className="text-gray-800 font-semibold">{item.subjectName ?? 'General'}</Text>
        <Text className="text-gray-500 text-sm">{item.attendanceDate}</Text>
        {item.remarks ? <Text className="text-gray-400 text-xs mt-1">{item.remarks}</Text> : null}
      </View>
      <View
        className="px-3 py-1 rounded-full"
        style={{ backgroundColor: ATTENDANCE_COLORS[item.status] + '22' }}
      >
        <Text style={{ color: ATTENDANCE_COLORS[item.status] }} className="font-bold text-sm">
          {item.status}
        </Text>
      </View>
    </View>
  );

  return (
    <View className="flex-1 bg-gray-50">
      {/* Summary */}
      <View className="bg-blue-800 px-6 pt-8 pb-6">
        <Text className="text-white text-xl font-bold mb-4">Attendance</Text>
        <View className="flex-row justify-between">
          {[
            { label: 'Present', value: summary?.present ?? 0, color: '#10b981' },
            { label: 'Absent', value: summary?.absent ?? 0, color: '#ef4444' },
            { label: 'Late', value: summary?.late ?? 0, color: '#f59e0b' },
            { label: 'Excused', value: summary?.excused ?? 0, color: '#6366f1' },
          ].map((s) => (
            <View key={s.label} className="items-center">
              <Text className="text-2xl font-bold" style={{ color: s.color }}>{s.value}</Text>
              <Text className="text-blue-200 text-xs mt-1">{s.label}</Text>
            </View>
          ))}
        </View>
      </View>

      {/* Attendance List */}
      <FlatList
        data={attendancePage?.content ?? []}
        keyExtractor={(item) => String(item.id)}
        renderItem={renderItem}
        contentContainerStyle={{ padding: 16 }}
        ListEmptyComponent={
          <View className="items-center py-12">
            <Text className="text-gray-400 text-lg">No attendance records found</Text>
          </View>
        }
      />
    </View>
  );
}
