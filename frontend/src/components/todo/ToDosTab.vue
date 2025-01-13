<script setup lang="ts">
/**
 * Component that displays and manages a list of ToDo items.
 * Features include:
 * - Displaying all ToDos in a card layout
 * - Filtering and sorting ToDos
 * - Creating new ToDos
 * - Editing existing ToDos
 * - Marking ToDos as finished
 * - Deleting ToDos
 * - Downloading ToDos as CSV
 * - Separate display of finished and unfinished ToDos
 */

import { Card } from '@/components/ui/card'
import type { Assignee, ToDo } from "@/types";
import { onMounted, type Ref, ref, computed } from "vue";
import config from "@/config";
import { Button } from "@/components/ui/button";
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert'
import { OctagonAlert } from 'lucide-vue-next';
import { Trash2 } from 'lucide-vue-next';
import { useToast } from '@/components/ui/toast/use-toast'
import NewToDo from "@/components/todo/NewToDo.vue"
import EventBus from "@/EventBus";
import { Check } from 'lucide-vue-next';
import EditToDo from './EditToDo.vue';
import ViewToDo from './ViewToDo.vue';
import { Input } from "@/components/ui/input";
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuTrigger,
} from "@/components/ui/dropdown-menu/index"
import { ChevronDown } from 'lucide-vue-next';
import { Download } from 'lucide-vue-next';

const assignees: Ref<Assignee[]> = ref([]);
const todos: Ref<ToDo[]> = ref([]);
const { toast } = useToast()
const searchQuery: Ref<string> = ref('');
const sortBy: Ref<'title' | 'dueDate'> = ref('title');
const sortDirection: Ref<'asc' | 'desc'> = ref('asc');

const filteredTodos = computed(() => {
    let filtered = todos.value.filter(todo =>
        todo.title.toLowerCase().includes(searchQuery.value.toLowerCase())
    );

    filtered.sort((a, b) => {
        if (sortBy.value === 'title') {
            return sortDirection.value === 'asc'
                ? a.title.localeCompare(b.title)
                : b.title.localeCompare(a.title);
        } else {
            const dateA = a.dueDate ? new Date(a.dueDate).getTime() : 0;
            const dateB = b.dueDate ? new Date(b.dueDate).getTime() : 0;
            return sortDirection.value === 'asc'
                ? dateA - dateB
                : dateB - dateA;
        }
    });

    return filtered;
});

function fetchAllToDos() {
    fetch(`${config.apiBaseUrl}/todos`)
        .then(response => response.json())
        .then(data => data as ToDo[])
        .then(data => {
            todos.value = data;
        })
        .catch(error => {
            toast({
                title: 'Uh oh! Something went wrong.',
                description: 'We can not reach our servers.',
                variant: 'destructive',
            });
        });
}


function deleteToDo(id: number) {
    fetch(`${config.apiBaseUrl}/todos/${id}`, { method: "DELETE" })
        .then(() => {
            todos.value = todos.value.filter((todo) => todo.id !== id);
            toast({
                description: 'Requested todo has been deleted.',
            });
        }).catch(
            error => toast({
                title: 'Uh oh! Something went wrong.',
                description: 'We can not process your request.',
                variant: 'destructive',
            })
        );
}
function finishToDo(todo: ToDo) {
    todo.finished = true;
    fetch(`${config.apiBaseUrl}/todos/${todo.id}`, {
        method: "PUT",
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(todo),
    })
        .then(response => response.json())
        .catch(error => {
            toast({
                title: 'Uh oh! Something went wrong.',
                description: 'We cannot process your request.',
                variant: 'destructive',
            });
        });

}

function formatDate(timestamp: string | number | Date) {
    if (!timestamp) return 'no data';
    const date = new Date(timestamp);
    return date.toLocaleString('de-DE', {
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
    });
}

function fetchAllAssignees() {
    fetch(`${config.apiBaseUrl}/assignees`)
        .then(response => response.json())
        .then(data => data as Assignee[])
        .then(data => {
            assignees.value = data;
        })
        .catch(error => {
            toast({
                title: 'Uh oh! Something went wrong.',
                description: 'We can not reach our servers.',
                variant: 'destructive',
            });
        });
}

function downloadTodosCSV() {
    fetch(`${config.apiBaseUrl}/csv-downloads/todos`)
        .then(response => response.text())
        .then(csv => {
            const blob = new Blob([csv], { type: 'text/csv' });
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.href = url;
            a.download = 'todos.csv';
            document.body.appendChild(a);
            a.click();
            window.URL.revokeObjectURL(url);
            document.body.removeChild(a);
        })
        .catch(error => {
            toast({
                title: 'Uh oh! Something went wrong.',
                description: 'Could not download CSV file.',
                variant: 'destructive',
            });
        });
}

onMounted(() => {
    fetchAllToDos()
    fetchAllAssignees()
});

EventBus.on("toDoCreated", fetchAllToDos)
EventBus.on("toDoUpdated", fetchAllToDos)
EventBus.on("assigneeCreated", fetchAllAssignees)
EventBus.on("assigneeUpdated", fetchAllAssignees)

</script>

