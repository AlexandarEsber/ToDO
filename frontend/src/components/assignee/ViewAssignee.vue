<script setup lang="ts">
/**
 * Component that displays detailed information about a specific assignee.
 * Features:
 * - Dialog with read-only view of assignee details
 * - Displays prename, name, and email
 * - Fetches assignee data from server
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
import type { Assignee } from '@/types';
import config from "@/config";
import { useToast } from '@/components/ui/toast/use-toast'
import { onMounted } from 'vue';
import Label from '../ui/label/Label.vue';


const { toast } = useToast();

const assignee: Ref<Assignee | undefined> = ref();
const dialogOpen = ref(false);

const props = defineProps({
  assigneeId: {
    type: Number,
    required: true
  }
})


function fetchAssigneeById() {
    fetch(`${config.apiBaseUrl}/assignees/${props.assigneeId}`)
        .then(response => response.json())
        .then(data => data as Assignee)
        .then(data => {
          assignee.value = data;
        })
        .catch(error => {
            toast({
                title: 'Uh oh! Something went wrong.',
                description: 'We can not reach our servers.',
                variant: 'destructive',
            });
        });
}

onMounted(() => fetchAssigneeById());

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
        <DialogTitle>View assignee</DialogTitle>
        <DialogDescription>
          Here is the requested information of the assigne.
        </DialogDescription>
      </DialogHeader>

      <div class="grid gap-4 py-4">
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="prename" class="text-right">
            Prename
          </Label>
          <Input id="prename" :modelValue="assignee?.prename" class="col-span-3"  disabled />
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="name" class="text-right">
            Name
          </Label>
          <Input id="name" :modelValue="assignee?.name" disabled class="col-span-3" />
        </div>
        <div class="grid grid-cols-4 items-center gap-4">
          <Label for="email" class="text-right">
            Email
          </Label>
          <Input id="email" :modelValue="assignee?.email" disabled class="col-span-3" />
        </div>
      </div>
    </DialogContent>
  </Dialog>
</template>