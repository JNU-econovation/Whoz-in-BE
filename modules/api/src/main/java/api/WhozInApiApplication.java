package api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {"domain", "jpa"})
public class WhozInApiApplication {

    public static void main(String[] args) {
        SpringApplication.run(WhozInApiApplication.class, args);
    }

}