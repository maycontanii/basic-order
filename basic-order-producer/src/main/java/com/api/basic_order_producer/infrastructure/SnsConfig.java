package com.api.basic_order_producer.infrastructure;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.net.URI;

@Slf4j
@Configuration
public class SnsConfig {

    private final String ENDPOINT = "http://localhost:4566";
    private final String REGION = "us-east-1";

    @Bean
    public SnsClient snsClient() {
        log.info("Configuring snsClient");

        try {
            return SnsClient.builder()
                    .region(Region.of(REGION))
                    .endpointOverride(URI.create(ENDPOINT))
                    .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create("test", "test")))
                    .build();
        } catch (Exception e) {
            log.error("Connection failed to snsClient");
            throw new RuntimeException(e);
        }
    }
}
