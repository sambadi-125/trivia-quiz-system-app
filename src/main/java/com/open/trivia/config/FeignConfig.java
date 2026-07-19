package com.open.trivia.config;

import com.open.trivia.utils.FeignErrorDecoder;
import feign.Retryer;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.TimeUnit;

@Configuration
@Slf4j
public class FeignConfig {

    @Bean
    public ErrorDecoder errorDecoder() {
        return new FeignErrorDecoder();
    }

    @Bean
    public Retryer retryer(
            @Value("${feign.retry.interval}") long interval,
            @Value("${feign.retry.max-interval}") long maxInterval,
            @Value("${feign.retry.attempts}") int totalAttempts
    ) {
        return new Retryer.Default(interval, maxInterval, totalAttempts);
    }
}
