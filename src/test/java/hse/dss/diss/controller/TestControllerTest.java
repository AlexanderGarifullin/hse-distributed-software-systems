package hse.dss.diss.controller;

import hse.dss.diss.service.TestService;
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
class TestControllerTest {

    @InjectMocks
    private TestController testController;

    @Mock
    private TestService testService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void generateTest_WithValidTaskId_ShouldReturnGeneratedTest() {
        Long taskId = 1L;
        String test = "Some test";
        when(testService.generateTest(taskId)).thenReturn(test);

        ResponseEntity<String> response = testController.generateTestByTask(taskId);

        verify(testService, times(1)).generateTest(taskId);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isEqualTo(test);
    }

}