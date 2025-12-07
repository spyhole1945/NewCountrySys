package com.ruralsync.sys.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.ruralsync.sys.entity.CommunityPost;
import com.ruralsync.sys.service.CommunityPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/community-posts")
public class CommunityPostController {

    @Autowired
    private CommunityPostService communityPostService;

    @GetMapping
    public List<CommunityPost> list() {
        return communityPostService.list(new QueryWrapper<CommunityPost>().orderByDesc("create_time"));
    }

    @PostMapping
    public boolean save(@RequestBody CommunityPost communityPost) {
        return communityPostService.save(communityPost);
    }
}
