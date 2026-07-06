export interface BaseResponse<T = any> {
  succeeded: boolean;
  message: string | null;
  statusCode: string | null;
  data: T | null;
  errors: any;
}

export interface PaginatedResponse<T = any> {
  pageNumber: number;
  pageSize: number;
  total: number;
  totalPages: number;
  data: T[];
}
