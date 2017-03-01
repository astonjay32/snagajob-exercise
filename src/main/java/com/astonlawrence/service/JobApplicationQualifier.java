package com.astonlawrence.service;

import com.astonlawrence.domain.Question;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class JobApplicationQualifier {

    @Autowired
    private QuestionLoader questionLoader;

    public static final String QUESTIONS_FILE = "/required-questions.json";

    private final Logger LOGGER = LoggerFactory.getLogger(JobApplicationQualifier.class);
    private final String WRONG_ANSWER_LOG_MESSAGE = "Application does not qualify because question %s was %s, when it neeeded to be %s";
    private final String MISSING_ANSWER_LOG_MESSAGE = "Application does not qualify because question %s was missing.";
    private final String QUALIFIED_LOG_MESSAGE = "Application qualifies";
    private List<Question> requiredQuestions = new ArrayList<>();

    @PostConstruct
    public void init(){
        requiredQuestions = questionLoader.loadFromClassPath(QUESTIONS_FILE);
    }

    public List<Question> getRequiredQuestions(){
        return requiredQuestions;
    }

    public Boolean isQualified(List<Question> applicationQuestions){
        for(Question requiredQuestion : requiredQuestions){
            if(!hasCorrectAnswer(requiredQuestion, applicationQuestions)){
                return false;
            }
        }
        LOGGER.info(QUALIFIED_LOG_MESSAGE);
        return true;
    }

    private Boolean hasCorrectAnswer(Question requiredQuestion, List<Question> appQuestions){
        for(Question appQuestion : appQuestions){
            if(appQuestion.getId().equals(requiredQuestion.getId())) {
                if (appQuestion.getAnswer().equals(requiredQuestion.getAnswer())) {
                    return true;
                } else {
                    LOGGER.info(String.format(WRONG_ANSWER_LOG_MESSAGE, requiredQuestion.getId(), appQuestion.getAnswer(), requiredQuestion.getAnswer()));
                    return false;
                }
            }

        }
        LOGGER.info(String.format(MISSING_ANSWER_LOG_MESSAGE, requiredQuestion.getId()));
        return false;
    }



}
