package com.astonlawrence.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class QuestionServiceTest {

    @Autowired
    private QuestionService subject;

    @Test
    public void verifyApplicationQuestions_getLoadedFromDisk(){
        assertThat(subject.getApplicationQuestions(), notNullValue());
        assertThat(subject.getApplicationQuestions().size(), greaterThan(0) );
    }
}
