export const COMPLAINT_STATUS = {
  PENDING: 'pending',
  REPLIED: 'replied',
  RESOLVED: 'resolved',
} as const;

export type ComplaintStatus = (typeof COMPLAINT_STATUS)[keyof typeof COMPLAINT_STATUS];

export const COMPLAINT_STATUS_TEXT: Record<ComplaintStatus, string> = {
  [COMPLAINT_STATUS.PENDING]: '待处理',
  [COMPLAINT_STATUS.REPLIED]: '已回复',
  [COMPLAINT_STATUS.RESOLVED]: '已处理',
};

export const COMPLAINT_STATUS_COLOR: Record<ComplaintStatus, string> = {
  [COMPLAINT_STATUS.PENDING]: 'warning',
  [COMPLAINT_STATUS.REPLIED]: 'primary',
  [COMPLAINT_STATUS.RESOLVED]: 'success',
};

export const COMPLAINT_CATEGORY = {
  COMPLAINT: 'complaint',
  SUGGESTION: 'suggestion',
} as const;

export type ComplaintCategory = (typeof COMPLAINT_CATEGORY)[keyof typeof COMPLAINT_CATEGORY];

export const COMPLAINT_CATEGORY_TEXT: Record<ComplaintCategory, string> = {
  [COMPLAINT_CATEGORY.COMPLAINT]: '投诉',
  [COMPLAINT_CATEGORY.SUGGESTION]: '建议',
};
