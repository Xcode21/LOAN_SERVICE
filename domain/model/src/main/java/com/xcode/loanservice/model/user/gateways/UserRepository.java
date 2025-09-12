package com.xcode.loanservice.model.user.gateways;

import com.xcode.loanservice.model.user.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Set;

public interface UserRepository {
    Mono<Boolean> existsByDocument(String document);
    Mono<Boolean> existsByDocumentWithToken(String document, String authorizationHeader);
    Flux<User> findSalaryByEmail(Set<String> email);

}
