package com.todolist.todolist_api.dto.response.base;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class PageResponseTest {

    @Test
    void mapToPageResponse_shouldCalculateTotalPages() {
        PageResponse<List<String>> response =
                PageResponse.mapToPageResponse(List.of("a", "b"), 0, 10, 25);

        assertThat(response.getPageNumber()).isEqualTo(0);
        assertThat(response.getPageSize()).isEqualTo(10);
        assertThat(response.getTotal()).isEqualTo(25);
        assertThat(response.getTotalPages()).isEqualTo(3);
        assertThat(response.getData()).containsExactly("a", "b");
    }

    @Test
    void mapToPageResponse_shouldReturnZeroTotalPagesWhenEmpty() {
        PageResponse<List<String>> response =
                PageResponse.mapToPageResponse(List.of(), 0, 10, 0);

        assertThat(response.getTotal()).isZero();
        assertThat(response.getTotalPages()).isZero();
    }
}
