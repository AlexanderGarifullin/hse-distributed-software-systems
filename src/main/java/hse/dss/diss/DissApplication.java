package hse.dss.diss;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
public class DissApplication {

	public static void main(String[] args) {
		SpringApplication.run(DissApplication.class, args);
	}

}
