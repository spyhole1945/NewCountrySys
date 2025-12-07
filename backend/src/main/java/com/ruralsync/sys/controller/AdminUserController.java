package com.ruralsync.sys.controller;

import com.ruralsync.sys.common.PageResult;
import com.ruralsync.sys.common.Result;
import com.ruralsync.sys.dto.UserListDTO;
import com.ruralsync.sys.dto.UserUpdateDTO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @GetMapping
    public Result<PageResult<UserListDTO>> getUserList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String keyword) {

        // TODO: 实现真实的用户查询逻辑
        List<UserListDTO> records = new ArrayList<>();
        String[] names = { "张三", "李四", "王五", "赵六", "钱七", "孙八", "周九", "吴十", "郑十一", "陈十二" };
        String[] roles = { "VILLAGER", "VILLAGER", "OFFICIAL", "VILLAGER", "OFFICIAL", "VILLAGER", "ADMIN", "VILLAGER",
                "OFFICIAL", "VILLAGER" };

        for (int i = 0; i < size && i < names.length; i++) {
            UserListDTO dto = new UserListDTO();
            dto.setId((long) (i + 1));
            dto.setUsername("user" + (i + 1));
            dto.setNickname(names[i]);
            dto.setRole(roles[i]);
            dto.setPhone("138****" + String.format("%04d", i + 1));
            dto.setPoints(100 + i * 10);
            dto.setCreateTime(LocalDateTime.now().minusDays(30 - i));
            dto.setStatus("ACTIVE");
            records.add(dto);
        }

        PageResult<UserListDTO> pageResult = PageResult.of(50L, page, size, records);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<UserListDTO> getUserDetail(@PathVariable Long id) {
        // TODO: 实现真实的用户详情查询
        UserListDTO dto = new UserListDTO();
        dto.setId(id);
        dto.setUsername("zhangsan");
        dto.setNickname("张三");
        dto.setRole("VILLAGER");
        dto.setPhone("13800138000");
        dto.setPoints(150);
        dto.setCreateTime(LocalDateTime.now().minusDays(30));
        dto.setStatus("ACTIVE");

        return Result.success(dto);
    }

    @PutMapping("/{id}/role")
    public Result<Void> updateUserRole(
            @PathVariable Long id,
            @RequestBody UserUpdateDTO updateDTO) {

        // TODO: 实现真实的角色更新逻辑
        System.out.println("更新用户 " + id + " 角色为: " + updateDTO.getRole());

        return Result.success(null);
    }

    @PutMapping("/{id}/status")
    public Result<Void> toggleUserStatus(
            @PathVariable Long id,
            @RequestParam Boolean active) {

        // TODO: 实现真实的状态切换逻辑
        System.out.println("切换用户 " + id + " 状态为: " + (active ? "ACTIVE" : "DISABLED"));

        return Result.success(null);
    }
}
