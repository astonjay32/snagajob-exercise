package com.astonlawrence.datastore;

import com.astonlawrence.domain.JobApplication;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface JobApplicationRepo extends CrudRepository<JobApplication, String> {

    List<JobApplication> findAll();
    JobApplication save(JobApplication jobApplication);

}
