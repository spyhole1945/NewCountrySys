package com.ruralsync.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruralsync.sys.entity.PointsRecord;
import com.ruralsync.sys.entity.User;
import com.ruralsync.sys.mapper.PointsRecordMapper;
import com.ruralsync.sys.mapper.UserMapper;
import com.ruralsync.sys.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class PointsServiceImpl extends ServiceImpl<PointsRecordMapper, PointsRecord> implements PointsService {

    @Autowired
    private UserMapper userMapper;

    @Override
    @Transactional
    public void addPoints(Long userId, Integer amount, String reason) {
        // 1. Update user points
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setPoints(user.getPoints() + amount);
            userMapper.updateById(user);
        }

        // 2. Create points record
        PointsRecord record = new PointsRecord();
        record.setUserId(userId);
        record.setChangeAmount(amount);
        record.setReason(reason);
        record.setCreateTime(LocalDateTime.now());
        baseMapper.insert(record);
    }
}
