package com.todolist.todolist_api.exception;

import com.todolist.todolist_api.dto.response.base.ApiResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import static org.assertj.core.api.Assertions.assertThat;

class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler handler;

    @BeforeEach
    void setUp() {
        handler = new GlobalExceptionHandler();
    }

    @Test
    void handleNotFound_shouldReturn404() {
        ResponseEntity<ApiResponse<Void>> response =
                handler.handleNotFound(new NotFoundException("Todo not found"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().isSucceeded()).isFalse();
        assertThat(response.getBody().getStatusCode()).isEqualTo(404);
        assertThat(response.getBody().getMessage()).isEqualTo("Todo not found");
    }

    @Test
    void handleBadRequest_shouldReturn400() {
        ResponseEntity<ApiResponse<Void>> response =
                handler.handleBadRequest(new BadRequestException("Invalid input"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatusCode()).isEqualTo(400);
        assertThat(response.getBody().getMessage()).isEqualTo("Invalid input");
    }

    @Test
    void handleRuntime_shouldReturn400() {
        ResponseEntity<ApiResponse<Void>> response =
                handler.handleRuntime(new RuntimeException("Something went wrong"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Something went wrong");
    }

    @Test
    void handleValidation_shouldReturnFieldErrors() throws NoSuchMethodException {
        BeanPropertyBindingResult bindingResult =
                new BeanPropertyBindingResult(new Object(), "request");
        bindingResult.addError(new FieldError("request", "title", "Title is required"));

        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(
                null, bindingResult);

        ResponseEntity<ApiResponse<Void>> response = handler.handleValidation(ex);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getMessage()).isEqualTo("Validation failed");
        assertThat(response.getBody().getErrors()).asList()
                .containsExactly("title: Title is required");
    }

    @Test
    void handleGeneral_shouldReturn500() {
        ResponseEntity<ApiResponse<Void>> response =
                handler.handleGeneral(new Exception("Unexpected"));

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getStatusCode()).isEqualTo(500);
        assertThat(response.getBody().getMessage()).isEqualTo("Internal server error");
    }
}
