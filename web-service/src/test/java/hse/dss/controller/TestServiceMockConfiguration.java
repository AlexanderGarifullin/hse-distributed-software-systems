package hse.dss.controller;

import hse.dss.service.TestService;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import static org.mockito.Mockito.mock;

@TestConfiguration
public class TestServiceMockConfiguration {

    @Bean
    public TestService testService() {
        return mock(TestService.class);
    }
}
