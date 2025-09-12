package com.xcode.loanservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Filtros para búsqueda de solicitudes pendientes")
public class ApplicationFilterRequest {
    
    @Schema(description = "Filtro por documento de identidad", example = "12345678")
    private String document;
    
    @Schema(description = "Filtro por email", example = "usuario@email.com")
    private String email;
    
    @Schema(description = "Filtro por tipo de préstamo", example = "1")
    private Integer loanType;
    
    @Schema(description = "Filtro por monto mínimo", example = "10000.00")
    private BigDecimal minAmount;
    
    @Schema(description = "Filtro por monto máximo", example = "100000.00")
    private BigDecimal maxAmount;
    
    @Schema(description = "Filtro por plazo mínimo en meses", example = "12")
    private Integer minTerm;
    
    @Schema(description = "Filtro por plazo máximo en meses", example = "48")
    private Integer maxTerm;
    
    @Schema(description = "Filtro por fecha de creación desde", example = "2024-01-01")
    private LocalDate dateFrom;
    
    @Schema(description = "Filtro por fecha de creación hasta", example = "2024-12-31")
    private LocalDate dateTo;
}