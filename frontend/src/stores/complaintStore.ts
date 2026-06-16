import { defineStore } from 'pinia';
import { ref } from 'vue';
import { listComplaints, replyComplaint, resolveComplaint } from '../api/complaint';
import { COMPLAINT_STATUS, type ComplaintStatus } from '../constants/complaint';
import type { Complaint } from '../types/complaint';

export const useComplaintStore = defineStore('complaint', () => {
  const complaints = ref<Complaint[]>([]);
  const loading = ref(false);
  const activeStatus = ref<ComplaintStatus | 'all'>('all');

  const pendingCount = () => complaints.value.filter((item) => item.status === COMPLAINT_STATUS.PENDING).length;

  async function fetchComplaints(status: ComplaintStatus | 'all' = activeStatus.value) {
    loading.value = true;
    activeStatus.value = status;
    try {
      complaints.value = await listComplaints(status);
    } finally {
      loading.value = false;
    }
  }

  async function reply(id: number, replyText: string) {
    const updated = await replyComplaint(id, replyText);
    complaints.value = complaints.value.map((item) => (item.id === id ? updated : item));
  }

  async function resolve(id: number) {
    const updated = await resolveComplaint(id);
    complaints.value = complaints.value.map((item) => (item.id === id ? updated : item));
  }

  return { complaints, loading, activeStatus, pendingCount, fetchComplaints, reply, resolve };
});
