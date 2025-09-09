package com.xcode.loanservice.r2dbc.repository.loantype;

import com.xcode.loanservice.model.loantype.LoanType;
import com.xcode.loanservice.r2dbc.entity.LoanTypeEntity;
import com.xcode.loanservice.r2dbc.helper.ReactiveAdapterOperations;
import com.xcode.loanservice.r2dbc.mapper.LoanTypeMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class LoanTypeRepositoryAdapter extends ReactiveAdapterOperations<
        LoanType,
        LoanTypeEntity,
    Integer,
        LoanTypeRepository
> implements com.xcode.loanservice.model.loantype.gateways.LoanTypeRepository {
    private final LoanTypeMapper loanTypeMapper;
    public LoanTypeRepositoryAdapter(LoanTypeRepository repository, ObjectMapper mapper, LoanTypeMapper loanTypeMapper) {

        super(repository, mapper, d -> mapper.map(d, LoanType.class));
        this.loanTypeMapper = loanTypeMapper;
    }

    @Override
    public Mono<LoanType> findById(Integer id) {
        return repository.findById(id)
                .doOnSubscribe(sub -> log.info("Finding LoanType with role: {}", id))
                .map(loanTypeMapper::toDomain)
                .doOnNext(LoanType -> log.info("LoanType found: {} (Role: {})", LoanType.getIdLoanType(), LoanType.getName()))
                .doOnError(error -> log.error("Database error finding LoanType with role. ID: {}, Error: {}", id, error.getClass().getSimpleName(), error));
    }

   /* @Override
    public Mono<Boolean> existsById(Integer id) {
        return super.findById(id).;
    }*/


}
