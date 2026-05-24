import React, { useState } from 'react';
import { View, Text, FlatList, TouchableOpacity, TextInput, ActivityIndicator } from 'react-native';
import { useQuery } from '@tanstack/react-query';
import { studentService } from '@/services/studentService';
import type { Student } from '@/types';
import { router } from 'expo-router';

export default function AdminStudentsScreen() {
  const [search, setSearch] = useState('');
  const { data, isLoading } = useQuery({
    queryKey: ['students', search],
    queryFn: () =>
      search.length >= 2
        ? studentService.searchStudents(search)
        : studentService.getStudents(),
  });

  const renderItem = ({ item }: { item: Student }) => (
    <TouchableOpacity className="bg-white rounded-xl p-4 mb-3 shadow-sm flex-row items-center">
      <View className="w-11 h-11 bg-blue-100 rounded-full items-center justify-center mr-4">
        <Text className="text-blue-700 font-bold text-base">{item.fullName.charAt(0)}</Text>
      </View>
      <View className="flex-1">
        <Text className="text-gray-800 font-semibold">{item.fullName}</Text>
        <Text className="text-gray-500 text-sm">{item.rollNumber} • {item.className ?? 'No class'}</Text>
        <Text className="text-gray-400 text-xs">{item.email}</Text>
      </View>
      <Text className="text-gray-400 text-xl">›</Text>
    </TouchableOpacity>
  );

  return (
    <View className="flex-1 bg-gray-50">
      <View className="bg-blue-800 px-6 pt-8 pb-4">
        <View className="flex-row items-center justify-between mb-4">
          <Text className="text-white text-xl font-bold">Students</Text>
          <TouchableOpacity className="bg-white/20 rounded-lg px-3 py-1">
            <Text className="text-white text-sm">+ Add</Text>
          </TouchableOpacity>
        </View>
        <View className="bg-white/10 rounded-xl flex-row items-center px-4">
          <Text className="text-white mr-2">🔍</Text>
          <TextInput
            value={search}
            onChangeText={setSearch}
            placeholder="Search by name, roll number..."
            placeholderTextColor="rgba(255,255,255,0.5)"
            className="flex-1 text-white py-3"
          />
        </View>
      </View>

      {isLoading ? (
        <View className="flex-1 items-center justify-center">
          <ActivityIndicator size="large" color="#1e40af" />
        </View>
      ) : (
        <FlatList
          data={data?.content ?? []}
          keyExtractor={(item) => String(item.id)}
          renderItem={renderItem}
          contentContainerStyle={{ padding: 16 }}
          ListHeaderComponent={
            <Text className="text-gray-500 text-sm mb-3">
              {data?.totalElements ?? 0} students total
            </Text>
          }
          ListEmptyComponent={
            <View className="items-center py-12">
              <Text className="text-gray-400 text-lg">No students found</Text>
            </View>
          }
        />
      )}
    </View>
  );
}
