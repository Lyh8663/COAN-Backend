package org.coan;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import springfox.documentation.oas.annotations.EnableOpenApi;

@SpringBootApplication
@EnableScheduling
@EnableOpenApi
@EnableWebMvc
public class CoanBackEndApplication {

    public static void main(String[] args) {
        SpringApplication.run(CoanBackEndApplication.class, args);
    }

}
