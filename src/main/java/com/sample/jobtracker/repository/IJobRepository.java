package com.sample.jobtracker.repository;

import com.sample.jobtracker.model.ApplicationStatus;
import com.sample.jobtracker.model.Job;
import com.sample.jobtracker.model.JobType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface IJobRepository extends ReactiveMongoRepository<Job, String> {

    Flux<Job> findAllBy(Pageable pageable);

    Flux<Job> findByStatus(ApplicationStatus status);

    Flux<Job> findByCompanyContainingIgnoreCase(String company);

    Flux<Job> findByTitleContainingIgnoreCase(String title);

    Flux<Job> findByJobType(JobType jobType);

    Flux<Job> findByPriority(Integer priority);

    Flux<Job> findByTagsContaining(String tag);

    Flux<Job> findByAppliedDateBetween(java.time.LocalDate start, java.time.LocalDate end);
}