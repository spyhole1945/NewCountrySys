package com.ruralsync.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("notices")
public class Notice {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String title;
    private String category; // PARTY, VILLAGE, FINANCE
    private String content;
    private LocalDateTime publishTime;
}
