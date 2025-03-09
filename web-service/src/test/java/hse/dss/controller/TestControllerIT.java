package hse.dss.controller;

import hse.dss.entity.Test;
import hse.dss.service.TestService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TestController.class)
@Import(TestServiceMockConfiguration.class)
public class TestControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TestService testService;

    @org.junit.jupiter.api.Test
    public void listTests_shouldReturnTestsPage() throws Exception {
        Long taskId = 1L;
        Test test = new Test();
        test.setId(100);
        test.setInput("some input");
        List<Test> tests = Collections.singletonList(test);
        when(testService.getTestsByTaskId(taskId)).thenReturn(tests);

        mockMvc.perform(get("/tasks/{taskId}/tests", taskId))
                .andExpect(status().isOk())
                .andExpect(view().name("tests"))
                .andExpect(model().attributeExists("tests"))
                .andExpect(model().attribute("taskId", taskId))
                .andExpect(model().attribute("tests", hasItem(hasProperty("id", is(100)))));
    }

    @org.junit.jupiter.api.Test
    public void downloadTests_shouldReturnZipFile() throws Exception {
        Long taskId = 1L;
        byte[] zipBytes = "zip data".getBytes();
        when(testService.exportTests(taskId)).thenReturn(zipBytes);

        mockMvc.perform(get("/tasks/{taskId}/tests/download", taskId))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE))
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, containsString("form-data; name=\"attachment\"; filename=\"tests_" + taskId + ".zip\"")))
                .andExpect(content().bytes(zipBytes));
    }

    @org.junit.jupiter.api.Test
    public void generateTests_shouldRedirectToTestsPage() throws Exception {
        Long taskId = 1L;
        int count = 5;
        doNothing().when(testService).requestTestGeneration(taskId, count);

        mockMvc.perform(post("/tasks/{taskId}/tests/generate", taskId)
                        .param("count", String.valueOf(count)))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/" + taskId + "/tests"));

        verify(testService).requestTestGeneration(taskId, count);
    }

    @org.junit.jupiter.api.Test
    public void editTestForm_shouldReturnEditTestPage() throws Exception {
        Long taskId = 1L;
        Integer testId = 100;
        Test test = new Test();
        test.setId(testId);
        test.setInput("input data");
        when(testService.getTestById(testId)).thenReturn(test);

        mockMvc.perform(get("/tasks/{taskId}/tests/{testId}/edit", taskId, testId))
                .andExpect(status().isOk())
                .andExpect(view().name("editTest"))
                .andExpect(model().attributeExists("test"))
                .andExpect(model().attribute("taskId", taskId))
                .andExpect(model().attribute("test", hasProperty("input", is("input data"))));
    }

    @org.junit.jupiter.api.Test
    public void updateTest_shouldRedirectToTestsPage() throws Exception {
        Long taskId = 1L;
        Integer testId = 100;
        Test test = new Test();
        test.setId(testId);
        when(testService.getTestById(testId)).thenReturn(test);
        when(testService.updateTest(test, "new input")).thenReturn(test);

        mockMvc.perform(post("/tasks/{taskId}/tests/{testId}", taskId, testId)
                        .param("input", "new input"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/" + taskId + "/tests"));

        verify(testService).updateTest(test, "new input");
    }

    @org.junit.jupiter.api.Test
    public void deleteTest_shouldRedirectToTestsPage() throws Exception {
        Long taskId = 1L;
        Integer testId = 100;
        doNothing().when(testService).deleteTest(testId);

        mockMvc.perform(post("/tasks/{taskId}/tests/{testId}/delete", taskId, testId))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks/" + taskId + "/tests"));

        verify(testService).deleteTest(testId);
    }
}
