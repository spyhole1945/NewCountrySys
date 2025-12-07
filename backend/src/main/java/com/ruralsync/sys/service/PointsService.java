package com.ruralsync.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruralsync.sys.entity.PointsRecord;

public interface PointsService extends IService<PointsRecord> {
    void addPoints(Long userId, Integer amount, String reason);
}
