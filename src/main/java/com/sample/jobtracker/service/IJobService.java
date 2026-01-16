package com.sample.jobtracker.service;

import com.sample.jobtracker.model.ApplicationStatus;
import com.sample.jobtracker.model.Interview;
import com.sample.jobtracker.model.Job;
import com.sample.jobtracker.model.PaginatedResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface IJobService {

    Flux<Job> getAllJobs();
    Mono<PaginatedResponse<Job>> getAllJobsPaginated(int offset, int limit);
    Mono<Job> getJobById(String id);
    Mono<Job> createJob(Job job);
    Mono<Job> updateJob(String id, Job job);
    Mono<Void> deleteJob(String id);
    Mono<Job> updateStatus(String id, ApplicationStatus status);
    Mono<Job> addInterview(String id, Interview interview);
    Flux<Job> getJobsByStatus(ApplicationStatus status);
    Flux<Job> searchJobs(String keyword);
    Flux<Job> getJobsByPriority(Integer priority);
    Mono<Long> getTotalJobs();
    Mono<Long> getJobCountByStatus(ApplicationStatus status);

}