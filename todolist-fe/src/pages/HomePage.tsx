import { useState } from "react";
import { Plus } from "lucide-react";
import Layout from "../components/layout/Layout";
import FilterBar from "../components/filter/FilterBar";
import TodoList from "../components/todo/TodoList";
import PaginationControls from "../components/filter/PaginationControls";
import TodoModal from "../components/todo/TodoModal";
import LoadingSpinner from "../components/common/LoadingSpinner";
import type { Todo } from "../types/todo";
import { useGetAllTodosQuery } from "../store/api/api-todo";

export default function HomePage() {
  const [keyword, setKeyword] = useState("");
  const [status, setStatus] = useState("");
  const [priority, setPriority] = useState("");
  const [sortBy, setSortBy] = useState("createdOn");
  const [sortDirection, setSortDirection] = useState("desc");
  const [pageNumber, setPageNumber] = useState(0);
  const [pageSize, setPageSize] = useState(10);

  const [isModalOpen, setIsModalOpen] = useState(false);
  const [selectedTodo, setSelectedTodo] = useState<Todo | undefined>(undefined);

  const {
    data: paginatedData,
    isLoading,
    isFetching,
    error,
  } = useGetAllTodosQuery({
    keyword: keyword.trim() || undefined,
    status: status || undefined,
    priority: priority || undefined,
    sortBy,
    sortDirection,
    pageNumber,
    pageSize,
  });

  const handleFilterChange = (newFilters: {
    keyword: string;
    status: string;
    priority: string;
    sortBy: string;
    sortDirection: string;
  }) => {
    setKeyword(newFilters.keyword);
    setStatus(newFilters.status);
    setPriority(newFilters.priority);
    setSortBy(newFilters.sortBy);
    setSortDirection(newFilters.sortDirection);
    setPageNumber(0);
  };

  const handleEdit = (todo: Todo) => {
    setSelectedTodo(todo);
    setIsModalOpen(true);
  };

  const handleCreateOpen = () => {
    setSelectedTodo(undefined);
    setIsModalOpen(true);
  };

  const handleModalClose = () => {
    setIsModalOpen(false);
    setSelectedTodo(undefined);
  };

  return (
    <Layout>
      <div className="flex flex-col sm:flex-row sm:items-end sm:justify-between gap-5 mb-8">
        <div>
          <h2 className="text-2xl font-extrabold text-gray-900 tracking-tight leading-tight">
            Danh sách công việc
          </h2>
          <p className="text-sm text-gray-400 mt-1">
            Quản lý, sắp xếp và theo dõi tiến độ công việc của bạn.
          </p>
        </div>

        <button
          onClick={handleCreateOpen}
          className="inline-flex items-center gap-2 bg-gradient-to-r from-blue-600 to-indigo-600 hover:from-blue-700 hover:to-indigo-700 text-white font-semibold text-sm px-5 py-3 rounded-2xl shadow-lg shadow-indigo-500/20 hover:shadow-indigo-500/30 transition-all duration-200 cursor-pointer whitespace-nowrap"
        >
          <Plus size={16} strokeWidth={2.5} />
          Thêm công việc
        </button>
      </div>

      <FilterBar
        keyword={keyword}
        status={status}
        priority={priority}
        sortBy={sortBy}
        sortDirection={sortDirection}
        onFilterChange={handleFilterChange}
      />

      {isLoading ? (
        <div className="py-28 flex justify-center">
          <LoadingSpinner size="lg" />
        </div>
      ) : error ? (
        <div className="p-5 bg-red-50 border border-red-200 text-red-700 text-sm font-semibold rounded-3xl text-center">
          Không thể tải danh sách. Vui lòng kiểm tra lại kết nối đến server.
        </div>
      ) : (
        <div
          className={`transition-opacity duration-200 ${
            isFetching ? "opacity-50 pointer-events-none" : "opacity-100"
          }`}
        >
          <TodoList todos={paginatedData?.data || []} onEdit={handleEdit} />

          {paginatedData && (
            <PaginationControls
              pageNumber={pageNumber}
              pageSize={pageSize}
              total={paginatedData.total}
              totalPages={paginatedData.totalPages}
              onPageChange={setPageNumber}
              onPageSizeChange={(size) => {
                setPageSize(size);
                setPageNumber(0);
              }}
            />
          )}
        </div>
      )}

      <TodoModal
        isOpen={isModalOpen}
        onClose={handleModalClose}
        todo={selectedTodo}
      />
    </Layout>
  );
}
