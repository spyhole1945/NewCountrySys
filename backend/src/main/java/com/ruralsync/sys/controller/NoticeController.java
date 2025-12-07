package com.ruralsync.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruralsync.sys.entity.Notice;
import com.ruralsync.sys.service.NoticeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notices")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;

    @GetMapping
    public List<Notice> list() {
        return noticeService.list(new QueryWrapper<Notice>().orderByDesc("publish_time"));
    }

    @PostMapping
    public boolean save(@RequestBody Notice notice) {
        return noticeService.save(notice);
    }
}
