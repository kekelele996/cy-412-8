package com.smartestate.dto;

import lombok.Data;

@Data
public class ComplaintRequest {
    private String category;
    private String title;
    private String content;
}
