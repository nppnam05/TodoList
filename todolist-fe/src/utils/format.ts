export const formatDateTime = (
  dateInput: Date | string | number | undefined | null,
  type: "full" | "date-only" | "time-only" | "short" = "full",
): string => {
  if (!dateInput) return "---";
  const date = new Date(dateInput);
  if (isNaN(date.getTime())) return "Invalid date";
  switch (type) {
    case "date-only":
      // Kết quả: 28/05/2026
      return new Intl.DateTimeFormat("vi-VN", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      }).format(date);

    case "time-only":
      // Kết quả: 14:30:05
      return new Intl.DateTimeFormat("vi-VN", {
        hour: "2-digit",
        minute: "2-digit",
        second: "2-digit",
        hour12: false,
      }).format(date);

    case "short":
      // Kết quả: 28/05/2026 14:30
      const d = new Intl.DateTimeFormat("vi-VN", {
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      }).format(date);
      const t = new Intl.DateTimeFormat("vi-VN", {
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
      }).format(date);
      return `${d} ${t}`;

    case "full":
    default:
      // Kết quả: Thứ Năm, 28/05/2026 lúc 14:30
      const datePart = new Intl.DateTimeFormat("vi-VN", {
        weekday: "long",
        day: "2-digit",
        month: "2-digit",
        year: "numeric",
      }).format(date);

      const timePart = new Intl.DateTimeFormat("vi-VN", {
        hour: "2-digit",
        minute: "2-digit",
        hour12: false,
      }).format(date);

      return `${datePart} lúc ${timePart}`;
  }
};

