package com.ruralsync.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@TableName("social_posts")
public class SocialPost {
    @TableId(type = IdType.AUTO)
    private Long id;

    private Long userId;
    private String content;
    private String images; // JSON array string

    private Integer likeCount;
    private Integer commentCount;
    private LocalDateTime createTime;

    @TableField(exist = false)
    private String userName;
    @TableField(exist = false)
    private String userAvatar;
    @TableField(exist = false)
    private Boolean liked;
    @TableField(exist = false)
    private List<SocialComment> comments;

    @TableField(exist = false)
    private List<String> imageUrls;
}
