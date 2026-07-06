package com.todolist.todolist_api.service;

import com.todolist.todolist_api.constant.TodoStatus;
import com.todolist.todolist_api.dto.request.CreateTodoRequest;
import com.todolist.todolist_api.dto.request.TodoFilterRequest;
import com.todolist.todolist_api.dto.request.UpdateStatusRequest;
import com.todolist.todolist_api.dto.request.UpdateTodoRequest;
import com.todolist.todolist_api.dto.response.TodoResponse;
import com.todolist.todolist_api.dto.response.TodoStatisticsResponse;
import com.todolist.todolist_api.dto.response.base.PageResponse;
import com.todolist.todolist_api.entity.Todo;
import com.todolist.todolist_api.exception.NotFoundException;
import com.todolist.todolist_api.mapper.TodoMapper;
import com.todolist.todolist_api.repository.TodoRepository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TodoServiceTest {

    @Mock
    private TodoRepository todoRepository;

    @Mock
    private TodoMapper todoMapper;

    @InjectMocks
    private TodoService todoService;

    @Test
    void getAll_shouldReturnPagedResults() {
        TodoFilterRequest filter = new TodoFilterRequest();
        filter.setStatus(TodoStatus.TODO.name());
        filter.setKeyword("test");
        filter.setPageNumber(0);
        filter.setPageSize(10);
        filter.setSortBy("createdOn");
        filter.setSortDirection("desc");

        Todo todo = buildTodo(1L, "Test todo");
        TodoResponse response = buildTodoResponse(1L, "Test todo");
        Page<Todo> page = new PageImpl<>(List.of(todo));

        when(todoRepository.findAllWithFilters(eq(TodoStatus.TODO.name()), eq("test"), any(Pageable.class)))
                .thenReturn(page);
        when(todoMapper.toResponse(todo)).thenReturn(response);

        PageResponse<List<TodoResponse>> result = todoService.getAll(filter);

        assertThat(result.getPageNumber()).isEqualTo(0);
        assertThat(result.getPageSize()).isEqualTo(10);
        assertThat(result.getTotal()).isEqualTo(1);
        assertThat(result.getData()).containsExactly(response);
    }

    @Test
    void getAll_shouldUseAscendingSortWhenRequested() {
        TodoFilterRequest filter = new TodoFilterRequest();
        filter.setPageNumber(0);
        filter.setPageSize(5);
        filter.setSortBy("title");
        filter.setSortDirection("asc");

        when(todoRepository.findAllWithFilters(any(), any(), any(Pageable.class)))
                .thenReturn(Page.empty());

        todoService.getAll(filter);

        ArgumentCaptor<Pageable> pageableCaptor = ArgumentCaptor.forClass(Pageable.class);
        verify(todoRepository).findAllWithFilters(any(), any(), pageableCaptor.capture());
        assertThat(pageableCaptor.getValue().getSort().getOrderFor("title").getDirection().name())
                .isEqualTo("ASC");
    }

    @Test
    void getById_shouldReturnTodoWhenFound() {
        Todo todo = buildTodo(1L, "Found");
        TodoResponse response = buildTodoResponse(1L, "Found");

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));
        when(todoMapper.toResponse(todo)).thenReturn(response);

        TodoResponse result = todoService.getById(1L);

        assertThat(result).isEqualTo(response);
    }

    @Test
    void getById_shouldThrowWhenNotFound() {
        when(todoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.getById(99L))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Todo not found with id: 99");
    }

    @Test
    void create_shouldSaveAndReturnMappedResponse() {
        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle("New todo");
        request.setDescription("Description");

        Todo entity = buildTodo(null, "New todo");
        Todo saved = buildTodo(1L, "New todo");
        TodoResponse response = buildTodoResponse(1L, "New todo");

        when(todoMapper.toEntity(request)).thenReturn(entity);
        when(todoRepository.save(entity)).thenReturn(saved);
        when(todoMapper.toResponse(saved)).thenReturn(response);

        TodoResponse result = todoService.create(request);

        assertThat(result).isEqualTo(response);
        verify(todoRepository).save(entity);
    }

    @Test
    void update_shouldUpdateExistingTodo() {
        UpdateTodoRequest request = new UpdateTodoRequest();
        request.setTitle("Updated title");

        Todo existing = buildTodo(1L, "Old title");
        Todo saved = buildTodo(1L, "Updated title");
        TodoResponse response = buildTodoResponse(1L, "Updated title");

        when(todoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(todoRepository.save(existing)).thenReturn(saved);
        when(todoMapper.toResponse(saved)).thenReturn(response);

        TodoResponse result = todoService.update(1L, request);

        assertThat(result.getTitle()).isEqualTo("Updated title");
        verify(todoMapper).updateEntity(request, existing);
        verify(todoRepository).save(existing);
    }

    @Test
    void update_shouldThrowWhenNotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.update(1L, new UpdateTodoRequest()))
                .isInstanceOf(NotFoundException.class);
    }

    @Test
    void updateStatus_shouldUpdateStatus() {
        UpdateStatusRequest request = new UpdateStatusRequest();
        request.setStatus(TodoStatus.DONE.name());

        Todo existing = buildTodo(1L, "Todo");
        existing.setStatus(TodoStatus.TODO.name());
        Todo saved = buildTodo(1L, "Todo");
        saved.setStatus(TodoStatus.DONE.name());
        TodoResponse response = buildTodoResponse(1L, "Todo");
        response.setStatus(TodoStatus.DONE.name());

        when(todoRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(todoRepository.save(existing)).thenReturn(saved);
        when(todoMapper.toResponse(saved)).thenReturn(response);

        TodoResponse result = todoService.updateStatus(1L, request);

        assertThat(result.getStatus()).isEqualTo(TodoStatus.DONE.name());
        assertThat(existing.getStatus()).isEqualTo(TodoStatus.DONE.name());
    }

    @Test
    void delete_shouldRemoveExistingTodo() {
        Todo todo = buildTodo(1L, "To delete");

        when(todoRepository.findById(1L)).thenReturn(Optional.of(todo));

        todoService.delete(1L);

        verify(todoRepository).delete(todo);
    }

    @Test
    void delete_shouldThrowWhenNotFound() {
        when(todoRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> todoService.delete(1L))
                .isInstanceOf(NotFoundException.class);

        verify(todoRepository, never()).delete(any());
    }

    @Test
    void getStatistics_shouldReturnCounts() {
        when(todoRepository.count()).thenReturn(10L);
        when(todoRepository.countByStatus(TodoStatus.TODO.name())).thenReturn(6L);
        when(todoRepository.countByStatus(TodoStatus.DONE.name())).thenReturn(4L);

        TodoStatisticsResponse result = todoService.getStatistics();

        assertThat(result.getTotal()).isEqualTo(10);
        assertThat(result.getTotalTodo()).isEqualTo(6);
        assertThat(result.getTotalDone()).isEqualTo(4);
    }

    @Test
    void getOverdue_shouldReturnMappedTodos() {
        Todo todo = buildTodo(1L, "Overdue");
        TodoResponse response = buildTodoResponse(1L, "Overdue");

        when(todoRepository.findOverdueSoon(any(LocalDateTime.class), any(LocalDateTime.class),
                eq(List.of(TodoStatus.TODO.name())))).thenReturn(List.of(todo));
        when(todoMapper.toResponse(todo)).thenReturn(response);

        List<TodoResponse> result = todoService.getOverdue(7);

        assertThat(result).containsExactly(response);
    }

    private Todo buildTodo(Long id, String title) {
        return Todo.builder()
                .id(id)
                .title(title)
                .description("Description")
                .status(TodoStatus.TODO.name())
                .createdOn(LocalDateTime.now())
                .modifiedOn(LocalDateTime.now())
                .build();
    }

    private TodoResponse buildTodoResponse(Long id, String title) {
        TodoResponse response = new TodoResponse();
        response.setId(id);
        response.setTitle(title);
        response.setDescription("Description");
        response.setStatus(TodoStatus.TODO.name());
        response.setCreatedOn(LocalDateTime.now());
        response.setModifiedOn(LocalDateTime.now());
        return response;
    }
}
