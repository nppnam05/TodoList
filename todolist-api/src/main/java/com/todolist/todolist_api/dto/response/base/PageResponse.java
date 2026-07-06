package com.todolist.todolist_api.dto.response.base;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
    private int pageNumber;
    private int pageSize;
    private long total;
    private int totalPages;
    private T data;

    public static <T> PageResponse<T> mapToPageResponse(T data, int pageNumber, int pageSize,
            long total) {
        return new PageResponse<>(pageNumber, pageSize, total,
                (int) Math.ceil((double) total / pageSize), data);
    }
}
