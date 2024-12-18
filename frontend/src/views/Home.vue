<script setup lang="ts">
import {
  Tabs,
  TabsContent,
  TabsList,
  TabsTrigger,
} from '@/components/ui/tabs'

import AssigneesTab from '@/components/assignee/AssigneesTab.vue';
import ToDosTab from '@/components/todo/ToDosTab.vue';
</script>

<template>
<div class="container mx-auto max-w-lg p-4">
  <h1 class="text-3xl font-bold mb-4">Todo and Assignee Management</h1>
  <Tabs default-value="todo">
    <TabsList class="grid w-full grid-cols-2">
      <TabsTrigger value="todo">
        ToDos
      </TabsTrigger>
      <TabsTrigger value="assignee">
        Assignees
      </TabsTrigger>
    </TabsList>
    <TabsContent value="todo">
      <ToDosTab />
    </TabsContent>
    <TabsContent value="assignee">
      <AssigneesTab />
    </TabsContent>
  </Tabs>
</div>
</template>