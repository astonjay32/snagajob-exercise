package com.astonlawrence.service;

import com.astonlawrence.domain.Answer;
import com.astonlawrence.domain.Question;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JobApplicationQualifier {

    private final Logger LOGGER = LoggerFactory.getLogger(JobApplicationQualifier.class);
    private final String WRONG_ANSWER_LOG_MESSAGE = "Application does not qualify because question %s did not have the required answer %s";
    private final String MISSING_ANSWER_LOG_MESSAGE = "Application does not qualify because question %s was missing.";
    private final String QUALIFIED_LOG_MESSAGE = "Application qualifies";

    public Boolean isQualified(List<Question> requiredAnswers, List<Answer> applicationAnswers) {
        for (Question requiredAnswer : requiredAnswers) {
            if (!hasCorrectAnswer(requiredAnswer, applicationAnswers)) {
                LOGGER.info(String.format(WRONG_ANSWER_LOG_MESSAGE, requiredAnswer.getId(), requiredAnswer.getAnswer()));
                return false;
            }
        }
        LOGGER.info(QUALIFIED_LOG_MESSAGE);
        return true;
    }

    private Boolean hasCorrectAnswer(Question requiredAnswer, List<Answer> appAnswers) {
        for (Answer appAnswer : appAnswers) {
            if (appAnswer.getId().equals(requiredAnswer.getId())) {
                return appAnswer.getAnswer().equals(requiredAnswer.getAnswer());
            }
        }
        LOGGER.info(String.format(MISSING_ANSWER_LOG_MESSAGE, requiredAnswer.getId()));
        return false;
    }
}
