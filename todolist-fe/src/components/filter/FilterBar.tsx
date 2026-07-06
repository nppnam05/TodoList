import { Search, ArrowDownUp, ChevronDown } from "lucide-react";
import { SORT_BY, SORT_DIRECTION, STATUS } from "../../constant/constant";

interface FilterBarProps {
  keyword: string;
  status: string;
  priority: string;
  sortBy: string;
  sortDirection: string;
  onFilterChange: (filters: {
    keyword: string;
    status: string;
    priority: string;
    sortBy: string;
    sortDirection: string;
  }) => void;
}

const selectClass =
  "w-full pl-4 pr-10 py-3 bg-white border border-gray-200 rounded-2xl text-sm text-gray-700 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200 cursor-pointer appearance-none";

const labelClass =
  "block text-[10px] font-bold text-gray-400 uppercase tracking-widest mb-2";

export default function FilterBar({
  keyword,
  status,
  priority,
  sortBy,
  sortDirection,
  onFilterChange,
}: FilterBarProps) {
  const update = (
    partial: Partial<{
      keyword: string;
      status: string;
      priority: string;
      sortBy: string;
      sortDirection: string;
    }>,
  ) =>
    onFilterChange({
      keyword,
      status,
      priority,
      sortBy,
      sortDirection,
      ...partial,
    });

  const toggleSort = () =>
    update({ sortDirection: sortDirection === "asc" ? "desc" : "asc" });

  return (
    <div className="bg-white rounded-3xl border border-gray-100 shadow-sm p-6 mb-8">
      <div className="flex flex-col lg:flex-row gap-5 lg:items-end">
        <div className="flex-1">
          <label className={labelClass}>Tìm kiếm công việc</label>
          <div className="relative">
            <Search
              size={16}
              strokeWidth={2.5}
              className="absolute left-4 top-1/2 -translate-y-1/2 text-gray-400 pointer-events-none"
            />
            <input
              type="text"
              value={keyword}
              onChange={(e) => update({ keyword: e.target.value })}
              placeholder="Tìm theo tiêu đề hoặc mô tả..."
              className="w-full pl-11 pr-4 py-3 bg-white border border-gray-200 rounded-2xl text-sm text-gray-900 placeholder-gray-400 focus:outline-none focus:ring-2 focus:ring-indigo-500 focus:border-indigo-500 transition-all duration-200"
            />
          </div>
        </div>

        <div className="flex flex-wrap gap-4 items-end">
          <div className="w-full sm:w-44">
            <label className={labelClass}>Trạng thái</label>
            <div className="relative flex items-center">
              <select
                value={status}
                onChange={(e) => update({ status: e.target.value })}
                className={selectClass}
              >
                <option value="">Tất cả</option>
                <option value={STATUS.DONE}>Hoàn thành</option>
                <option value={STATUS.TODO}>Chưa hoàn thành</option>
              </select>
              <ChevronDown
                size={16}
                className="absolute right-4 text-gray-400 pointer-events-none"
              />
            </div>
          </div>

          <div className="flex gap-2 items-end">
            <div className="w-40">
              <label className={labelClass}>Sắp xếp theo</label>
              <div className="relative flex items-center">
                <select
                  value={sortBy}
                  onChange={(e) => update({ sortBy: e.target.value })}
                  className={selectClass}
                >
                  <option value={SORT_BY.CREATED_ON}>Ngày tạo</option>
                  <option value={SORT_BY.DUE_DATE}>Hạn hoàn thành</option>
                </select>
                <ChevronDown
                  size={16}
                  className="absolute right-4 text-gray-400 pointer-events-none"
                />
              </div>
            </div>

            <button
              type="button"
              onClick={toggleSort}
              title={sortDirection === SORT_DIRECTION.ASC ? "Tăng dần" : "Giảm dần"}
              className="px-3.5 py-3 flex items-center justify-center border border-gray-200 bg-white hover:bg-indigo-50 hover:border-indigo-300 hover:text-indigo-600 text-gray-500 rounded-2xl transition-all duration-200 shrink-0 cursor-pointer"
            >
              <ArrowDownUp
                size={16}
                strokeWidth={2.5}
                className={`transition-transform duration-300 ${sortDirection === SORT_DIRECTION.ASC ? "rotate-180" : "rotate-0"}`}
              />
            </button>
          </div>
        </div>
      </div>
    </div>
  );
}
