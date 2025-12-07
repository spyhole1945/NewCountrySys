package com.ruralsync.sys.dto;

import lombok.Data;

@Data
public class UserUpdateDTO {
    private String role; // 新角色: VILLAGER, OFFICIAL, ADMIN
}
