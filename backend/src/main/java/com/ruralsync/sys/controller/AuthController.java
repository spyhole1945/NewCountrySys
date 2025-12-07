package com.ruralsync.sys.controller;

import com.ruralsync.sys.common.Result;
import com.ruralsync.sys.dto.LoginDTO;
import com.ruralsync.sys.dto.LoginResponseDTO;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@RequestBody Map<String, String> loginData) {
        String username = loginData.get("username");
        String password = loginData.get("password");

        // TODO: 实际项目中应该查询数据库验证用户
        // 这里使用mock数据
        if ("admin".equals(username) && "admin123".equals(password)) {
            // 生成token (mock)
            String token = "mock-jwt-token-" + System.currentTimeMillis();

            // 用户信息
            Map<String, Object> userInfo = new HashMap<>();
            userInfo.put("id", 1L);
            userInfo.put("username", "admin");
            userInfo.put("nickname", "系统管理员");
            userInfo.put("role", "ADMIN");

            Map<String, Object> result = new HashMap<>();
            result.put("token", token);
            result.put("userInfo", userInfo);

            return Result.success(result);
        }

        // 模拟OFFICIAL角色登录尝试
        if ("official".equals(username) && "official123".equals(password)) {
            return Result.error("村务人员请使用微信小程序登录");
        }

        // 模拟VILLAGER角色登录尝试
        if ("villager".equals(username)) {
            return Result.error("村民请使用微信小程序登录");
        }

        return Result.error("用户名或密码错误");
    }

    @PostMapping("/logout")
    public Result<Void> logout() {
        // TODO: 实现登出逻辑 (清除token等)
        return Result.success(null);
    }
}
