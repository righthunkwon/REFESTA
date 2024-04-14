package com.a601.refesta;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class RefestaApplication {

    public static void main(String[] args) {
        SpringApplication.run(RefestaApplication.class, args);
    }
}
