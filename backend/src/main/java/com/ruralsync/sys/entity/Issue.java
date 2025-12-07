package com.ruralsync.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("issues")
public class Issue {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long reporterId;
    private String type; // FACILITY, HYGIENE, DISPUTE, OTHER
    private String content;
    private String mediaUrls; // JSON string
    private String status; // PENDING, PROCESSING, DONE
    private Long currentHandlerId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
