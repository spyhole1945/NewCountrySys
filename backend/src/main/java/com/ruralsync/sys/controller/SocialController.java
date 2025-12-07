package com.ruralsync.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruralsync.sys.common.Result;
import com.ruralsync.sys.entity.SocialComment;
import com.ruralsync.sys.entity.SocialPost;
import com.ruralsync.sys.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/social")
public class SocialController {

    @Autowired
    private SocialService socialService;

    /**
     * 获取村友圈动态列表
     */
    @GetMapping("/posts")
    public Result<Page<SocialPost>> getPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(socialService.getPostList(page, size));
    }

    /**
     * 发布动态
     */
    @PostMapping("/posts")
    public Result<Map<String, Object>> createPost(@RequestBody SocialPost post) {
        // Mock user ID for now
        post.setUserId(1L);

        socialService.createPost(post);

        Map<String, Object> result = new HashMap<>();
        result.put("id", post.getId());
        result.put("pointsAwarded", 3); // 发布动态奖励3积分

        return Result.success(result);
    }

    /**
     * 点赞动态
     */
    @PostMapping("/posts/{postId}/like")
    public Result<Map<String, Object>> likePost(@PathVariable Long postId) {
        // Mock user ID for now
        Long userId = 1L;

        int result = socialService.likePost(postId, userId);

        Map<String, Object> map = new HashMap<>();
        if (result > 0) {
            map.put("pointsAwarded", 1); // 点赞奖励1积分
        } else {
            map.put("pointsAwarded", 0);
        }

        return Result.success(map);
    }

    /**
     * 取消点赞
     */
    @DeleteMapping("/posts/{postId}/like")
    public Result<Void> unlikePost(@PathVariable Long postId) {
        // Mock user ID for now
        Long userId = 1L;

        socialService.unlikePost(postId, userId);

        return Result.success(null);
    }

    /**
     * 评论动态
     */
    @PostMapping("/posts/{postId}/comments")
    public Result<Map<String, Object>> commentPost(
            @PathVariable Long postId,
            @RequestBody SocialComment comment) {
        // Mock user ID for now
        comment.setUserId(1L);
        comment.setPostId(postId);

        socialService.commentPost(comment);

        Map<String, Object> result = new HashMap<>();
        result.put("pointsAwarded", 2); // 评论奖励2积分

        return Result.success(result);
    }
}
