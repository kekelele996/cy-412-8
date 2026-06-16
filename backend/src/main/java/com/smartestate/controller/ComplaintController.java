package com.smartestate.controller;

import com.smartestate.common.Constants;
import com.smartestate.common.Result;
import com.smartestate.dto.ComplaintReplyRequest;
import com.smartestate.dto.ComplaintRequest;
import com.smartestate.entity.Complaint;
import com.smartestate.service.ComplaintService;
import com.smartestate.utils.LogUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/complaints")
public class ComplaintController {
    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    @GetMapping
    public Result<List<Complaint>> list(@RequestParam(required = false) String status, HttpServletRequest request) {
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("ComplaintController list status=%s role=%s", status, role);
        return Result.ok(complaintService.list(status, role));
    }

    @PostMapping
    public Result<Complaint> create(@RequestBody ComplaintRequest body, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute(Constants.CURRENT_USER_ID);
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("ComplaintController create title=%s role=%s", body.getTitle(), role);
        return Result.ok(complaintService.create(userId, role, body));
    }

    @PutMapping("/{id}/reply")
    public Result<Complaint> reply(@PathVariable Long id, @RequestBody ComplaintReplyRequest body, HttpServletRequest request) {
        Long replierId = (Long) request.getAttribute(Constants.CURRENT_USER_ID);
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("ComplaintController reply complaint_id=%s role=%s", id, role);
        return Result.ok(complaintService.reply(id, replierId, role, body));
    }

    @PutMapping("/{id}/resolve")
    public Result<Complaint> resolve(@PathVariable Long id, HttpServletRequest request) {
        String role = (String) request.getAttribute(Constants.CURRENT_USER_ROLE);
        LogUtil.info("ComplaintController resolve complaint_id=%s role=%s", id, role);
        return Result.ok(complaintService.resolve(id, role));
    }
}
