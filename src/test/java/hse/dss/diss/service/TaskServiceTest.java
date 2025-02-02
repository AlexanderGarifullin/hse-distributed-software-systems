package hse.dss.diss.service;

import hse.dss.diss.entity.Task;
import hse.dss.diss.entity.User;
import hse.dss.diss.exception.EntityNotFoundException;
import hse.dss.diss.mapper.TaskMapper;
import hse.dss.diss.repository.storage.InMemoryStorage;
import hse.dss.diss.repository.storage.TaskInMemoryStorage;
import hse.dss.diss.transfer.TaskPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class TaskServiceTest {

    @InjectMocks
    private TaskService taskService;

    @Mock
    private TaskInMemoryStorage taskInMemoryStorage;

    @Mock
    private InMemoryStorage<User, Long> userInMemoryStorage;

    @Mock
    private TaskMapper taskMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }



    @Test
    void getTasksByUser_WhenUserDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long userId = 1L;
        assertThatThrownBy(() -> taskService.getTasksByUser(userId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("User with ID %d not found", userId));

    }

    @Test
    void getTaskById_WhenTaskExists_ShouldReturnTask() {
        Long taskId = 1L;
        Task task = generateTask();

        when(taskInMemoryStorage.findById(taskId)).thenReturn(Optional.of(task));

        Task result = taskService.getTaskById(taskId);

        assertThat(result).isEqualTo(task);
        verify(taskInMemoryStorage, times(1)).findById(taskId);
    }

    @Test
    void getTaskById_WhenTaskDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long taskId = 1L;
        when(taskInMemoryStorage.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.getTaskById(taskId))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("Task with ID %d not found", taskId));

        verify(taskInMemoryStorage, times(1)).findById(taskId);
    }


    @Test
    void createTask_WhenUserDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long userId = 1L;
        TaskPayload payload = generateTaskPayload();

        when(userInMemoryStorage.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.createTask(userId, payload))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("User with ID %d not found", userId));
    }

    @Test
    void updateTask_WhenTaskExists_ShouldReturnUpdatedTask() {
        Long taskId = 1L;
        Task existingTask = generateTask();
        TaskPayload updatedPayload = generateTaskPayload();

        when(taskInMemoryStorage.findById(taskId)).thenReturn(Optional.of(existingTask));

        Task result = taskService.updateTask(taskId, updatedPayload);

        assertThat(result.getName()).isEqualTo(updatedPayload.name());
        assertThat(result.getStatement()).isEqualTo(updatedPayload.statement());
        verify(taskInMemoryStorage, times(1)).findById(taskId);
        verify(taskInMemoryStorage, times(1)).update(taskId, existingTask);
    }

    @Test
    void updateTask_WhenTaskDoesNotExist_ShouldThrowEntityNotFoundException() {
        Long taskId = 1L;
        TaskPayload updatedPayload = generateTaskPayload();

        when(taskInMemoryStorage.findById(taskId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> taskService.updateTask(taskId, updatedPayload))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessageContaining(String.format("Task with ID %d not found", taskId));

        verify(taskInMemoryStorage, times(1)).findById(taskId);
        verifyNoMoreInteractions(taskInMemoryStorage);
    }

    @Test
    void deleteTask_ShouldDeleteTaskSuccessfully() {
        Long taskId = 1L;

        taskService.deleteTask(taskId);

        verify(taskInMemoryStorage, times(1)).delete(taskId);
    }

    private User generateUser() {
        return User.builder()
                .id(1L)
                .login("USER")
                .build();
    }

    private Task generateTask() {
        return Task.builder()
                .id(1L)
                .userId(1L)
                .statement("state")
                .name("name")
                .build();
    }

    private TaskPayload generateTaskPayload() {
        return TaskPayload.builder()
                .taskId(1L)
                .name("name")
                .statement("state")
                .build();
    }
}
