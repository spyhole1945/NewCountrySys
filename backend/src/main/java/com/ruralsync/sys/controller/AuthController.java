package com.ruralsync.sys.controller;

import com.ruralsync.sys.common.Result;
import com.ruralsync.sys.dto.LoginDTO;
import com.ruralsync.sys.dto.LoginResponseDTO;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public Result<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) {
        // TODO: 实现真实的认证逻辑
        // 1. 验证用户名密码
        // 2. 生成JWT token
        // 3. 返回token和用户信息

        // 临时mock实现
        if ("admin".equals(loginDTO.getUsername()) && "admin123".equals(loginDTO.getPassword())) {
            LoginResponseDTO.UserInfoDTO userInfo = new LoginResponseDTO.UserInfoDTO(
                    1L,
                    "admin",
                    "ADMIN",
                    "系统管理员");

            // 生成简单的mock token (实际应使用JWT)
            String token = "mock-jwt-token-" + System.currentTimeMillis();

            LoginResponseDTO response = new LoginResponseDTO(token, userInfo);
            return Result.success(response);
        }

        return Result.error("用户名或密码错误");
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        // TODO: 实现登出逻辑 (清除token等)
        return Result.success(null);
    }
}
