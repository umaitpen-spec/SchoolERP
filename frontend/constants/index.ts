export const API_BASE_URL = process.env.EXPO_PUBLIC_API_BASE_URL ?? 'http://localhost:8080/api/v1';

export const ROUTES = {
  LOGIN: '/(auth)/login',
  ADMIN_DASHBOARD: '/(app)/admin/dashboard',
  STUDENT_DASHBOARD: '/(app)/student/dashboard',
  TEACHER_DASHBOARD: '/(app)/teacher/dashboard',
  PARENT_DASHBOARD: '/(app)/parent/dashboard',
  ATTENDANCE: '/(app)/attendance',
  EXAMS: '/(app)/exams',
  FEES: '/(app)/fees',
  TIMETABLE: '/(app)/timetable',
  NOTIFICATIONS: '/(app)/notifications',
  STUDENTS: '/(app)/admin/students',
  TEACHERS: '/(app)/admin/teachers',
} as const;

export const STORAGE_KEYS = {
  ACCESS_TOKEN: 'access_token',
  REFRESH_TOKEN: 'refresh_token',
  USER: 'user',
} as const;

export const COLORS = {
  primary: '#1e40af',
  primaryLight: '#3b82f6',
  success: '#10b981',
  danger: '#ef4444',
  warning: '#f59e0b',
  info: '#06b6d4',
  textPrimary: '#1e293b',
  textSecondary: '#64748b',
  bgLight: '#f8fafc',
  bgDark: '#0f0f1a',
  white: '#ffffff',
  border: '#e2e8f0',
} as const;

export const FEE_STATUS_COLORS: Record<string, string> = {
  PENDING: '#f59e0b',
  PAID: '#10b981',
  OVERDUE: '#ef4444',
  PARTIAL: '#06b6d4',
  WAIVED: '#6366f1',
};

export const ATTENDANCE_COLORS: Record<string, string> = {
  PRESENT: '#10b981',
  ABSENT: '#ef4444',
  LATE: '#f59e0b',
  EXCUSED: '#6366f1',
};

export const DAYS_ORDER = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'];
