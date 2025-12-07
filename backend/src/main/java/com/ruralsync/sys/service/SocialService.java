package com.ruralsync.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruralsync.sys.entity.SocialComment;
import com.ruralsync.sys.entity.SocialPost;

public interface SocialService extends IService<SocialPost> {
    Page<SocialPost> getPostList(Integer page, Integer size);

    SocialPost createPost(SocialPost post);

    int likePost(Long postId, Long userId);

    void unlikePost(Long postId, Long userId);

    int commentPost(SocialComment comment);
}
