<script setup lang="ts">
import { Button } from '@/components/ui/button'
import { CalendarDate, DateFormatter, getLocalTimeZone, parseDate, today } from '@internationalized/date'
import { Popover, PopoverContent, PopoverTrigger } from '@/components/ui/popover'
import { Calendar } from '@/components/ui/calendar'
import { CalendarIcon } from '@radix-icons/vue'
import type { Assignee, ToDo } from "@/types";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogFooter,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from '@/components/ui/dialog'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import { LayoutList } from 'lucide-vue-next';
import { toTypedSchema } from '@vee-validate/zod';
import * as z from 'zod';
import { useForm, configure } from 'vee-validate';
import axios from "axios";
import { useToast } from '@/components/ui/toast/use-toast';
import config from "@/config";
import EventBus from "@/EventBus";
import { computed, ref } from 'vue'
import assignees from '@/components/assignee/AssigneesTab.vue'
import {
  FormControl,
  FormDescription,
  FormField,
  FormItem,
  FormLabel,
  FormMessage
} from '@/components/ui/form';
import Textarea from '../ui/textarea/Textarea.vue';
import Checkbox from '../ui/checkbox/Checkbox.vue';
import type { PropType } from 'vue';
import { cn } from '@/lib/utils';
import { toDate } from 'radix-vue/date'

const df = new DateFormatter('de-DE', {
  dateStyle: 'long',
})

const dialogOpen = ref(false);

configure({
  validateOnBlur: false,
  validateOnInput: false,
})

const placeholder = ref()

const { toast } = useToast();

const props = defineProps({
  assignees: {
    type: Array as PropType<Assignee[]>,
  }
});

const formSchema = toTypedSchema(z.object({
  title: z.string().refine(v => v, { message: 'Title is required.' }),
  description: z.string().refine(v => v, { message: 'Description is required.' }),
  assigneeIdList: z.array(z.number()).optional(),
  date: z.date().optional(),
  dob: z
    .string()
    .optional()
}))

const { handleSubmit, setFieldValue, values } = useForm({
  validationSchema: formSchema,
  initialValues: {
    assigneeIdList: [],
  },
})

const onSubmit = handleSubmit((values) => {
  var todo = {
    title: values.title,
    description: values.description,
    assigneeIdList: values.assigneeIdList,
    dueDate: values.dob ? new Date(values.dob).getTime() : undefined,
  }
  
  fetch(`${config.apiBaseUrl}/todos`, {
    method: "POST",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(todo)
  }).then(() => {
    EventBus.emit("toDoCreated", {});
    dialogOpen.value = false;
    toast({
      description: 'New todo has been created.',
    });
  }).catch(
    error => toast({
      title: 'Uh oh! Something went wrong.',
      description: 'We can not process your request.',
      variant: 'destructive',
    })
  );
})

const value = computed({
  get: () => values.dob ? parseDate(values.dob) : undefined,
  set: val => val,
})

</script>

<template>
  <Dialog v-model:open="dialogOpen">
    <DialogTrigger as-child>
      <Button>
        <LayoutList />
        New ToDo
      </Button>
    </DialogTrigger>
    <DialogContent class="sm:max-w-[425px]">
      <form @submit="onSubmit">
        <DialogHeader>
          <DialogTitle>Add new ToDo</DialogTitle>
          <DialogDescription>
            Fill in the form to create a new todo.
          </DialogDescription>
        </DialogHeader>

        <div class="grid gap-4 py-4">
          <FormField v-slot="{ componentField }" name="title">
            <FormItem>
              <FormLabel>Title</FormLabel>
              <FormControl>
                <Input type="text" v-bind="componentField" />
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="description">
            <FormItem>
              <FormLabel>Description</FormLabel>
              <FormControl>
                <Textarea v-bind="componentField" />
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>


          <FormField name="assigneeIdList">
            <FormItem>
              <div class="mb-4">
                <FormLabel class="text-base">
                  Assignees
                </FormLabel>
              </div>

              <FormField v-for="item in props.assignees" v-slot="{ value, handleChange }" :key="item.id" type="checkbox"
                :value="item.id" :unchecked-value="false" name="assigneeIdList">
                <FormItem class="flex flex-row items-start space-x-3 space-y-0">
                  <FormControl>
                    <Checkbox :checked="value.includes(item.id)" @update:checked="handleChange" />
                  </FormControl>
                  <FormLabel class="font-normal">
                    {{ item.prename + " " + item.name }}
                  </FormLabel>
                </FormItem>
              </FormField>
              <FormMessage />
            </FormItem>
          </FormField>



          <FormField name="dob">
            <FormItem class="flex flex-col mt-2">
              <FormLabel>Due Date</FormLabel>
              <Popover>
                <PopoverTrigger as-child>
                  <FormControl>
                    <Button variant="outline" :class="cn(
                      'ps-3 text-start font-normal',
                      !value && 'text-muted-foreground',
                    )">
                      <span>{{ value ? df.format(toDate(value)) : "Pick a date" }}</span>
                      <CalendarIcon class="ms-auto h-4 w-4 opacity-50" />
                    </Button>
                    <input hidden>
                  </FormControl>
                </PopoverTrigger>
                <PopoverContent class="w-auto p-0 z-50">
                  <Calendar v-model:placeholder="placeholder" v-model="value" calendar-label="Date of birth"
                    initial-focus :min-value="today(getLocalTimeZone())"
                    @update:model-value="(v) => {
                      if (v) {
                        setFieldValue('dob', v.toString())
                      }
                      else {
                        setFieldValue('dob', undefined)
                      }
                    }" />
                </PopoverContent>
              </Popover>
              <FormMessage />
            </FormItem>
          </FormField>



        </div>
        <DialogFooter>
          <Button type="submit">
            Submit
          </Button>
        </DialogFooter>
      </form>
    </DialogContent>
  </Dialog>
</template>