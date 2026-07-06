package com.todolist.todolist_api.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TodoFilterRequest {

    private String status;
    private String keyword;
    private int pageNumber;
    private int pageSize;
    private String sortBy;
    private String sortDirection;
}
