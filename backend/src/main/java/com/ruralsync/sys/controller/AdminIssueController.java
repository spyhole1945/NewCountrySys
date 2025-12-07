package com.ruralsync.sys.controller;

import com.ruralsync.sys.common.PageResult;
import com.ruralsync.sys.common.Result;
import com.ruralsync.sys.dto.IssueListDTO;
import com.ruralsync.sys.dto.IssueUpdateDTO;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/admin/issues")
public class AdminIssueController {

    @GetMapping
    public Result<PageResult<IssueListDTO>> getIssueList(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        // TODO: 实现真实的分页查询逻辑
        // 临时mock数据
        List<IssueListDTO> records = new ArrayList<>();
        for (int i = 1; i <= size; i++) {
            IssueListDTO dto = new IssueListDTO();
            dto.setId((long) i);
            dto.setReporterName("村民" + i);
            dto.setType(i % 2 == 0 ? "道路损坏" : "垃圾堆积");
            dto.setContent("这是问题描述内容" + i);
            dto.setStatus(i % 3 == 0 ? "DONE" : (i % 2 == 0 ? "PROCESSING" : "PENDING"));
            dto.setCreateTime(LocalDateTime.now().minusDays(i));
            dto.setMediaUrls(Arrays.asList("https://example.com/image" + i + ".jpg"));
            dto.setLocation("XX村XX路");
            records.add(dto);
        }

        PageResult<IssueListDTO> pageResult = PageResult.of(100L, page, size, records);
        return Result.success(pageResult);
    }

    @GetMapping("/{id}")
    public Result<IssueListDTO> getIssueDetail(@PathVariable Long id) {
        // TODO: 实现真实的详情查询
        IssueListDTO dto = new IssueListDTO();
        dto.setId(id);
        dto.setReporterName("张三");
        dto.setType("道路损坏");
        dto.setContent("村口主干道出现大坑,影响通行");
        dto.setStatus("PENDING");
        dto.setCreateTime(LocalDateTime.now().minusDays(1));
        dto.setMediaUrls(Arrays.asList(
                "https://example.com/image1.jpg",
                "https://example.com/image2.jpg"));
        dto.setLocation("XX村村口主干道");

        return Result.success(dto);
    }

    @PutMapping("/{id}/status")
    public Result<Void> updateIssueStatus(
            @PathVariable Long id,
            @RequestBody IssueUpdateDTO updateDTO) {

        // TODO: 实现真实的状态更新逻辑
        // 1. 验证状态值
        // 2. 更新数据库
        // 3. 记录操作日志

        System.out.println("更新问题 " + id + " 状态为: " + updateDTO.getStatus());
        System.out.println("处理意见: " + updateDTO.getProcessNotes());

        return Result.success(null);
    }

    @GetMapping("/stats")
    public Result<Map<String, Object>> getIssueStats() {
        // TODO: 实现真实的统计查询
        Map<String, Object> stats = new HashMap<>();
        stats.put("totalIssues", 156);
        stats.put("pendingIssues", 45);
        stats.put("processingIssues", 38);
        stats.put("doneIssues", 73);

        // 月度趋势数据 (用于折线图)
        List<Map<String, Object>> monthlyTrend = new ArrayList<>();
        String[] months = { "1月", "2月", "3月", "4月", "5月", "6月" };
        int[] counts = { 12, 18, 25, 30, 35, 36 };
        for (int i = 0; i < months.length; i++) {
            Map<String, Object> point = new HashMap<>();
            point.put("month", months[i]);
            point.put("count", counts[i]);
            monthlyTrend.add(point);
        }
        stats.put("monthlyTrend", monthlyTrend);

        // 状态分布 (用于饼图)
        List<Map<String, Object>> statusDistribution = new ArrayList<>();
        statusDistribution.add(Map.of("name", "待处理", "value", 45));
        statusDistribution.add(Map.of("name", "处理中", "value", 38));
        statusDistribution.add(Map.of("name", "已完成", "value", 73));
        stats.put("statusDistribution", statusDistribution);

        return Result.success(stats);
    }
}
