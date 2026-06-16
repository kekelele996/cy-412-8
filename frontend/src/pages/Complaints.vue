<script setup lang="ts">
import { onMounted, ref, reactive } from 'vue';
import { ElMessage } from 'element-plus';
import ComplaintCard from '../components/common/ComplaintCard.vue';
import EmptyState from '../components/common/EmptyState.vue';
import PermissionButton from '../components/common/PermissionButton.vue';
import { useComplaintStore } from '../stores/complaintStore';
import { createComplaint } from '../api/complaint';
import { COMPLAINT_STATUS, COMPLAINT_STATUS_TEXT, COMPLAINT_CATEGORY, COMPLAINT_CATEGORY_TEXT, type ComplaintStatus, type ComplaintCategory } from '../constants/complaint';

const complaintStore = useComplaintStore();

const statusOptions: Array<{ label: string; value: ComplaintStatus | 'all' }> = [
  { label: '全部', value: 'all' },
  ...Object.values(COMPLAINT_STATUS).map((status) => ({ label: COMPLAINT_STATUS_TEXT[status], value: status })),
];

const showCreate = ref(false);
const createForm = reactive({ category: 'complaint' as ComplaintCategory, title: '', content: '' });
const replyDialog = ref(false);
const replyTarget = ref<number | null>(null);
const replyText = ref('');

async function submitCreate() {
  if (!createForm.title.trim() || !createForm.content.trim()) {
    ElMessage.warning('请填写标题和内容');
    return;
  }
  await createComplaint({ category: createForm.category, title: createForm.title, content: createForm.content });
  ElMessage.success('提交成功');
  showCreate.value = false;
  createForm.title = '';
  createForm.content = '';
  createForm.category = 'complaint';
  await complaintStore.fetchComplaints();
}

function openReply(id: number) {
  replyTarget.value = id;
  replyText.value = '';
  replyDialog.value = true;
}

async function submitReply() {
  if (!replyText.value.trim()) {
    ElMessage.warning('请输入回复内容');
    return;
  }
  await complaintStore.reply(replyTarget.value!, replyText.value);
  ElMessage.success('回复成功');
  replyDialog.value = false;
}

async function handleResolve(id: number) {
  await complaintStore.resolve(id);
  ElMessage.success('已标记为已处理');
}

onMounted(async () => {
  await complaintStore.fetchComplaints();
});
</script>

<template>
  <section class="section-panel">
    <div class="filter-row">
      <el-segmented
        :model-value="complaintStore.activeStatus"
        :options="statusOptions"
        @change="(status: string | number | boolean) => complaintStore.fetchComplaints(status as ComplaintStatus | 'all')"
      />
      <PermissionButton permission="complaint:create" plain @click="showCreate = true">提交投诉/建议</PermissionButton>
    </div>
    <div v-if="complaintStore.complaints.length" class="list-stack">
      <ComplaintCard
        v-for="item in complaintStore.complaints"
        :key="item.id"
        :complaint="item"
        @reply="openReply"
        @resolve="handleResolve"
      />
    </div>
    <EmptyState v-else title="暂无投诉建议" description="切换状态筛选或提交新的投诉/建议" />

    <el-dialog v-model="showCreate" title="提交投诉/建议" width="480" :close-on-click-modal="false">
      <el-form label-position="top">
        <el-form-item label="类型">
          <el-radio-group v-model="createForm.category">
            <el-radio value="complaint">{{ COMPLAINT_CATEGORY_TEXT[COMPLAINT_CATEGORY.COMPLAINT] }}</el-radio>
            <el-radio value="suggestion">{{ COMPLAINT_CATEGORY_TEXT[COMPLAINT_CATEGORY.SUGGESTION] }}</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="标题">
          <el-input v-model="createForm.title" maxlength="120" placeholder="简要概括问题或建议" />
        </el-form-item>
        <el-form-item label="详细内容">
          <el-input v-model="createForm.content" type="textarea" :rows="4" placeholder="请详细描述您的投诉或建议" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showCreate = false">取消</el-button>
        <el-button type="primary" @click="submitCreate">提交</el-button>
      </template>
    </el-dialog>

    <el-dialog v-model="replyDialog" title="回复投诉/建议" width="480" :close-on-click-modal="false">
      <el-form label-position="top">
        <el-form-item label="回复内容">
          <el-input v-model="replyText" type="textarea" :rows="4" placeholder="请输入回复内容" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="replyDialog = false">取消</el-button>
        <el-button type="primary" @click="submitReply">确认回复</el-button>
      </template>
    </el-dialog>
  </section>
</template>
