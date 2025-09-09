package com.xcode.loanservice.consumer;

import com.xcode.loanservice.consumer.dto.UserValidationResponse;
import com.xcode.loanservice.consumer.exception.UserNotFoundException;
import com.xcode.loanservice.model.user.gateways.UserRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.github.resilience4j.timelimiter.annotation.TimeLimiter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserRestConsumer implements UserRepository {
    private final WebClient client;


    @Override
    @CircuitBreaker(name = "userservice", fallbackMethod = "validateUserActiveFallback")
    @Retry(name = "userservice", fallbackMethod = "validateUserActiveFallback")
    @TimeLimiter(name = "userservice", fallbackMethod = "validateUserActiveFallback")
    public Mono<Boolean> existsByDocument(String documento) {
        log.info("Validate User {} is active", documento);

        return client
                .get()
                .uri("/api/v1/users/by-document/{document}", documento)
                .retrieve()
                .bodyToMono(UserValidationResponse.class)
                .timeout(Duration.ofSeconds(3))
                .map(UserValidationResponse::isActive)
                .doOnSuccess(isActive -> log.info("User {} activo: {}", documento, isActive ? "ACTIVO" : "INACTIVO"))
                .doOnError(error -> log.warn("Error validando usuario {}: {}", documento, error.getMessage()))
                .onErrorResume(ex -> Mono.error(new Exception("Error validando usuario " + documento)));
    }

    public Mono<Boolean> validateUserActiveFallback(String documento, Exception ex) {
        log.warn("Fallback activado para usuario: {}. Error: {}", documento, ex.getMessage());
        if (ex instanceof UserNotFoundException) {
            return Mono.just(false);
        }
        log.warn("Tipo de excepción: {}", ex.getClass().getSimpleName());
        return Mono.just(false);
    }

}
