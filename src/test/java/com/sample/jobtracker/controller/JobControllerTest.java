package com.sample.jobtracker.controller;

import com.sample.jobtracker.model.*;
import com.sample.jobtracker.service.JobService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Job Controller Tests")
class JobControllerTest {

    @Mock
    private JobService jobService;

    private JobController jobController;

    private Job sampleJob1;
    private Job sampleJob2;
    private Job sampleJob3;

    @BeforeEach
    void setUp() {
        jobController = new JobController(jobService);

        sampleJob1 = createSampleJob("1", "Software Engineer", "TechCorp");
        sampleJob2 = createSampleJob("2", "Senior Developer", "StartupXYZ");
        sampleJob3 = createSampleJob("3", "Tech Lead", "BigCompany");
    }

    private Job createSampleJob(String id, String title, String companyName) {
        Job job = new Job();
        job.setId(id);
        job.setTitle(title);

        Company company = new Company();
        company.setName(companyName);
        job.setCompany(company);

        job.setLocation("San Francisco, CA");
        job.setJobType(JobType.FULL_TIME);
        job.setStatus(ApplicationStatus.WISHLIST);
        job.setSalaryRange("$100k-$150k");
        job.setPriority(3);
        job.setCreatedAt(LocalDateTime.now());
        job.setUpdatedAt(LocalDateTime.now());

        return job;
    }

    @Test
    @DisplayName("Should return paginated jobs with default parameters")
    void testGetAllJobsWithDefaultPagination() {
        List<Job> jobs = Arrays.asList(sampleJob1, sampleJob2, sampleJob3);
        PaginatedResponse<Job> response = new PaginatedResponse<>(jobs, 3, 0, 10);

        when(jobService.getAllJobsPaginated(0, 10)).thenReturn(Mono.just(response));

        Mono<PaginatedResponse<Job>> result = jobController.getAllJobs(0, 10);

        StepVerifier.create(result)
                .assertNext(paginatedResponse -> {
                    assertNotNull(paginatedResponse);
                    assertEquals(3, paginatedResponse.getTotal());
                    assertEquals(0, paginatedResponse.getOffset());
                    assertEquals(10, paginatedResponse.getLimit());
                    assertNull(paginatedResponse.getNext());
                    assertEquals(3, paginatedResponse.getData().size());
                })
                .verifyComplete();

        verify(jobService, times(1)).getAllJobsPaginated(0, 10);
    }

    @Test
    @DisplayName("Should return paginated jobs with custom offset and limit")
    void testGetAllJobsWithCustomPagination() {
        List<Job> jobs = Arrays.asList(sampleJob2, sampleJob3);
        PaginatedResponse<Job> response = new PaginatedResponse<>(jobs, 10, 2, 2);

        when(jobService.getAllJobsPaginated(2, 2)).thenReturn(Mono.just(response));

        Mono<PaginatedResponse<Job>> result = jobController.getAllJobs(2, 2);

        StepVerifier.create(result)
                .assertNext(paginatedResponse -> {
                    assertNotNull(paginatedResponse);
                    assertEquals(10, paginatedResponse.getTotal());
                    assertEquals(2, paginatedResponse.getOffset());
                    assertEquals(2, paginatedResponse.getLimit());
                    assertEquals(4, paginatedResponse.getNext());
                    assertEquals(2, paginatedResponse.getData().size());
                })
                .verifyComplete();

        verify(jobService, times(1)).getAllJobsPaginated(2, 2);
    }

    @Test
    @DisplayName("Should return next as null when no more items")
    void testGetAllJobsWithNoNextPage() {
        List<Job> jobs = Arrays.asList(sampleJob1);
        PaginatedResponse<Job> response = new PaginatedResponse<>(jobs, 1, 0, 10);

        when(jobService.getAllJobsPaginated(0, 10)).thenReturn(Mono.just(response));

        Mono<PaginatedResponse<Job>> result = jobController.getAllJobs(0, 10);

        StepVerifier.create(result)
                .assertNext(paginatedResponse -> {
                    assertNull(paginatedResponse.getNext());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return empty data list when no jobs exist")
    void testGetAllJobsWithEmptyResult() {
        List<Job> emptyJobs = Arrays.asList();
        PaginatedResponse<Job> response = new PaginatedResponse<>(emptyJobs, 0, 0, 10);

        when(jobService.getAllJobsPaginated(0, 10)).thenReturn(Mono.just(response));

        Mono<PaginatedResponse<Job>> result = jobController.getAllJobs(0, 10);

        StepVerifier.create(result)
                .assertNext(paginatedResponse -> {
                    assertEquals(0, paginatedResponse.getTotal());
                    assertTrue(paginatedResponse.getData().isEmpty());
                    assertNull(paginatedResponse.getNext());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return job when found by ID")
    void testGetJobByIdFound() {
        when(jobService.getJobById("1")).thenReturn(Mono.just(sampleJob1));

        Mono<ResponseEntity<Job>> result = jobController.getJobById("1");

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertNotNull(response.getBody());
                    assertEquals("Software Engineer", response.getBody().getTitle());
                })
                .verifyComplete();

        verify(jobService, times(1)).getJobById("1");
    }

    @Test
    @DisplayName("Should return 404 when job not found by ID")
    void testGetJobByIdNotFound() {
        when(jobService.getJobById("999")).thenReturn(Mono.empty());

        Mono<ResponseEntity<Job>> result = jobController.getJobById("999");

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                    assertNull(response.getBody());
                })
                .verifyComplete();

        verify(jobService, times(1)).getJobById("999");
    }

    // Create Job Tests
    @Test
    @DisplayName("Should create new job successfully")
    void testCreateJob() {
        when(jobService.createJob(any(Job.class))).thenReturn(Mono.just(sampleJob1));

        Mono<Job> result = jobController.createJob(sampleJob1);

        StepVerifier.create(result)
                .assertNext(job -> {
                    assertNotNull(job);
                    assertEquals("Software Engineer", job.getTitle());
                    assertEquals("1", job.getId());
                })
                .verifyComplete();

        verify(jobService, times(1)).createJob(any(Job.class));
    }

    @Test
    @DisplayName("Should update job when found")
    void testUpdateJobFound() {
        Job updatedJob = createSampleJob("1", "Senior Software Engineer", "TechCorp");
        when(jobService.updateJob(eq("1"), any(Job.class))).thenReturn(Mono.just(updatedJob));

        Mono<ResponseEntity<Job>> result = jobController.updateJob("1", updatedJob);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertNotNull(response.getBody());
                    assertEquals("Senior Software Engineer", response.getBody().getTitle());
                })
                .verifyComplete();

        verify(jobService, times(1)).updateJob(eq("1"), any(Job.class));
    }

