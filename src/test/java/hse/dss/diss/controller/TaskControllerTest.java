package hse.dss.diss.controller;

import hse.dss.diss.entity.Task;
import hse.dss.diss.service.TaskService;
import hse.dss.diss.transfer.TaskPayload;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ActiveProfiles("test")
class TaskControllerTest {

    @InjectMocks
    private TaskController taskController;

    @Mock
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTasksByUser_WithExistingTasks_ShouldReturnTaskList() {
        Task t1 = generateTask();
        Task t2 = generateTask();
        Long taskUser = 1L;
        when(taskService.getTasksByUser(taskUser)).thenReturn(List.of(t1, t2));

        ResponseEntity<List<Task>> response = taskController.getTasksByUser(taskUser);

        verify(taskService, times(1)).getTasksByUser(taskUser);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody())
                .hasSize(2)
                .containsExactly(t1, t2);
    }

    @Test
    void getTasksByUser_WithNoTasks_ShouldReturnEmptyList() {
        Long taskUser = 1L;
        when(taskService.getTasksByUser(taskUser)).thenReturn(List.of());

        ResponseEntity<List<Task>> response = taskController.getTasksByUser(taskUser);

        verify(taskService, times(1)).getTasksByUser(taskUser);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEmpty();
    }

    @Test
    void getTaskById_WithValidId_ShouldReturnTask() {
        Task task = generateTask();
        Long taskId = 1L;
        when(taskService.getTaskById(taskId)).thenReturn(task);

        ResponseEntity<Task> response = taskController.getTaskById(taskId);

        verify(taskService, times(1)).getTaskById(taskId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(task);
    }

    @Test
    void createTask_WithValidPayload_ShouldReturnCreatedTask() {
        Long userId = 1L;
        TaskPayload payload = generateTaskPayload();
        Task task = generateTask();
        when(taskService.createTask(userId, payload)).thenReturn(task);

        ResponseEntity<Task> response = taskController.createTask(userId, payload);

        verify(taskService, times(1)).createTask(userId, payload);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(task);
    }

    @Test
    void updateTask_WithValidIdAndPayload_ShouldReturnUpdatedTask() {
        Long taskId = 1L;
        TaskPayload payload = generateTaskPayload();
        Task task = generateTask();
        when(taskService.updateTask(taskId, payload)).thenReturn(task);

        ResponseEntity<Task> response = taskController.updateTask(taskId, payload);

        verify(taskService, times(1)).updateTask(taskId, payload);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(task);
    }

    @Test
    void deleteTask_WithValidId_ShouldCallDeleteMethodOnce() {
        Long taskId = 1L;
        doNothing().when(taskService).deleteTask(taskId);

        ResponseEntity<Void> response = taskController.deleteTask(taskId);

        verify(taskService, times(1)).deleteTask(taskId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    private Task generateTask() {
        return Task.builder()
                .id(1L)
                .userId(2L)
                .name("name")
                .statement("state")
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
