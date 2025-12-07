package com.ruralsync.sys.controller;

import com.ruralsync.sys.common.PageResult;
import com.ruralsync.sys.common.Result;
import com.ruralsync.sys.dto.ContentListDTO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/admin/content")
public class AdminContentController {

    @GetMapping("/posts")
    public Result<PageResult<ContentListDTO>> getCommunityPosts(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        // TODO: 实现真实的社区动态查询
        List<ContentListDTO> records = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            ContentListDTO dto = new ContentListDTO();
            dto.setId((long) i);
            dto.setType("COMMUNITY_POST");
            dto.setUserId((long) (i + 10));
            dto.setUserName("用户" + i);
            dto.setContent("这是一条社区动态内容" + i);
            dto.setMediaUrls(Arrays.asList("https://example.com/post" + i + ".jpg"));
            dto.setStatus(i % 3 == 0 ? "APPROVED" : "PENDING_REVIEW");
            dto.setCreateTime(LocalDateTime.now().minusHours(i));
            records.add(dto);
        }

        PageResult<ContentListDTO> pageResult = PageResult.of(30L, page, size, records);
        return Result.success(pageResult);
    }

    @GetMapping("/market-items")
    public Result<PageResult<ContentListDTO>> getMarketItems(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        // TODO: 实现真实的集市商品查询
        List<ContentListDTO> records = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            ContentListDTO dto = new ContentListDTO();
            dto.setId((long) i);
            dto.setType("MARKET_ITEM");
            dto.setUserId((long) (i + 20));
            dto.setUserName("商家" + i);
            dto.setContent("出售新鲜蔬菜" + i);
            dto.setMediaUrls(Arrays.asList("https://example.com/market" + i + ".jpg"));
            dto.setStatus(i % 3 == 0 ? "APPROVED" : "PENDING_REVIEW");
            dto.setCreateTime(LocalDateTime.now().minusHours(i * 2));
            records.add(dto);
        }

        PageResult<ContentListDTO> pageResult = PageResult.of(25L, page, size, records);
        return Result.success(pageResult);
    }

    @PutMapping("/{type}/{id}/approve")
    public Result<Void> approveContent(
            @PathVariable String type,
            @PathVariable Long id) {

        // TODO: 实现真实的内容审核通过逻辑
        System.out.println("审核通过: " + type + " ID: " + id);

        return Result.success(null);
    }

    @DeleteMapping("/{type}/{id}")
    public Result<Void> deleteContent(
            @PathVariable String type,
            @PathVariable Long id) {

        // TODO: 实现真实的内容删除逻辑
        System.out.println("删除内容: " + type + " ID: " + id);

        return Result.success(null);
    }
}
