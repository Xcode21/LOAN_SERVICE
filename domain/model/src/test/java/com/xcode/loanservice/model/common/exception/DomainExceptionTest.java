package com.xcode.loanservice.model.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DomainExceptionTest {

    @Nested
    @DisplayName("DomainException Base Class Tests")
    class DomainExceptionBaseTests {

        @Test
        @DisplayName("Should create domain exception with error code")
        void shouldCreateDomainExceptionWithErrorCode() {
            DomainException exception = new DomainException(DomainErrorCode.USER_NOT_FOUND);

            assertEquals("USR_001", exception.getErrorCode());
            assertEquals("The user dont exists", exception.getErrorMessage());
            assertEquals("The user dont exists", exception.getMessage());
        }

        @Test
        @DisplayName("Should create domain exception with error code and detail")
        void shouldCreateDomainExceptionWithErrorCodeAndDetail() {
            String detail = "User ID: 12345";
            DomainException exception = new DomainException(DomainErrorCode.USER_NOT_FOUND, detail);

            assertEquals("USR_001", exception.getErrorCode());
            assertEquals("The user dont exists: User ID: 12345", exception.getErrorMessage());
            assertEquals("The user dont exists: User ID: 12345", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("DomainErrorCode Tests")
    class DomainErrorCodeTests {

        @Test
        @DisplayName("Should have correct error codes and messages for all domain errors")
        void shouldHaveCorrectErrorCodesAndMessagesForAllDomainErrors() {
            // Test INVALID_AMOUNT
            assertEquals("LOA_001", DomainErrorCode.INVALID_AMOUNT.getCode());
            assertEquals("The amount isn't valid", DomainErrorCode.INVALID_AMOUNT.getDefaultMessage());
            assertEquals(400, DomainErrorCode.INVALID_AMOUNT.getHttpStatusCode());

            // Test MISSING_REQUIRED_FIELD
            assertEquals("LOA_002", DomainErrorCode.MISSING_REQUIRED_FIELD.getCode());
            assertEquals("The field is missing", DomainErrorCode.MISSING_REQUIRED_FIELD.getDefaultMessage());
            assertEquals(400, DomainErrorCode.MISSING_REQUIRED_FIELD.getHttpStatusCode());

            // Test INVALID_TERM
            assertEquals("LOA_003", DomainErrorCode.INVALID_TERM.getCode());
            assertEquals("The amount isn't valid", DomainErrorCode.INVALID_TERM.getDefaultMessage());
            assertEquals(400, DomainErrorCode.INVALID_TERM.getHttpStatusCode());

            // Test INVALID_AMOUNT_RANGE
            assertEquals("LOA_004", DomainErrorCode.INVALID_AMOUNT_RANGE.getCode());
            assertEquals("The amount is out of range for loan type", DomainErrorCode.INVALID_AMOUNT_RANGE.getDefaultMessage());
            assertEquals(400, DomainErrorCode.INVALID_AMOUNT_RANGE.getHttpStatusCode());

            // Test INVALID_STATUS_CHANGE
            assertEquals("LOA_005", DomainErrorCode.INVALID_STATUS_CHANGE.getCode());
            assertEquals("The status change is not allowed", DomainErrorCode.INVALID_STATUS_CHANGE.getDefaultMessage());
            assertEquals(400, DomainErrorCode.INVALID_STATUS_CHANGE.getHttpStatusCode());

            // Test APPLICATION_NOT_FOUND
            assertEquals("LOA_006", DomainErrorCode.APPLICATION_NOT_FOUND.getCode());
            assertEquals("Application not found", DomainErrorCode.APPLICATION_NOT_FOUND.getDefaultMessage());
            assertEquals(404, DomainErrorCode.APPLICATION_NOT_FOUND.getHttpStatusCode());

            // Test LOANTYPE_NOT_FOUND
            assertEquals("LOA_007", DomainErrorCode.LOANTYPE_NOT_FOUND.getCode());
            assertEquals("LOAN TYPE NOT FOUND", DomainErrorCode.LOANTYPE_NOT_FOUND.getDefaultMessage());
            assertEquals(404, DomainErrorCode.LOANTYPE_NOT_FOUND.getHttpStatusCode());

            // Test STATUS_NOT_FOUND
            assertEquals("LOA_008", DomainErrorCode.STATUS_NOT_FOUND.getCode());
            assertEquals("STATUS NOT FOUND", DomainErrorCode.STATUS_NOT_FOUND.getDefaultMessage());
            assertEquals(404, DomainErrorCode.STATUS_NOT_FOUND.getHttpStatusCode());

            // Test USER_ALREADY_EXISTS
            assertEquals("USR_004", DomainErrorCode.USER_ALREADY_EXISTS.getCode());
            assertEquals("The user already exists", DomainErrorCode.USER_ALREADY_EXISTS.getDefaultMessage());
            assertEquals(409, DomainErrorCode.USER_ALREADY_EXISTS.getHttpStatusCode());

            // Test USER_NOT_FOUND
            assertEquals("USR_001", DomainErrorCode.USER_NOT_FOUND.getCode());
            assertEquals("The user dont exists", DomainErrorCode.USER_NOT_FOUND.getDefaultMessage());
            assertEquals(404, DomainErrorCode.USER_NOT_FOUND.getHttpStatusCode());

            // Test ROLE_NOT_ALLOWED
            assertEquals("ROLE_002", DomainErrorCode.ROLE_NOT_ALLOWED.getCode());
            assertEquals("Role not allowed for user creation", DomainErrorCode.ROLE_NOT_ALLOWED.getDefaultMessage());
            assertEquals(403, DomainErrorCode.ROLE_NOT_ALLOWED.getHttpStatusCode());
        }

        @Test
        @DisplayName("Should have all expected domain error codes")
        void shouldHaveAllExpectedDomainErrorCodes() {
            DomainErrorCode[] codes = DomainErrorCode.values();
            assertEquals(11, codes.length);

            // Verify all enum values exist
            assertDoesNotThrow(() -> DomainErrorCode.valueOf("INVALID_AMOUNT"));
            assertDoesNotThrow(() -> DomainErrorCode.valueOf("MISSING_REQUIRED_FIELD"));
            assertDoesNotThrow(() -> DomainErrorCode.valueOf("INVALID_TERM"));
            assertDoesNotThrow(() -> DomainErrorCode.valueOf("INVALID_AMOUNT_RANGE"));
            assertDoesNotThrow(() -> DomainErrorCode.valueOf("INVALID_STATUS_CHANGE"));
            assertDoesNotThrow(() -> DomainErrorCode.valueOf("APPLICATION_NOT_FOUND"));
            assertDoesNotThrow(() -> DomainErrorCode.valueOf("LOANTYPE_NOT_FOUND"));
            assertDoesNotThrow(() -> DomainErrorCode.valueOf("STATUS_NOT_FOUND"));
            assertDoesNotThrow(() -> DomainErrorCode.valueOf("USER_ALREADY_EXISTS"));
            assertDoesNotThrow(() -> DomainErrorCode.valueOf("USER_NOT_FOUND"));
            assertDoesNotThrow(() -> DomainErrorCode.valueOf("ROLE_NOT_ALLOWED"));
        }
    }
}