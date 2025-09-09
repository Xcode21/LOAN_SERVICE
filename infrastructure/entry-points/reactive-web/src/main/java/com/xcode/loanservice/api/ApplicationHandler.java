package com.xcode.loanservice.api;

import com.xcode.loanservice.api.config.ValidationConfig;
import com.xcode.loanservice.api.dto.ApiResponse;
import com.xcode.loanservice.api.dto.ApplicationRequest;
import com.xcode.loanservice.api.dto.ApplicationResponse;
import com.xcode.loanservice.api.mapper.ApplicationMapper;
import com.xcode.loanservice.usecase.solicitud.RegisterApplicationUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
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
    public Mono<ServerResponse> createApplication(
        @Parameter(description = "Datos de la solicitud de préstamo", required = true)
        ServerRequest request
    ) {
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
                        .contentType(MediaType.APPLICATION_JSON)
                        .bodyValue(ApiResponse.success("Solicitud creada exitosamente", userResponse)));
    }
}
