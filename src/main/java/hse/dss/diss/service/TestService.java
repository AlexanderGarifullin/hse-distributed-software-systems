package hse.dss.diss.service;



import hse.dss.diss.exception.EntityNotFoundException;
import hse.dss.diss.repository.storage.TaskInMemoryStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static hse.dss.diss.util.MsgGenerator.getErrorMsg;

/**
 * TestService is a service that handles the logic related to generating tests for tasks.
 * This class currently contains a placeholder method for generating a test, which needs to be
 * implemented later.
 * // TODO: Implement the logic for generating tests based on task details.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {

    private final TaskInMemoryStorage taskInMemoryStorage;

    /**
     * Placeholder method for generating a test.
     * // TODO: Implement the logic for generating a test for the task with the given taskId.
     */
    public String generateTest(Long taskId) {
        var task = taskInMemoryStorage.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(getErrorMsg("Task", taskId)));;
        log.info("Placeholder: Generating test for task with ID {}", taskId);
        return "Some test"; // TODO: Replace with real logic
    }
}
