package com.xcode.loanservice.r2dbc.repository.loanstatus;

import com.xcode.loanservice.r2dbc.entity.LoanStatusEntity;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

// TODO: This file is just an example, you should delete or modify it
public interface LoanStatusRepository extends ReactiveCrudRepository<LoanStatusEntity, Integer>, ReactiveQueryByExampleExecutor<LoanStatusEntity> {

    /*Mono<Boolean> existsById(Integer id);*/
    Mono<LoanStatusEntity>findByName(String name);
}
