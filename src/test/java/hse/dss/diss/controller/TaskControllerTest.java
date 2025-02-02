package hse.dss.diss.controller;

import hse.dss.diss.entity.Task;
import hse.dss.diss.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;


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
    void testGetTasksByUser() {
        Task t1 = Task.builder().build();
        Task t2 = Task.builder().build();
        Long taskUser = 1L;
        when(taskService.getTasksByUser(taskUser)).thenReturn(List.of(t1, t2));

        List<Task> response = taskService.getTasksByUser(taskUser);

        assertThat(response)
                .hasSize(2)
                .containsExactly(t1, t2);
    }


}