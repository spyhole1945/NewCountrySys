package com.ruralsync.sys.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

@Data
@TableName("officials")
public class Official {
    @TableId(type = IdType.AUTO)
    private Long id;
    private String name;
    private String position;
    private String phone;
    private String photoUrl;
    private Integer displayOrder;
}
