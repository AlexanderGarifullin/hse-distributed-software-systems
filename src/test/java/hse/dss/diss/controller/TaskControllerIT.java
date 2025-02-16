package hse.dss.diss.controller;

import hse.dss.diss.entity.Task;
import hse.dss.diss.repository.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDateTime;

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@Testcontainers
@SpringBootTest
@AutoConfigureMockMvc
@ExtendWith(SpringExtension.class)
class TaskControllerIT {

    @Container
    public static PostgreSQLContainer<?> postgresContainer = new PostgreSQLContainer<>("postgres:17")
            .withDatabaseName("dss")
            .withUsername("postgres")
            .withPassword("123");

    @DynamicPropertySource
    static void overrideProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", postgresContainer::getJdbcUrl);
        registry.add("spring.datasource.username", postgresContainer::getUsername);
        registry.add("spring.datasource.password", postgresContainer::getPassword);
        registry.add("spring.liquibase.enabled", () -> true);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskRepository taskRepository;

    @BeforeEach
    public void setup() {
        taskRepository.deleteAll();
    }

    @Test
    public void listTasks_whenTasksExist_thenReturnTaskList() throws Exception {
        Task task = new Task();
        task.setTimeLimit(500);
        task.setMemoryLimit(100);
        task.setName("Test Task");
        task.setCreatedAt(LocalDateTime.now());
        taskRepository.save(task);

        var mvcResult = mockMvc.perform(get("/tasks"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(view().name("tasks"))
                .andExpect(model().attributeExists("tasks"))
                .andExpect(model().attribute("tasks", hasItem(
                        hasProperty("name", is("Test Task"))
                )));
    }

    @Test
    public void newTaskForm_whenCalled_thenReturnNewTaskForm() throws Exception {
        mockMvc.perform(get("/tasks/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("newTask"))
                .andExpect(model().attributeExists("task"));
    }

    @Test
    public void createTask_whenInvalidInput_thenReturnNewTaskFormWithValidationErrors() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/tasks")
                        .param("timeLimit", "200")
                        .param("memoryLimit", "3")
                        .param("name", ""))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(view().name("newTask"))
                .andExpect(model().attributeHasFieldErrors("task", "timeLimit"))
                .andExpect(model().attributeHasFieldErrors("task", "memoryLimit"))
                .andExpect(model().attributeHasFieldErrors("task", "name"));
    }

    @Test
    public void createTask_whenValidInput_thenRedirectToTaskList() throws Exception {
        MvcResult mvcResult = mockMvc.perform(post("/tasks")
                        .param("timeLimit", "500")
                        .param("memoryLimit", "100")
                        .param("name", "New Task"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }

    @Test
    public void editTaskForm_whenTaskExists_thenReturnEditTaskForm() throws Exception {
        Task task = new Task();
        task.setTimeLimit(500);
        task.setMemoryLimit(100);
        task.setName("Edit Task");
        task.setCreatedAt(LocalDateTime.now());
        taskRepository.save(task);

        var mvcResult = mockMvc.perform(get("/tasks/" + task.getId() + "/edit"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(view().name("editTask"))
                .andExpect(model().attribute("task", hasProperty("name", is("Edit Task"))));
    }

    @Test
    public void editTaskForm_whenTaskNotFound_thenRedirectToTaskList() throws Exception {
        var mvcResult = mockMvc.perform(get("/tasks/9999/edit"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }

    @Test
    public void updateTask_whenInvalidInput_thenReturnEditTaskFormWithValidationErrors() throws Exception {
        Task task = new Task();
        task.setTimeLimit(500);
        task.setMemoryLimit(100);
        task.setName("Update Task");
        task.setCreatedAt(LocalDateTime.now());
        taskRepository.save(task);

        MvcResult mvcResult = mockMvc.perform(post("/tasks/" + task.getId())
                        .param("timeLimit", "200")
                        .param("memoryLimit", "3")
                        .param("name", ""))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().isOk())
                .andExpect(view().name("editTask"))
                .andExpect(model().attributeHasFieldErrors("task", "timeLimit"))
                .andExpect(model().attributeHasFieldErrors("task", "memoryLimit"))
                .andExpect(model().attributeHasFieldErrors("task", "name"));
    }

    @Test
    public void updateTask_whenValidInput_thenRedirectToTaskList() throws Exception {
        Task task = new Task();
        task.setTimeLimit(500);
        task.setMemoryLimit(100);
        task.setName("Old Task");
        task.setCreatedAt(LocalDateTime.now());
        taskRepository.save(task);

        MvcResult mvcResult = mockMvc.perform(post("/tasks/" + task.getId())
                        .param("timeLimit", "600")
                        .param("memoryLimit", "150")
                        .param("name", "Updated Task"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }

    @Test
    public void deleteTask_whenCalled_thenRedirectToTaskList() throws Exception {
        Task task = new Task();
        task.setTimeLimit(500);
        task.setMemoryLimit(100);
        task.setName("Task to Delete");
        task.setCreatedAt(LocalDateTime.now());
        taskRepository.save(task);

        var mvcResult = mockMvc.perform(post("/tasks/" + task.getId() + "/delete"))
                .andExpect(request().asyncStarted())
                .andReturn();

        mockMvc.perform(asyncDispatch(mvcResult))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/tasks"));
    }

}