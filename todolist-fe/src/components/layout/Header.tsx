import { CheckCircle2, ListTodo, Calendar, XCircle } from "lucide-react";
import { useGetStatisticsQuery } from "../../store/api/api-todo";

export default function Header() {
  const { data: stats } = useGetStatisticsQuery();
  const currentDate = new Date().toLocaleDateString("vi-VN", {
    weekday: "long",
    year: "numeric",
    month: "long",
    day: "numeric",
  });

  return (
    <header className="bg-white/90 backdrop-blur-md border-b border-gray-100 sticky top-0 z-30 px-6 md:px-8 py-5">
      <div className="flex flex-col md:flex-row md:items-center md:justify-between gap-4">
        <div>
          <h1 className="text-xl font-extrabold tracking-tight text-gray-900">
            Chào mừng quay trở lại 👋
          </h1>
          <div className="flex items-center gap-1.5 text-xs text-gray-400 font-medium mt-1">
            <Calendar size={12} />
            <span>{currentDate}</span>
          </div>
        </div>
        {stats && (
          <div className="flex items-center gap-2.5 overflow-x-auto pb-0.5 md:pb-0 scrollbar-none">
            <div className="flex items-center gap-2.5 bg-blue-50 border border-blue-100 rounded-2xl px-4 py-2.5 text-blue-700 whitespace-nowrap shrink-0">
              <ListTodo size={15} strokeWidth={2.5} />
              <div>
                <p className="text-[10px] font-bold uppercase tracking-wider text-blue-500 leading-none">
                  Chưa xong
                </p>
                <p className="text-lg font-extrabold leading-tight tabular-nums">
                  {stats.totalTodo}
                </p>
              </div>
            </div>

            <div className="flex items-center gap-2.5 bg-emerald-50 border border-emerald-100 rounded-2xl px-4 py-2.5 text-emerald-700 whitespace-nowrap shrink-0">
              <CheckCircle2 size={15} strokeWidth={2.5} />
              <div>
                <p className="text-[10px] font-bold uppercase tracking-wider text-emerald-500 leading-none">
                  Hoàn thành
                </p>
                <p className="text-lg font-extrabold leading-tight tabular-nums">
                  {stats.totalDone}
                </p>
              </div>
            </div>

            <div className="flex items-center gap-2.5 bg-gray-50 border border-gray-200 rounded-2xl px-4 py-2.5 text-gray-600 whitespace-nowrap shrink-0">
              <XCircle size={15} strokeWidth={2.5} />
              <div>
                <p className="text-[10px] font-bold uppercase tracking-wider text-gray-400 leading-none">
                  Tổng cộng
                </p>
                <p className="text-lg font-extrabold leading-tight tabular-nums">
                  {stats.total}
                </p>
              </div>
            </div>
          </div>
        )}
      </div>
    </header>
  );
}