<template>
    <div class="flex justify-between items-center my-4 gap-2">
        <Input v-model="searchQuery" placeholder="Search for ToDo..." class="w-64" />
        <NewToDo :assignees="assignees"></NewToDo>
    </div>
    <div class="flex justify-between items-center mt-4">
        <h2 className="text-2xl font-bold">ToDos</h2>
        <div class="flex gap-2">
            <DropdownMenu>
                <DropdownMenuTrigger as-child>
                    <Button variant="outline" class="gap-2">
                        Sort by {{ sortBy === 'title' ? 'Title' : 'Due Date' }}
                        ({{ sortDirection === 'asc' ? '↑' : '↓' }})
                        <ChevronDown class="h-4 w-4" />
                    </Button>
                </DropdownMenuTrigger>
                <DropdownMenuContent>
                    <DropdownMenuItem @click="() => { sortBy = 'title'; sortDirection = 'asc'; }">
                        Title (A → Z)
                    </DropdownMenuItem>
                    <DropdownMenuItem @click="() => { sortBy = 'title'; sortDirection = 'desc'; }">
                        Title (Z → A)
                    </DropdownMenuItem>
                    <DropdownMenuItem @click="() => { sortBy = 'dueDate'; sortDirection = 'asc'; }">
                        Due Date (Oldest first)
                    </DropdownMenuItem>
                    <DropdownMenuItem @click="() => { sortBy = 'dueDate'; sortDirection = 'desc'; }">
                        Due Date (Newest first)
                    </DropdownMenuItem>
                </DropdownMenuContent>
            </DropdownMenu>
            <Button variant="outline" @click="downloadTodosCSV">
                <Download class="h-4 w-4" />
            </Button>
        </div>
    </div>

    <div>
        <!-- Render a message if no todos are available -->
        <div v-if="todos.length === 0">
            <Alert class="p-4">
                <OctagonAlert class="h-6 w-6 mt-2" />
                <AlertTitle class="mx-2">Heads up!</AlertTitle>
                <AlertDescription class="mx-2">
                    No todos are available on Server..
                </AlertDescription>
            </Alert>
        </div>
        <!-- Render the list of todos -->
        <div v-else>
            <Card v-for="todo in filteredTodos.filter((todo: ToDo) => !todo.finished)" :key="todo.id"
                class="mt-3 p-4 relative min-h-32">
                <h3 class="font-medium text-lg">{{ todo.title }} </h3>
                <p class="text-gray-300 m-4">{{ todo.description }}</p>
                <div class="text-gray-600 text-xs">
                    <div class="flex">
                        <div>
                            Created on: {{ formatDate(todo.createdDate) }}
                        </div>
                        <div v-if="todo.dueDate">&nbsp;|&nbsp;Due Date: {{ formatDate(todo.dueDate) }}</div>
                    </div>
                    <div>
                        {{ todo.assigneeList.map((a: Assignee) => a.prename + " " + a.name).join(", ") }}
                    </div>
                    <div v-if="todo.category" class="mt-1">
                        Category: {{ todo.category }}
                    </div>
                </div>
                


                <div class="flex gap-2 absolute right-0 top-0 p-4">
                    <ViewToDo :todoId="todo.id" />
                    <EditToDo :todoId="todo.id" :todoTitle="todo.title" :todoDescription="todo.description"
                        :todoAssigneeIdList="todo.assigneeList.map(a => a.id)" :todoDueDate="todo.dueDate"
                        :assignees="assignees" :todo-finished="todo.finished" :todo-finished-date="todo.finishedDate" />
                    <Button @click="deleteToDo(todo.id)" variant="destructive" size="icon">
                        <Trash2 class="w-4 h-4" />
                    </Button>
                </div>
                <div class="flex gap-2 absolute right-0 bottom-0 p-4 ">
                    <Button @click="finishToDo(todo)" variant="secondary" size="icon" class="w-20">
                        <Check class="w-4 h-4" />
                        Check
                    </Button>
                </div>
            </Card>
            <!-- Finished ToDos from here on !!! -->
            <div class="flex justify-between items-center mt-4">
                <h3 className="text-xl font-bold">Finished ToDos</h3>
            </div>
            <Card v-for="todo in filteredTodos.filter((todo: ToDo) => todo.finished)" :key="todo.id"
                class="mt-3 p-4 relative">
                <h3 class="font-medium text-lg">{{ todo.title }} </h3>
                <p class="text-gray-300 m-4">{{ todo.description }}</p>
                <div class="text-gray-600 text-xs">
                    <div class="flex">
                        <div>
                            Created on: {{ formatDate(todo.createdDate) }}
                        </div>
                        <div v-if="todo.dueDate">&nbsp;|&nbsp;Due Date: {{ formatDate(todo.dueDate) }}</div>
                        <div v-if="todo.finishedDate">&nbsp;|&nbsp;Finish Date: {{ formatDate(todo.finishedDate) }}
                        </div>
                    </div>
                    <div>
                        {{ todo.assigneeList.map((a: Assignee) => a.prename + " " + a.name).join(", ") }}
                    </div>
                    <div v-if="todo.category" class="mt-1">
                        Category: {{ todo.category }}
                    </div>
                </div>

                <div class="flex gap-2 absolute right-0 top-0 p-4">
                    <ViewToDo :todoId="todo.id" />
                    <EditToDo :todoId="todo.id" :todoTitle="todo.title" :todoDescription="todo.description"
                        :todoAssigneeIdList="todo.assigneeList.map(a => a.id)" :todoDueDate="todo.dueDate"
                        :assignees="assignees" :todo-finished="todo.finished" :todo-finished-date="todo.finishedDate" />
                    <Button @click="deleteToDo(todo.id)" variant="destructive" size="icon">
                        <Trash2 class="w-4 h-4" />
                    </Button>
                </div>
            </Card>
        </div>
    </div>
</template>