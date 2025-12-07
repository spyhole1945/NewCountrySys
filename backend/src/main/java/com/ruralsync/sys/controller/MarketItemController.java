package com.ruralsync.sys.controller;

import com.ruralsync.sys.common.Result;
import com.ruralsync.sys.entity.MarketItem;
import com.ruralsync.sys.service.MarketItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/market-items")
public class MarketItemController {

    @Autowired
    private MarketItemService marketItemService;

    /**
     * 获取集市列表(支持分类和分页)
     */
    @GetMapping
    public Result<Map<String, Object>> list(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {

        // TODO: 实现真实的数据库查询和分类筛选
        // 暂时返回mock数据
        List<Map<String, Object>> items = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("id", (long) (page * size + i));
            item.put("userId", 1L);
            item.put("userName", "村民" + i);
            item.put("userAvatar", "https://mock-cdn.com/avatar" + i + ".jpg");
            item.put("title", "出租拖拉机" + i);
            item.put("description", "九成新拖拉机,价格实惠");
            item.put("price", 100.0 + i * 10);
            item.put("category", category != null ? category : "machinery");
            item.put("phone", "138****" + String.format("%04d", i));
            item.put("images", Arrays.asList("https://mock-cdn.com/img" + i + ".jpg"));
            item.put("createTime", "2025-12-07 10:00:00");

            items.add(item);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("records", items);
        result.put("total", 50);
        result.put("page", page);
        result.put("size", size);

        return Result.success(result);
    }

    /**
     * 发布集市信息
     */
    @PostMapping
    public Result<Map<String, Object>> save(@RequestBody MarketItem marketItem) {
        // TODO: 实现真实的数据库插入和积分奖励
        boolean success = marketItemService.save(marketItem);

        if (success) {
            Map<String, Object> result = new HashMap<>();
            result.put("id", marketItem.getId());
            result.put("pointsAwarded", 5); // 发布集市信息奖励5积分
            return Result.success(result);
        }

        return Result.error("发布失败");
    }
}
