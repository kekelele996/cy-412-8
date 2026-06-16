package com.smartestate.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.smartestate.constants.ComplaintConstants;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("complaints")
public class Complaint {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String category;
    private String title;
    private String content;
    private String status = ComplaintConstants.PENDING;
    private String reply;
    private Long replierId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @TableField(exist = false)
    private User user;
    @TableField(exist = false)
    private User replier;
}
