package com.xcode.loanservice.r2dbc.repository.loanstatus;


import com.xcode.loanservice.model.loanstatus.LoanStatus;
import com.xcode.loanservice.r2dbc.entity.LoanStatusEntity;
import com.xcode.loanservice.r2dbc.helper.ReactiveAdapterOperations;
import com.xcode.loanservice.r2dbc.mapper.LoanStatusMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
@Slf4j
public class LoanStatusRepositoryAdapter extends ReactiveAdapterOperations<
        LoanStatus,
        LoanStatusEntity,
    Integer,
        LoanStatusRepository
> implements com.xcode.loanservice.model.loanstatus.gateways.LoanStatusRepository {
    private final LoanStatusMapper loanStatusMapper;
    public LoanStatusRepositoryAdapter(LoanStatusRepository repository, ObjectMapper mapper, LoanStatusMapper loanStatusMapper) {

        super(repository, mapper, d -> mapper.map(d, LoanStatus.class));
        this.loanStatusMapper = loanStatusMapper;
    }


    @Override
    public Mono<LoanStatus> findByName(String name) {
        return repository.findByName(name)
                .doOnSubscribe(sub -> log.info("Finding LoanStatus with name: {}", name))
                .map(loanStatusMapper::toDomain)
                .doOnNext(LoanStatus -> log.info("LoanStatus found: {} ", name))
                .doOnError(error -> log.error("Database error finding LoanStatus with role. NAME: {}, Error: {}", name, error.getClass().getSimpleName(), error));

    }




}
