package com.xcode.loanservice.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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
public class ApplicationRequest {
    @Schema(description = "Monto del préstamo solicitado", example = "50000.00", minimum = "1000.00", maximum = "500000.00")
    @NotNull(message = "El monto es obligatorio")
    @DecimalMin(value = "1000.00", message = "El monto mínimo permitido es 1000.00")
    @DecimalMax(value = "500000.00", message = "El monto máximo permitido es 100000.00")
    @Positive
    private BigDecimal amount;

    @Schema(description = "Plazo del préstamo en meses", example = "12", minimum = "1", maximum = "60")
    @NotNull(message = "El plazo es obligatorio")
    @Min(value = 1, message = "El plazo mínimo es 1 mes")
    @Max(value = 60, message = "El plazo máximo es 60 meses")
    @Positive
    private Integer term;

    @Schema(description = "Email del solicitante", example = "usuario@email.com")
    @NotBlank(message = "El email es obligatorio")
    @Email(message = "El email no tiene un formato válido")
    private String email;

    @Schema(description = "Número de documento de identidad", example = "12345678", pattern = "\\d{8,12}")
    @NotBlank(message = "El documento es obligatorio")
    @Pattern(regexp = "\\d{8,12}", message = "El documento debe tener entre 8 y 12 dígitos")
    private String document;

    @Schema(description = "Tipo de préstamo solicitado (ID)", example = "1")
    @NotNull(message = "El tipo de préstamo es obligatorio")
    private Integer loanType;
}