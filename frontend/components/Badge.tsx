import React from 'react';
import { View, Text } from 'react-native';

interface BadgeProps {
  label: string;
  color: string;
  textColor?: string;
}

export function Badge({ label, color, textColor = '#ffffff' }: BadgeProps) {
  return (
    <View
      className="px-2 py-1 rounded-full self-start"
      style={{ backgroundColor: color }}
    >
      <Text className="text-xs font-semibold" style={{ color: textColor }}>
        {label}
      </Text>
    </View>
  );
}
