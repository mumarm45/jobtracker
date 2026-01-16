package com.sample.jobtracker.controller;

import com.sample.jobtracker.model.ApplicationStatus;
import com.sample.jobtracker.model.Interview;
import com.sample.jobtracker.model.Job;
import com.sample.jobtracker.model.PaginatedResponse;
import com.sample.jobtracker.service.IJobService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private final IJobService jobService;

    public JobController(IJobService jobService) {
        this.jobService = jobService;
    }

    @GetMapping
    public Mono<PaginatedResponse<Job>> getAllJobs(
            @RequestParam(defaultValue = "0") int offset,
            @RequestParam(defaultValue = "10") int limit) {
        return jobService.getAllJobsPaginated(offset, limit);
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<Job>> getJobById(@PathVariable String id) {
        return jobService.getJobById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<Job> createJob(@RequestBody Job job) {
        return jobService.createJob(job);
    }

    @PutMapping("/{id}")
    public Mono<ResponseEntity<Job>> updateJob(
            @PathVariable String id,
            @RequestBody Job job) {
        return jobService.updateJob(id, job)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public Mono<Void> deleteJob(@PathVariable String id) {
        return jobService.deleteJob(id);
    }

    // Status Management
    @PatchMapping("/{id}/status")
    public Mono<ResponseEntity<Job>> updateStatus(
            @PathVariable String id,
            @RequestParam ApplicationStatus status) {
        return jobService.updateStatus(id, status)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Interview Management
    @PostMapping("/{id}/interviews")
    public Mono<ResponseEntity<Job>> addInterview(
            @PathVariable String id,
            @RequestBody Interview interview) {
        return jobService.addInterview(id, interview)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    // Queries
    @GetMapping("/status/{status}")
    public Flux<Job> getJobsByStatus(@PathVariable ApplicationStatus status) {
        return jobService.getJobsByStatus(status);
    }

    @GetMapping("/search")
    public Flux<Job> searchJobs(@RequestParam String keyword) {
        return jobService.searchJobs(keyword);
    }

    @GetMapping("/priority/{priority}")
    public Flux<Job> getJobsByPriority(@PathVariable Integer priority) {
        return jobService.getJobsByPriority(priority);
    }

    // Statistics
    @GetMapping("/stats/total")
    public Mono<Long> getTotalJobs() {
        return jobService.getTotalJobs();
    }

    @GetMapping("/stats/status/{status}")
    public Mono<Long> getJobCountByStatus(@PathVariable ApplicationStatus status) {
        return jobService.getJobCountByStatus(status);
    }
}