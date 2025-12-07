package com.ruralsync.sys.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.ruralsync.sys.entity.QaAnswer;
import com.ruralsync.sys.entity.QaQuestion;

import java.util.Map;

public interface QaService extends IService<QaQuestion> {
    Page<QaQuestion> getQuestionList(Integer page, Integer size);

    Map<String, Object> getQuestionDetail(Long id);

    QaQuestion createQuestion(QaQuestion question);

    QaAnswer createAnswer(QaAnswer answer);

    void acceptAnswer(Long answerId);
}
