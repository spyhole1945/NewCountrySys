package com.ruralsync.sys.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class IssueTimelineDTO {
    private String status;
    private String action;
    private String comment;
    private String operatorName;
    private LocalDateTime time;
}
