package com.ruralsync.sys.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class IssueListDTO {
    private Long id;
    private String reporterName; // 上报人姓名
    private String type; // 问题类型
    private String content; // 问题描述
    private String status; // 状态: PENDING, PROCESSING, DONE
    private LocalDateTime createTime;
    private List<String> mediaUrls; // 图片/视频URL列表
    private String location; // 位置信息
}
