import type { ComplaintCategory, ComplaintStatus } from '../constants/complaint';
import type { User } from './user';

export interface Complaint {
  id: number;
  userId: number;
  category: ComplaintCategory;
  title: string;
  content: string;
  status: ComplaintStatus;
  reply?: string;
  replierId?: number;
  createdAt: string;
  updatedAt: string;
  user?: User;
  replier?: User;
}

export interface ComplaintPayload {
  category: ComplaintCategory;
  title: string;
  content: string;
}
