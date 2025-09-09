package com.xcode.loanservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Respuesta con los datos del usuario creado")
public class ApplicationResponse {
    @Schema(description = "Identificador único del solicitud", example = "550e8400-e29b-41d4-a716-446655440000")
    private String idApplication;
    
    @Schema(description = "monto solicitado", example = "15000.50", type = "number", format = "double")
    private BigDecimal amount;
    
    @Schema(description = "Plazo en meses", example = "12", type = "integer", format = "int32")
    private Integer term;

    @Schema(description = "Correo electrónico", example = "juan.perez@email.com")
    private String email;
    
    @Schema(description = "Documento de identidad", example = "12345678")
    private String document;
    
    @Schema(description = "Estado del préstamo", example = "APROBADO")
    private String loanStatus;
    
    @Schema(description = "Tipo de préstamo", example = "PERSONAL")
    private String loanType;
}