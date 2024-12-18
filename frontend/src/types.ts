export interface Assignee {
    id: number;
    prename: string;
    name: string;
    email: string;
}

export interface ToDo {
    id: number;
    title: string;
    description: string;
    finished: boolean;
    assigneeList: Assignee[];
    createdDate: number;
    dueDate: number;
    finishedDate: number;
}