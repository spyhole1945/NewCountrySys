package com.ruralsync.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("issue_logs")
public class IssueLog {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long issueId;
    private Long operatorId;
    private String action; // SUBMIT, ASSIGN, COMPLETE, REJECT
    private String comment;
    private LocalDateTime createTime;
}
