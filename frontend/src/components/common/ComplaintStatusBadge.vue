<script setup lang="ts">
import { computed } from 'vue';
import { COMPLAINT_STATUS_COLOR, COMPLAINT_STATUS_TEXT, type ComplaintStatus } from '../../constants/complaint';

const props = defineProps<{ status: ComplaintStatus }>();

const label = computed(() => COMPLAINT_STATUS_TEXT[props.status]);
const type = computed(() => COMPLAINT_STATUS_COLOR[props.status] as 'success' | 'info' | 'warning' | 'primary' | undefined);
</script>

<template>
  <el-tag :type="type" effect="light" round>{{ label }}</el-tag>
</template>
