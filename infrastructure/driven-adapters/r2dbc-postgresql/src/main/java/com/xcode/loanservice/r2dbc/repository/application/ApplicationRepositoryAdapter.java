package com.xcode.loanservice.r2dbc.repository.application;

import com.xcode.loanservice.model.application.Application;
import com.xcode.loanservice.r2dbc.entity.ApplicationEntity;
import com.xcode.loanservice.r2dbc.helper.ReactiveAdapterOperations;
import com.xcode.loanservice.r2dbc.mapper.ApplicationMapper;
import lombok.extern.slf4j.Slf4j;
import org.reactivecommons.utils.ObjectMapper;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
@Slf4j
public class ApplicationRepositoryAdapter extends ReactiveAdapterOperations<
        Application,
        ApplicationEntity,
        UUID,
        ApplicationRepository
        > implements com.xcode.loanservice.model.application.gateways.ApplicationRepository {
    private final ApplicationMapper mapper;

    public ApplicationRepositoryAdapter(ApplicationRepository repository, ObjectMapper mapper, ApplicationMapper mapper1) {
        super(repository, mapper, d -> mapper.map(d, Application.class));
        this.mapper = mapper1;
    }

    @Override
    public Flux<Application> findAllByDocument(String document) {
        return null;
    }

    @Override
    public Mono<Application> save(Application application) {
        return repository.save(mapper.toEntity(application))
                .doOnSubscribe(sub -> log.info("Saving application - Document: {}", application.getDocument()))
                .map(mapper::toDomain)
                .doOnNext(savedApplication -> log.info("application saved successfully - Document: {}", savedApplication.getDocument()))
                .doOnError(error -> log.error("Error saving application - Document: {}, Error: {}", application.getDocument(), error.getClass().getSimpleName(), error));

    }

    @Override
    public Mono<Application> findById(UUID id) {
        return repository.findByIdProjection(id)
                .doOnSubscribe(sub -> log.info("Finding application - Id: {}", id))
                .map(mapper::toDomain)
                .doOnNext(application -> log.info("application finding with  - document: {}", application.getDocument()))
                .doOnError(error -> log.error("Error finding application - ID: {}, Error: {}", id, error.getClass().getSimpleName(), error));

    }


}
