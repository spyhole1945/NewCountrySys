package com.ruralsync.sys.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class UserListDTO {
    private Long id;
    private String username;
    private String nickname;
    private String role; // VILLAGER, OFFICIAL, ADMIN
    private String phone;
    private Integer points; // 积分
    private LocalDateTime createTime;
    private String status; // ACTIVE, DISABLED
}
