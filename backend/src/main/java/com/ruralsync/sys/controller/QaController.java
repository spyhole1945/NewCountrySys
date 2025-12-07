package com.ruralsync.sys.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ruralsync.sys.common.Result;
import com.ruralsync.sys.entity.QaAnswer;
import com.ruralsync.sys.entity.QaQuestion;
import com.ruralsync.sys.service.QaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/qa")
public class QaController {

    @Autowired
    private QaService qaService;

    /**
     * 获取问题列表
     */
    @GetMapping("/questions")
    public Result<Page<QaQuestion>> getQuestions(
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer size) {
        return Result.success(qaService.getQuestionList(page, size));
    }

    /**
     * 获取问题详情
     */
    @GetMapping("/questions/{id}")
    public Result<Map<String, Object>> getQuestionDetail(@PathVariable Long id) {
        return Result.success(qaService.getQuestionDetail(id));
    }

    /**
     * 发布问题
     */
    @PostMapping("/questions")
    public Result<QaQuestion> createQuestion(@RequestBody QaQuestion question) {
        // TODO: Get current user ID from security context
        question.setUserId(1L); // Mock user ID
        return Result.success(qaService.createQuestion(question));
    }

    /**
     * 提交回答
     */
    @PostMapping("/questions/{id}/answers")
    public Result<Map<String, Object>> createAnswer(
            @PathVariable Long id,
            @RequestBody QaAnswer answer) {
        // TODO: Get current user ID from security context
        answer.setUserId(1L); // Mock user ID
        answer.setQuestionId(id);

        qaService.createAnswer(answer);

        Map<String, Object> result = new HashMap<>();
        result.put("pointsAwarded", 2); // 回答问题奖励2积分

        return Result.success(result);
    }

    /**
     * 采纳回答
     */
    @PostMapping("/answers/{id}/accept")
    public Result<Map<String, Object>> acceptAnswer(@PathVariable Long id) {
        qaService.acceptAnswer(id);

        Map<String, Object> result = new HashMap<>();
        result.put("pointsAwarded", 5); // 回答被采纳奖励5积分(给回答者)

        return Result.success(result);
    }
}
