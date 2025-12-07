package com.ruralsync.sys.controller;

import com.ruralsync.sys.common.Result;
import com.ruralsync.sys.entity.CommunityPost;
import com.ruralsync.sys.service.CommunityPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/community-posts")
public class CommunityPostController {

    @Autowired
    private CommunityPostService communityPostService;

    /**
     * 获取社区动态列表(支持分页)
     */
    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        // TODO: 实现真实的数据库查询
        // 暂时返回mock数据
        List<Map<String, Object>> posts = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Map<String, Object> post = new HashMap<>();
            post.put("id", (long) (page * size + i));
            post.put("userId", 1L);
            post.put("userName", "村民" + i);
            post.put("content", "这是一条社区动态" + i);
            post.put("mediaUrls", Arrays.asList("https://mock-cdn.com/img" + i + ".jpg"));
            post.put("likes", 10 + i);
            post.put("commentsCount", 5 + i);
            post.put("createTime", "2025-12-07 10:00:00");

            posts.add(post);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("records", posts);
        result.put("total", 50);

        return Result.success(result);
    }

    /**
     * 发布社区动态
     */
    @PostMapping
    public Result<Map<String, Object>> save(@RequestBody CommunityPost communityPost) {
        // TODO: 实现真实的数据库插入和积分奖励
        boolean success = communityPostService.save(communityPost);

        if (success) {
            Map<String, Object> result = new HashMap<>();
            result.put("id", communityPost.getId());
            result.put("pointsAwarded", 3); // 发布动态奖励3积分
            return Result.success(result);
        }

        return Result.error("发布失败");
    }
}
