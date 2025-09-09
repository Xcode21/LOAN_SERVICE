package com.xcode.loanservice.usecase.solicitud;

import com.xcode.loanservice.model.application.Application;
import com.xcode.loanservice.model.application.LoanStatusName;
import com.xcode.loanservice.model.application.gateways.ApplicationRepository;
import com.xcode.loanservice.model.application.gateways.TransactionalExecutor;
import com.xcode.loanservice.model.common.exception.LoanStatusNotFoundException;
import com.xcode.loanservice.model.common.exception.LoanTypeNotFoundException;
import com.xcode.loanservice.model.loanstatus.LoanStatus;
import com.xcode.loanservice.model.loanstatus.gateways.LoanStatusRepository;
import com.xcode.loanservice.model.loantype.LoanType;
import com.xcode.loanservice.model.loantype.gateways.LoanTypeRepository;
import com.xcode.loanservice.usecase.user.UserVaLidateUseCase;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

@RequiredArgsConstructor
public class RegisterApplicationUseCase {
    private final ApplicationRepository applicationRepository;
    private final LoanTypeRepository loanTypeRepository;
    private final LoanStatusRepository loanStatusRepository;
    private final UserVaLidateUseCase userVaLidateUseCase;
    private final TransactionalExecutor txExecutor;

    public Mono<Application> execute(Application application) {
        return userVaLidateUseCase.validateApplication(application.getDocument())
                .then(loadRequiredEntities(application))
                .flatMap(dependencies -> persistAndEnrich(application, dependencies)
                        .as(txExecutor::executeInTransaction));
    }


    private Mono<Tuple2<LoanType, LoanStatus>> loadRequiredEntities(Application application) {
        return Mono.zip(
                findLoanTypeById(application.getLoanType().getIdLoanType()),
                findPendingReviewStatus()
        );
    }

    private Mono<LoanType> findLoanTypeById(Integer loanTypeId) {
        return loanTypeRepository.findById(loanTypeId)
                .switchIfEmpty(Mono.error(() -> new LoanTypeNotFoundException(loanTypeId)));
    }

    private Mono<LoanStatus> findPendingReviewStatus() {
        return loanStatusRepository.findByName(LoanStatusName.PENDING_REVIEW.name())
                .switchIfEmpty(Mono.error(() -> new LoanStatusNotFoundException(LoanStatusName.PENDING_REVIEW.name())))
                .cache();
    }

    private Mono<Application> persistAndEnrich(Application application, Tuple2<LoanType, LoanStatus> dependencies) {
        Application newApplication = createNewApplication(application, dependencies);
        return applicationRepository.save(newApplication)
                .map(saved -> enrichApplicationWithRelations(saved, dependencies));
    }

    private Application enrichApplicationWithRelations(Application savedApplication, Tuple2<LoanType, LoanStatus> dependencies) {
        return savedApplication.toBuilder()
                .loanType(dependencies.getT1())
                .loanStatus(dependencies.getT2())
                .build();
    }

    private static Application createNewApplication(Application application, Tuple2<LoanType, LoanStatus> tuple) {
        return Application.createNew(application.getAmount(), application.getTerm(), application.getEmail(), application.getDocument(), tuple.getT2(), tuple.getT1());
    }

}
