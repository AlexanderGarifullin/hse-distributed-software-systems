package hse.dss.diss.service;

import hse.dss.diss.entity.User;
import hse.dss.diss.repository.storage.InMemoryStorage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Profile("prod")
@RequiredArgsConstructor
public class DataLoaderService {

    private final InMemoryStorage<User, Long> userInMemoryStorage;

    @EventListener(ApplicationStartedEvent.class)
    public void initData() {
        log.info("Starting data initialization...");
        initUsers();
        log.info("Data initialization completed.");
    }

    private void initUsers() {
        log.info("Starting initialization of user data...");
        User user = createStarterUser();
        userInMemoryStorage.create(user.getId(), user);
        log.info("User data initialization completed.");
    }

    private User createStarterUser() {
        return User.builder()
                .id(1L)
                .login("STARTER")
                .build();
    }
}
