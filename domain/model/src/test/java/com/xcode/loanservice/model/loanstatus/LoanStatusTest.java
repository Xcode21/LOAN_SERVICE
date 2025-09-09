package com.xcode.loanservice.model.loanstatus;

import com.xcode.loanservice.model.application.LoanStatusName;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LoanStatusTest {

    @Nested
    @DisplayName("Status Type Validation")
    class StatusTypeValidationTests {

        @Test
        @DisplayName("Should identify pending review status correctly")
        void shouldIdentifyPendingReviewStatusCorrectly() {
            LoanStatus pendingStatus = LoanStatus.builder()
                    .idLoanStatus(1)
                    .name(LoanStatusName.PENDING_REVIEW)
                    .description("Pending Review")
                    .build();

            assertTrue(pendingStatus.isPendingReview());
            assertFalse(pendingStatus.isApproved());
            assertFalse(pendingStatus.isRejected());
        }

        @Test
        @DisplayName("Should identify approved status correctly")
        void shouldIdentifyApprovedStatusCorrectly() {
            LoanStatus approvedStatus = LoanStatus.builder()
                    .idLoanStatus(2)
                    .name(LoanStatusName.APPROVED)
                    .description("Approved")
                    .build();

            assertFalse(approvedStatus.isPendingReview());
            assertTrue(approvedStatus.isApproved());
            assertFalse(approvedStatus.isRejected());
        }

        @Test
        @DisplayName("Should identify rejected status correctly")
        void shouldIdentifyRejectedStatusCorrectly() {
            LoanStatus rejectedStatus = LoanStatus.builder()
                    .idLoanStatus(3)
                    .name(LoanStatusName.REJECTED)
                    .description("Rejected")
                    .build();

            assertFalse(rejectedStatus.isPendingReview());
            assertFalse(rejectedStatus.isApproved());
            assertTrue(rejectedStatus.isRejected());
        }
    }

    @Nested
    @DisplayName("Builder Pattern Tests")
    class BuilderPatternTests {

        @Test
        @DisplayName("Should create loan status using builder")
        void shouldCreateLoanStatusUsingBuilder() {
            Integer id = 1;
            LoanStatusName name = LoanStatusName.PENDING_REVIEW;
            String description = "Application is under review";

            LoanStatus loanStatus = LoanStatus.builder()
                    .idLoanStatus(id)
                    .name(name)
                    .description(description)
                    .build();

            assertNotNull(loanStatus);
            assertEquals(id, loanStatus.getIdLoanStatus());
            assertEquals(name, loanStatus.getName());
            assertEquals(description, loanStatus.getDescription());
        }

        @Test
        @DisplayName("Should create copy using toBuilder")
        void shouldCreateCopyUsingToBuilder() {
            LoanStatus original = LoanStatus.builder()
                    .idLoanStatus(1)
                    .name(LoanStatusName.PENDING_REVIEW)
                    .description("Original description")
                    .build();

            LoanStatus copy = original.toBuilder()
                    .description("Modified description")
                    .build();

            assertNotEquals(original.getDescription(), copy.getDescription());
            assertEquals("Modified description", copy.getDescription());
            
            // Other fields should remain the same
            assertEquals(original.getIdLoanStatus(), copy.getIdLoanStatus());
            assertEquals(original.getName(), copy.getName());
        }
    }

    @Nested
    @DisplayName("Equality and Identity Tests")
    class EqualityTests {

        @Test
        @DisplayName("Should have equal status names for same enum values")
        void shouldHaveEqualStatusNamesForSameEnumValues() {
            LoanStatus status1 = LoanStatus.builder()
                    .idLoanStatus(1)
                    .name(LoanStatusName.APPROVED)
                    .description("First approved")
                    .build();

            LoanStatus status2 = LoanStatus.builder()
                    .idLoanStatus(2)
                    .name(LoanStatusName.APPROVED)
                    .description("Second approved")
                    .build();

            assertEquals(status1.getName(), status2.getName());
            assertTrue(status1.isApproved());
            assertTrue(status2.isApproved());
        }

        @Test
        @DisplayName("Should have different behavior for different enum values")
        void shouldHaveDifferentBehaviorForDifferentEnumValues() {
            LoanStatus pendingStatus = LoanStatus.builder()
                    .idLoanStatus(1)
                    .name(LoanStatusName.PENDING_REVIEW)
                    .description("Pending")
                    .build();

            LoanStatus approvedStatus = LoanStatus.builder()
                    .idLoanStatus(2)
                    .name(LoanStatusName.APPROVED)
                    .description("Approved")
                    .build();

            assertNotEquals(pendingStatus.getName(), approvedStatus.getName());
            assertTrue(pendingStatus.isPendingReview());
            assertFalse(approvedStatus.isPendingReview());
            assertFalse(pendingStatus.isApproved());
            assertTrue(approvedStatus.isApproved());
        }
    }

    @Nested
    @DisplayName("All Status Types Coverage")
    class AllStatusTypesCoverageTests {

        @Test
        @DisplayName("Should cover all possible loan status enum values")
        void shouldCoverAllPossibleLoanStatusEnumValues() {
            // Test PENDING_REVIEW
            LoanStatus pendingStatus = LoanStatus.builder()
                    .idLoanStatus(1)
                    .name(LoanStatusName.PENDING_REVIEW)
                    .description("Under review")
                    .build();
            assertTrue(pendingStatus.isPendingReview());

            // Test APPROVED
            LoanStatus approvedStatus = LoanStatus.builder()
                    .idLoanStatus(2)
                    .name(LoanStatusName.APPROVED)
                    .description("Loan approved")
                    .build();
            assertTrue(approvedStatus.isApproved());

            // Test REJECTED
            LoanStatus rejectedStatus = LoanStatus.builder()
                    .idLoanStatus(3)
                    .name(LoanStatusName.REJECTED)
                    .description("Loan rejected")
                    .build();
            assertTrue(rejectedStatus.isRejected());

            // Verify enum values exist
            assertEquals(3, LoanStatusName.values().length);
            assertNotNull(LoanStatusName.valueOf("PENDING_REVIEW"));
            assertNotNull(LoanStatusName.valueOf("APPROVED"));
            assertNotNull(LoanStatusName.valueOf("REJECTED"));
        }
    }

    @Nested
    @DisplayName("Null Safety Tests")
    class NullSafetyTests {

        @Test
        @DisplayName("Should handle null name gracefully in status checks")
        void shouldHandleNullNameGracefullyInStatusChecks() {
            LoanStatus statusWithNullName = LoanStatus.builder()
                    .idLoanStatus(1)
                    .name(null)
                    .description("Status with null name")
                    .build();

            assertFalse(statusWithNullName.isPendingReview());
            assertFalse(statusWithNullName.isApproved());
            assertFalse(statusWithNullName.isRejected());
        }
    }

    @Nested
    @DisplayName("Field Access Tests")
    class FieldAccessTests {

        @Test
        @DisplayName("Should provide access to all fields")
        void shouldProvideAccessToAllFields() {
            Integer expectedId = 123;
            LoanStatusName expectedName = LoanStatusName.APPROVED;
            String expectedDescription = "Loan has been approved for processing";

            LoanStatus loanStatus = LoanStatus.builder()
                    .idLoanStatus(expectedId)
                    .name(expectedName)
                    .description(expectedDescription)
                    .build();

            assertEquals(expectedId, loanStatus.getIdLoanStatus());
            assertEquals(expectedName, loanStatus.getName());
            assertEquals(expectedDescription, loanStatus.getDescription());
        }

        @Test
        @DisplayName("Should handle empty description")
        void shouldHandleEmptyDescription() {
            LoanStatus loanStatus = LoanStatus.builder()
                    .idLoanStatus(1)
                    .name(LoanStatusName.PENDING_REVIEW)
                    .description("")
                    .build();

            assertEquals("", loanStatus.getDescription());
            assertTrue(loanStatus.isPendingReview());
        }

        @Test
        @DisplayName("Should handle null description")
        void shouldHandleNullDescription() {
            LoanStatus loanStatus = LoanStatus.builder()
                    .idLoanStatus(1)
                    .name(LoanStatusName.PENDING_REVIEW)
                    .description(null)
                    .build();

            assertNull(loanStatus.getDescription());
            assertTrue(loanStatus.isPendingReview());
        }
    }
}