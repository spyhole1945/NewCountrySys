package com.ruralsync.sys.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageResult<T> {
    private Long total; // 总记录数
    private Integer page; // 当前页码
    private Integer size; // 每页大小
    private List<T> records; // 数据列表

    public static <T> PageResult<T> of(Long total, Integer page, Integer size, List<T> records) {
        return new PageResult<>(total, page, size, records);
    }
}
