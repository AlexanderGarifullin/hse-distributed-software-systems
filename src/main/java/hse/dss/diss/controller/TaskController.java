package hse.dss.diss.controller;

import hse.dss.diss.entity.Task;
import hse.dss.diss.service.TaskService;
import hse.dss.diss.transfer.TaskPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * TaskController manages HTTP requests related to tasks.
 * It provides endpoints for creating, updating, retrieving, and deleting tasks.
 */
@RestController
@RequestMapping("/tasks")
@RequiredArgsConstructor
@Slf4j
public class TaskController {

    private final TaskService taskService;

    /**
     * Retrieves all tasks for a specific user.
     *
     * @param userId the ID of the user whose tasks are to be fetched
     * @return a list of tasks for the specified user
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Task>> getTasksByUser(@PathVariable Long userId) {
        log.info("Received request to get tasks for user with ID: {}", userId);
        List<Task> tasks = taskService.getTasksByUser(userId);
        log.info("Returning {} tasks for user with ID: {}", tasks.size(), userId);
        return ResponseEntity.ok(tasks);
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param taskId the ID of the task to retrieve
     * @return the task with the specified ID
     */
    @GetMapping("/{taskId}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId) {
        log.info("Received request to get task with ID: {}", taskId);
        Task task = taskService.getTaskById(taskId);
        log.info("Returning task with ID: {}", taskId);
        return ResponseEntity.ok(task);
    }

    /**
     * Creates a new task for a specific user.
     *
     * @param userId the ID of the user for whom the task is being created
     * @param task the task payload containing task details
     * @return the created task
     */
    @PostMapping("/user/{userId}")
    public ResponseEntity<Task> createTask(@PathVariable Long userId, @RequestBody TaskPayload task) {
        log.info("Received request to create task for user with ID: {}", userId);
        Task createdTask = taskService.createTask(userId, task);
        log.info("Created task with ID: {} for user with ID: {}", createdTask.getId(), userId);
        return ResponseEntity.ok(createdTask);
    }

    /**
     * Updates an existing task.
     *
     * @param taskId the ID of the task to update
     * @param task the task payload containing updated task details
     * @return the updated task
     */
    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable Long taskId, @RequestBody TaskPayload task) {
        log.info("Received request to update task with ID: {}", taskId);
        Task updatedTask = taskService.updateTask(taskId, task);
        log.info("Updated task with ID: {}", taskId);
        return ResponseEntity.ok(updatedTask);
    }

    /**
     * Deletes a task by its ID.
     *
     * @param taskId the ID of the task to delete
     * @return a response indicating the task was deleted
     */
    @DeleteMapping("/{taskId}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long taskId) {
        log.info("Received request to delete task with ID: {}", taskId);
        taskService.deleteTask(taskId);
        log.info("Deleted task with ID: {}", taskId);
        return ResponseEntity.noContent().build();
    }
}
