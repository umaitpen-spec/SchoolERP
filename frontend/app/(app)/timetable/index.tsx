import React from 'react';
import { View, Text, SectionList } from 'react-native';
import { useQuery } from '@tanstack/react-query';
import apiClient from '@/services/apiClient';
import { ApiResponse, Timetable } from '@/types';
import { DAYS_ORDER } from '@/constants';

export default function TimetableScreen() {
  const classRoomId = 1; // resolve from user profile
  const academicYear = '2025-2026';

  const { data: timetable = [] } = useQuery({
    queryKey: ['timetable', classRoomId, academicYear],
    queryFn: async () => {
      const { data } = await apiClient.get<ApiResponse<Timetable[]>>(
        `/timetable/class/${classRoomId}`,
        { params: { academicYear } }
      );
      return data.data;
    },
  });

  // Group by day
  const sections = DAYS_ORDER
    .map((day) => ({
      title: day,
      data: timetable.filter((t) => t.dayOfWeek === day).sort((a, b) =>
        a.startTime.localeCompare(b.startTime)
      ),
    }))
    .filter((s) => s.data.length > 0);

  return (
    <View className="flex-1 bg-gray-50">
      <View className="bg-blue-800 px-6 pt-8 pb-6">
        <Text className="text-white text-xl font-bold">Timetable</Text>
        <Text className="text-blue-200 text-sm mt-1">{academicYear}</Text>
      </View>

      <SectionList
        sections={sections}
        keyExtractor={(item) => String(item.id)}
        contentContainerStyle={{ padding: 16 }}
        renderSectionHeader={({ section: { title } }) => (
          <View className="bg-blue-100 rounded-lg px-4 py-2 mb-2 mt-3">
            <Text className="text-blue-700 font-bold text-sm">{title}</Text>
          </View>
        )}
        renderItem={({ item }) => (
          <View className="bg-white rounded-xl p-4 mb-2 shadow-sm flex-row items-center">
            <View className="bg-blue-50 rounded-lg px-3 py-2 mr-4 items-center min-w-20">
              <Text className="text-blue-700 font-bold text-xs">{item.startTime}</Text>
              <Text className="text-blue-400 text-xs">─</Text>
              <Text className="text-blue-700 font-bold text-xs">{item.endTime}</Text>
            </View>
            <View className="flex-1">
              <Text className="text-gray-800 font-semibold">{item.subjectName}</Text>
              <Text className="text-gray-500 text-sm">{item.teacherName}</Text>
              {item.roomNumber && (
                <Text className="text-gray-400 text-xs">Room: {item.roomNumber}</Text>
              )}
            </View>
          </View>
        )}
        ListEmptyComponent={
          <View className="items-center py-12">
            <Text className="text-gray-400 text-lg">No timetable entries found</Text>
          </View>
        }
      />
    </View>
  );
}
