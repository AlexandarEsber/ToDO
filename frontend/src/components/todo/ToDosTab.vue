<script setup lang="ts">
import { Card } from '@/components/ui/card'
import type { Assignee, ToDo } from "@/types";
import { onMounted, type Ref, ref } from "vue";
import config from "@/config";
import { Button } from "@/components/ui/button";
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert'
import { OctagonAlert } from 'lucide-vue-next';
import { Trash2 } from 'lucide-vue-next';
import { useToast } from '@/components/ui/toast/use-toast'
import NewToDo from "@/components/todo/NewToDo.vue"
import EventBus from "@/EventBus";
import { Check, LayoutList } from 'lucide-vue-next';
import EditToDo from './EditToDo.vue';
import ViewToDo from './ViewToDo.vue';

const assignees: Ref<Assignee[]> = ref([]);
const todos: Ref<ToDo[]> = ref([]);
const { toast } = useToast()

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
        fetchAllAssignees()
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
    <div class="flex justify-between items-center my-4">
        <h2 className="text-2xl font-bold">ToDos</h2>
        <NewToDo :assignees="assignees"></NewToDo>
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
            <Card v-for="todo in todos.filter(todo => !todo.finished)" :key="todo.id" class="mt-3 p-4 relative min-h-32">
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
                        {{ todo.assigneeList.map(a => a.prename + " " + a.name).join(", ") }}
                    </div>
                </div>


                <div class="flex gap-2 absolute right-0 top-0 p-4">
                    <ViewToDo :todoId="todo.id" />
                    <EditToDo :todoId="todo.id" :todoTitle="todo.title" :todoDescription="todo.description"
                        :todoAssigneeIdList="todo.assigneeList.map(a => a.id)" :todoDueDate="todo.dueDate"  :assignees="assignees" :todo-finished="todo.finished" :todo-finished-date="todo.finishedDate"/>
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
            <div class="flex justify-between items-center my-4">
                <h2 className="text-2xl font-bold">Finished ToDos</h2>
            </div>
            <Card v-for="todo in todos.filter(todo => todo.finished)" :key="todo.id" class="mt-3 p-4 relative">
                <h3 class="font-medium text-lg">{{ todo.title }} </h3>
                <p class="text-gray-300 m-4">{{ todo.description }}</p>
                <div class="text-gray-600 text-xs">
                    <div class="flex">
                        <div>
                            Created on: {{ formatDate(todo.createdDate) }}
                        </div>
                        <div v-if="todo.dueDate">&nbsp;|&nbsp;Due Date: {{ formatDate(todo.dueDate) }}</div>
                        <div v-if="todo.finishedDate">&nbsp;|&nbsp;Finish Date: {{ formatDate(todo.finishedDate) }}</div>
                    </div>
                    <div>
                        {{ todo.assigneeList.map(a => a.prename + " " + a.name).join(", ") }}
                    </div>
                </div>

                <div class="flex gap-2 absolute right-0 top-0 p-4">
                    <ViewToDo :todoId="todo.id" />
                    <EditToDo :todoId="todo.id" :todoTitle="todo.title" :todoDescription="todo.description"
                        :todoAssigneeIdList="todo.assigneeList.map(a => a.id)" :todoDueDate="todo.dueDate"  :assignees="assignees" :todo-finished="todo.finished" :todo-finished-date="todo.finishedDate"/>
                    <Button @click="deleteToDo(todo.id)" variant="destructive" size="icon">
                        <Trash2 class="w-4 h-4" />
                    </Button>
                </div>
            </Card>
        </div>
    </div>
</template>