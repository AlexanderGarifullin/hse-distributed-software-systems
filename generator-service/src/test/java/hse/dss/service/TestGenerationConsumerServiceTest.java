package hse.dss.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hse.dss.dto.TestGenerationRequest;
import hse.dss.entity.Task;
import hse.dss.entity.Test;
import hse.dss.repository.TaskRepository;
import hse.dss.repository.TestRepository;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestGenerationConsumerServiceTest {

    @Mock
    private TestRepository testRepository;

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private TestGenerationConsumerService consumerService;

    private final ObjectMapper objectMapper = new ObjectMapper();


    @org.junit.jupiter.api.Test
    void listenGenerateTests_validMessage_taskExists() throws Exception {

        Long taskId = 1L;
        int count = 3;
        TestGenerationRequest request = new TestGenerationRequest(taskId, count);
        String jsonMessage = objectMapper.writeValueAsString(request);

        Task task = new Task();
        task.setId(taskId);

        when(taskRepository.findById(taskId)).thenReturn(Optional.of(task));

        consumerService.listenGenerateTests(jsonMessage);

        verify(testRepository, times(count)).save(argThat(new ArgumentMatcher<Test>() {
            @Override
            public boolean matches(Test t) {
                return t.getTask().equals(task) && t.getInput() != null && !t.getInput().isEmpty();
            }
        }));
    }

    @org.junit.jupiter.api.Test
    void listenGenerateTests_validMessage_taskNotFound() throws Exception {
        Long taskId = 1L;
        int count = 3;
        TestGenerationRequest request = new TestGenerationRequest(taskId, count);
        String jsonMessage = objectMapper.writeValueAsString(request);

        when(taskRepository.findById(taskId)).thenReturn(Optional.empty());

        consumerService.listenGenerateTests(jsonMessage);

        verify(testRepository, never()).save(any());
    }

    @org.junit.jupiter.api.Test
    void listenGenerateTests_invalidJsonMessage() {
        String invalidJson = "this is not valid json";

        consumerService.listenGenerateTests(invalidJson);

        verify(testRepository, never()).save(any());
    }
}
