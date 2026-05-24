import React from 'react';
import { View, Text, FlatList } from 'react-native';
import { useQuery } from '@tanstack/react-query';
import { examService } from '@/services/examService';
import type { Exam } from '@/types';

export default function ExamsScreen() {
  const { data, isLoading } = useQuery({
    queryKey: ['exams'],
    queryFn: () => examService.getExams(),
  });

  const renderItem = ({ item }: { item: Exam }) => (
    <View className="bg-white rounded-xl p-4 mb-3 shadow-sm">
      <View className="flex-row items-start justify-between">
        <View className="flex-1">
          <Text className="text-gray-800 font-bold text-base">{item.examName}</Text>
          <Text className="text-gray-500 text-sm mt-1">{item.subjectName} • {item.className}</Text>
          <Text className="text-gray-400 text-sm mt-1">📅 {item.examDate}</Text>
          {item.startTime && (
            <Text className="text-gray-400 text-sm">⏰ {item.startTime} - {item.endTime}</Text>
          )}
        </View>
        <View className="items-end">
          <View className="bg-blue-100 px-2 py-1 rounded mb-2">
            <Text className="text-blue-700 text-xs font-semibold">{item.examType.replace('_', ' ')}</Text>
          </View>
          <Text className="text-gray-600 text-sm">Max: {item.totalMarks}</Text>
          <Text className="text-gray-500 text-xs">Pass: {item.passingMarks}</Text>
        </View>
      </View>
    </View>
  );

  return (
    <View className="flex-1 bg-gray-50">
      <View className="bg-blue-800 px-6 pt-8 pb-6">
        <Text className="text-white text-xl font-bold">Exams & Marks</Text>
        <Text className="text-blue-200 text-sm mt-1">{data?.totalElements ?? 0} exams scheduled</Text>
      </View>
      <FlatList
        data={data?.content ?? []}
        keyExtractor={(item) => String(item.id)}
        renderItem={renderItem}
        contentContainerStyle={{ padding: 16 }}
        ListEmptyComponent={
          <View className="items-center py-12">
            <Text className="text-gray-400 text-lg">No exams found</Text>
          </View>
        }
      />
    </View>
  );
}
