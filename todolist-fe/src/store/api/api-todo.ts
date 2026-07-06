import { createApi } from "@reduxjs/toolkit/query/react";
import { baseQuery } from "../../lib/api";
import type { BaseResponse, PaginatedResponse } from "../../types/response";
import type {
  Todo,
  TodoStatistics,
} from "../../types/todo";

interface CreateTodoRequest {
  title: string;
  description?: string;
  priority?: string;
  dueDate?: string;
  status?: string;
}

interface UpdateTodoRequest {
  title?: string;
  description?: string;
  priority?: string;
  dueDate?: string;
  status?: string;
}

interface UpdateStatusRequest {
  status: string;
}


export const todoApi = createApi({
  reducerPath: "todo",
  baseQuery: baseQuery,
  tagTypes: ["Todo"],
  endpoints: (builder) => ({
    getAll: builder.query<PaginatedResponse<Todo>, any>({
      query: (params) => ({
        url: "/todos",
        method: "GET",
        params,
      }),
      transformResponse: (response: BaseResponse<PaginatedResponse<Todo>>) => {
        if (response.succeeded && response.data) {
          return response.data;
        }
        throw new Error(response.message || "Get all todos failed");
      },
      providesTags: ["Todo"],
    }),

    getStatistics: builder.query<TodoStatistics, void>({
      query: () => ({
        url: "/todos/statistics",
        method: "GET",
      }),
      transformResponse: (response: BaseResponse<TodoStatistics>) => {
        if (response.succeeded && response.data) {
          return response.data;
        }
        throw new Error(response.message || "Get statistics failed");
      },
      providesTags: ["Todo"],
    }),

    getOverdue: builder.query<Todo[], number | void>({
      query: (days) => ({
        url: "/todos/overdue",
        method: "GET",
        params: days ? { days } : undefined,
      }),
      transformResponse: (response: BaseResponse<Todo[]>) => {
        if (response.succeeded && response.data) {
          return response.data;
        }
        throw new Error(response.message || "Get overdue todos failed");
      },
      providesTags: ["Todo"],
    }),

    getById: builder.query<Todo, number>({
      query: (id) => ({
        url: `/todos/${id}`,
        method: "GET",
      }),
      transformResponse: (response: BaseResponse<Todo>) => {
        if (response.succeeded && response.data) {
          return response.data;
        }
        throw new Error(response.message || "Get todo by id failed");
      },
      providesTags: (_, __, id) => [{ type: "Todo", id }],
    }),

    create: builder.mutation<Todo, CreateTodoRequest>({
      query: (body) => ({
        url: "/todos",
        method: "POST",
        body,
      }),
      transformResponse: (response: BaseResponse<Todo>) => {
        if (response.succeeded && response.data) {
          return response.data;
        }
        throw new Error(response.message || "Create todo failed");
      },
      invalidatesTags: ["Todo"],
    }),

    update: builder.mutation<Todo, { id: number; body: UpdateTodoRequest }>({
      query: ({ id, body }) => ({
        url: `/todos/${id}`,
        method: "PUT",
        body,
      }),
      transformResponse: (response: BaseResponse<Todo>) => {
        if (response.succeeded && response.data) {
          return response.data;
        }
        throw new Error(response.message || "Update todo failed");
      },
      invalidatesTags: (_, __, { id }) => [{ type: "Todo", id }, "Todo"],
    }),

    updateStatus: builder.mutation<Todo, { id: number; body: UpdateStatusRequest }>({
      query: ({ id, body }) => ({
        url: `/todos/${id}/status`,
        method: "PATCH",
        body,
      }),
      transformResponse: (response: BaseResponse<Todo>) => {
        if (response.succeeded && response.data) {
          return response.data;
        }
        throw new Error(response.message || "Update status failed");
      },
      invalidatesTags: (_, __, { id }) => [{ type: "Todo", id }, "Todo"],
    }),

    delete: builder.mutation<void, number>({
      query: (id) => ({
        url: `/todos/${id}`,
        method: "DELETE",
      }),
      transformResponse: (response: BaseResponse<void>) => {
        if (response.succeeded) {
          return;
        }
        throw new Error(response.message || "Delete todo failed");
      },
      invalidatesTags: (_, __, id) => [{ type: "Todo", id }, "Todo"],
    }),
  }),
});

export const {
  useGetAllQuery: useGetAllTodosQuery,
  useGetStatisticsQuery,
  useGetOverdueQuery,
  useGetByIdQuery,
  useCreateMutation,
  useUpdateMutation,
  useUpdateStatusMutation,
  useDeleteMutation,
} = todoApi;
