<script setup lang="ts">
/**
 * Component that displays detailed information about a specific ToDo item.
 * Features:
 * - Dialog with read-only view of ToDo details
 * - Displays title, description, assignees, and dates
 * - Shows creation date, due date, and finished date if available
 * - Shows category information
 * - Error handling with toast notifications
 */

import { Button } from '@/components/ui/button'
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { ref, type Ref } from 'vue'
import { Eye } from 'lucide-vue-next';
import type { Assignee, ToDo } from '@/types';
import config from "@/config";
import { useToast } from '@/components/ui/toast/use-toast'
import { onMounted } from 'vue';
import Label from '../ui/label/Label.vue';
import type { PropType } from 'vue';
import Textarea from '../ui/textarea/Textarea.vue';

const { toast } = useToast();
const todo: Ref<ToDo | undefined> = ref();
const dialogOpen = ref(false);

const props = defineProps({
  todoId: {
    type: Number,
    required: true
  },
  assignees: {
    type: Array as PropType<Assignee[]>,
  },
});

function fetchToDoById() {
  fetch(`${config.apiBaseUrl}/todos/${props.todoId}`)
    .then(response => response.json())
    .then(data => data as ToDo)
    .then(data => {
      todo.value = data;
    })
    .catch(error => {
      toast({
        title: 'Uh oh! Something went wrong.',
        description: 'We can not reach our servers.',
        variant: 'destructive',
      });
    });
}

function formatDate(timestamp?: string | number | Date) {
    if (!timestamp) return '';
    const date = new Date(timestamp);
    return date.toLocaleString('de-DE', {
        year: 'numeric',
        month: 'numeric',
        day: 'numeric',
    });
}

onMounted(() => fetchToDoById());
</script>

<template>
  <Dialog v-model:open="dialogOpen">
    <DialogTrigger as-child>
      <Button>
        <Eye />
      </Button>
    </DialogTrigger>

    <DialogContent class="sm:max-w-[425px]">
      <DialogHeader>
        <DialogTitle>View todo</DialogTitle>
        <DialogDescription>
          Here is the requested information of the todo.
        </DialogDescription>
      </DialogHeader>
      <div class="grid gap-4 py-4">
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="title" class="text-right">
            Title
          </Label>
          <Input id="prename" :modelValue="todo?.title" class="col-span-3" disabled />
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="description" class="text-right">
            Description
          </Label>
          <Textarea id="description" :modelValue="todo?.description" disabled class="col-span-3" />
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="finished" class="text-right">
            Finished
          </Label>
          <Input id="finished" :modelValue="todo?.finished.toString()" disabled class="col-span-3" />
        </div>
        <div v-if="todo?.assigneeList && todo.assigneeList.length > 0" class="grid grid-cols-4 items-center gap-4">
          <Label for="email" class="text-right">
            Assignees
          </Label>
          <Textarea id="email" :modelValue="todo?.assigneeList.map(a => a.prename + ' ' + a.name).join(', ')" disabled class="col-span-3" />
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="due-date" class="text-right">
            Created Date
          </Label>
          <Input id="due-date" :modelValue="formatDate(todo?.createdDate)" disabled class="col-span-3" />
        </div>
        <div v-if="todo?.dueDate" class="grid grid-cols-4 items-center gap-4">
          <Label for="due-date" class="text-right">
            Due Date
          </Label>
          <Input id="due-date" :modelValue="formatDate(todo?.dueDate)" disabled class="col-span-3" />
        </div>
        <div v-if="todo?.finishedDate" class="grid grid-cols-4 items-center gap-4">
          <Label for="finished-date" class="text-right">
            finished Date
          </Label>
          <Input id="finished-date" :modelValue="formatDate(todo?.finishedDate)" disabled class="col-span-3" />
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="category" class="text-right">
            Category
          </Label>
          <Input id="category" :modelValue="todo?.category" disabled class="col-span-3" />
        </div>
      </div>
    </DialogContent>
  </Dialog>
</template>