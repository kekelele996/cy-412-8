package com.smartestate.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.smartestate.common.ErrorCode;
import com.smartestate.constants.ComplaintConstants;
import com.smartestate.constants.LogTemplates;
import com.smartestate.constants.Messages;
import com.smartestate.dto.ComplaintReplyRequest;
import com.smartestate.dto.ComplaintRequest;
import com.smartestate.entity.Complaint;
import com.smartestate.entity.User;
import com.smartestate.mapper.ComplaintMapper;
import com.smartestate.mapper.UserMapper;
import com.smartestate.service.ComplaintService;
import com.smartestate.service.OperationLogService;
import com.smartestate.utils.LogUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ComplaintServiceImpl implements ComplaintService {
    private final ComplaintMapper complaintMapper;
    private final UserMapper userMapper;
    private final OperationLogService operationLogService;

    public ComplaintServiceImpl(ComplaintMapper complaintMapper, UserMapper userMapper, OperationLogService operationLogService) {
        this.complaintMapper = complaintMapper;
        this.userMapper = userMapper;
        this.operationLogService = operationLogService;
    }

    @Override
    public List<Complaint> list(String status, String role) {
        LambdaQueryWrapper<Complaint> wrapper = new LambdaQueryWrapper<Complaint>().orderByDesc(Complaint::getCreatedAt);
        if (StringUtils.hasText(status)) {
            wrapper.eq(Complaint::getStatus, status);
        }
        List<Complaint> complaints = complaintMapper.selectList(wrapper);
        complaints.forEach(this::hydrate);
        LogUtil.info(LogTemplates.COMPLAINT_LIST, status == null ? "all" : status, role);
        return complaints;
    }

    @Override
    public Complaint create(Long userId, String role, ComplaintRequest request) {
        if (!ComplaintConstants.ALL_CATEGORIES.contains(request.getCategory())) {
            throw new IllegalArgumentException(ErrorCode.COMPLAINT_CATEGORY_INVALID.format(request.getCategory(), role));
        }
        Complaint complaint = new Complaint();
        complaint.setUserId(userId);
        complaint.setCategory(request.getCategory());
        complaint.setTitle(request.getTitle());
        complaint.setContent(request.getContent());
        complaint.setStatus(ComplaintConstants.PENDING);
        complaint.setCreatedAt(LocalDateTime.now());
        complaint.setUpdatedAt(LocalDateTime.now());
        complaintMapper.insert(complaint);
        operationLogService.record(userId, role, "complaint.create", "Complaint", complaint.getId(),
                String.format(LogTemplates.COMPLAINT_CREATE, complaint.getId(), complaint.getCategory(), role) + " | " + Messages.FRONT_COMPLAINT_CREATED);
        hydrate(complaint);
        return complaint;
    }

    @Override
    public Complaint reply(Long id, Long replierId, String role, ComplaintReplyRequest request) {
        Complaint complaint = complaintMapper.selectById(id);
        if (complaint == null) {
            throw new IllegalArgumentException(ErrorCode.COMPLAINT_NOT_FOUND.format(id, role));
        }
        complaint.setReply(request.getReply());
        complaint.setReplierId(replierId);
        complaint.setStatus(ComplaintConstants.REPLIED);
        complaint.setUpdatedAt(LocalDateTime.now());
        complaintMapper.updateById(complaint);
        operationLogService.record(replierId, role, "complaint.reply", "Complaint", id,
                String.format(LogTemplates.COMPLAINT_REPLY, id, role) + " | " + Messages.FRONT_COMPLAINT_REPLIED);
        hydrate(complaint);
        return complaint;
    }

    @Override
    public Complaint resolve(Long id, String role) {
        Complaint complaint = complaintMapper.selectById(id);
        if (complaint == null) {
            throw new IllegalArgumentException(ErrorCode.COMPLAINT_NOT_FOUND.format(id, role));
        }
        complaint.setStatus(ComplaintConstants.RESOLVED);
        complaint.setUpdatedAt(LocalDateTime.now());
        complaintMapper.updateById(complaint);
        operationLogService.record(complaint.getUserId(), role, "complaint.resolve", "Complaint", id,
                String.format(LogTemplates.COMPLAINT_RESOLVE, id, role) + " | " + Messages.FRONT_COMPLAINT_RESOLVED);
        hydrate(complaint);
        return complaint;
    }

    private void hydrate(Complaint complaint) {
        if (complaint.getUserId() != null) {
            User user = userMapper.selectById(complaint.getUserId());
            if (user != null) user.setPasswordHash(null);
            complaint.setUser(user);
        }
        if (complaint.getReplierId() != null) {
            User replier = userMapper.selectById(complaint.getReplierId());
            if (replier != null) replier.setPasswordHash(null);
            complaint.setReplier(replier);
        }
    }
}
