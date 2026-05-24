import apiClient from './apiClient';
import { ApiResponse, PageResponse, Attendance, AttendanceSummary } from '@/types';

export const attendanceService = {
  async markAttendance(payload: Record<string, unknown>): Promise<Attendance[]> {
    const { data } = await apiClient.post<ApiResponse<Attendance[]>>('/attendance/mark', payload);
    return data.data;
  },

  async getStudentAttendance(studentId: number, page = 0): Promise<PageResponse<Attendance>> {
    const { data } = await apiClient.get<ApiResponse<PageResponse<Attendance>>>(
      `/attendance/student/${studentId}`,
      { params: { page, size: 30 } }
    );
    return data.data;
  },

  async getAttendanceSummary(studentId: number, from: string, to: string): Promise<AttendanceSummary> {
    const { data } = await apiClient.get<ApiResponse<AttendanceSummary>>(
      `/attendance/student/${studentId}/summary`,
      { params: { from, to } }
    );
    return data.data;
  },

  async getClassAttendance(classRoomId: number, date: string): Promise<Attendance[]> {
    const { data } = await apiClient.get<ApiResponse<Attendance[]>>(
      `/attendance/class/${classRoomId}`,
      { params: { date } }
    );
    return data.data;
  },
};
