package com.smartestate.service;

import com.smartestate.dto.ComplaintReplyRequest;
import com.smartestate.dto.ComplaintRequest;
import com.smartestate.entity.Complaint;

import java.util.List;

public interface ComplaintService {
    List<Complaint> list(String status, String role);
    Complaint create(Long userId, String role, ComplaintRequest request);
    Complaint reply(Long id, Long replierId, String role, ComplaintReplyRequest request);
    Complaint resolve(Long id, String role);
}
