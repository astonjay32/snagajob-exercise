package com.astonlawrence.api;

import com.astonlawrence.datastore.JobApplicationRepo;
import com.astonlawrence.domain.JobApplication;
import com.astonlawrence.service.JobApplicationQualifier;
import com.astonlawrence.domain.Question;
import com.astonlawrence.service.QuestionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
public class JobApplicationController {

    @Autowired
    private JobApplicationQualifier jobApplicationQualifier;

    @Autowired
    private JobApplicationRepo jobApplicationRepo;

    private final Logger LOGGER = LoggerFactory.getLogger(JobApplicationController.class);
    private final String SAVED_LOG_MESSAGE = "Saved qualified job application for %s";

    @RequestMapping(value="/applications", method = RequestMethod.POST)
    public ResponseEntity newApplication(@RequestBody JobApplication jobApplication){

        if(jobApplicationQualifier.isQualified(jobApplication.getQuestions())){
            return createdApplicationResponse(jobApplication);
        }else{
            return noContentResponse();

        }
    }


    @RequestMapping(value="/answers", method = RequestMethod.GET)
    public List<Question> acceptableAnswers(){
        return jobApplicationQualifier.getRequiredQuestions();
    }

    @RequestMapping(value="/applications", method = RequestMethod.GET)
    public List<JobApplication> getAll(){
        return jobApplicationRepo.findAll();
    }

    @RequestMapping(value = "/applications/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    public JobApplication getById(@PathVariable String id){
        return jobApplicationRepo.findOne(id);
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
