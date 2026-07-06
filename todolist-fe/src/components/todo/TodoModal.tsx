import Modal from "../common/Modal";
import TodoForm from "./TodoForm";
import type { Todo } from "../../types/todo";

interface TodoModalProps {
  isOpen: boolean;
  onClose: () => void;
  todo?: Todo;
}

export default function TodoModal({ isOpen, onClose, todo }: TodoModalProps) {
  return (
    <Modal
      isOpen={isOpen}
      onClose={onClose}
      title={todo ? "Chỉnh sửa công việc" : "Tạo công việc mới"}
    >
      <TodoForm todo={todo} onSuccess={onClose} />
    </Modal>
  );
}
