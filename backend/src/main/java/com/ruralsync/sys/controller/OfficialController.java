package com.ruralsync.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruralsync.sys.entity.Official;
import com.ruralsync.sys.service.OfficialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/officials")
public class OfficialController {

    @Autowired
    private OfficialService officialService;

    @GetMapping
    public List<Official> list() {
        return officialService.list(new QueryWrapper<Official>().orderByAsc("display_order"));
    }

    @PostMapping
    public boolean save(@RequestBody Official official) {
        return officialService.save(official);
    }
}
