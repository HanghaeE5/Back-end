package com.example.backend;

import com.example.backend.user.config.AppProperties;
import com.example.backend.user.config.CorsProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableJpaAuditing
@SpringBootApplication
@PropertySources({ @PropertySource("classpath:application-aws.properties") })
@EnableConfigurationProperties({CorsProperties.class, AppProperties.class})
@EnableScheduling
public class BackEndApplication {
    public static void main(String[] args) {
        SpringApplication.run(BackEndApplication.class, args);
    }

}
