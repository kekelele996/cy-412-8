import { request } from '../utils/request';
import type { Complaint, ComplaintPayload } from '../types/complaint';
import type { ComplaintStatus } from '../constants/complaint';

export const listComplaints = (status?: ComplaintStatus | 'all') =>
  request.get<never, Complaint[]>('/complaints', { params: { status: status === 'all' ? undefined : status } });
export const createComplaint = (data: ComplaintPayload) => request.post<never, Complaint>('/complaints', data);
export const replyComplaint = (id: number, reply: string) =>
  request.put<never, Complaint>(`/complaints/${id}/reply`, { reply });
export const resolveComplaint = (id: number) =>
  request.put<never, Complaint>(`/complaints/${id}/resolve`);
