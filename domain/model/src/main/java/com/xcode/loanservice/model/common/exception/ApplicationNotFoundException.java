package com.xcode.loanservice.model.common.exception;

import java.util.UUID;

public class ApplicationNotFoundException extends DomainException{
    public ApplicationNotFoundException(UUID applicationId) {
        super(DomainErrorCode.APPLICATION_NOT_FOUND, "Application not found with ID: " + applicationId);
    }

    public ApplicationNotFoundException(String message) {
        super(DomainErrorCode.APPLICATION_NOT_FOUND, message);
    }
}
