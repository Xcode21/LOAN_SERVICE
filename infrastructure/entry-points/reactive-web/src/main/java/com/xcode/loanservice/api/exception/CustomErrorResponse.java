package com.xcode.loanservice.api.exception;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Respuesta de error estándar")
public class CustomErrorResponse {
    
    @Schema(description = "Código de error", example = "VALIDATION_ERROR")
    private String code;
    
    @Schema(description = "Mensaje descriptivo del error", example = "Los datos proporcionados no son válidos")
    private String message;
    
    @Schema(description = "Datos adicionales del error (opcional)")
    private Object data;
    
    @Schema(description = "Ruta del endpoint donde ocurrió el error", example = "/api/v1/application")
    private String path;
    
    @JsonIgnore
    private Integer statusCode;
    
    @JsonIgnore
    private String logLevel;
}