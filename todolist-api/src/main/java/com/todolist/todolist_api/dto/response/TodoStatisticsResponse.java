package com.todolist.todolist_api.dto.response;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TodoStatisticsResponse {

    private long total;
    private long totalTodo;
    private long totalDone;
}
