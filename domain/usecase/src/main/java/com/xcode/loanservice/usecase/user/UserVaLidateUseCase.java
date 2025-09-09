package com.xcode.loanservice.usecase.user;

import com.xcode.loanservice.model.common.exception.DomainErrorCode;
import com.xcode.loanservice.model.common.exception.DomainException;
import com.xcode.loanservice.model.user.gateways.UserRepository;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
public class UserVaLidateUseCase {

    private final UserRepository userRepository;

    public Mono<Boolean> validateApplication(String document) {

        return userRepository.existsByDocument(document)
                .filter(Boolean.TRUE::equals)
                .switchIfEmpty(Mono.error(new DomainException(DomainErrorCode.USER_NOT_FOUND)));
    }
}
