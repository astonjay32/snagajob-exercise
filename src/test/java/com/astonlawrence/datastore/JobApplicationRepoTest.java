package com.astonlawrence.datastore;

import com.astonlawrence.domain.Answer;
import com.astonlawrence.domain.JobApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
@RunWith(SpringRunner.class)
public class JobApplicationRepoTest {

    @Autowired
    private JobApplicationRepo subject;

    @Test
    public void testFindOne_whenRecordExists()  {

        // Create application to save
        JobApplication jobApplication = new JobApplication();
        jobApplication.setName("Repo test");
        List<Answer> questionList = new ArrayList();
        questionList.add(new Answer("1", "Answer to question 1"));
        questionList.add(new Answer("2", "Answer to question 2"));
        questionList.add(new Answer("3", "Answer to question 3"));
        jobApplication.setQuestions(questionList);

        JobApplication savedApplication = subject.save(jobApplication);
        assertThat(savedApplication.getId(), notNullValue());

        JobApplication retrievedApplication = subject.findOne(savedApplication.getId());
        assertThat(retrievedApplication, notNullValue());

        assertThat(retrievedApplication.getQuestionsJson(), notNullValue());
        assertThat(retrievedApplication.getQuestions().size(), is(3));
        assertThat(retrievedApplication.getQuestions().get(0).getAnswer(), is("Answer to question 1"));
        assertThat(retrievedApplication.getQuestions().get(0).getId(), is("1"));
        assertThat(retrievedApplication.getQuestions().get(1).getAnswer(), is("Answer to question 2"));
        assertThat(retrievedApplication.getQuestions().get(1).getId(), is("2"));
        assertThat(retrievedApplication.getQuestions().get(2).getAnswer(), is("Answer to question 3"));
        assertThat(retrievedApplication.getQuestions().get(2).getId(), is("3"));

    }

    @Test
    public void testFindAll_whenNoRecordsExist(){
        subject.deleteAll();
        List<JobApplication> applications = subject.findAll();
        assertThat(applications.size(), is(0));
    }


}
