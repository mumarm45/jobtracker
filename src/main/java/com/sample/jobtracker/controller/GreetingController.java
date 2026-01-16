package com.sample.jobtracker.controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;


@RestController
public class GreetingController {
    @GetMapping("/hello")
    public Mono<String> hello() {
        return Mono.just("Hello, WebFlux World!");
    }

    @GetMapping("/")
    public Mono<String> home() {
        return Mono.just("Job Tracker API is running!");
    }
}