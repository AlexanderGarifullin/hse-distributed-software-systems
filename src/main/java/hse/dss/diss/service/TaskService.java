package hse.dss.diss.service;

import hse.dss.diss.entity.Task;
import hse.dss.diss.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;

    @Async
    public CompletableFuture<List<Task>> getAllTasks() {
        log.info("Fetching the list of tasks...");
        List<Task> tasks = taskRepository.findAll();
        log.info("Found {} tasks.", tasks.size());
        return CompletableFuture.completedFuture(tasks);
    }

    @Async
    public CompletableFuture<Task> createTask(Task task) {
        log.info("Creating a new task: {}", task.getName());
        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with ID: {}", savedTask.getId());
        return CompletableFuture.completedFuture(savedTask);
    }

    @Async
    public CompletableFuture<Task> updateTask(Task task) {
        log.info("Updating task with ID: {}", task.getId());
        Task updatedTask = taskRepository.save(task);
        log.info("Task updated successfully.");
        return CompletableFuture.completedFuture(updatedTask);
    }

    @Async
    public CompletableFuture<Void> deleteTask(Long id) {
        log.info("Deleting task with ID: {}", id);
        taskRepository.deleteById(id);
        log.info("Task with ID {} has been deleted.", id);
        return CompletableFuture.completedFuture(null);
    }

    @Async
    public CompletableFuture<Task> getTaskById(Long id) {
        log.info("Fetching task with ID: {}", id);
        Task task = taskRepository.findById(id).orElse(null);
        return CompletableFuture.completedFuture(task);
    }
}
