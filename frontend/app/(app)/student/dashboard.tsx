import React from 'react';
import { View, Text, ScrollView } from 'react-native';
import { useQuery } from '@tanstack/react-query';
import { useAuthStore } from '@/store/authStore';
import { attendanceService } from '@/services/attendanceService';
import { examService } from '@/services/examService';
import { feesService } from '@/services/feesService';
import { format } from 'date-fns';

export default function StudentDashboard() {
  const { user } = useAuthStore();

  const today = format(new Date(), 'yyyy-MM-dd');
  const monthStart = format(new Date(new Date().getFullYear(), new Date().getMonth(), 1), 'yyyy-MM-dd');

  // In a real app these IDs come from linking user → student
  const studentId = 1; // placeholder

  const { data: attendanceSummary } = useQuery({
    queryKey: ['attendance-summary', studentId],
    queryFn: () => attendanceService.getAttendanceSummary(studentId, monthStart, today),
  });

  const { data: marks } = useQuery({
    queryKey: ['marks', studentId],
    queryFn: () => examService.getStudentMarks(studentId),
  });

  const totalPresent = attendanceSummary?.present ?? 0;
  const totalAbsent = attendanceSummary?.absent ?? 0;
  const total = totalPresent + totalAbsent;
  const attendancePercent = total > 0 ? Math.round((totalPresent / total) * 100) : 0;

  return (
    <ScrollView className="flex-1 bg-gray-50">
      <View className="bg-blue-800 px-6 pt-12 pb-8">
        <Text className="text-blue-200 text-sm">Welcome back,</Text>
        <Text className="text-white text-2xl font-bold mt-1">{user?.fullName ?? 'Student'}</Text>
      </View>

      <View className="px-4 mt-4">
        {/* Attendance Card */}
        <View className="bg-white rounded-2xl p-5 mb-4 shadow-sm">
          <Text className="text-gray-500 text-sm mb-1">This Month Attendance</Text>
          <View className="flex-row items-end justify-between">
            <Text className="text-4xl font-bold text-blue-700">{attendancePercent}%</Text>
            <View className="items-end">
              <Text className="text-green-600 font-semibold">Present: {totalPresent}</Text>
              <Text className="text-red-500 font-semibold">Absent: {totalAbsent}</Text>
            </View>
          </View>
          {/* Progress bar */}
          <View className="bg-gray-200 rounded-full h-2 mt-3">
            <View
              className="bg-blue-600 h-2 rounded-full"
              style={{ width: `${attendancePercent}%` }}
            />
          </View>
        </View>

        {/* Recent Marks */}
        <Text className="text-gray-700 text-lg font-bold mb-3">Recent Marks</Text>
        {marks?.content?.slice(0, 5).map((m) => (
          <View key={m.id} className="bg-white rounded-xl p-4 mb-3 shadow-sm flex-row items-center justify-between">
            <View className="flex-1">
              <Text className="text-gray-800 font-semibold">{m.subjectName}</Text>
              <Text className="text-gray-500 text-sm">{m.examName}</Text>
            </View>
            <View className="items-end">
              <Text className="text-blue-700 font-bold text-lg">{m.marksObtained}/{m.totalMarks}</Text>
              <Text className="text-gray-500 text-sm">{m.percentage}%</Text>
            </View>
          </View>
        ))}

        {!marks?.content?.length && (
          <View className="bg-white rounded-xl p-6 items-center">
            <Text className="text-gray-400">No marks recorded yet</Text>
          </View>
        )}
      </View>
    </ScrollView>
  );
}
