package com.xcode.loanservice.r2dbc.repository.application;


import com.xcode.loanservice.model.application.gateways.TransactionalExecutor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class TransactionalExecutorAdapter implements TransactionalExecutor {
    private final TransactionalOperator txOperator;

    @Override
    public <T> Mono<T> executeInTransaction(Mono<T> publisher) {
        return publisher.as(txOperator::transactional);
    }
}
