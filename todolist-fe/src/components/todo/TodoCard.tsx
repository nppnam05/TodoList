import { Edit2, Trash2, CalendarDays } from "lucide-react";
import type { Todo } from "../../types/todo";
import { formatDateTime } from "../../utils/format";
import {
  useUpdateStatusMutation,
  useDeleteMutation,
} from "../../store/api/api-todo";
import { STATUS } from "../../constant/constant";

interface TodoCardProps {
  todo: Todo;
  onEdit: (todo: Todo) => void;
}

export default function TodoCard({ todo, onEdit }: TodoCardProps) {
  const [updateStatus, { isLoading: isUpdating }] = useUpdateStatusMutation();
  const [deleteTodo, { isLoading: isDeleting }] = useDeleteMutation();

  const isDone = todo.status === STATUS.DONE;
  const isOverdue =
    todo.dueDate && new Date(todo.dueDate).getTime() < Date.now() && !isDone;

  const handleToggleStatus = async () => {
    const nextStatus = isDone ? STATUS.TODO : STATUS.DONE;
    try {
      await updateStatus({
        id: todo.id,
        body: { status: nextStatus },
      }).unwrap();
    } catch (err) {
      console.error("Failed to update status:", err);
    }
  };

  const handleDelete = async () => {
    if (window.confirm(`Xóa công việc "${todo.title}"?`)) {
      try {
        await deleteTodo(todo.id).unwrap();
      } catch (err) {
        console.error("Failed to delete todo:", err);
      }
    }
  };

  return (
    <div
      className={`group relative bg-white rounded-3xl border transition-all duration-200 hover:shadow-lg hover:-translate-y-0.5 flex flex-col overflow-hidden ${
        isOverdue
          ? "border-red-200 shadow-sm shadow-red-100"
          : "border-gray-100 shadow-sm"
      }`}
    >
      <div
        className={`absolute left-0 top-0 bottom-0 w-1.5 rounded-l-3xl transition-colors ${
          isDone ? "bg-emerald-500" : "bg-indigo-500"
        }`}
      />

      <div className="pl-6 pr-5 pt-5 pb-4 flex flex-col gap-3 flex-1">
        <div className="flex items-start justify-between">
          <div className="flex items-center gap-3">
            <input
              type="checkbox"
              checked={isDone}
              onChange={handleToggleStatus}
              disabled={isUpdating}
              className="h-6 w-6 rounded-xl border-2 border-gray-300 text-emerald-600 focus:ring-emerald-500 focus:ring-offset-0 cursor-pointer accent-emerald-600 disabled:opacity-50"
            />

            <div>
              <span
                className={`inline-flex items-center px-3 py-1 text-xs font-bold uppercase tracking-widest rounded-full border ${
                  isDone
                    ? "bg-emerald-100 text-emerald-700 border-emerald-200"
                    : "bg-indigo-100 text-indigo-700 border-indigo-200"
                }`}
              >
                {isDone ? "HOÀN THÀNH" : "CHƯA LÀM"}
              </span>
            </div>
          </div>

          <div className="flex items-center gap-1 opacity-0 group-hover:opacity-100 transition-opacity">
            <button
              onClick={() => onEdit(todo)}
              title="Chỉnh sửa"
              className="h-8 w-8 flex items-center justify-center rounded-2xl text-gray-400 hover:bg-indigo-50 hover:text-indigo-600 transition-colors"
            >
              <Edit2 size={16} strokeWidth={2.5} />
            </button>
            <button
              onClick={handleDelete}
              disabled={isDeleting}
              title="Xóa"
              className="h-8 w-8 flex items-center justify-center rounded-2xl text-gray-400 hover:bg-red-50 hover:text-red-600 transition-colors disabled:opacity-50"
            >
              <Trash2 size={16} strokeWidth={2.5} />
            </button>
          </div>
        </div>

        <h4
          className={`font-semibold text-[15.5px] leading-tight text-gray-900 pr-8 ${
            isDone ? "line-through text-gray-400" : ""
          }`}
        >
          {todo.title}
        </h4>

        {todo.description && (
          <p
            className={`text-sm text-gray-600 leading-relaxed line-clamp-2 pr-8 ${
              isDone ? "line-through opacity-70" : ""
            }`}
          >
            {todo.description}
          </p>
        )}
      </div>

      <div className="px-6 py-3.5 border-t border-gray-100 flex items-center justify-between bg-gray-50/70 text-sm">
        <div
          className={`flex items-center gap-1.5 font-medium ${
            isOverdue ? "text-red-600" : "text-gray-500"
          }`}
        >
          <CalendarDays size={14} />
          {todo.dueDate ? (
            <span>Hạn: {formatDateTime(todo.dueDate, "short")}</span>
          ) : (
            <span>Không có hạn chót</span>
          )}
        </div>

        <span className="text-xs text-gray-400 tabular-nums">
          {formatDateTime(todo.createdOn, "date-only")}
        </span>
      </div>
    </div>
  );
}
