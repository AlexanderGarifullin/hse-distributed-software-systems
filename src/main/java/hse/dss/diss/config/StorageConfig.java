package hse.dss.diss.config;

import hse.dss.diss.entity.User;
import hse.dss.diss.repository.storage.HashMapStorage;
import hse.dss.diss.repository.storage.InMemoryStorage;
import hse.dss.diss.repository.storage.TaskInMemoryStorage;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * StorageConfig defines the configuration of in-memory storages for tasks and users.
 * It provides beans for the task and user storage mechanisms.
 */
@Configuration
public class StorageConfig {


    /**
     * Provides a bean for {@link TaskInMemoryStorage}.
     *
     * @return an instance of TaskInMemoryStorage
     */
    @Bean
    public TaskInMemoryStorage taskInMemoryStorage() {
        return new TaskInMemoryStorage();
    }

    /**
     * Provides a bean for {@link InMemoryStorage} to store {@link User}.
     *
     * @return an instance of InMemoryStorage for users
     */
    @Bean
    public InMemoryStorage<User, Long> userInMemoryStorage() {
        return new HashMapStorage<>();
    }
}
