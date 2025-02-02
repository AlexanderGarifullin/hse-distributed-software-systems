package hse.dss.diss.controller;

import hse.dss.diss.entity.Task;
import hse.dss.diss.repository.storage.TaskInMemoryStorage;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class TestControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskInMemoryStorage taskInMemoryStorage;

    private static final String BASE_URL = "/tests/task/";

    @BeforeEach
    void setUp() {
        Task task = generateTask();
        taskInMemoryStorage.create(1L, task);
    }

    @AfterEach
    void clear() {
        taskInMemoryStorage.clear();
    }

    @Test
    void testGenerateTestByTask_isSuccess() throws Exception {
        mockMvc.perform(get(BASE_URL + "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Some test"));
    }

    @Test
    void testGenerateTestByTask_NotFound() throws Exception {
        mockMvc.perform(get(BASE_URL + "999"))
                .andExpect(status().isNotFound());
    }


    private Task generateTask() {
        return Task.builder()
                .name("name")
                .statement("state")
                .id(1L)
                .userId(1L)
                .build();
    }
}