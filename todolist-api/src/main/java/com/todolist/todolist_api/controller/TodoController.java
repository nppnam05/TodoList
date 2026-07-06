package com.todolist.todolist_api.controller;

import com.todolist.todolist_api.constant.AppConstants;
import com.todolist.todolist_api.dto.request.CreateTodoRequest;
import com.todolist.todolist_api.dto.request.TodoFilterRequest;
import com.todolist.todolist_api.dto.request.UpdateStatusRequest;
import com.todolist.todolist_api.dto.request.UpdateTodoRequest;
import com.todolist.todolist_api.dto.response.TodoResponse;
import com.todolist.todolist_api.dto.response.TodoStatisticsResponse;
import com.todolist.todolist_api.dto.response.base.ApiResponse;
import com.todolist.todolist_api.dto.response.base.PageResponse;
import com.todolist.todolist_api.service.TodoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/todos")
@RequiredArgsConstructor
public class TodoController {

    private final TodoService todoService;


    @GetMapping
    public ResponseEntity<ApiResponse<PageResponse<List<TodoResponse>>>> getAll(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String keyword,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_NUMBER) int pageNumber,
            @RequestParam(defaultValue = AppConstants.DEFAULT_PAGE_SIZE) int pageSize,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_BY_CREATED_ON) String sortBy,
            @RequestParam(defaultValue = AppConstants.DEFAULT_SORT_DIRECTION_DESC) String sortDirection) {

        TodoFilterRequest filter = new TodoFilterRequest();
        filter.setStatus(status);
        filter.setKeyword(keyword);
        filter.setPageNumber(pageNumber);
        filter.setPageSize(pageSize);
        filter.setSortBy(sortBy);
        filter.setSortDirection(sortDirection);

        PageResponse<List<TodoResponse>> data = todoService.getAll(filter);
        return ResponseEntity.ok(ApiResponse.success(data, "Get all todos successfully", 200));
    }

    @GetMapping("/statistics")
    public ResponseEntity<ApiResponse<TodoStatisticsResponse>> getStatistics() {
        TodoStatisticsResponse statistics = todoService.getStatistics();
        return ResponseEntity.ok(ApiResponse.success(statistics, "Get statistics successfully", 200));
    }

    @GetMapping("/overdue")
    public ResponseEntity<ApiResponse<List<TodoResponse>>> getOverdue(
            @RequestParam(defaultValue = "7") int days) {
        List<TodoResponse> data = todoService.getOverdue(days);
        return ResponseEntity.ok(ApiResponse.success(data, "Get overdue todos successfully", 200));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponse>> getById(@PathVariable Long id) {
        TodoResponse data = todoService.getById(id);
        return ResponseEntity.ok(ApiResponse.success(data, "Get todo successfully", 200));
    }

    @PostMapping
    public ResponseEntity<ApiResponse<TodoResponse>> create(
            @Valid @RequestBody CreateTodoRequest request) {
        TodoResponse data = todoService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(data, "Create todo successfully", 201));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TodoResponse>> update(
            @PathVariable Long id,
            @Valid @RequestBody UpdateTodoRequest request) {
        TodoResponse data = todoService.update(id, request);
        return ResponseEntity.ok(ApiResponse.success(data, "Update todo successfully", 200));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<TodoResponse>> updateStatus(
            @PathVariable Long id,
            @Valid @RequestBody UpdateStatusRequest request) {
        TodoResponse data = todoService.updateStatus(id, request);
        return ResponseEntity.ok(ApiResponse.success(data, "Update status successfully", 200));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> delete(@PathVariable Long id) {
        todoService.delete(id);
        return ResponseEntity.ok(ApiResponse.success(null, "Delete todo successfully", 200));
    }
}
