package hse.dss.diss.service;

import hse.dss.diss.entity.Task;
import hse.dss.diss.repository.TaskRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TaskService taskService;

    @Test
    public void getAllTasks_whenRepositoryReturnsList_thenReturnTaskList() throws Exception {
        // Arrange
        Task task1 = new Task(1L, 500, 100, LocalDateTime.now(), "Task1", "Legend1", "Input1", "Output1", "Rules1");
        Task task2 = new Task(2L, 600, 200, LocalDateTime.now(), "Task2", "Legend2", "Input2", "Output2", "Rules2");
        List<Task> tasks = Arrays.asList(task1, task2);
        when(taskRepository.findAll()).thenReturn(tasks);

        // Act
        CompletableFuture<List<Task>> future = taskService.getAllTasks();
        List<Task> result = future.get();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(taskRepository, times(1)).findAll();
    }

    @Test
    public void createTask_whenValidTask_thenReturnSavedTask() throws Exception {
        // Arrange
        Task inputTask = new Task(null, 500, 100, LocalDateTime.now(), "NewTask", null, null, null, null);

        Task savedTask = new Task(1L, 500, 100, LocalDateTime.now(), "NewTask", null, null, null, null);
        when(taskRepository.save(inputTask)).thenReturn(savedTask);

        // Act
        CompletableFuture<Task> future = taskService.createTask(inputTask);
        Task result = future.get();

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(taskRepository, times(1)).save(inputTask);
    }

    @Test
    public void updateTask_whenExistingTask_thenReturnUpdatedTask() throws Exception {
        // Arrange
        Task task = new Task(1L, 500, 100, LocalDateTime.now(), "ExistingTask", null, null, null, null);
        when(taskRepository.save(task)).thenReturn(task);

        // Act
        CompletableFuture<Task> future = taskService.updateTask(task);
        Task result = future.get();

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(taskRepository, times(1)).save(task);
    }

    @Test
    public void deleteTask_whenCalled_thenRepositoryDeletesTask() throws Exception {
        // Arrange
        Long taskId = 1L;
        // Act
        CompletableFuture<Void> future = taskService.deleteTask(taskId);
        future.get(); // ждем завершения

        // Assert
        verify(taskRepository, times(1)).deleteById(taskId);
    }

    @Test
    public void getTaskById_whenTaskExists_thenReturnTask() throws Exception {
        // Arrange
        Long taskId = 1L;
        Task task = new Task(taskId, 500, 100, LocalDateTime.now(), "Task1", "Legend1", "Input1", "Output1", "Rules1");
        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        // Act
        CompletableFuture<Task> future = taskService.getTaskById(taskId);
        Task result = future.get();

        // Assert
        assertNotNull(result);
        assertEquals(taskId, result.getId());
        verify(taskRepository, times(1)).findById(taskId);
    }

    @Test
    public void getTaskById_whenTaskNotExists_thenReturnNull() throws Exception {
        // Arrange
        Long taskId = 2L;
        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        // Act
        CompletableFuture<Task> future = taskService.getTaskById(taskId);
        Task result = future.get();

        // Assert
        assertNull(result);
        verify(taskRepository, times(1)).findById(taskId);
    }
}