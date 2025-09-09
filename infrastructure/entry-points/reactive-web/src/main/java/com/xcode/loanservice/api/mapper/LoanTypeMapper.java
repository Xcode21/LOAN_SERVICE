package com.xcode.loanservice.api.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanTypeMapper {
   /* @Named("stringToTipoPrestamo")
    default LoanType stringToLoanType(Integer loanType) {
        try {
            return loanType != null ? new LoanType().toBuilder().id(prestamo).build() : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Prestamo inválido: " + prestamo);
        }
    }

    @Named("tipoPrestamoToString")
    default String estadoToString(TipoPrestamo prestamo) {
        return prestamo != null ? prestamo.getNombre() : null;
    }

    @Named("tasaInteresToDecimal")
    default BigDecimal tasaInteresToDecimal(TipoPrestamo prestamo) {
        return prestamo != null ? prestamo.getTasaInteres() : null;
    }
*/

}
