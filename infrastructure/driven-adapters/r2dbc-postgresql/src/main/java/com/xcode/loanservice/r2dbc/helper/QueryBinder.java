package com.xcode.loanservice.r2dbc.helper;

import org.springframework.r2dbc.core.DatabaseClient;

public class QueryBinder {
    public static DatabaseClient.GenericExecuteSpec bindEmail(
            DatabaseClient.GenericExecuteSpec spec, String email) {
        return (email != null && !email.isBlank())
                ? spec.bind("email", "%" + email + "%")
                : spec.bindNull("email", String.class);
    }

    public static DatabaseClient.GenericExecuteSpec bindLoanType(
            DatabaseClient.GenericExecuteSpec spec, String loanType) {
        return (loanType != null && !loanType.isBlank())
                ? spec.bind("tipoPrestamo", loanType)
                : spec.bindNull("tipoPrestamo", String.class);
    }
}
