import { Drawer } from 'expo-router/drawer';
import { useAuthStore } from '@/store/authStore';
import { Redirect } from 'expo-router';
import { DrawerContentScrollView, DrawerItemList, DrawerItem } from '@react-navigation/drawer';
import { View, Text, Image } from 'react-native';

function CustomDrawerContent(props: any) {
  const { user, logout } = useAuthStore();
  return (
    <DrawerContentScrollView {...props} className="bg-blue-900">
      <View className="p-6 pb-4 border-b border-blue-700">
        {user?.profileImageUrl ? (
          <Image source={{ uri: user.profileImageUrl }} className="w-16 h-16 rounded-full mb-3" />
        ) : (
          <View className="w-16 h-16 rounded-full bg-blue-500 items-center justify-center mb-3">
            <Text className="text-white text-2xl font-bold">
              {user?.fullName?.charAt(0) ?? '?'}
            </Text>
          </View>
        )}
        <Text className="text-white font-bold text-base">{user?.fullName}</Text>
        <Text className="text-blue-200 text-sm">{user?.email}</Text>
        <View className="mt-2 bg-blue-700 self-start px-2 py-1 rounded">
          <Text className="text-blue-100 text-xs font-semibold">{user?.role}</Text>
        </View>
      </View>
      <DrawerItemList {...props} />
      <DrawerItem
        label="Logout"
        onPress={logout}
        labelStyle={{ color: '#ef4444' }}
      />
    </DrawerContentScrollView>
  );
}

export default function AppLayout() {
  const { isAuthenticated } = useAuthStore();
  if (!isAuthenticated) return <Redirect href="/(auth)/login" />;

  return (
    <Drawer
      drawerContent={(props) => <CustomDrawerContent {...props} />}
      screenOptions={{
        drawerStyle: { backgroundColor: '#1e3a8a', width: 260 },
        drawerLabelStyle: { color: '#e0e7ff' },
        drawerActiveBackgroundColor: '#1d4ed8',
        drawerActiveTintColor: '#ffffff',
        drawerInactiveTintColor: '#93c5fd',
        headerStyle: { backgroundColor: '#1e40af' },
        headerTintColor: '#ffffff',
        headerTitleStyle: { fontWeight: 'bold' },
      }}
    >
      <Drawer.Screen name="admin" options={{ title: 'Admin', drawerLabel: 'Admin Panel' }} />
      <Drawer.Screen name="teacher" options={{ title: 'Teacher', drawerLabel: 'Teacher Dashboard' }} />
      <Drawer.Screen name="student" options={{ title: 'Student', drawerLabel: 'Student Portal' }} />
      <Drawer.Screen name="parent" options={{ title: 'Parent', drawerLabel: 'Parent Portal' }} />
      <Drawer.Screen name="attendance" options={{ title: 'Attendance', drawerLabel: 'Attendance' }} />
      <Drawer.Screen name="exams" options={{ title: 'Exams', drawerLabel: 'Exams & Marks' }} />
      <Drawer.Screen name="fees" options={{ title: 'Fees', drawerLabel: 'Fee Management' }} />
      <Drawer.Screen name="timetable" options={{ title: 'Timetable', drawerLabel: 'Timetable' }} />
      <Drawer.Screen name="notifications" options={{ title: 'Notifications', drawerLabel: 'Notifications' }} />
    </Drawer>
  );
}
