package com.example.resource_client_2.advisors;

import com.example.resource_client_2.exceptions.BadRequest;
import com.example.resource_client_2.exceptions.NotFound;
import com.example.resource_client_2.models.ErrorResponseModel;
import com.example.resource_client_2.models.MasterResponse;
import com.example.resource_client_2.models.Status;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class BaseExceptionAdvisor {

        private static final Logger log = LoggerFactory.getLogger(BaseExceptionAdvisor.class);

        // -----------------------------------------------------------
        // 400 - BadRequest
        // -----------------------------------------------------------
        @ExceptionHandler(BadRequest.class)
        public ResponseEntity<MasterResponse<ErrorResponseModel>> handleBadRequest(BadRequest ex) {

                log.warn("Bad Request: {}", ex.getMessage());

                Status status = Status.of(
                                HttpStatus.BAD_REQUEST.value(),
                                "BAD_REQUEST",
                                ex.getMessage());

                return buildErrorResponse(status, ex.getMessage());
        }

        // -----------------------------------------------------------
        // 404 - NotFound
        // -----------------------------------------------------------
        @ExceptionHandler(NotFound.class)
        public ResponseEntity<MasterResponse<ErrorResponseModel>> handleNotFound(NotFound ex) {

                log.warn("Not Found: {}", ex.getMessage());

                Status status = Status.of(
                                HttpStatus.NOT_FOUND.value(),
                                "NOT_FOUND",
                                ex.getMessage());

                return buildErrorResponse(status, ex.getMessage());
        }

        // -----------------------------------------------------------
        // Data Integrity Violation (409)
        // -----------------------------------------------------------
        @ExceptionHandler(DataIntegrityViolationException.class)
        public ResponseEntity<MasterResponse<ErrorResponseModel>> handleConflict(DataIntegrityViolationException ex) {

                log.error("Data Integrity Constraint: {}", ex.getMessage());

                Status status = Status.of(
                                HttpStatus.CONFLICT.value(),
                                "DATA_INTEGRITY_VIOLATION",
                                "Database constraint violated");

                return buildErrorResponse(status, "Data integrity violation occurred",
                                ex.getMostSpecificCause().getMessage());
        }

        // -----------------------------------------------------------
        // Validation Errors (400)
        // -----------------------------------------------------------
        @ExceptionHandler(MethodArgumentNotValidException.class)
        public ResponseEntity<MasterResponse<ErrorResponseModel>> handleValidation(MethodArgumentNotValidException ex) {

                log.warn("Validation Failed: {}", ex.getMessage());

                Map<String, String> errors = ex.getBindingResult().getFieldErrors()
                                .stream()
                                .collect(Collectors.toMap(
                                                field -> field.getField(),
                                                field -> field.getDefaultMessage(),
                                                (a, b) -> a));

                Status status = Status.of(
                                HttpStatus.BAD_REQUEST.value(),
                                "VALIDATION_FAILED",
                                "Invalid input data");

                return buildErrorResponse(status, "Validation error", errors);
        }

        // -----------------------------------------------------------
        // FALLBACK - Internal Server Error (500)
        // -----------------------------------------------------------
        @ExceptionHandler(Exception.class)
        public ResponseEntity<MasterResponse<ErrorResponseModel>> handleGeneric(Exception ex) {

                log.error("Unhandled Exception: ", ex);

                Status status = Status.of(
                                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                                "INTERNAL_SERVER_ERROR",
                                "Unexpected error");

                return buildErrorResponse(status, "Unexpected server error", ex.getMessage());
        }

        // -----------------------------------------------------------
        // Core Utility Builder
        // -----------------------------------------------------------
        private ResponseEntity<MasterResponse<ErrorResponseModel>> buildErrorResponse(
                        Status status,
                        String message,
                        Object details,
                        HttpHeaders headers) {

                ErrorResponseModel error = ErrorResponseModel.builder()
                                .message(message)
                                .timestamp(Instant.now())
                                .details(details != null ? details : new HashMap<>())
                                .build();

                MasterResponse<ErrorResponseModel> response = MasterResponse.<ErrorResponseModel>builder()
                                .status(status)
                                .data(error)
                                .build();

                if (headers == null) {
                        headers = new HttpHeaders();
                }

                return ResponseEntity.status(status.getStatusCode())
                                .headers(headers)
                                .body(response);
        }

        private ResponseEntity<MasterResponse<ErrorResponseModel>> buildErrorResponse(
                        Status status,
                        String message) {
                return buildErrorResponse(status, message, null, null);
        }

        private ResponseEntity<MasterResponse<ErrorResponseModel>> buildErrorResponse(
                        Status status,
                        String message,
                        Object details) {
                return buildErrorResponse(status, message, details, null);
        }
}
