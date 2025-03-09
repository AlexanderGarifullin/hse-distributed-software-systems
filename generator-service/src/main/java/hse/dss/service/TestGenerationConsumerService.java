package hse.dss.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import hse.dss.dto.TestGenerationRequest;
import hse.dss.entity.Task;
import hse.dss.entity.Test;
import hse.dss.repository.TaskRepository;
import hse.dss.repository.TestRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestGenerationConsumerService {

    private final TestRepository testRepository;
    private final TaskRepository taskRepository;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @KafkaListener(topics = "generate-tests", groupId = "test-generation-group")
    public void listenGenerateTests(String message) {
        try {
            // Преобразуем JSON-сообщение в POJO
            TestGenerationRequest request = objectMapper.readValue(message, TestGenerationRequest.class);
            Long taskId = request.taskId();
            int count = request.count();
            log.info("Received test generation request for task {} with count {}", taskId, count);

            // Найдем задачу по taskId
            Optional<Task> taskOptional = taskRepository.findById(taskId);
            if (!taskOptional.isPresent()) {
                log.error("Task with id {} not found", taskId);
                return;
            }
            Task task = taskOptional.get();

            // Генерируем и сохраняем указанное количество тестов
            for (int i = 0; i < count; i++) {
                Test test = new Test();
                test.setTask(task);
                // Генерация случайного input с помощью UUID
                test.setInput(UUID.randomUUID().toString());
                testRepository.save(test);
                log.info("Generated test with id {} for task {}", test.getId(), taskId);
            }
        } catch (Exception e) {
            log.error("Error processing test generation message", e);
        }
    }
}
