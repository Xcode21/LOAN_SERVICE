package com.xcode.loanservice.api.mapper;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface LoanStatusMapper {
    /*@Named("stringToEstado")
    default LoanStatus stringToEstado(String estado) {
        try {
            return estado != null ? new LoanStatus() : null;
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Estado inválido: " + estado);
        }
    }

    @Named("estadoToString")
    default String  estadoToString(Estado estado) {
        return estado != null ? estado.getNombre() : null;
    }*/
}
