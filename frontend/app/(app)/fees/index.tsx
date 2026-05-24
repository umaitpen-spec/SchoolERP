import React from 'react';
import { View, Text, FlatList } from 'react-native';
import { useQuery } from '@tanstack/react-query';
import { feesService } from '@/services/feesService';
import { FEE_STATUS_COLORS } from '@/constants';
import type { Fees } from '@/types';

export default function FeesScreen() {
  const studentId = 1; // resolve from user profile in production
  const academicYear = '2025-2026';

  const { data } = useQuery({
    queryKey: ['fees', studentId, academicYear],
    queryFn: () => feesService.getStudentFees(studentId, academicYear),
  });

  const renderItem = ({ item }: { item: Fees }) => (
    <View className="bg-white rounded-xl p-4 mb-3 shadow-sm">
      <View className="flex-row items-start justify-between">
        <View className="flex-1">
          <Text className="text-gray-800 font-bold text-base">{item.feeType}</Text>
          <Text className="text-gray-500 text-sm mt-1">Due: {item.dueDate}</Text>
          {item.paidDate && <Text className="text-green-600 text-sm">Paid: {item.paidDate}</Text>}
          {item.term && <Text className="text-gray-400 text-xs mt-1">{item.term}</Text>}
        </View>
        <View className="items-end">
          <Text className="text-gray-800 font-bold text-lg">₹{item.amount}</Text>
          {item.balance > 0 && (
            <Text className="text-red-500 text-sm">Balance: ₹{item.balance}</Text>
          )}
          <View
            className="mt-2 px-2 py-1 rounded-full"
            style={{ backgroundColor: FEE_STATUS_COLORS[item.status] + '22' }}
          >
            <Text style={{ color: FEE_STATUS_COLORS[item.status] }} className="text-xs font-semibold">
              {item.status}
            </Text>
          </View>
        </View>
      </View>
    </View>
  );

  const totalDue = data?.content?.reduce((sum, f) => sum + (f.balance ?? 0), 0) ?? 0;

  return (
    <View className="flex-1 bg-gray-50">
      <View className="bg-blue-800 px-6 pt-8 pb-6">
        <Text className="text-white text-xl font-bold">Fee Management</Text>
        <Text className="text-blue-200 text-sm mt-1">{academicYear}</Text>
        {totalDue > 0 && (
          <View className="mt-3 bg-red-500/30 rounded-xl px-4 py-3">
            <Text className="text-white font-semibold">Total Outstanding: ₹{totalDue.toFixed(2)}</Text>
          </View>
        )}
      </View>
      <FlatList
        data={data?.content ?? []}
        keyExtractor={(item) => String(item.id)}
        renderItem={renderItem}
        contentContainerStyle={{ padding: 16 }}
        ListEmptyComponent={
          <View className="items-center py-12">
            <Text className="text-gray-400 text-lg">No fee records found</Text>
          </View>
        }
      />
    </View>
  );
}
