<script setup lang="ts">
import { Card } from '@/components/ui/card'
import type { Assignee } from "@/types";
import { onMounted, type Ref, ref } from "vue";
import config from "@/config";
import { Button } from "@/components/ui/button";
import { Alert, AlertDescription, AlertTitle } from '@/components/ui/alert'
import { OctagonAlert } from 'lucide-vue-next';
import { Trash2 } from 'lucide-vue-next';
import { useToast } from '@/components/ui/toast/use-toast'
import NewAssignee from "@/components/assignee/NewAssignee.vue"
import EventBus from "@/EventBus";
import EditAssignee from './EditAssignee.vue';
import ViewAssignee from './ViewAssignee.vue';


const assignees: Ref<Assignee[]> = ref([]);
const { toast } = useToast()


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



function deleteAssignee(id: number) {
    fetch(`${config.apiBaseUrl}/assignees/${id}`, { method: "DELETE" })
        .then(() => {
            assignees.value = assignees.value.filter((assignee) => assignee.id !== id);
            toast({
                description: 'requested assignee has been deleted.',
            });
        }).catch(
            error => toast({
                title: 'Uh oh! Something went wrong.',
                description: 'We can not process your request.',
                variant: 'destructive',
            })
        );
}


onMounted(() => fetchAllAssignees());
EventBus.on("assigneeCreated", fetchAllAssignees)
EventBus.on("assigneeUpdated", fetchAllAssignees)
</script>

<template>
    <div class="flex justify-between items-center my-4">
        <h2 className="text-2xl font-bold">Assignees</h2>
        <NewAssignee></NewAssignee>
    </div>

    <div>
        <!-- Render a message if no assignees are available -->
        <div v-if="assignees.length === 0">
            <Alert class="p-4">
                <OctagonAlert class="h-6 w-6 mt-2" />
                <AlertTitle class="mx-2">Heads up!</AlertTitle>
                <AlertDescription class="mx-2">
                    No assignees available on Server..
                </AlertDescription>
            </Alert>
        </div>
        <!-- Render the list of assignees -->
        <div v-else>
            <Card v-for="assignee in assignees" :key="assignee.id" class="mt-3 p-4 relative">
                <h3 class="font-medium text-lg">{{ assignee.prename }} {{ assignee.name }}</h3>
                <p class="text-gray-600">{{ assignee.email }}</p>

                <div class="flex gap-2 absolute right-0 top-0 p-4">
                    <ViewAssignee :assigneeId="assignee.id" />
                    <EditAssignee :assigneeId="assignee.id" :assigneeName="assignee.name"
                        :assigneePrename="assignee.prename" :assigneeEmail="assignee.email" />
                    <Button @click="deleteAssignee(assignee.id)" variant="destructive" size="icon">
                        <Trash2 class="w-4 h-4" />
                    </Button>
                </div>
            </Card>
        </div>
    </div>

</template>