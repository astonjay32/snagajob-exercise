package com.astonlawrence.domain;

import com.astonlawrence.service.JobApplicationQualifier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.springframework.util.Assert.notEmpty;

@SpringBootTest
@RunWith(SpringRunner.class)
public class JobApplicationQualifierTest {

    @Autowired
    private JobApplicationQualifier subject;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testAutoWire(){
        assertThat(subject, notNullValue());
        assertThat(subject.getRequiredQuestions(), is(not(empty())));
    }

    @Test
    public void testIsQualified_withQualifiedApp_passes() throws IOException {
        List<Question> appQuestions = passingApp().getQuestions();
        assertThat(subject.isQualified(appQuestions), is(true));
    }

    @Test
    public void testIsQualified_withMissingQuestion_fails(){
        List<Question> emptyQuestionList = new ArrayList<>();
        assertThat(subject.isQualified(emptyQuestionList), is(false));
    }

    @Test
    public void testIsQualified_withIncorrectQuestion_fails() throws IOException {
        JobApplication application = passingApp();
        List<Question> appQuestions = application.getQuestions();
        appQuestions.get(0).setAnswer("WRONG ANSWER!!!");
        application.setQuestions(appQuestions);
        assertThat(subject.isQualified(application.getQuestions()), is(false));
    }

    private JobApplication passingApp() throws JsonProcessingException {
        JobApplication jobApplication = new JobApplication();
        jobApplication.setName("Test Application");
        ArrayList<Question> appQuestions = new ArrayList();
        for(Question question : subject.getRequiredQuestions()){
            appQuestions.add(new Question(question.getId(), question.getAnswer()));
        }
        jobApplication.setQuestions(appQuestions);
        return jobApplication;
    }


}
