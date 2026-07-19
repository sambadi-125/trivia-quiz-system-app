package com.open.trivia.utils;

import feign.FeignException;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

@Slf4j
public class FeignErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        FeignException exception = feign.FeignException.errorStatus(methodKey, response);

        log.error("Feign client error. Method: {}, Status: {}",
                methodKey, status);

        return switch (status) {
            case TOO_MANY_REQUESTS, SERVICE_UNAVAILABLE -> {
                log.warn("Starting retry mechanism. Method: {}, Status: {}",
                        methodKey, status);
                yield createRetryableException(response, exception, null);
            }
            default -> new Exception("Unexpected error: " + response.body());
        };
    }

    private RetryableException createRetryableException(
            Response response,
            FeignException exception,
            Long retryInterval) {
        return new RetryableException(
                response.status(),
                exception.getMessage(),
                response.request().httpMethod(),
                exception,
                retryInterval,
                response.request()
        );
    }
}
