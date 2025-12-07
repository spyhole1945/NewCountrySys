package com.ruralsync.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruralsync.sys.entity.SocialComment;
import com.ruralsync.sys.entity.SocialLike;
import com.ruralsync.sys.entity.SocialPost;
import com.ruralsync.sys.entity.User;
import com.ruralsync.sys.mapper.SocialCommentMapper;
import com.ruralsync.sys.mapper.SocialLikeMapper;
import com.ruralsync.sys.mapper.SocialPostMapper;
import com.ruralsync.sys.mapper.UserMapper;
import com.ruralsync.sys.service.PointsService;
import com.ruralsync.sys.service.SocialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SocialServiceImpl extends ServiceImpl<SocialPostMapper, SocialPost> implements SocialService {

    @Autowired
    private SocialLikeMapper socialLikeMapper;

    @Autowired
    private SocialCommentMapper socialCommentMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointsService pointsService;

    @Override
    public Page<SocialPost> getPostList(Integer page, Integer size) {
        Page<SocialPost> pageParam = new Page<>(page, size);
        Page<SocialPost> result = baseMapper.selectPage(pageParam,
                new QueryWrapper<SocialPost>().orderByDesc("create_time"));

        // Populate user info and comments for each post
        result.getRecords().forEach(post -> {
            User user = userMapper.selectById(post.getUserId());
            if (user != null) {
                post.setUserName(user.getUsername());
                // Mock avatar for now if not present
                post.setUserAvatar(
                        "https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0");
            }

            // Get comments
            List<SocialComment> comments = socialCommentMapper.selectList(new QueryWrapper<SocialComment>()
                    .eq("post_id", post.getId())
                    .orderByDesc("create_time")
                    .last("LIMIT 5")); // Show latest 5 comments

            comments.forEach(comment -> {
                User commentUser = userMapper.selectById(comment.getUserId());
                if (commentUser != null) {
                    comment.setUserName(commentUser.getUsername());
                    comment.setUserAvatar(
                            "https://mmbiz.qpic.cn/mmbiz/icTdbqWNOwNRna42FI242Lcia07jQodd2FJGIYQfG0LAJGFxM4FbnQP6yfMxBgJ0F3YRqJCJ1aPAK2dQagdusBZg/0");
                }
            });
            post.setComments(comments);

            // Check if current user liked (Mock user ID 1 for now, should be from context)
            Long currentUserId = 1L;
            Long likeCount = socialLikeMapper.selectCount(new QueryWrapper<SocialLike>()
                    .eq("post_id", post.getId())
                    .eq("user_id", currentUserId));
            post.setLiked(likeCount > 0);

            // Parse images JSON string to List
            if (post.getImages() != null && !post.getImages().isEmpty()) {
                try {
                    com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                    List<String> urls = mapper.readValue(post.getImages(),
                            new com.fasterxml.jackson.core.type.TypeReference<List<String>>() {
                            });
                    post.setImageUrls(urls);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        return result;
    }

    @Override
    @Transactional
    public SocialPost createPost(SocialPost post) {
        post.setCreateTime(LocalDateTime.now());
        post.setLikeCount(0);
        post.setCommentCount(0);
        baseMapper.insert(post);

        // Award points
        pointsService.addPoints(post.getUserId(), 3, "SOCIAL_POST");

        return post;
    }

    @Override
    @Transactional
    public int likePost(Long postId, Long userId) {
        // Check if already liked
        Long count = socialLikeMapper.selectCount(new QueryWrapper<SocialLike>()
                .eq("post_id", postId)
                .eq("user_id", userId));

        if (count > 0) {
            return 0; // Already liked
        }

        // Create like record
        SocialLike like = new SocialLike();
        like.setPostId(postId);
        like.setUserId(userId);
        like.setCreateTime(LocalDateTime.now());
        socialLikeMapper.insert(like);

        // Update post like count
        SocialPost post = baseMapper.selectById(postId);
        post.setLikeCount(post.getLikeCount() + 1);
        baseMapper.updateById(post);

        // Award points
        pointsService.addPoints(userId, 1, "SOCIAL_LIKE");

        return 1;
    }

    @Override
    @Transactional
    public void unlikePost(Long postId, Long userId) {
        socialLikeMapper.delete(new QueryWrapper<SocialLike>()
                .eq("post_id", postId)
                .eq("user_id", userId));

        // Update post like count
        SocialPost post = baseMapper.selectById(postId);
        if (post.getLikeCount() > 0) {
            post.setLikeCount(post.getLikeCount() - 1);
            baseMapper.updateById(post);
        }
    }

    @Override
    @Transactional
    public int commentPost(SocialComment comment) {
        comment.setCreateTime(LocalDateTime.now());
        socialCommentMapper.insert(comment);

        // Update post comment count
        SocialPost post = baseMapper.selectById(comment.getPostId());
        post.setCommentCount(post.getCommentCount() + 1);
        baseMapper.updateById(post);

        // Award points
        pointsService.addPoints(comment.getUserId(), 2, "SOCIAL_COMMENT");

        return 2;
    }
}
