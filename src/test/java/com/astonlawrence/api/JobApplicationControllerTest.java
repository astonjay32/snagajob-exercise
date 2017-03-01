package com.astonlawrence.api;

import com.astonlawrence.datastore.JobApplicationRepo;
import com.astonlawrence.domain.JobApplication;
import com.astonlawrence.service.JobApplicationQualifier;
import com.astonlawrence.domain.Question;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment= SpringBootTest.WebEnvironment.RANDOM_PORT)
public class JobApplicationControllerTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private JobApplicationQualifier jobApplicationQualifier;

    @Autowired
    private JobApplicationRepo jobApplicationRepo;

    @Test
    public void testPostApplication_withQualifiedApp_shouldReturn201_andHaveResourceUrl() throws JsonProcessingException {

        // Prepare request
        JobApplication jobApplication = passingApp();
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(jobApplication);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);

        // Send request, check response
        ResponseEntity<JobApplication> saveResponse = testRestTemplate.postForEntity("/applications", entity, JobApplication.class);
        assertThat(saveResponse.getStatusCode(), is(HttpStatus.CREATED));
        JobApplication savedApplication = saveResponse.getBody();
        assertThat(savedApplication, notNullValue());
        assertThat(savedApplication.getId(), notNullValue());

        // Verify resource is available
        ResponseEntity<JobApplication> retrieveResponse = testRestTemplate.getForEntity("/applications/" + savedApplication.getId(), JobApplication.class);
        assertThat(retrieveResponse.getStatusCode(), is(HttpStatus.OK));
        JobApplication retrievedApplication = retrieveResponse.getBody();
        assertThat(retrievedApplication, notNullValue());
        assertThat(retrievedApplication.getId(), is(savedApplication.getId()));
        assertThat(retrievedApplication.getName(), is("Test Application"));

    }

    @Test
    public void testPostApplication_withUnqualifiedApp_withReturn204_noResponseBody() throws JsonProcessingException {

        // Prepare request
        JobApplication jobApplication = passingApp();
        jobApplication.setQuestions(new ArrayList<>());
        ObjectMapper objectMapper = new ObjectMapper();
        String requestJson = objectMapper.writeValueAsString(jobApplication);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> entity = new HttpEntity<String>(requestJson,headers);

        // Send request, check response
        ResponseEntity<JobApplication> saveResponse = testRestTemplate.postForEntity("/applications", entity, JobApplication.class);
        assertThat(saveResponse.getStatusCode(), is(HttpStatus.NO_CONTENT));
        JobApplication savedApplication = saveResponse.getBody();
        assertThat(savedApplication, nullValue());

    }

    @Test
    public void testGetAll_withoutExistingRecords_returnsEmptyList(){
        jobApplicationRepo.deleteAll();
        ResponseEntity<JobApplication[]> getAllResponse = testRestTemplate.getForEntity("/applications", JobApplication[].class);
        assertThat(getAllResponse.getStatusCode(), is(HttpStatus.OK));
        JobApplication[] jobApplications = getAllResponse.getBody();
        assertThat(jobApplications.length, is(0));
    }

    @Test
    public void testGetAll_withExistingRecords_returnsRecords(){

        // Wipe database to ensure we're saving and retrieving correctly
        jobApplicationRepo.deleteAll();

        // Add a few arbitrary records
        jobApplicationRepo.save(passingApp());
        jobApplicationRepo.save(passingApp());
        jobApplicationRepo.save(passingApp());

        // Get all records, make sure you get 3
        ResponseEntity<JobApplication[]> getAllResponse = testRestTemplate.getForEntity("/applications", JobApplication[].class);
        assertThat(getAllResponse.getStatusCode(), is(HttpStatus.OK));
        JobApplication[] jobApplications = getAllResponse.getBody();
        assertThat(jobApplications.length, is(3));

        // Check the values of one of them to ensure correctness
        JobApplication appOne = jobApplications[0];
        assertThat(appOne.getName(), is("Test Application"));
        assertThat(appOne.getQuestionsJson(), notNullValue());
        assertThat(appOne.getId(), notNullValue());

    }

    private JobApplication passingApp() {
        JobApplication jobApplication = new JobApplication();
        jobApplication.setName("Test Application");
        ArrayList<Question> appQuestions = new ArrayList();
        for(Question question : jobApplicationQualifier.getRequiredQuestions()){
            appQuestions.add(new Question(question.getId(), question.getAnswer()));
        }
        jobApplication.setQuestions(appQuestions);
        return jobApplication;
    }
}
