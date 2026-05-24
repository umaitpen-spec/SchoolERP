import React from 'react';
import { View, ActivityIndicator } from 'react-native';

interface LoadingSpinnerProps {
  fullScreen?: boolean;
  color?: string;
}

export function LoadingSpinner({ fullScreen = true, color = '#1e40af' }: LoadingSpinnerProps) {
  return (
    <View className={`items-center justify-center ${fullScreen ? 'flex-1' : 'p-8'}`}>
      <ActivityIndicator size="large" color={color} />
    </View>
  );
}
