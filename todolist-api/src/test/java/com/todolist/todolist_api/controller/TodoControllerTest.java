package com.todolist.todolist_api.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.todolist.todolist_api.constant.TodoStatus;
import com.todolist.todolist_api.dto.request.CreateTodoRequest;
import com.todolist.todolist_api.dto.request.UpdateStatusRequest;
import com.todolist.todolist_api.dto.request.UpdateTodoRequest;
import com.todolist.todolist_api.dto.response.TodoResponse;
import com.todolist.todolist_api.dto.response.TodoStatisticsResponse;
import com.todolist.todolist_api.dto.response.base.PageResponse;
import com.todolist.todolist_api.exception.GlobalExceptionHandler;
import com.todolist.todolist_api.exception.NotFoundException;
import com.todolist.todolist_api.service.TodoService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TodoController.class)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class TodoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private TodoService todoService;

    @Test
    void getAll_shouldReturnPagedTodos() throws Exception {
        TodoResponse todo = buildTodoResponse(1L);
        PageResponse<List<TodoResponse>> page = PageResponse.mapToPageResponse(List.of(todo), 0, 10, 1);

        when(todoService.getAll(any())).thenReturn(page);

        mockMvc.perform(get("/api/v1/todos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.succeeded").value(true))
                .andExpect(jsonPath("$.message").value("Get all todos successfully"))
                .andExpect(jsonPath("$.data.total").value(1))
                .andExpect(jsonPath("$.data.data[0].id").value(1));
    }

    @Test
    void getById_shouldReturnTodo() throws Exception {
        when(todoService.getById(1L)).thenReturn(buildTodoResponse(1L));

        mockMvc.perform(get("/api/v1/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.succeeded").value(true))
                .andExpect(jsonPath("$.data.id").value(1))
                .andExpect(jsonPath("$.data.title").value("Test todo"));
    }

    @Test
    void getById_shouldReturn404WhenNotFound() throws Exception {
        when(todoService.getById(99L)).thenThrow(new NotFoundException("Todo not found with id: 99"));

        mockMvc.perform(get("/api/v1/todos/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.succeeded").value(false))
                .andExpect(jsonPath("$.statusCode").value(404));
    }

    @Test
    void create_shouldReturnCreatedTodo() throws Exception {
        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle("New todo");

        when(todoService.create(any(CreateTodoRequest.class))).thenReturn(buildTodoResponse(1L));

        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.succeeded").value(true))
                .andExpect(jsonPath("$.statusCode").value(201))
                .andExpect(jsonPath("$.data.title").value("Test todo"));
    }

    @Test
    void create_shouldReturn400WhenTitleMissing() throws Exception {
        CreateTodoRequest request = new CreateTodoRequest();

        mockMvc.perform(post("/api/v1/todos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.succeeded").value(false))
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void update_shouldReturnUpdatedTodo() throws Exception {
        UpdateTodoRequest request = new UpdateTodoRequest();
        request.setTitle("Updated");

        TodoResponse updated = buildTodoResponse(1L);
        updated.setTitle("Updated");

        when(todoService.update(eq(1L), any(UpdateTodoRequest.class))).thenReturn(updated);

        mockMvc.perform(put("/api/v1/todos/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.title").value("Updated"));
    }

    @Test
    void updateStatus_shouldReturnUpdatedTodo() throws Exception {
        UpdateStatusRequest request = new UpdateStatusRequest();
        request.setStatus(TodoStatus.DONE.name());

        TodoResponse updated = buildTodoResponse(1L);
        updated.setStatus(TodoStatus.DONE.name());

        when(todoService.updateStatus(eq(1L), any(UpdateStatusRequest.class))).thenReturn(updated);

        mockMvc.perform(patch("/api/v1/todos/1/status")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.status").value("DONE"));
    }

    @Test
    void delete_shouldReturnSuccess() throws Exception {
        doNothing().when(todoService).delete(1L);

        mockMvc.perform(delete("/api/v1/todos/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.succeeded").value(true))
                .andExpect(jsonPath("$.message").value("Delete todo successfully"));
    }

    @Test
    void delete_shouldReturn404WhenNotFound() throws Exception {
        doThrow(new NotFoundException("Todo not found with id: 1")).when(todoService).delete(1L);

        mockMvc.perform(delete("/api/v1/todos/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.statusCode").value(404));
    }

    @Test
    void getStatistics_shouldReturnCounts() throws Exception {
        TodoStatisticsResponse statistics = TodoStatisticsResponse.builder()
                .total(10)
                .totalTodo(6)
                .totalDone(4)
                .build();

        when(todoService.getStatistics()).thenReturn(statistics);

        mockMvc.perform(get("/api/v1/todos/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.total").value(10))
                .andExpect(jsonPath("$.data.totalTodo").value(6))
                .andExpect(jsonPath("$.data.totalDone").value(4));
    }

    @Test
    void getOverdue_shouldReturnList() throws Exception {
        when(todoService.getOverdue(7)).thenReturn(List.of(buildTodoResponse(1L)));

        mockMvc.perform(get("/api/v1/todos/overdue").param("days", "7"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data[0].id").value(1));
    }

    private TodoResponse buildTodoResponse(Long id) {
        TodoResponse response = new TodoResponse();
        response.setId(id);
        response.setTitle("Test todo");
        response.setDescription("Description");
        response.setStatus(TodoStatus.TODO.name());
        response.setCreatedOn(LocalDateTime.of(2026, 1, 1, 10, 0));
        response.setModifiedOn(LocalDateTime.of(2026, 1, 1, 10, 0));
        return response;
    }
}
