package hse.dss.service;

import hse.dss.entity.Test;
import hse.dss.repository.TestRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TestService {

    private final TestRepository testRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final RestTemplate restTemplate;

    private static final String TOPIC_GENERATE_TESTS = "generate-tests";

    @Value("${export.service.url:http://localhost:8083}")
    private String exportServiceUrl;

    public List<Test> getTestsByTaskId(Long taskId) {
        return testRepository.findByTaskId(taskId);
    }


    // Получение одного теста по id
    public Test getTestById(Integer testId) {
        return testRepository.findById(testId).orElse(null);
    }

    // Обновление теста
    @Transactional
    public Test updateTest(Test test, String input) {
        test.setInput(input);
        return testRepository.save(test);
    }

    // Удаление теста
    @Transactional
    public void deleteTest(Integer testId) {
        testRepository.deleteById(testId);
    }

    // Асинхронное создание тестов: отправляем сообщение в Kafka
    public void requestTestGeneration(Long taskId, int count) {
        String message = String.format("{\"taskId\": %d, \"count\": %d}", taskId, count);
        kafkaTemplate.send(TOPIC_GENERATE_TESTS, message);
        log.info("Sent test generation request for task {} with count {}", taskId, count);
    }

    // Синхронный вызов микросервиса экспорта через RestTemplate
    public byte[] exportTests(Long taskId) {
        String url = exportServiceUrl + "/export/tests?taskId=" + taskId;
        byte[] zipBytes = restTemplate.getForObject(url, byte[].class);
        log.info("Exported tests for task {}", taskId);
        return zipBytes;
    }
}
