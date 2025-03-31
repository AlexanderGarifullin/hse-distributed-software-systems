package hse.dss.diss.config;

import hse.dss.diss.mapper.TaskMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MapperConfig {

    @Bean
    public TaskMapper taskMapper() {
        return new TaskMapper();
    }
}
