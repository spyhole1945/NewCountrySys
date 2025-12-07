package com.ruralsync.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@TableName("users")
public class User {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String username;
    private String password;
    private String role; // VILLAGER, OFFICIAL, ADMIN
    private String phone;
    private String openid;
    private Integer points;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
