package com.ruralsync.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("market_items")
public class MarketItem {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long userId;
    private String type; // RENT, SELL, HELP
    private String title;
    private String description;
    private BigDecimal price;
    private String status; // ACTIVE, SOLD, REMOVED
    private LocalDateTime createTime;
}
