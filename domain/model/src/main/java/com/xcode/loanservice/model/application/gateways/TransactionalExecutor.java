package com.xcode.loanservice.model.application.gateways;

import reactor.core.publisher.Mono;

public interface TransactionalExecutor {
    <T> Mono<T> executeInTransaction(Mono<T> publisher);
}
