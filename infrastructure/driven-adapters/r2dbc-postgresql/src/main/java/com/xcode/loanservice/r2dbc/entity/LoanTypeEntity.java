package com.xcode.loanservice.r2dbc.entity;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table(name = "tipo_prestamo")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanTypeEntity {

    @Id
    @Column("id")
    private Integer idLoanType;
    @Column("nombre")
    private String name;
    @Column("monto_minimo")
    private BigDecimal minAmount;
    @Column("monto_maximo")
    private BigDecimal maxAmount;
    @Column("tasa_interes")
    private BigDecimal interestRate;
    @Column("validacion_automatica")
    private Boolean automaticValidation;
    @Column("fecha_creacion")
    private LocalDateTime createdAt;
    @Column("fecha_actualizacion")
    private LocalDateTime updatedAt;
}
