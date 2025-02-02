package hse.dss.diss.service;

import hse.dss.diss.entity.Task;
import hse.dss.diss.entity.User;
import hse.dss.diss.exception.EntityNotFoundException;
import hse.dss.diss.mapper.TaskMapper;
import hse.dss.diss.repository.storage.InMemoryStorage;
import hse.dss.diss.repository.storage.TaskInMemoryStorage;
import hse.dss.diss.transfer.TaskPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static hse.dss.diss.util.MsgGenerator.getErrorMsg;

/**
 * TaskService provides business logic for managing tasks.
 * It handles task creation, retrieval, updating, and deletion.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskInMemoryStorage taskInMemoryStorage;
    private final InMemoryStorage<User, Long> userInMemoryStorage;

    private final TaskMapper taskMapper;

    /**
     * Retrieves all tasks for a specific user.
     *
     * @param userId the ID of the user whose tasks are to be fetched
     * @return a list of tasks for the specified user
     */
    public List<Task> getTasksByUser(Long userId) {
        log.info("Fetching tasks for user with ID: {}", userId);

        var user = userInMemoryStorage.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(getErrorMsg("User", userId)));

        List<Task> tasks = taskInMemoryStorage.findByUserId(userId);
        log.info("Found {} tasks for user with ID: {}", tasks.size(), userId);
        return tasks;
    }

    /**
     * Retrieves a task by its ID.
     *
     * @param taskId the ID of the task to retrieve
     * @return the task with the specified ID
     */
    public Task getTaskById(Long taskId) {
        log.info("Fetching task with ID: {}", taskId);
        return findTaskById(taskId);
    }

    /**
     * Creates a new task for a specific user.
     *
     * @param userId the ID of the user for whom the task is being created
     * @param payload the task payload containing task details
     * @return the created task
     */
    public Task createTask(Long userId, TaskPayload payload) {
        log.info("Creating task for user with ID: {}", userId);
        Task task = taskMapper.payloadToEntity(payload);

        var user = userInMemoryStorage.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException(getErrorMsg("User", userId)));

        task.setUserId(userId);
        taskInMemoryStorage.create(task.getId(), task);
        log.info("Task with ID: {} created for user with ID: {}", task.getId(), userId);
        return task;
    }

    /**
     * Updates an existing task.
     *
     * @param taskId the ID of the task to update
     * @param updatedTask the task payload containing updated task details
     * @return the updated task
     */
    public Task updateTask(Long taskId, TaskPayload updatedTask) {
        log.info("Updating task with ID: {}", taskId);
        Task task = findTaskById(taskId);
        task.setName(updatedTask.name());
        task.setStatement(updatedTask.statement());
        taskInMemoryStorage.update(taskId, task);
        log.info("Task with ID: {} updated", taskId);
        return task;
    }

    /**
     * Deletes a task by its ID.
     *
     * @param taskId the ID of the task to delete
     */
    public void deleteTask(Long taskId) {
        log.info("Deleting task with ID: {}", taskId);
        taskInMemoryStorage.delete(taskId);
        log.info("Task with ID: {} deleted", taskId);
    }

    public Task findTaskById(Long taskId) {
        return taskInMemoryStorage.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException(getErrorMsg("Task", taskId)));
    }
}
