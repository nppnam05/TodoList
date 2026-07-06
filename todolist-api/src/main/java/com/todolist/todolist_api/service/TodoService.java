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

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TodoService {

    private final TodoRepository todoRepository;
    private final TodoMapper todoMapper;

    public PageResponse<List<TodoResponse>> getAll(TodoFilterRequest filter) {
        Sort sort = filter.getSortDirection().equalsIgnoreCase("asc")
                ? Sort.by(filter.getSortBy()).ascending()
                : Sort.by(filter.getSortBy()).descending();

        Pageable pageable = PageRequest.of(filter.getPageNumber(), filter.getPageSize(), sort);

        Page<Todo> page = todoRepository.findAllWithFilters(
                filter.getStatus(),
                filter.getKeyword(),
                pageable);

        List<TodoResponse> data = page.getContent()
                .stream()
                .map(todoMapper::toResponse)
                .toList();

        return PageResponse.mapToPageResponse(data, filter.getPageNumber(), filter.getPageSize(),
                page.getTotalElements());
    }

    public TodoResponse getById(Long id) {
        Todo todo = findById(id);
        return todoMapper.toResponse(todo);
    }

    @Transactional
    public TodoResponse create(CreateTodoRequest request) {
        Todo todo = todoMapper.toEntity(request);
        Todo saved = todoRepository.save(todo);
        return todoMapper.toResponse(saved);
    }

    @Transactional
    public TodoResponse update(Long id, UpdateTodoRequest request) {
        Todo todo = findById(id);
        todoMapper.updateEntity(request, todo);
        Todo saved = todoRepository.save(todo);
        return todoMapper.toResponse(saved);
    }

    @Transactional
    public TodoResponse updateStatus(Long id, UpdateStatusRequest request) {
        Todo todo = findById(id);
        todo.setStatus(request.getStatus());
        Todo saved = todoRepository.save(todo);
        return todoMapper.toResponse(saved);
    }

    @Transactional
    public void delete(Long id) {
        Todo todo = findById(id);
        todoRepository.delete(todo);
    }

    public TodoStatisticsResponse getStatistics() {
        long total = todoRepository.count();
        long totalTodo = todoRepository.countByStatus(TodoStatus.TODO.name());
        long totalDone = todoRepository.countByStatus(TodoStatus.DONE.name());

        return TodoStatisticsResponse.builder()
                .total(total)
                .totalTodo(totalTodo)
                .totalDone(totalDone)
                .build();
    }

    public List<TodoResponse> getOverdue(int days) {
        List<String> statuses = Arrays.asList(TodoStatus.TODO.name());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime deadline = now.plusDays(days);
        return todoRepository.findOverdueSoon(now, deadline, statuses)
                .stream()
                .map(todoMapper::toResponse)
                .toList();
    }

    private Todo findById(Long id) {
        return todoRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Todo not found with id: " + id));
    }
}
