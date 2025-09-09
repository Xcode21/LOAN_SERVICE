package com.xcode.loanservice.model.user.gateways;

import reactor.core.publisher.Mono;

public interface UserRepository {
    Mono<Boolean> existsByDocument(String documento);



}
