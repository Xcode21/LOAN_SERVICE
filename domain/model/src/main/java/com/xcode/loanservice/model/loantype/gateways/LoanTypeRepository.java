package com.xcode.loanservice.model.loantype.gateways;

import com.xcode.loanservice.model.loantype.LoanType;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface LoanTypeRepository {
    Mono<LoanType> findById(UUID id);
}
