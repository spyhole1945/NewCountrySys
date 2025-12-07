package com.ruralsync.sys.controller;

import com.ruralsync.sys.common.Result;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/points")
public class PointsController {

    /**
     * 获取积分记录
     */
    @GetMapping("/log")
    public Result<Map<String, Object>> getPointsLog(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "20") Integer size) {

        // TODO: 实现真实的数据库查询
        // 暂时返回mock数据
        List<Map<String, Object>> logs = new ArrayList<>();

        String[] titles = { "发布集市信息", "发布动态", "点赞互动", "评论互动", "问题上报" };
        String[] descriptions = {
                "在线上集市发布了一条信息",
                "在村友圈发布了一条动态",
                "为村友圈动态点赞",
                "在村友圈发表了评论",
                "通过随手拍上报了问题"
        };
        int[] points = { 5, 3, 1, 2, 5 };

        for (int i = 0; i < size; i++) {
            int index = i % titles.length;
            Map<String, Object> log = new HashMap<>();
            log.put("id", (long) (page * size + i));
            log.put("title", titles[index]);
            log.put("description", descriptions[index]);
            log.put("points", points[index]);
            log.put("createTime", "2025-12-0" + (7 - i % 7) + " " + (10 + i % 12) + ":00:00");

            logs.add(log);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("records", logs);
        result.put("total", 100);
        result.put("page", page);
        result.put("size", size);

        return Result.success(result);
    }
}
