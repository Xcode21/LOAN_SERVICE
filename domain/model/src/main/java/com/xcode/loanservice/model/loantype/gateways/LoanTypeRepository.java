package com.xcode.loanservice.model.loantype.gateways;

import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    Mono<Boolean> existsById(Integer id);
}
