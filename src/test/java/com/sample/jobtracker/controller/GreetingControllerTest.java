package com.sample.jobtracker.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@DisplayName("Greeting Controller Tests")
class GreetingControllerTest {

    private GreetingController controller;

    @BeforeEach
    void setUp() {
        controller = new GreetingController();
    }

    @Test
    @DisplayName("Should return hello message")
    void testHelloEndpoint() {
        Mono<String> result = controller.hello();

        StepVerifier.create(result)
                .expectNext("Hello, WebFlux World!")
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return home message")
    void testHomeEndpoint() {
        Mono<String> result = controller.home();

        StepVerifier.create(result)
                .expectNext("Job Tracker API is running!")
                .verifyComplete();
    }

    @Test
    @DisplayName("Should return non-null Mono for hello")
    void testHelloReturnsNonNull() {
        Mono<String> result = controller.hello();
        assertNotNull(result);
    }


    @Test
    @DisplayName("Should emit exactly one item for hello")
    void testHelloEmitsOneItem() {
        StepVerifier.create(controller.hello())
                .expectNextCount(1)
                .verifyComplete();
    }
}
