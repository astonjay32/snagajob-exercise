package com.astonlawrence.service;

import com.astonlawrence.domain.Question;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

@Service
public class QuestionLoader {

    private static final Logger LOGGER = LoggerFactory.getLogger(QuestionLoader.class);

    public List<Question> loadFromClassPath(String fileName) {

        File file = null;
        try {
            file = new File(getClass().getResource(fileName).toURI());
            ObjectMapper objectMapper = new ObjectMapper();
            return Arrays.asList(objectMapper.readValue(file, Question[].class));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException("Unable to load questions from file " + fileName);
        }
    }

}
