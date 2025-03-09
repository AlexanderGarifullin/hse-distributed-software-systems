package hse.dss.controller;

import hse.dss.entity.Task;
import hse.dss.service.TaskService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Controller
@RequestMapping("/tasks")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    // Display task list
    @GetMapping
    public CompletableFuture<String> listTasks(Model model) {
        log.info("Received request to list tasks.");
        return taskService.getAllTasks().thenApply(tasks -> {
            model.addAttribute("tasks", tasks);
            return "tasks";  // Thymeleaf template: tasks.html
        });
    }

    // Form to create a new task
    @GetMapping("/new")
    public String newTaskForm(Model model) {
        log.info("Navigating to the task creation form.");
        model.addAttribute("task", new Task());
        return "newTask";  // Thymeleaf template: newTask.html
    }

    // Processing task creation
    @PostMapping
    public CompletableFuture<String> createTask(@Valid @ModelAttribute("task") Task task,
                                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            // Ошибки валидации – возвращаем форму с сообщениями об ошибках
            log.warn("Validation errors when creating task: {}", bindingResult.getAllErrors());
            return CompletableFuture.completedFuture("newTask");
        }
        return taskService.createTask(task)
                .handle((savedTask, ex) -> {
                    if (ex != null) {
                        log.error("Error creating task", ex);
                        model.addAttribute("errorMessage", "Error creating task: " + ex.getMessage());
                        return "newTask";
                    }
                    return "redirect:/tasks";
                });
    }

    // Form for editing an existing task
    @GetMapping("/{id}/edit")
    public CompletableFuture<String> editTaskForm(@PathVariable Long id, Model model) {
        log.info("Received request to edit task with ID: {}", id);
        return taskService.getTaskById(id).thenApply(task -> {
            if (task == null) {
                model.addAttribute("errorMessage", "Task not found.");
                return "redirect:/tasks";
            }
            model.addAttribute("task", task);
            return "editTask";  // Thymeleaf template: editTask.html
        });
    }

    // Processing task update
    @PostMapping("/{id}")
    public CompletableFuture<String> updateTask(@PathVariable Long id,
                                                @Valid @ModelAttribute("task") Task task,
                                                BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            log.warn("Validation errors when updating task: {}", bindingResult.getAllErrors());
            return CompletableFuture.completedFuture("editTask");
        }
        task.setId(id);
        return taskService.updateTask(task)
                .handle((updatedTask, ex) -> {
                    if (ex != null) {
                        log.error("Error updating task", ex);
                        model.addAttribute("errorMessage", "Error updating task: " + ex.getMessage());
                        return "editTask";
                    }
                    return "redirect:/tasks";
                });
    }

    // Deleting a task
    @PostMapping("/{id}/delete")
    public CompletableFuture<String> deleteTask(@PathVariable Long id, Model model) {
        log.info("Received request to delete task with ID: {}", id);
        return taskService.deleteTask(id)
                .handle((v, ex) -> {
                    if (ex != null) {
                        log.error("Error deleting task", ex);
                        model.addAttribute("errorMessage", "Error deleting task: " + ex.getMessage());
                        return "tasks";
                    }
                    return "redirect:/tasks";
                });
    }
}
