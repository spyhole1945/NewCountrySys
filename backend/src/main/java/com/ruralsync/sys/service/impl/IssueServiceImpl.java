package com.ruralsync.sys.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ruralsync.sys.dto.IssueDTO;
import com.ruralsync.sys.dto.IssueTimelineDTO;
import com.ruralsync.sys.entity.Issue;
import com.ruralsync.sys.entity.IssueLog;
import com.ruralsync.sys.entity.PointsRecord;
import com.ruralsync.sys.entity.User;
import com.ruralsync.sys.mapper.IssueLogMapper;
import com.ruralsync.sys.mapper.IssueMapper;
import com.ruralsync.sys.mapper.PointsRecordMapper;
import com.ruralsync.sys.mapper.UserMapper;
import com.ruralsync.sys.service.IssueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class IssueServiceImpl extends ServiceImpl<IssueMapper, Issue> implements IssueService {

    @Autowired
    private IssueMapper issueMapper;

    @Autowired
    private IssueLogMapper issueLogMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointsRecordMapper pointsRecordMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void reportIssue(IssueDTO issueDTO, Long userId) {
        // 1. Save Issue
        Issue issue = new Issue();
        issue.setReporterId(userId);
        issue.setType(issueDTO.getType());
        issue.setContent(issueDTO.getContent());
        issue.setStatus("PENDING");
        issue.setCreateTime(LocalDateTime.now());
        issue.setUpdateTime(LocalDateTime.now());

        try {
            issue.setMediaUrls(objectMapper.writeValueAsString(issueDTO.getMediaUrls()));
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error processing media URLs", e);
        }

        issueMapper.insert(issue);

        // 2. Create Initial Log
        IssueLog log = new IssueLog();
        log.setIssueId(issue.getId());
        log.setOperatorId(userId); // The reporter is the operator for the initial submission
        log.setAction("SUBMIT");
        log.setComment("Issue reported by user");
        log.setCreateTime(LocalDateTime.now());
        issueLogMapper.insert(log);

        // 3. Award Points (Gamification)
        int pointsAwarded = 10; // Fixed points for reporting
        User user = userMapper.selectById(userId);
        if (user != null) {
            user.setPoints(user.getPoints() + pointsAwarded);
            userMapper.updateById(user);

            PointsRecord record = new PointsRecord();
            record.setUserId(userId);
            record.setChangeAmount(pointsAwarded);
            record.setReason("REPORT_ISSUE");
            record.setCreateTime(LocalDateTime.now());
            pointsRecordMapper.insert(record);
        }
    }

    @Override
    public List<IssueTimelineDTO> getIssueTimeline(Long issueId) {
        // Simple implementation: fetch logs and map to DTO
        // In a real app, we might join with User table to get operator names
        List<IssueLog> logs = issueLogMapper
                .selectList(new com.baomidou.mybatisplus.core.conditions.query.QueryWrapper<IssueLog>()
                        .eq("issue_id", issueId).orderByAsc("create_time"));

        List<IssueTimelineDTO> timeline = new ArrayList<>();
        for (IssueLog log : logs) {
            IssueTimelineDTO dto = new IssueTimelineDTO();
            dto.setAction(log.getAction());
            dto.setComment(log.getComment());
            dto.setTime(log.getCreateTime());
            dto.setStatus("UNKNOWN"); // Status might be derived from action or stored in log if needed

            // Fetch operator name (simplified)
            if (log.getOperatorId() != null) {
                User operator = userMapper.selectById(log.getOperatorId());
                dto.setOperatorName(operator != null ? operator.getUsername() : "Unknown");
            }

            timeline.add(dto);
        }
        return timeline;
    }
}
