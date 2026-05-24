import React, { useEffect } from 'react';
import {
  View,
  Text,
  TouchableOpacity,
  Image,
  ActivityIndicator,
  Platform,
} from 'react-native';
import * as WebBrowser from 'expo-web-browser';
import * as AuthSession from 'expo-auth-session';
import { router } from 'expo-router';
import { useAuthStore } from '@/store/authStore';
import { authService } from '@/services/authService';
import { API_BASE_URL } from '@/constants';
import Toast from 'react-native-toast-message';

WebBrowser.maybeCompleteAuthSession();

export default function LoginScreen() {
  const { setUser, setAuthenticated, isAuthenticated, isLoading } = useAuthStore();
  const redirectUri = AuthSession.makeRedirectUri({ scheme: 'schoolerp', path: 'auth/callback' });

  useEffect(() => {
    if (isAuthenticated) {
      router.replace('/');
    }
  }, [isAuthenticated]);

  const handleGoogleLogin = async () => {
    try {
      const authUrl =
        `${API_BASE_URL}/oauth2/authorization/google?redirect_uri=${encodeURIComponent(redirectUri)}`;

      const result = await WebBrowser.openAuthSessionAsync(authUrl, redirectUri);

      if (result.type === 'success' && result.url) {
        const params = new URL(result.url).searchParams;
        const token = params.get('token');
        const refreshToken = params.get('refresh_token');

        if (token && refreshToken) {
          // Decode user from token
          const payloadBase64 = token.split('.')[1];
          const payload = JSON.parse(atob(payloadBase64));

          const user = {
            id: parseInt(payload.sub),
            email: payload.email,
            fullName: payload.email,
            role: payload.role,
            active: true,
            createdAt: new Date().toISOString(),
          };

          await authService.saveAuthData({ accessToken: token, refreshToken, tokenType: 'Bearer', expiresIn: 86400, user });
          setUser(user as any);
          setAuthenticated(true);
          router.replace('/');
        }
      }
    } catch (err) {
      Toast.show({ type: 'error', text1: 'Login failed', text2: 'Please try again.' });
    }
  };

  if (isLoading) {
    return (
      <View className="flex-1 bg-blue-900 items-center justify-center">
        <ActivityIndicator size="large" color="#fff" />
      </View>
    );
  }

  return (
    <View className="flex-1 bg-gradient-to-br from-blue-900 to-blue-700 items-center justify-center px-8">
      {/* Logo / Header */}
      <View className="items-center mb-12">
        <View className="w-24 h-24 bg-white rounded-2xl items-center justify-center mb-6 shadow-2xl">
          <Text className="text-5xl">🏫</Text>
        </View>
        <Text className="text-white text-4xl font-bold tracking-tight">School ERP</Text>
        <Text className="text-blue-200 text-lg mt-2">Enterprise School Management</Text>
      </View>

      {/* Login Card */}
      <View className="w-full bg-white rounded-3xl p-8 shadow-2xl">
        <Text className="text-gray-800 text-2xl font-bold text-center mb-2">Welcome Back</Text>
        <Text className="text-gray-500 text-center mb-8">Sign in to continue</Text>

        <TouchableOpacity
          onPress={handleGoogleLogin}
          className="flex-row items-center justify-center bg-white border-2 border-gray-200 rounded-xl py-4 px-6 mb-4"
          style={{ elevation: 2 }}
        >
          <Text className="text-2xl mr-3">🔵</Text>
          <Text className="text-gray-700 font-semibold text-base">Continue with Google</Text>
        </TouchableOpacity>

        <Text className="text-gray-400 text-xs text-center mt-4">
          By signing in, you agree to our Terms of Service and Privacy Policy
        </Text>
      </View>

      <Text className="text-blue-200 text-sm mt-8">© 2026 School ERP System</Text>
    </View>
  );
}
