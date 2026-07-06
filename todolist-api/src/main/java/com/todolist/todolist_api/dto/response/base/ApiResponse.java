package com.todolist.todolist_api.dto.response.base;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> {
    private boolean succeeded;
    private String message;
    private int statusCode;
    private T data;
    private Object errors;

    public static <T> ApiResponse<T> success(T data, String message, int statusCode) {
        return ApiResponse.<T>builder().succeeded(true).message(message).statusCode(statusCode)
                .data(data).build();
    }

    public static <T> ApiResponse<T> error(String message, int statusCode, Object errors) {
        return ApiResponse.<T>builder().succeeded(false).message(message).statusCode(statusCode)
                .errors(errors).build();
    }
}
