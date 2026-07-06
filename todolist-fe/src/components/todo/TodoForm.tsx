import React, { useState, useEffect } from "react";
import Button from "../common/Button";
import type { Todo } from "../../types/todo";
import { useCreateMutation, useUpdateMutation } from "../../store/api/api-todo";
import { STATUS } from "../../constant/constant";

interface TodoFormProps {
  todo?: Todo;
  onSuccess: () => void;
}

const fieldLabel =
  "block text-[10px] font-bold text-gray-400 uppercase tracking-widest mb-2";
const fieldInput =
  "w-full px-4 py-3 bg-white border border-gray-200 rounded-2xl text-sm text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200";

export default function TodoForm({ todo, onSuccess }: TodoFormProps) {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [status, setStatus] = useState(STATUS.TODO);
  const [dueDate, setDueDate] = useState("");
  const [error, setError] = useState("");

  const [createTodo, { isLoading: isCreating }] = useCreateMutation();
  const [updateTodo, { isLoading: isUpdating }] = useUpdateMutation();

  const formatDatetimeForInput = (isoString?: string) => {
    if (!isoString) return "";
    const date = new Date(isoString);
    if (isNaN(date.getTime())) return "";
    const pad = (n: number) => String(n).padStart(2, "0");
    return `${date.getFullYear()}-${pad(date.getMonth() + 1)}-${pad(date.getDate())}T${pad(date.getHours())}:${pad(date.getMinutes())}`;
  };

  useEffect(() => {
    if (todo) {
      setTitle(todo.title || "");
      setDescription(todo.description || "");
      setStatus(todo.status || STATUS.TODO);
      setDueDate(formatDatetimeForInput(todo.dueDate));
    } else {
      setTitle("");
      setDescription("");
      setStatus("TODO");
      setDueDate("");
    }
    setError("");
  }, [todo]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    if (!title.trim()) {
      setError("Tiêu đề không được để trống.");
      return;
    }
    const payload = {
      title,
      description,
      status,
      dueDate: dueDate ? new Date(dueDate).toISOString() : undefined,
    };
    try {
      if (todo) {
        await updateTodo({ id: todo.id, body: payload }).unwrap();
      } else {
        await createTodo(payload).unwrap();
      }
      onSuccess();
    } catch (err: any) {
      console.error(err);
      setError(err?.data?.message || "Đã xảy ra lỗi. Vui lòng thử lại.");
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-5">
      {error && (
        <div className="px-4 py-3 bg-red-50 border border-red-200 rounded-2xl text-sm text-red-700 font-medium">
          {error}
        </div>
      )}

      <div>
        <label className={fieldLabel}>Tiêu đề *</label>
        <input
          type="text"
          value={title}
          onChange={(e) => setTitle(e.target.value)}
          placeholder="Nhập tiêu đề công việc..."
          className={fieldInput}
        />
      </div>

      <div>
        <label className={fieldLabel}>Mô tả chi tiết</label>
        <textarea
          rows={3}
          value={description}
          onChange={(e) => setDescription(e.target.value)}
          placeholder="Ghi chú thêm về công việc..."
          className={`${fieldInput} resize-none leading-relaxed`}
        />
      </div>

      <div className="grid grid-cols-2 gap-4">
        <div>
          <label className={fieldLabel}>Trạng thái</label>
          <select
            value={status}
            onChange={(e) => setStatus(e.target.value)}
            className={`${fieldInput} cursor-pointer`}
          >
            <option value="TODO">Chưa hoàn thành</option>{" "}
            <option value="DONE">Hoàn thành</option>
          </select>
        </div>
      </div>

      <div>
        <label className={fieldLabel}>Hạn chót hoàn thành</label>
        <input
          type="datetime-local"
          value={dueDate}
          onChange={(e) => setDueDate(e.target.value)}
          className={`${fieldInput} cursor-pointer`}
        />
      </div>

      <div className="flex justify-end pt-2 border-t border-gray-100">
        <Button
          type="submit"
          isLoading={isCreating || isUpdating}
          className="px-7 font-semibold"
        >
          {todo ? "Lưu thay đổi" : "✨ Tạo công việc"}
        </Button>
      </div>
    </form>
  );
}
