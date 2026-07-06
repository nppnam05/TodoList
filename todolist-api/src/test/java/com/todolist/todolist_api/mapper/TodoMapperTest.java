package com.todolist.todolist_api.mapper;

import com.todolist.todolist_api.constant.TodoStatus;
import com.todolist.todolist_api.dto.request.CreateTodoRequest;
import com.todolist.todolist_api.dto.request.UpdateTodoRequest;
import com.todolist.todolist_api.dto.response.TodoResponse;
import com.todolist.todolist_api.entity.Todo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class TodoMapperTest {

    private TodoMapper todoMapper;

    @BeforeEach
    void setUp() {
        todoMapper = Mappers.getMapper(TodoMapper.class);
    }

    @Test
    void toResponse_shouldMapAllFields() {
        LocalDateTime now = LocalDateTime.of(2026, 3, 15, 12, 0);
        Todo todo = Todo.builder()
                .id(1L)
                .title("Buy milk")
                .description("From supermarket")
                .dueDate(now.plusDays(1))
                .status(TodoStatus.TODO.name())
                .createdOn(now)
                .modifiedOn(now)
                .build();

        TodoResponse response = todoMapper.toResponse(todo);

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getTitle()).isEqualTo("Buy milk");
        assertThat(response.getDescription()).isEqualTo("From supermarket");
        assertThat(response.getDueDate()).isEqualTo(now.plusDays(1));
        assertThat(response.getStatus()).isEqualTo(TodoStatus.TODO.name());
        assertThat(response.getCreatedOn()).isEqualTo(now);
        assertThat(response.getModifiedOn()).isEqualTo(now);
    }

    @Test
    void toEntity_shouldMapRequestAndIgnoreAuditFields() {
        CreateTodoRequest request = new CreateTodoRequest();
        request.setTitle("New task");
        request.setDescription("Details");
        request.setStatus(TodoStatus.DONE.name());

        Todo entity = todoMapper.toEntity(request);

        assertThat(entity.getId()).isNull();
        assertThat(entity.getTitle()).isEqualTo("New task");
        assertThat(entity.getDescription()).isEqualTo("Details");
        assertThat(entity.getStatus()).isEqualTo(TodoStatus.DONE.name());
        assertThat(entity.getCreatedOn()).isNull();
        assertThat(entity.getModifiedOn()).isNull();
    }

    @Test
    void updateEntity_shouldUpdateOnlyNonNullFields() {
        Todo todo = Todo.builder()
                .id(1L)
                .title("Old title")
                .description("Old description")
                .status(TodoStatus.TODO.name())
                .build();

        UpdateTodoRequest request = new UpdateTodoRequest();
        request.setTitle("New title");

        todoMapper.updateEntity(request, todo);

        assertThat(todo.getId()).isEqualTo(1L);
        assertThat(todo.getTitle()).isEqualTo("New title");
        assertThat(todo.getDescription()).isEqualTo("Old description");
        assertThat(todo.getStatus()).isEqualTo(TodoStatus.TODO.name());
    }
}
