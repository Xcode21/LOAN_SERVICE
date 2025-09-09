package com.xcode.loanservice.model.loantype.gateways;

import com.xcode.loanservice.model.loantype.LoanType;
import reactor.core.publisher.Mono;

public interface LoanTypeRepository {
    /*Mono<Boolean> existsById(Integer id);*/
    Mono<LoanType>findById(Integer id);
}
