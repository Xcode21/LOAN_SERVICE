package com.xcode.loanservice.model.application.gateways;

import com.xcode.loanservice.model.application.Application;
import com.xcode.loanservice.model.common.exception.vo.ApplicationSeachCriteria;
import com.xcode.loanservice.model.common.exception.vo.PageRequest;
import com.xcode.loanservice.model.common.exception.vo.PaginatedResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ApplicationRepository {
    Mono<Application> save(Application application);
    Mono<Application> findById(UUID id);
    Flux<Application> findAllByDocument(String document);
    Mono<PaginatedResponse<Application>> findPending(ApplicationSeachCriteria criteria, PageRequest pageRequest);
    Flux<Application> findApproveByEmail(String email);


}