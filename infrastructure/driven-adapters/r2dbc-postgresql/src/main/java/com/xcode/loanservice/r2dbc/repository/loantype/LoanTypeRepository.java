package com.xcode.loanservice.r2dbc.repository.loantype;

import com.xcode.loanservice.r2dbc.entity.LoanTypeEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface LoanTypeRepository extends ReactiveCrudRepository<LoanTypeEntity, Integer>, ReactiveQueryByExampleExecutor<LoanTypeEntity> {

    /*Mono<Boolean> existsById(Integer id);*/
    Mono<LoanTypeEntity>findById(Integer id);
}
