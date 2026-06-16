<script setup lang="ts">
import type { Complaint } from '../../types/complaint';
import { COMPLAINT_CATEGORY_TEXT, type ComplaintCategory } from '../../constants/complaint';
import ComplaintStatusBadge from './ComplaintStatusBadge.vue';
import PermissionButton from './PermissionButton.vue';

defineProps<{
  complaint: Complaint;
}>();

defineEmits<{
  reply: [complaintId: number];
  resolve: [complaintId: number];
}>();

const categoryText = (cat: ComplaintCategory) => COMPLAINT_CATEGORY_TEXT[cat] ?? cat;
</script>

<template>
  <article class="complaint-card">
    <div class="complaint-card__head">
      <div>
        <strong>{{ complaint.title }}</strong>
        <span>{{ categoryText(complaint.category) }} · {{ complaint.user?.nickname || `用户 ${complaint.userId}` }}</span>
      </div>
      <ComplaintStatusBadge :status="complaint.status" />
    </div>
    <p>{{ complaint.content }}</p>
    <div v-if="complaint.reply" class="complaint-card__reply">
      <div class="complaint-card__reply-label">物业回复（{{ complaint.replier?.nickname || '物业' }}）</div>
      <p>{{ complaint.reply }}</p>
    </div>
    <div class="complaint-card__actions">
      <PermissionButton permission="complaint:reply" size="small" @click="$emit('reply', complaint.id)">
        回复
      </PermissionButton>
      <PermissionButton permission="complaint:resolve" size="small" type="success" @click="$emit('resolve', complaint.id)">
        标记已处理
      </PermissionButton>
    </div>
  </article>
</template>

<style scoped>
.complaint-card {
  border: 1px solid #dfe7d8;
  border-radius: 8px;
  background: #fff;
  padding: 16px;
}

.complaint-card__head {
  display: flex;
  align-items: flex-start;
  justify-content: space-between;
  gap: 12px;
}

.complaint-card strong,
.complaint-card span {
  display: block;
}

.complaint-card span {
  color: #778273;
  font-size: 13px;
  margin-top: 5px;
}

.complaint-card p {
  color: #3e4b44;
  line-height: 1.65;
}

.complaint-card__reply {
  background: #f4f8f2;
  border-radius: 6px;
  padding: 12px;
  margin-top: 10px;
}

.complaint-card__reply-label {
  font-size: 13px;
  color: #45624f;
  font-weight: 600;
  margin-bottom: 4px;
}

.complaint-card__reply p {
  margin: 0;
  font-size: 14px;
  color: #3e4b44;
}

.complaint-card__actions {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-top: 10px;
}
</style>
