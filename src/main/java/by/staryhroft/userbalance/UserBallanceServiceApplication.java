package by.staryhroft.userbalance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableCaching
public class UserBallanceServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserBallanceServiceApplication.class, args);
    }
}
