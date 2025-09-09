package com.xcode.loanservice.model.loanstatus.gateways;

import com.xcode.loanservice.model.loanstatus.LoanStatus;
import reactor.core.publisher.Mono;

public interface LoanStatusRepository {
    Mono<LoanStatus> findByName(String name);
}
