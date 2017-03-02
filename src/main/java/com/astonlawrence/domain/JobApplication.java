package com.astonlawrence.domain;


import com.astonlawrence.service.JobApplicationQualifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Entity
public class JobApplication {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private String id;
    private String name;
    private String questionsJson;

    @Transient
    private List<Answer> questions;

    @Transient
    private ObjectMapper objectMapper = new ObjectMapper();

    @Transient
    private final Logger LOGGER = LoggerFactory.getLogger(JobApplicationQualifier.class);

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getQuestionsJson() {
        return questionsJson;
    }

    public void setQuestionsJson(String questionsJson) {
        this.questionsJson = questionsJson;
    }

    public List<Answer> getQuestions(){
        if(null == questions){
           setQuestionsFromJson();
        }
        return questions;
    }

    private void setQuestionsFromJson(){
        try{
            questions = Arrays.asList(objectMapper.readValue(questionsJson, Answer[].class));
        } catch (IOException e) {
            LOGGER.warn("Unable to create list of Questions from JSON: " + questionsJson);
        }
    }


    public void setQuestions(List<Answer> questions) {
        this.questions = questions;
        if(null != questions){
            try{
                questionsJson = objectMapper.writeValueAsString(questions);
            } catch (JsonProcessingException e) {
                LOGGER.warn("Unable to parse question list into JSON");
            }
        }

    }
}
