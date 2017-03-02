package com.astonlawrence.api;

import com.astonlawrence.datastore.JobApplicationRepo;
import com.astonlawrence.domain.JobApplication;
import com.astonlawrence.service.JobApplicationQualifier;
import com.astonlawrence.domain.Question;
import com.astonlawrence.service.QuestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;

@RestController
public class ApplicationController {

    @Autowired
    private JobApplicationQualifier jobApplicationQualifier;

    @Autowired
    private JobApplicationRepo jobApplicationRepo;

    @Autowired
    private QuestionService questionService;

    private final Logger LOGGER = LoggerFactory.getLogger(ApplicationController.class);
    private final String SAVED_LOG_MESSAGE = "Saved qualified job application for %s";


    @RequestMapping(value="/applications", method = RequestMethod.POST)
    public ResponseEntity newApplication(@RequestBody JobApplication jobApplication){
        LOGGER.info("Request to add new application for " + jobApplication.getName());
        if(jobApplicationQualifier.isQualified(questionService.getApplicationQuestions(), jobApplication.getQuestions())){
            return createdApplicationResponse(jobApplication);
        }else{
            return noContentResponse();

        }
    }

    @RequestMapping(value="/applications", method = RequestMethod.GET)
    public List<JobApplication> getAll(){
        return jobApplicationRepo.findAll();
    }

    @RequestMapping(value="/application/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JobApplication getById(@PathVariable String id){
        return jobApplicationRepo.findOne(id);
    }

    @RequestMapping(value="/applications/questions", method = RequestMethod.GET)
    public List<Question> questions(){
        return questionService.getApplicationQuestions();
    }

    private ResponseEntity createdApplicationResponse(JobApplication jobApplication){
        JobApplication savedApplication = jobApplicationRepo.save(jobApplication);
        LOGGER.info(String.format(SAVED_LOG_MESSAGE, jobApplication.getName()));
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest().path("/{id}")
                .buildAndExpand(savedApplication.getId()).toUri();
        return ResponseEntity.created(location).body(savedApplication);
    }

    private ResponseEntity noContentResponse(){
        return ResponseEntity.noContent().build();
    }

}
