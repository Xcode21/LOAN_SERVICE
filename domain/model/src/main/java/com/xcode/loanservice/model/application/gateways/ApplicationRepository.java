package com.xcode.loanservice.model.application.gateways;

import com.xcode.loanservice.model.application.Application;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ApplicationRepository {
    Mono<Application> save(Application application);
    Mono<Application> findById(UUID id);
    Flux<Application> findAllByDocument(String document);
}
