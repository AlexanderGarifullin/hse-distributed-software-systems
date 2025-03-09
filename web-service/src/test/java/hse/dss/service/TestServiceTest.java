package hse.dss.service;

import hse.dss.entity.Test;
import hse.dss.repository.TestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceTest {

    @Mock
    private TestRepository testRepository;

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TestService testService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(testService, "exportServiceUrl", "http://localhost:8083");
    }

    @org.junit.jupiter.api.Test
    void getTestsByTaskId_returnsTests() {
        Long taskId = 1L;
        List<Test> tests = Arrays.asList(new Test());
        when(testRepository.findByTaskId(taskId)).thenReturn(tests);

        List<Test> result = testService.getTestsByTaskId(taskId);

        assertEquals(tests, result);
        verify(testRepository).findByTaskId(taskId);
    }

    @org.junit.jupiter.api.Test
    void getTestById_whenExists() {
        Integer testId = 1;
        Test test = new Test();
        test.setId(testId);
        when(testRepository.findById(testId)).thenReturn(Optional.of(test));

        Test result = testService.getTestById(testId);

        assertEquals(test, result);
        verify(testRepository).findById(testId);
    }

    @org.junit.jupiter.api.Test
    void getTestById_whenNotFound() {
        Integer testId = 1;
        when(testRepository.findById(testId)).thenReturn(Optional.empty());

        Test result = testService.getTestById(testId);

        assertNull(result);
        verify(testRepository).findById(testId);
    }

    @org.junit.jupiter.api.Test
    void updateTest_updatesInput() {
        Test test = new Test();
        test.setInput("old input");
        String newInput = "new input";
        when(testRepository.save(test)).thenReturn(test);

        Test result = testService.updateTest(test, newInput);

        assertEquals(newInput, result.getInput());
        verify(testRepository).save(test);
    }

    @org.junit.jupiter.api.Test
    void deleteTest_callsRepositoryDelete() {
        Integer testId = 1;
        testService.deleteTest(testId);
        verify(testRepository).deleteById(testId);
    }

    @org.junit.jupiter.api.Test
    void requestTestGeneration_sendsKafkaMessage() {
        Long taskId = 1L;
        int count = 5;
        String expectedMessage = "{\"taskId\": 1, \"count\": 5}";

        testService.requestTestGeneration(taskId, count);

        verify(kafkaTemplate).send("generate-tests", expectedMessage);
    }

    @org.junit.jupiter.api.Test
    void exportTests_returnsZipBytes() {
        Long taskId = 1L;
        byte[] expectedBytes = "zip data".getBytes();
        String url = "http://localhost:8083/export/tests?taskId=" + taskId;

        when(restTemplate.getForObject(url, byte[].class)).thenReturn(expectedBytes);

        byte[] result = testService.exportTests(taskId);

        assertArrayEquals(expectedBytes, result);
        verify(restTemplate).getForObject(url, byte[].class);
    }
}
