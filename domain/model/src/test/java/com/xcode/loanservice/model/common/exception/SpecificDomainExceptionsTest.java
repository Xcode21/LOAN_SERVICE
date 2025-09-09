package com.xcode.loanservice.model.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class SpecificDomainExceptionsTest {

    @Nested
    @DisplayName("InvalidAmountException Tests")
    class InvalidAmountExceptionTests {

        @Test
        @DisplayName("Should create InvalidAmountException with BigDecimal amount")
        void shouldCreateInvalidAmountExceptionWithBigDecimalAmount() {
            BigDecimal invalidAmount = new BigDecimal("-1000");
            
            InvalidAmountException exception = new InvalidAmountException(invalidAmount);
            
            assertEquals("LOA_001", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("The amount isn't valid"));
            assertTrue(exception.getMessage().contains("-1000"));
        }

        @Test
        @DisplayName("Should create InvalidAmountException with zero amount")
        void shouldCreateInvalidAmountExceptionWithZeroAmount() {
            BigDecimal zeroAmount = BigDecimal.ZERO;
            
            InvalidAmountException exception = new InvalidAmountException(zeroAmount);
            
            assertEquals("LOA_001", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("0"));
        }

        @Test
        @DisplayName("Should create InvalidAmountException with large amount")
        void shouldCreateInvalidAmountExceptionWithLargeAmount() {
            BigDecimal largeAmount = new BigDecimal("999999999.99");
            
            InvalidAmountException exception = new InvalidAmountException(largeAmount);
            
            assertEquals("LOA_001", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("999999999.99"));
        }
    }

    @Nested
    @DisplayName("MissingRequiredFieldException Tests")
    class MissingRequiredFieldExceptionTests {

        @Test
        @DisplayName("Should create MissingRequiredFieldException with field name")
        void shouldCreateMissingRequiredFieldExceptionWithFieldName() {
            String fieldName = "amount";
            
            MissingRequiredFieldException exception = new MissingRequiredFieldException(fieldName);
            
            assertEquals("LOA_002", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("The field is missing"));
            assertTrue(exception.getMessage().contains("amount"));
        }

        @Test
        @DisplayName("Should create MissingRequiredFieldException with different field names")
        void shouldCreateMissingRequiredFieldExceptionWithDifferentFieldNames() {
            String[] fieldNames = {"email", "document", "term", "loanType"};
            
            for (String fieldName : fieldNames) {
                MissingRequiredFieldException exception = new MissingRequiredFieldException(fieldName);
                
                assertEquals("LOA_002", exception.getErrorCode());
                assertTrue(exception.getMessage().contains(fieldName));
            }
        }

        @Test
        @DisplayName("Should handle empty field name")
        void shouldHandleEmptyFieldName() {
            MissingRequiredFieldException exception = new MissingRequiredFieldException("");
            
            assertEquals("LOA_002", exception.getErrorCode());
            assertNotNull(exception.getMessage());
        }

        @Test
        @DisplayName("Should handle null field name")
        void shouldHandleNullFieldName() {
            MissingRequiredFieldException exception = new MissingRequiredFieldException(null);
            
            assertEquals("LOA_002", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("null"));
        }
    }

    @Nested
    @DisplayName("InvalidTermException Tests")
    class InvalidTermExceptionTests {

        @Test
        @DisplayName("Should create InvalidTermException with negative term")
        void shouldCreateInvalidTermExceptionWithNegativeTerm() {
            Integer invalidTerm = -5;
            
            InvalidTermException exception = new InvalidTermException(invalidTerm);
            
            assertEquals("LOA_003", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("The amount isn't valid"));
            assertTrue(exception.getMessage().contains("-5"));
        }

        @Test
        @DisplayName("Should create InvalidTermException with zero term")
        void shouldCreateInvalidTermExceptionWithZeroTerm() {
            Integer zeroTerm = 0;
            
            InvalidTermException exception = new InvalidTermException(zeroTerm);
            
            assertEquals("LOA_003", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("0"));
        }

        @Test
        @DisplayName("Should handle null term")
        void shouldHandleNullTerm() {
            InvalidTermException exception = new InvalidTermException(null);
            
            assertEquals("LOA_003", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("null"));
        }
    }

    @Nested
    @DisplayName("ValidateAmountRangeException Tests")
    class ValidateAmountRangeExceptionTests {

        @Test
        @DisplayName("Should create ValidateAmountRangeException with out-of-range amount")
        void shouldCreateValidateAmountRangeExceptionWithOutOfRangeAmount() {
            BigDecimal outOfRangeAmount = new BigDecimal("100000");
            
            ValidateAmountRangeException exception = new ValidateAmountRangeException(outOfRangeAmount);
            
            assertEquals("LOA_004", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("The amount is out of range for loan type"));
            assertTrue(exception.getMessage().contains("100000"));
        }

        @Test
        @DisplayName("Should create ValidateAmountRangeException with small amount")
        void shouldCreateValidateAmountRangeExceptionWithSmallAmount() {
            BigDecimal smallAmount = new BigDecimal("50");
            
            ValidateAmountRangeException exception = new ValidateAmountRangeException(smallAmount);
            
            assertEquals("LOA_004", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("50"));
        }
    }

    @Nested
    @DisplayName("StatusChangeNotAllowedException Tests")
    class StatusChangeNotAllowedExceptionTests {

        @Test
        @DisplayName("Should create StatusChangeNotAllowedException with status")
        void shouldCreateStatusChangeNotAllowedExceptionWithStatus() {
            String currentStatus = "APPROVED";
            
            StatusChangeNotAllowedException exception = new StatusChangeNotAllowedException(currentStatus);
            
            assertEquals("LOA_005", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("Status not allowed change"));
            assertTrue(exception.getMessage().contains("APPROVED"));
        }

        @Test
        @DisplayName("Should create StatusChangeNotAllowedException with different statuses")
        void shouldCreateStatusChangeNotAllowedExceptionWithDifferentStatuses() {
            String[] statuses = {"REJECTED", "PENDING_REVIEW", "CANCELLED"};
            
            for (String status : statuses) {
                StatusChangeNotAllowedException exception = new StatusChangeNotAllowedException(status);
                
                assertEquals("LOA_005", exception.getErrorCode());
                assertTrue(exception.getMessage().contains(status));
            }
        }
    }

    @Nested
    @DisplayName("ApplicationNotFoundException Tests")
    class ApplicationNotFoundExceptionTests {

        @Test
        @DisplayName("Should create ApplicationNotFoundException with UUID")
        void shouldCreateApplicationNotFoundExceptionWithUUID() {
            UUID applicationId = UUID.randomUUID();
            
            ApplicationNotFoundException exception = new ApplicationNotFoundException(applicationId);
            
            assertEquals("LOA_006", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("Application not found"));
            assertTrue(exception.getMessage().contains("Application not found with ID: " + applicationId));
        }

        @Test
        @DisplayName("Should create ApplicationNotFoundException with custom message")
        void shouldCreateApplicationNotFoundExceptionWithCustomMessage() {
            String customMessage = "Application with document 12345 not found";
            
            ApplicationNotFoundException exception = new ApplicationNotFoundException(customMessage);
            
            assertEquals("LOA_006", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("Application not found"));
            assertTrue(exception.getMessage().contains(customMessage));
        }

        @Test
        @DisplayName("Should handle null UUID")
        void shouldHandleNullUUID() {
            ApplicationNotFoundException exception = new ApplicationNotFoundException((UUID) null);
            
            assertEquals("LOA_006", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("null"));
        }

        @Test
        @DisplayName("Should handle null message")
        void shouldHandleNullMessage() {
            ApplicationNotFoundException exception = new ApplicationNotFoundException((String) null);
            
            assertEquals("LOA_006", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("null"));
        }
    }

    @Nested
    @DisplayName("LoanTypeNotFoundException Tests")
    class LoanTypeNotFoundExceptionTests {

        @Test
        @DisplayName("Should create LoanTypeNotFoundException with loan type ID")
        void shouldCreateLoanTypeNotFoundExceptionWithLoanTypeId() {
            Integer loanTypeId = 123;
            
            LoanTypeNotFoundException exception = new LoanTypeNotFoundException(loanTypeId);
            
            assertEquals("LOA_007", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("LOAN TYPE NOT FOUND"));
            assertTrue(exception.getMessage().contains("123"));
        }

        @Test
        @DisplayName("Should create LoanTypeNotFoundException with negative ID")
        void shouldCreateLoanTypeNotFoundExceptionWithNegativeId() {
            Integer negativeId = -1;
            
            LoanTypeNotFoundException exception = new LoanTypeNotFoundException(negativeId);
            
            assertEquals("LOA_007", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("-1"));
        }

        @Test
        @DisplayName("Should handle null loan type ID")
        void shouldHandleNullLoanTypeId() {
            LoanTypeNotFoundException exception = new LoanTypeNotFoundException(null);
            
            assertEquals("LOA_007", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("null"));
        }
    }

    @Nested
    @DisplayName("LoanStatusNotFoundException Tests")
    class LoanStatusNotFoundExceptionTests {

        @Test
        @DisplayName("Should create LoanStatusNotFoundException with status name")
        void shouldCreateLoanStatusNotFoundExceptionWithStatusName() {
            String statusName = "PENDING_APPROVAL";
            
            LoanStatusNotFoundException exception = new LoanStatusNotFoundException(statusName);
            
            assertEquals("LOA_008", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("STATUS NOT FOUND"));
            assertTrue(exception.getMessage().contains("PENDING_APPROVAL"));
        }

        @Test
        @DisplayName("Should create LoanStatusNotFoundException with different status names")
        void shouldCreateLoanStatusNotFoundExceptionWithDifferentStatusNames() {
            String[] statusNames = {"UNKNOWN", "DRAFT", "PROCESSING"};
            
            for (String statusName : statusNames) {
                LoanStatusNotFoundException exception = new LoanStatusNotFoundException(statusName);
                
                assertEquals("LOA_008", exception.getErrorCode());
                assertTrue(exception.getMessage().contains(statusName));
            }
        }

        @Test
        @DisplayName("Should handle null status name")
        void shouldHandleNullStatusName() {
            LoanStatusNotFoundException exception = new LoanStatusNotFoundException(null);
            
            assertEquals("LOA_008", exception.getErrorCode());
            assertTrue(exception.getMessage().contains("null"));
        }

        @Test
        @DisplayName("Should handle empty status name")
        void shouldHandleEmptyStatusName() {
            LoanStatusNotFoundException exception = new LoanStatusNotFoundException("");
            
            assertEquals("LOA_008", exception.getErrorCode());
            assertNotNull(exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Exception Inheritance Tests")
    class ExceptionInheritanceTests {

        @Test
        @DisplayName("All specific exceptions should extend DomainException")
        void allSpecificExceptionsShouldExtendDomainException() {
            assertTrue(DomainException.class.isAssignableFrom(InvalidAmountException.class));
            assertTrue(DomainException.class.isAssignableFrom(MissingRequiredFieldException.class));
            assertTrue(DomainException.class.isAssignableFrom(InvalidTermException.class));
            assertTrue(DomainException.class.isAssignableFrom(ValidateAmountRangeException.class));
            assertTrue(DomainException.class.isAssignableFrom(StatusChangeNotAllowedException.class));
            assertTrue(DomainException.class.isAssignableFrom(ApplicationNotFoundException.class));
            assertTrue(DomainException.class.isAssignableFrom(LoanTypeNotFoundException.class));
            assertTrue(DomainException.class.isAssignableFrom(LoanStatusNotFoundException.class));
        }

        @Test
        @DisplayName("All domain exceptions should extend RuntimeException")
        void allDomainExceptionsShouldExtendRuntimeException() {
            assertTrue(RuntimeException.class.isAssignableFrom(DomainException.class));
            assertTrue(RuntimeException.class.isAssignableFrom(InvalidAmountException.class));
            assertTrue(RuntimeException.class.isAssignableFrom(MissingRequiredFieldException.class));
            assertTrue(RuntimeException.class.isAssignableFrom(ApplicationNotFoundException.class));
        }
    }
}