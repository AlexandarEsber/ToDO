<script setup lang="ts">
/**
 * Component that provides a dialog for creating new assignees.
 * Features:
 * - Form validation for assignee data
 * - Input fields for prename, name, and email
 * - Email validation for uni-stuttgart.de domain
 * - Server communication for creating new assignees
 * - Toast notifications for success/error feedback
 */

import { Button } from '@/components/ui/button'
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
import { UserRoundPlus } from 'lucide-vue-next';
import { toTypedSchema } from '@vee-validate/zod';
import * as z from 'zod';
import { useForm, configure } from 'vee-validate';
import axios from "axios";
import { useToast } from '@/components/ui/toast/use-toast';
import config from "@/config";
import EventBus from "@/EventBus";
import { ref } from 'vue'

const dialogOpen = ref(false);

configure({
  validateOnBlur: false,
  validateOnInput: false,
})

import {
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage
} from '@/components/ui/form';

const { toast } = useToast();

const formSchema = toTypedSchema(z.object({
  prename: z.string().refine(v => v, { message: 'Prename is required.' }),
  name: z.string().refine(v => v, { message: 'Name is required.' }),
  email: z.string().refine((value) => /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9._%+-]*uni-stuttgart\.de$/.test(value ?? ""), 'Email is not valid'),
}))

const form = useForm({
  validationSchema: formSchema,
})

const onSubmit = form.handleSubmit((values) => {
  const assignee = {
    prename: values.prename,
    name: values.name,
    email: values.email
  }
  fetch(`${config.apiBaseUrl}/assignees`, {
    method: "POST",
    headers: {
      'Accept': 'application/json',
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(assignee)
  }).then(() => {
      EventBus.emit("assigneeCreated", {});
      dialogOpen.value = false;
      toast({
        description: 'New assignee has been created.',
      });
    }).catch(
      error => toast({
        title: 'Uh oh! Something went wrong.',
        description: 'We can not process your request.',
        variant: 'destructive',
      })
    );
})

</script>

<template>
  <Dialog v-model:open="dialogOpen">
    <DialogTrigger as-child>
      <Button>
        <UserRoundPlus />
        New Assignee
      </Button>
    </DialogTrigger>
    <DialogContent class="sm:max-w-[425px]">
      <form @submit="onSubmit">
        <DialogHeader>
          <DialogTitle>Add new assignee</DialogTitle>
          <DialogDescription>
            Fill in the form to create a new assigne.
          </DialogDescription>
        </DialogHeader>
        
        <div class="grid gap-4 py-4">
          <FormField v-slot="{ componentField }" name="prename">
            <FormItem>
              <FormLabel>Prename</FormLabel>
              <FormControl>
                <Input type="text" v-bind="componentField" />
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="name">
            <FormItem>
              <FormLabel>Name</FormLabel>
              <FormControl>
                <Input type="text" v-bind="componentField" />
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="email">
            <FormItem>
              <FormLabel>E-Mail</FormLabel>
              <FormControl>
                <Input type="text" v-bind="componentField" />
              </FormControl>
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