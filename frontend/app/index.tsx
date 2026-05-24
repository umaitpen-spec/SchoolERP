import { Redirect } from 'expo-router';
import { useAuthStore } from '@/store/authStore';
import { View, ActivityIndicator } from 'react-native';

export default function Index() {
  const { isAuthenticated, isLoading, user } = useAuthStore();

  if (isLoading) {
    return (
      <View className="flex-1 items-center justify-center bg-blue-900">
        <ActivityIndicator size="large" color="#ffffff" />
      </View>
    );
  }

  if (!isAuthenticated) {
    return <Redirect href="/(auth)/login" />;
  }

  switch (user?.role) {
    case 'ADMIN':    return <Redirect href="/(app)/admin/dashboard" />;
    case 'TEACHER':  return <Redirect href="/(app)/teacher/dashboard" />;
    case 'STUDENT':  return <Redirect href="/(app)/student/dashboard" />;
    case 'PARENT':   return <Redirect href="/(app)/parent/dashboard" />;
    default:         return <Redirect href="/(auth)/login" />;
  }
}
