package com.astonlawrence.service;

import com.astonlawrence.domain.Question;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@Service
public class QuestionService {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionService.class);
    private List<Question> applicationQuestions;
    public static final String APPLICATION_QUESTIONS_FILE = "/application-questions.json";

    @PostConstruct
    public void init(){
        applicationQuestions = loadFromClassPath(APPLICATION_QUESTIONS_FILE);
    }

    public List<Question> getApplicationQuestions(){
        return applicationQuestions;
    }

    private List<Question> loadFromClassPath(String fileName) {
        LOGGER.debug("Loading questions from " + fileName);
        try {
            InputStream in = getClass().getResourceAsStream(APPLICATION_QUESTIONS_FILE);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String questionJSON = org.apache.commons.io.IOUtils.toString(reader);
            ObjectMapper objectMapper = new ObjectMapper();
            return Arrays.asList(objectMapper.readValue(questionJSON, Question[].class));
        } catch (IOException e) {
            throw new RuntimeException("Unable to load questions from file " + fileName);
        }
    }

}
