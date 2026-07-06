package com.todolist.todolist_api.mapper;

import com.todolist.todolist_api.dto.request.CreateTodoRequest;
import com.todolist.todolist_api.dto.request.UpdateTodoRequest;
import com.todolist.todolist_api.dto.response.TodoResponse;
import com.todolist.todolist_api.entity.Todo;
import org.mapstruct.*;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface TodoMapper {

    TodoResponse toResponse(Todo todo);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "modifiedOn", ignore = true)
    Todo toEntity(CreateTodoRequest request);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createdOn", ignore = true)
    @Mapping(target = "modifiedOn", ignore = true)
    void updateEntity(UpdateTodoRequest request, @MappingTarget Todo todo);
}
