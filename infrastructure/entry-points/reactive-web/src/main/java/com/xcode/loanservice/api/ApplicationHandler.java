package com.xcode.loanservice.api;

import com.xcode.loanservice.api.config.ValidationConfig;
import com.xcode.loanservice.api.dto.ApiResponse;
import com.xcode.loanservice.api.dto.ApplicationRequest;
import com.xcode.loanservice.api.mapper.ApplicationMapper;
import com.xcode.loanservice.model.application.Application;
import com.xcode.loanservice.model.common.exception.DomainErrorCode;
import com.xcode.loanservice.model.common.exception.DomainException;
import com.xcode.loanservice.usecase.solicitud.RegisterApplicationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Applications", description = "API para gestión de solicitudes de préstamos")
public class ApplicationHandler {
    private final RegisterApplicationUseCase registerApplicationUseCase;
    private final ValidationConfig validationConfig;
    private final ApplicationMapper applicationMapper;
    @Operation(
        summary = "Crear nueva solicitud de préstamo",
        description = "Registra una nueva solicitud de préstamo validando los datos del usuario y el tipo de préstamo",
        operationId = "createApplication"
    )
    @ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "201",
            description = "Solicitud creada exitosamente",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "400",
            description = "Datos de entrada inválidos",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "404",
            description = "Tipo de préstamo no encontrado",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiResponse.class)
            )
        ),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = @Content(
                mediaType = MediaType.APPLICATION_JSON_VALUE,
                schema = @Schema(implementation = ApiResponse.class)
            )
        )
    })

    public Mono<ServerResponse> createApplication(ServerRequest serverRequest) {
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .flatMap(auth -> processRequest(serverRequest, auth));
                /*.flatMap(auth -> {
                    String documento = (String) auth.getPrincipal();
                    String token = (String) auth.getCredentials();
                    String email = (String) auth.getDetails();

                    return serverRequest.bodyToMono(ApplicationRequest.class)
                            .doOnNext(req -> log.info("Request recibido: {}", req))
                            // 🔹 Validación: si lanza excepción, GlobalException la atrapa
                            .flatMap(userRequest -> Mono.defer(() -> {
                                validationConfig.validate(userRequest);
                                return Mono.just(userRequest);
                            }))
                            .map(applicationMapper::toDomain)
                            .flatMap(solicitud -> {
                                if (!(documento.equals(solicitud.getDocument()) &&
                                        email.equalsIgnoreCase(solicitud.getEmail()))) {
                                    return Mono.error(new DomainException(DomainErrorCode.ACCESS_DENIED));
                                }
                                return registerApplicationUseCase.execute(solicitud, "Bearer " + token);
                            })
                            .map(applicationMapper::toResponse)
                            .flatMap(resp -> ServerResponse
                                    .status(HttpStatus.CREATED)
                                    .contentType(MediaType.APPLICATION_JSON)
                                    .bodyValue(ApiResponse.success("Solicitud creada exitosamente", resp))
                            );
                });*/
    }

    private Mono<ServerResponse> processRequest(ServerRequest serverRequest, Authentication auth) {
        String documento = (String) auth.getPrincipal();
        String token = (String) auth.getCredentials();
        String email = auth.getDetails() != null ? auth.getDetails().toString() : null;

        return serverRequest.bodyToMono(ApplicationRequest.class)
                .doOnNext(req -> log.info("Request recibido: {}", req))
                .flatMap(this::validateRequest)
                .map(applicationMapper::toDomain)
                .flatMap(solicitud -> validateOwnershipAndExecute(solicitud, documento, email, token))
                .map(applicationMapper::toResponse)
                .flatMap(resp -> ServerResponse
                        .status(HttpStatus.CREATED)
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.success("Solicitud creada exitosamente", resp))
                );
    }

    private Mono<ApplicationRequest> validateRequest(ApplicationRequest request) {
        return Mono.fromCallable(() -> {
                    validationConfig.validate(request);
                    return request;
                });
    }

    private Mono<Application> validateOwnershipAndExecute(Application solicitud, String documento, String email, String token) {
        if (!(documento.equals(solicitud.getDocument()) &&
                email != null &&
                email.equalsIgnoreCase(solicitud.getEmail()))) {
            return Mono.error(new DomainException(DomainErrorCode.ACCESS_DENIED));
        }
        return registerApplicationUseCase.execute(solicitud, "Bearer " + token);
    }
}
