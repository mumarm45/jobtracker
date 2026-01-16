package com.sample.jobtracker.service;

import com.sample.jobtracker.model.ApplicationStatus;
import com.sample.jobtracker.model.Interview;
import com.sample.jobtracker.model.Job;
import com.sample.jobtracker.model.PaginatedResponse;
import com.sample.jobtracker.repository.IJobRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class JobService implements IJobService {

    private final IJobRepository jobRepository;

    public JobService(IJobRepository jobRepository) {
        this.jobRepository = jobRepository;
    }

    // CRUD Operations
    public Flux<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    public Mono<PaginatedResponse<Job>> getAllJobsPaginated(int offset, int limit) {
        Pageable pageable = PageRequest.of(offset / limit, limit);
        return jobRepository.count()
                .flatMap(total ->
                    jobRepository.findAllBy(pageable)
                            .collectList()
                            .map(jobs -> new PaginatedResponse<>(jobs, total, offset, limit))
                );
    }

    public Mono<Job> getJobById(String id) {
        return jobRepository.findById(id);
    }

    public Mono<Job> createJob(Job job) {
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());
        return jobRepository.save(job);
    }

    public Mono<Job> updateJob(String id, Job job) {
        return jobRepository.findById(id)
                .flatMap(existingJob -> {
                    existingJob.setTitle(job.getTitle());
                    existingJob.setCompany(job.getCompany());
                    existingJob.setLocation(job.getLocation());
                    existingJob.setJobType(job.getJobType());
                    existingJob.setStatus(job.getStatus());
                    existingJob.setSalaryRange(job.getSalaryRange());
                    existingJob.setDescription(job.getDescription());
                    existingJob.setNotes(job.getNotes());
                    existingJob.setPriority(job.getPriority());
                    existingJob.setTags(job.getTags());
                    existingJob.setUpdatedAt(LocalDateTime.now());
                    return jobRepository.save(existingJob);
                });
    }

    public Mono<Void> deleteJob(String id) {
        return jobRepository.deleteById(id);
    }

    public Mono<Job> updateStatus(String id, ApplicationStatus status) {
        return jobRepository.findById(id)
                .flatMap(job -> {
                    job.setStatus(status);
                    job.setUpdatedAt(LocalDateTime.now());

                    if (status == ApplicationStatus.APPLIED && job.getAppliedDate() == null) {
                        job.setAppliedDate(LocalDate.now());
                    }

                    return jobRepository.save(job);
                });
    }

    public Mono<Job> addInterview(String id, Interview interview) {
        return jobRepository.findById(id)
                .flatMap(job -> {
                    job.addInterview(interview);
                    if (job.getStatus() == ApplicationStatus.APPLIED) {
                        job.setStatus(ApplicationStatus.INTERVIEWING);
                    }
                    return jobRepository.save(job);
                });
    }

    public Flux<Job> getJobsByStatus(ApplicationStatus status) {
        return jobRepository.findByStatus(status);
    }

    public Flux<Job> searchJobs(String keyword) {
        return jobRepository.findByTitleContainingIgnoreCase(keyword)
                .concatWith(jobRepository.findByCompanyContainingIgnoreCase(keyword))
                .distinct();
    }

    public Flux<Job> getJobsByPriority(Integer priority) {
        return jobRepository.findByPriority(priority);
    }

    public Mono<Long> getTotalJobs() {
        return jobRepository.count();
    }

    public Mono<Long> getJobCountByStatus(ApplicationStatus status) {
        return jobRepository.findByStatus(status).count();
    }
}