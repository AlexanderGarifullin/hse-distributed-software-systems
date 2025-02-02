package hse.dss.diss.controller;

import hse.dss.diss.service.TestService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.ActiveProfiles;


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


}