    @Test
    @DisplayName("Should return 404 when updating non-existent job")
    void testUpdateJobNotFound() {
        when(jobService.updateJob(eq("999"), any(Job.class))).thenReturn(Mono.empty());

        Mono<ResponseEntity<Job>> result = jobController.updateJob("999", sampleJob1);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
                })
                .verifyComplete();
    }

    @Test
    @DisplayName("Should delete job successfully")
    void testDeleteJob() {
        when(jobService.deleteJob("1")).thenReturn(Mono.empty());

        Mono<Void> result = jobController.deleteJob("1");

        StepVerifier.create(result)
                .verifyComplete();

        verify(jobService, times(1)).deleteJob("1");
    }

    @Test
    @DisplayName("Should update job status successfully")
    void testUpdateStatus() {
        sampleJob1.setStatus(ApplicationStatus.APPLIED);
        when(jobService.updateStatus("1", ApplicationStatus.APPLIED))
                .thenReturn(Mono.just(sampleJob1));

        Mono<ResponseEntity<Job>> result = jobController.updateStatus("1", ApplicationStatus.APPLIED);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertNotNull(response.getBody());
                    assertEquals(ApplicationStatus.APPLIED, response.getBody().getStatus());
                })
                .verifyComplete();

        verify(jobService, times(1)).updateStatus("1", ApplicationStatus.APPLIED);
    }

    @Test
    @DisplayName("Should add interview to job successfully")
    void testAddInterview() {
        Interview interview = new Interview();
        interview.setInterviewType("Technical");

        when(jobService.addInterview(eq("1"), any(Interview.class)))
                .thenReturn(Mono.just(sampleJob1));

        Mono<ResponseEntity<Job>> result = jobController.addInterview("1", interview);

        StepVerifier.create(result)
                .assertNext(response -> {
                    assertEquals(HttpStatus.OK, response.getStatusCode());
                    assertNotNull(response.getBody());
                })
                .verifyComplete();

        verify(jobService, times(1)).addInterview(eq("1"), any(Interview.class));
    }

    @Test
    @DisplayName("Should return jobs filtered by status")
    void testGetJobsByStatus() {
        when(jobService.getJobsByStatus(ApplicationStatus.WISHLIST))
                .thenReturn(Flux.just(sampleJob1, sampleJob2));

        Flux<Job> result = jobController.getJobsByStatus(ApplicationStatus.WISHLIST);

        StepVerifier.create(result)
                .expectNextCount(2)
                .verifyComplete();

        verify(jobService, times(1)).getJobsByStatus(ApplicationStatus.WISHLIST);
    }

    @Test
    @DisplayName("Should search jobs by keyword")
    void testSearchJobs() {
        when(jobService.searchJobs("Engineer"))
                .thenReturn(Flux.just(sampleJob1));

        Flux<Job> result = jobController.searchJobs("Engineer");

        StepVerifier.create(result)
                .assertNext(job -> {
                    assertTrue(job.getTitle().contains("Engineer"));
                })
                .verifyComplete();

        verify(jobService, times(1)).searchJobs("Engineer");
    }

    @Test
    @DisplayName("Should return jobs filtered by priority")
    void testGetJobsByPriority() {
        when(jobService.getJobsByPriority(3))
                .thenReturn(Flux.just(sampleJob1, sampleJob2, sampleJob3));

        Flux<Job> result = jobController.getJobsByPriority(3);

        StepVerifier.create(result)
                .expectNextCount(3)
                .verifyComplete();

        verify(jobService, times(1)).getJobsByPriority(3);
    }

    @Test
    @DisplayName("Should return total job count")
    void testGetTotalJobs() {
        when(jobService.getTotalJobs()).thenReturn(Mono.just(10L));

        Mono<Long> result = jobController.getTotalJobs();

        StepVerifier.create(result)
                .expectNext(10L)
                .verifyComplete();

        verify(jobService, times(1)).getTotalJobs();
    }

    @Test
    @DisplayName("Should return job count by status")
    void testGetJobCountByStatus() {
        when(jobService.getJobCountByStatus(ApplicationStatus.APPLIED))
                .thenReturn(Mono.just(5L));

        Mono<Long> result = jobController.getJobCountByStatus(ApplicationStatus.APPLIED);

        StepVerifier.create(result)
                .expectNext(5L)
                .verifyComplete();

        verify(jobService, times(1)).getJobCountByStatus(ApplicationStatus.APPLIED);
    }
}
