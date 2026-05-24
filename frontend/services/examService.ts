import apiClient from './apiClient';
import { ApiResponse, PageResponse, Exam, Marks } from '@/types';

export const examService = {
  async getExams(page = 0): Promise<PageResponse<Exam>> {
    const { data } = await apiClient.get<ApiResponse<PageResponse<Exam>>>('/exams', {
      params: { page, size: 20 },
    });
    return data.data;
  },

  async createExam(payload: Record<string, unknown>): Promise<Exam> {
    const { data } = await apiClient.post<ApiResponse<Exam>>('/exams', payload);
    return data.data;
  },

  async enterMarks(payload: Record<string, unknown>): Promise<Marks[]> {
    const { data } = await apiClient.post<ApiResponse<Marks[]>>('/exams/marks', payload);
    return data.data;
  },

  async getStudentMarks(studentId: number, page = 0): Promise<PageResponse<Marks>> {
    const { data } = await apiClient.get<ApiResponse<PageResponse<Marks>>>(
      `/exams/marks/student/${studentId}`,
      { params: { page, size: 20 } }
    );
    return data.data;
  },
};
