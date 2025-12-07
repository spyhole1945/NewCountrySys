package com.ruralsync.sys.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ruralsync.sys.entity.QaAnswer;
import com.ruralsync.sys.entity.QaQuestion;
import com.ruralsync.sys.entity.User;
import com.ruralsync.sys.mapper.QaAnswerMapper;
import com.ruralsync.sys.mapper.QaQuestionMapper;
import com.ruralsync.sys.mapper.UserMapper;
import com.ruralsync.sys.service.QaService;
import com.ruralsync.sys.service.PointsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class QaServiceImpl extends ServiceImpl<QaQuestionMapper, QaQuestion> implements QaService {

    @Autowired
    private QaAnswerMapper qaAnswerMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private PointsService pointsService;

    @Override
    public Page<QaQuestion> getQuestionList(Integer page, Integer size) {
        Page<QaQuestion> pageParam = new Page<>(page, size);
        Page<QaQuestion> result = baseMapper.selectPage(pageParam,
                new QueryWrapper<QaQuestion>().orderByDesc("create_time"));

        // Populate author info
        result.getRecords().forEach(q -> {
            User user = userMapper.selectById(q.getUserId());
            if (user != null) {
                q.setAuthorName(user.getUsername());
                // q.setAuthorAvatar(user.getAvatar()); // Assuming User has avatar
            }
        });

        return result;
    }

    @Override
    public Map<String, Object> getQuestionDetail(Long id) {
        QaQuestion question = baseMapper.selectById(id);
        if (question == null) {
            throw new RuntimeException("Question not found");
        }

        // Update view count
        question.setViewCount(question.getViewCount() + 1);
        baseMapper.updateById(question);

        // Populate author info
        User user = userMapper.selectById(question.getUserId());
        if (user != null) {
            question.setAuthorName(user.getUsername());
        }

        // Get answers
        List<QaAnswer> answers = qaAnswerMapper.selectList(new QueryWrapper<QaAnswer>()
                .eq("question_id", id)
                .orderByDesc("accepted") // Accepted answers first
                .orderByDesc("create_time"));

        answers.forEach(a -> {
            User u = userMapper.selectById(a.getUserId());
            if (u != null) {
                a.setAuthorName(u.getUsername());
            }
        });

        Map<String, Object> result = new HashMap<>();
        result.put("question", question);
        result.put("answers", answers);

        return result;
    }

    @Override
    @Transactional
    public QaQuestion createQuestion(QaQuestion question) {
        question.setCreateTime(LocalDateTime.now());
        question.setViewCount(0);
        question.setAnswerCount(0);
        question.setSolved(false);
        save(question);
        return question;
    }

    @Override
    @Transactional
    public QaAnswer createAnswer(QaAnswer answer) {
        answer.setCreateTime(LocalDateTime.now());
        answer.setAccepted(false);
        answer.setLikes(0);
        qaAnswerMapper.insert(answer);

        // Update question answer count
        QaQuestion question = baseMapper.selectById(answer.getQuestionId());
        if (question != null) {
            question.setAnswerCount(question.getAnswerCount() + 1);
            baseMapper.updateById(question);
        }

        // Award points for answering
        pointsService.addPoints(answer.getUserId(), 2, "ANSWER_QUESTION");

        return answer;
    }

    @Override
    @Transactional
    public void acceptAnswer(Long answerId) {
        QaAnswer answer = qaAnswerMapper.selectById(answerId);
        if (answer == null) {
            throw new RuntimeException("Answer not found");
        }

        if (answer.getAccepted()) {
            return; // Already accepted
        }

        // Mark answer as accepted
        answer.setAccepted(true);
        qaAnswerMapper.updateById(answer);

        // Mark question as solved
        QaQuestion question = baseMapper.selectById(answer.getQuestionId());
        if (question != null) {
            question.setSolved(true);
            baseMapper.updateById(question);
        }

        // Award points for accepted answer
        pointsService.addPoints(answer.getUserId(), 5, "ANSWER_ACCEPTED");
    }
}
