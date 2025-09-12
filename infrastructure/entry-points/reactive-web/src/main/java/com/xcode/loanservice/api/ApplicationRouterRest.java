package com.xcode.loanservice.api;

import com.xcode.loanservice.api.dto.ApiResponse;
import com.xcode.loanservice.api.dto.ApplicationRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springdoc.core.annotations.RouterOperation;
import org.springdoc.core.annotations.RouterOperations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

@Configuration
public class ApplicationRouterRest {
    
    @Bean
    @RouterOperations({
        @RouterOperation(
            path = "/api/v1/application",
            method = RequestMethod.POST,
            operation = @Operation(
                operationId = "createApplication",
                summary = "Crear nueva solicitud de préstamo",
                description = "Registra una nueva solicitud de préstamo validando los datos del usuario y el tipo de préstamo",
                tags = {"Applications"},
                requestBody = @RequestBody(
                    description = "Datos de la solicitud de préstamo",
                    required = true,
                    content = @Content(
                        mediaType = MediaType.APPLICATION_JSON_VALUE,
                        schema = @Schema(implementation = ApplicationRequest.class)
                    )
                ),
                responses = {
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
                }
            )
        )
    })
    public RouterFunction<ServerResponse> routerFunction(ApplicationHandler applicationHandler) {
        return RouterFunctions.route()
                .path("/api/v1/application", builder -> builder
                        .POST("", applicationHandler::createApplication)
                        .GET("/pending", applicationHandler::listPendingApplications)
                )
                .build();
    }
}
