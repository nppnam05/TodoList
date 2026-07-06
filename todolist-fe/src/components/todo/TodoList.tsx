import TodoCard from "./TodoCard";
import type { Todo } from "../../types/todo";
import { Sparkles } from "lucide-react";

interface TodoListProps {
  todos: Todo[];
  onEdit: (todo: Todo) => void;
}

export default function TodoList({ todos, onEdit }: TodoListProps) {
  if (todos.length === 0) {
    return (
      <div className="flex flex-col items-center justify-center py-20 px-4 bg-white border border-dashed border-gray-200 rounded-3xl text-center">
        <div className="h-14 w-14 bg-gradient-to-br from-indigo-100 to-blue-100 text-indigo-500 rounded-2xl flex items-center justify-center mb-4 shadow-inner">
          <Sparkles size={24} strokeWidth={1.5} />
        </div>
        <h3 className="text-base font-bold text-gray-800 mb-1.5">
          Không có công việc nào
        </h3>
        <p className="text-sm text-gray-400 max-w-xs leading-relaxed">
          Nhấn{" "}
          <span className="font-semibold text-indigo-600">Thêm công việc</span>{" "}
          để bắt đầu, hoặc thay đổi bộ lọc để xem các công việc khác.
        </p>
      </div>
    );
  }

  return (
    <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
      {todos.map((todo) => (
        <TodoCard key={todo.id} todo={todo} onEdit={onEdit} />
      ))}
    </div>
  );
}
