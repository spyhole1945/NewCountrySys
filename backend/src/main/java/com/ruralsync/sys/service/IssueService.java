package com.ruralsync.sys.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.ruralsync.sys.dto.IssueDTO;
import com.ruralsync.sys.dto.IssueTimelineDTO;
import com.ruralsync.sys.entity.Issue;
import java.util.List;

public interface IssueService extends IService<Issue> {
    void reportIssue(IssueDTO issueDTO, Long userId);

    List<IssueTimelineDTO> getIssueTimeline(Long issueId);
}
