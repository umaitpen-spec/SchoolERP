import apiClient from './apiClient';
import { ApiResponse, PageResponse, Student } from '@/types';

export const studentService = {
  async getStudents(page = 0, size = 20): Promise<PageResponse<Student>> {
    const { data } = await apiClient.get<ApiResponse<PageResponse<Student>>>('/students', {
      params: { page, size, sort: 'createdAt,desc' },
    });
    return data.data;
  },

  async getStudentById(id: number): Promise<Student> {
    const { data } = await apiClient.get<ApiResponse<Student>>(`/students/${id}`);
    return data.data;
  },

  async getStudentsByClass(classRoomId: number): Promise<PageResponse<Student>> {
    const { data } = await apiClient.get<ApiResponse<PageResponse<Student>>>(`/students/class/${classRoomId}`);
    return data.data;
  },

  async searchStudents(query: string): Promise<PageResponse<Student>> {
    const { data } = await apiClient.get<ApiResponse<PageResponse<Student>>>('/students/search', {
      params: { query },
    });
    return data.data;
  },

  async createStudent(payload: Record<string, unknown>): Promise<Student> {
    const { data } = await apiClient.post<ApiResponse<Student>>('/students', payload);
    return data.data;
  },

  async updateStudent(id: number, payload: Record<string, unknown>): Promise<Student> {
    const { data } = await apiClient.put<ApiResponse<Student>>(`/students/${id}`, payload);
    return data.data;
  },

  async deleteStudent(id: number): Promise<void> {
    await apiClient.delete(`/students/${id}`);
  },
};
