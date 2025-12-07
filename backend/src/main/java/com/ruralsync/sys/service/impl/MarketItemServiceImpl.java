package com.ruralsync.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruralsync.sys.entity.MarketItem;
import com.ruralsync.sys.mapper.MarketItemMapper;
import com.ruralsync.sys.service.MarketItemService;
import org.springframework.stereotype.Service;

@Service
public class MarketItemServiceImpl extends ServiceImpl<MarketItemMapper, MarketItem> implements MarketItemService {
}
