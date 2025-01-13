<script setup lang="ts">
/**
 * Component that provides functionality to edit existing assignees.
 * Features:
 * - Pre-filled form with current assignee data
 * - Form validation for updated data
 * - Email validation for uni-stuttgart.de domain
 * - Server communication for updating assignee information
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
import { toTypedSchema } from '@vee-validate/zod';
import * as z from 'zod';
import { useForm, configure } from 'vee-validate';
import { useToast } from '@/components/ui/toast/use-toast';
import config from "@/config";
import EventBus from "@/EventBus";
import { ref, watch } from 'vue'
import { Pencil } from 'lucide-vue-next';


const dialogOpen = ref(false);

configure({
  validateOnBlur: false,
  validateOnInput: false,
})

import {
  FormControl,
  FormDescription,
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



const props = defineProps({
  assigneeId: {
    type: Number,
    required: true
  },
  assigneePrename: {
    type: String,
    required: true
  },
  assigneeName: {
    type: String,
    required: true
  },
  assigneeEmail: {
    type: String,
    required: true
  }
})

const form = useForm({
  validationSchema: formSchema,
  initialValues: {
    email: props.assigneeEmail,
    prename: props.assigneePrename,
    name: props.assigneeName
  }
});

const onSubmit = form.handleSubmit(values => {
  const assignee = {
    prename: values.prename,
    name: values.name,
    email: values.email
  }
  fetch(`${config.apiBaseUrl}/assignees/${props.assigneeId}`, {
    method: "PUT",
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(assignee)
  }).then(() => {
      EventBus.emit("assigneeUpdated", {});
      dialogOpen.value = false;
      toast({
        description: 'Changes have been saved.',
      });
    }).catch(
      error => toast({
        title: 'Uh oh! Something went wrong.',
        description: 'We can not process your request.',
        variant: 'destructive',
      })
    );
})

// Watch for changes to dialogOpen and reset form values when opened
watch(dialogOpen, (isOpen) => {
  if (isOpen) {
    // Reset form values when the dialog opens
    form.setFieldValue('email', props.assigneeEmail);
    form.setFieldValue('prename', props.assigneePrename);
    form.setFieldValue('name', props.assigneeName);
  }
});

</script>

<template>
  <Dialog v-model:open="dialogOpen">
    <DialogTrigger as-child>
      <Button class="bg-gray-800 text-white" variant="outline">
        <Pencil />
      </Button>
    </DialogTrigger>

    <DialogContent class="sm:max-w-[425px]">
      <form @submit="onSubmit">
        <DialogHeader>
          <DialogTitle>Edit assignee</DialogTitle>
          <DialogDescription>
            Fill in the form to edit an assigne.
          </DialogDescription>
        </DialogHeader>

        <div class="grid gap-4 py-4">
          <FormField v-slot="{ componentField }" name="prename">
            <FormItem>
              <FormLabel>Prename</FormLabel>
              <FormControl>
                <Input type="text" v-bind="componentField"/>
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="name">
            <FormItem>
              <FormLabel>Name</FormLabel>
              <FormControl>
                <Input type="text" v-bind="componentField"/>
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
          <FormField v-slot="{ componentField }" name="email">
            <FormItem>
              <FormLabel>E-Mail</FormLabel>
              <FormControl>
                <Input type="text" v-bind="componentField"/>
              </FormControl>
              <FormMessage />
            </FormItem>
          </FormField>
        </div>

        <DialogFooter>
          <Button type="submit">
            Save Changes
          </Button>
        </DialogFooter>
      </form>
    </DialogContent>
  </Dialog>
</template>