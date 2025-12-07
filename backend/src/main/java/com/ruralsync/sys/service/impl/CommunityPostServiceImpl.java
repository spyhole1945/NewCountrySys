package com.ruralsync.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruralsync.sys.entity.CommunityPost;
import com.ruralsync.sys.mapper.CommunityPostMapper;
import com.ruralsync.sys.service.CommunityPostService;
import org.springframework.stereotype.Service;

@Service
public class CommunityPostServiceImpl extends ServiceImpl<CommunityPostMapper, CommunityPost>
        implements CommunityPostService {
}
