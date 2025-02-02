package hse.dss.diss.controller;

import hse.dss.diss.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * TestController is a REST controller that handles HTTP requests related to tests.
 * It provides an endpoint for fetching a test related to a specific task.
 */
@RestController
@RequestMapping("/tests")
@RequiredArgsConstructor
@Slf4j
public class TestController {

    private final TestService testService;

    /**
     * Endpoint for retrieving a generated test for a specific task.
     *
     * @param taskId the ID of the task for which to generate a test
     * @return a ResponseEntity containing the generated test as a String
     */
    @GetMapping("/task/{taskId}")
    public ResponseEntity<String> getTasksByUser(@PathVariable Long taskId) {

        String test = testService.generateTest(taskId);
        return ResponseEntity.ok(test);
    }
}
