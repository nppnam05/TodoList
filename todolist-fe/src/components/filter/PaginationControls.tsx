import { ChevronLeft, ChevronRight } from "lucide-react";

interface PaginationControlsProps {
  pageNumber: number;
  pageSize: number;
  total: number;
  totalPages: number;
  onPageChange: (page: number) => void;
  onPageSizeChange: (size: number) => void;
}

export default function PaginationControls({
  pageNumber,
  pageSize,
  total,
  totalPages,
  onPageChange,
  onPageSizeChange,
}: PaginationControlsProps) {
  if (totalPages <= 0) return null;

  const from = Math.min(pageNumber * pageSize + 1, total);
  const to = Math.min((pageNumber + 1) * pageSize, total);

  const navBtn =
    "h-9 w-9 flex items-center justify-center rounded-xl border border-gray-200 text-gray-500 hover:bg-indigo-50 hover:border-indigo-300 hover:text-indigo-600 transition-all duration-150 disabled:opacity-40 disabled:pointer-events-none cursor-pointer";

  return (
    <div className="flex flex-col sm:flex-row items-center justify-between gap-4 mt-8 bg-white border border-gray-100 rounded-3xl px-6 py-4 shadow-sm">
      <p className="text-sm text-gray-500">
        Hiển thị{" "}
        <span className="font-semibold text-gray-800">
          {from}–{to}
        </span>{" "}
        trong <span className="font-semibold text-gray-800">{total}</span> công
        việc
      </p>

      <div className="flex items-center gap-5">
        <div className="flex items-center gap-2">
          <span className="text-xs text-gray-400 font-medium">Mỗi trang:</span>
          <select
            value={pageSize}
            onChange={(e) => onPageSizeChange(Number(e.target.value))}
            className="px-3 py-1.5 bg-white border border-gray-200 rounded-xl text-xs text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 transition-colors cursor-pointer"
          >
            <option value={5}>5</option>
            <option value={10}>10</option>
            <option value={20}>20</option>
            <option value={50}>50</option>
          </select>
        </div>

        <div className="flex items-center gap-2">
          <button
            className={navBtn}
            onClick={() => onPageChange(pageNumber - 1)}
            disabled={pageNumber === 0}
          >
            <ChevronLeft size={15} strokeWidth={2.5} />
          </button>

          <span className="text-sm font-bold text-gray-700 tabular-nums min-w-[80px] text-center">
            {pageNumber + 1} / {totalPages}
          </span>

          <button
            className={navBtn}
            onClick={() => onPageChange(pageNumber + 1)}
            disabled={pageNumber === totalPages - 1}
          >
            <ChevronRight size={15} strokeWidth={2.5} />
          </button>
        </div>
      </div>
    </div>
  );
}
