export interface Todo {
  id: number;
  title: string;
  description: string;
  priority: string;
  status: string;
  dueDate: string;
  createdOn: string;
  modifiedOn: string;
}

export interface TodoStatistics {
  total: number;
  totalTodo: number;
  totalDone: number;
}