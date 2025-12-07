package com.ruralsync.sys.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ContentListDTO {
    private Long id;
    private String type; // COMMUNITY_POST, MARKET_ITEM
    private Long userId;
    private String userName;
    private String content;
    private List<String> mediaUrls;
    private String status; // PENDING_REVIEW, APPROVED, REJECTED
    private LocalDateTime createTime;
}
