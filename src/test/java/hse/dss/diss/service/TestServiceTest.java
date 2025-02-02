package hse.dss.diss.service;

import hse.dss.diss.entity.Task;
import hse.dss.diss.repository.storage.TaskInMemoryStorage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
class TestServiceTest {


    @InjectMocks
    private TestService testService;

    @Mock
    private TaskInMemoryStorage taskInMemoryStorage;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateTest_WhenTaskExists_ShouldReturnExpectedTestString() {
        Long taskId = 1L;
        Task task = generateTask();
        when(taskInMemoryStorage.findById(taskId)).thenReturn(Optional.ofNullable(task));

        String msg = testService.generateTest(taskId);

        verify(taskInMemoryStorage, times(1)).findById(taskId);
        assertThat(msg)
                .isEqualTo("Some test");
    }


    private Task generateTask() {
        return Task.builder()
                .id(1L)
                .userId(2L)
                .name("name")
                .statement("state")
                .build();
    }
}