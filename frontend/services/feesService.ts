import apiClient from './apiClient';
import { ApiResponse, PageResponse, Fees } from '@/types';

export const feesService = {
  async getStudentFees(studentId: number, academicYear: string, page = 0): Promise<PageResponse<Fees>> {
    const { data } = await apiClient.get<ApiResponse<PageResponse<Fees>>>(
      `/fees/student/${studentId}`,
      { params: { academicYear, page, size: 20 } }
    );
    return data.data;
  },

  async createFee(payload: Record<string, unknown>): Promise<Fees> {
    const { data } = await apiClient.post<ApiResponse<Fees>>('/fees', payload);
    return data.data;
  },

  async recordPayment(feeId: number, amount: number, transactionId?: string): Promise<Fees> {
    const { data } = await apiClient.post<ApiResponse<Fees>>(
      `/fees/${feeId}/pay`,
      null,
      { params: { amount, transactionId } }
    );
    return data.data;
  },
};
