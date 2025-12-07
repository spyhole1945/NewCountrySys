package com.ruralsync.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruralsync.sys.entity.MarketItem;
import com.ruralsync.sys.service.MarketItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/market-items")
public class MarketItemController {

    @Autowired
    private MarketItemService marketItemService;

    @GetMapping
    public List<MarketItem> list() {
        return marketItemService.list(new QueryWrapper<MarketItem>().orderByDesc("create_time"));
    }

    @PostMapping
    public boolean save(@RequestBody MarketItem marketItem) {
        return marketItemService.save(marketItem);
    }
}
