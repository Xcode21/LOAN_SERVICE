package com.xcode.loanservice.api;

import com.xcode.loanservice.api.config.ValidationConfig;
import com.xcode.loanservice.api.dto.ApiResponse;
import com.xcode.loanservice.api.dto.ApplicationRequest;
import com.xcode.loanservice.api.mapper.ApplicationMapper;
import com.xcode.loanservice.usecase.solicitud.RegisterApplicationUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ApplicationHandler {
    private final RegisterApplicationUseCase registerApplicationUseCase;
    private final ValidationConfig validationConfig;
    private final ApplicationMapper applicationMapper;
    public Mono<ServerResponse> createApplication(ServerRequest request) {
        return request.bodyToMono(ApplicationRequest.class)
                .switchIfEmpty(Mono.error(new ServerWebInputException("Request body is required")))
                .flatMap(applicationRequest ->
                        Mono.fromCallable(() -> {
                            validationConfig.validate(applicationRequest);
                            return applicationRequest;
                        })
                )
                .map(applicationMapper::toDomain)
                .flatMap(registerApplicationUseCase::execute)
                .map(applicationMapper::toResponse)
                .flatMap(userResponse -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .bodyValue(ApiResponse.success("Solicitud creada exitosamente", userResponse)));
    }
}
