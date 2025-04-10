package com.ibs_demo.invoice_service.service.exception;

import com.ibs_demo.invoice_service.exception.ErrorResponse;
import com.ibs_demo.invoice_service.exception.GlobalExceptionHandler;
import com.ibs_demo.invoice_service.exception.appexceptions.AlreadyPaidInvoiceException;
import com.ibs_demo.invoice_service.exception.appexceptions.DuplicateEmailException;
import com.ibs_demo.invoice_service.exception.appexceptions.InactiveInvoiceException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.http.HttpStatus.BAD_REQUEST;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleUsernameNotFoundException_shouldReturnNotFound() {
        UsernameNotFoundException exception = new UsernameNotFoundException("User not found");
        ResponseEntity<ErrorResponse> response = handler.handleUsernameNotFoundException(exception);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Not Found", Objects.requireNonNull(response.getBody()).getError());
        assertEquals("User not found", response.getBody().getMessage());
    }

    @Test
    void handleDuplicateEmailException_shouldReturnConflict() {
        DuplicateEmailException exception = new DuplicateEmailException("Email already exists");
        ResponseEntity<ErrorResponse> response = handler.handleDuplicateEmailException(exception);

        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Duplicate email", response.getBody().getError());
        assertEquals("Email already exists", response.getBody().getMessage());
    }

    @Test
    void handleRuntimeException_shouldReturnInternalServerError() {
        RuntimeException exception = new RuntimeException("Something went wrong");
        ResponseEntity<ErrorResponse> response = handler.handleRuntimeException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal Server Error", response.getBody().getError());
        assertEquals("Something went wrong", response.getBody().getMessage());
    }

    @Test
    void handleGenericException_shouldReturnInternalServerError() {
        Exception exception = new Exception("Unexpected error");
        ResponseEntity<ErrorResponse> response = handler.handleGenericException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Unexpected Error", response.getBody().getError());
        assertEquals("Unexpected error", response.getBody().getMessage());
    }

    @Test
    void testHandleInactiveInvoiceException() {
        InactiveInvoiceException exception = new InactiveInvoiceException(1L);
        ResponseEntity<ErrorResponse> response = handler.handleInactiveInvoiceException(exception);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();

    }

    @Test
    void testHandleAlreadyPaidInvoiceException() {
        AlreadyPaidInvoiceException exception = new AlreadyPaidInvoiceException(1L);
        ResponseEntity<ErrorResponse> response = handler.handleAlreadyPaidInvoiceException(exception);

        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();

    }

}
