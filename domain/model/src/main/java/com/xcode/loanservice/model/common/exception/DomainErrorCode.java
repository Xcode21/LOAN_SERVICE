package com.xcode.loanservice.model.common.exception;

public enum DomainErrorCode {
    INVALID_AMOUNT("LOA_001", "The amount isn't valid", 400),
    MISSING_REQUIRED_FIELD("LOA_002", "The field is missing", 400),
    INVALID_TERM("LOA_003", "The amount isn't valid", 400),
    INVALID_AMOUNT_RANGE("LOA_004", "The amount is out of range for loan type", 400),
    INVALID_STATUS_CHANGE("LOA_005", "The status change is not allowed", 400),
    APPLICATION_NOT_FOUND("LOA_006", "Application not found", 404),
    LOANTYPE_NOT_FOUND("LOA_007", "LOAN TYPE NOT FOUND", 404),
    USER_ALREADY_EXISTS("USR_004", "The user already exists", 409),
    ROLE_NOT_FOUND("ROLE_001", "The role dont exists", 404),
    ROLE_NOT_ALLOWED("ROLE_002", "Role not allowed for user creation", 403);
    
    private final String code;
    private final String defaultMessage;
    private final int httpStatusCode;

    DomainErrorCode(String code, String defaultMessage, int httpStatusCode) {
        this.code = code;
        this.defaultMessage = defaultMessage;
        this.httpStatusCode = httpStatusCode;
    }

    public String getCode() {
        return code;
    }

    public String getDefaultMessage() {
        return defaultMessage;
    }
    
    public int getHttpStatusCode() {
        return httpStatusCode;
    }
}
