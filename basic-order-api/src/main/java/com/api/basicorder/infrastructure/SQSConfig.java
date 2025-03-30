package com.api.basicorder.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SQSConfig {

    @Bean
    public String sqsClient() {
        log.info("Configuring SQSClient");
        return "SQS configuration";
    }

}
