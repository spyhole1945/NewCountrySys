package com.ruralsync.sys.dto;

import lombok.Data;

@Data
public class IssueUpdateDTO {
    private String status; // 新状态: PENDING, PROCESSING, DONE
    private String processNotes; // 处理意见
}
