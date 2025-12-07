package com.ruralsync.sys.controller;

import com.ruralsync.sys.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    /**
     * 获取用户信息(包含积分)
     */
    @GetMapping("/info")
    public Result<Map<String, Object>> getUserInfo() {
        // TODO: 从SecurityContext获取当前用户ID,查询数据库
        // 暂时返回mock数据

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("id", 1L);
        userInfo.put("nickname", "张三");
        userInfo.put("phone", "138****1234");
        userInfo.put("avatar", "https://mock-cdn.com/avatar.jpg");
        userInfo.put("points", 128); // 当前积分
        userInfo.put("role", "VILLAGER");

        return Result.success(userInfo);
    }
}
