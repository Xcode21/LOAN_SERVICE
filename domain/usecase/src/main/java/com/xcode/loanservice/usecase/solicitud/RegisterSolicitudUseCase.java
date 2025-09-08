package com.xcode.loanservice.usecase.solicitud;

import com.xcode.loanservice.model.application.Application;
import com.xcode.loanservice.model.application.gateways.ApplicationRepository;
import com.xcode.loanservice.model.application.gateways.TransactionalExecutor;
import com.xcode.loanservice.model.common.exception.LoanTypeNotFoundException;
import com.xcode.loanservice.model.loantype.gateways.LoanTypeRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class RegisterSolicitudUseCase {
    private final ApplicationRepository applicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final TransactionalExecutor txExecutor;

    public Mono<Application> execute(Application application) {
        return loanTypeRepository.existsById(application.getLoanType().getIdLoanType())
                .filter(Boolean::booleanValue)
                .switchIfEmpty(Mono.error(new LoanTypeNotFoundException(application.getLoanType().getIdLoanType())))
                .flatMap(exists -> applicationRepository.save(application))
                .flatMap(applicationEntity->applicationRepository.findById(applicationEntity.getIdApplication()))
                .as(txExecutor::executeInTransaction);
    }
}
