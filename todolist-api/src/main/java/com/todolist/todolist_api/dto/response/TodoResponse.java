package com.todolist.todolist_api.dto.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class TodoResponse {

    private Long id;
    private String title;
    private String description;
    private LocalDateTime dueDate;
    private String status;
    private LocalDateTime createdOn;
    private LocalDateTime modifiedOn;
}
