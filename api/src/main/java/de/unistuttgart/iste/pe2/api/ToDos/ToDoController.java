package de.unistuttgart.iste.pe2.api.ToDos;

import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import de.unistuttgart.iste.pe2.api.ApiVersion1;
import de.unistuttgart.iste.pe2.api.Assignees.Assignee;
import de.unistuttgart.iste.pe2.api.Assignees.AssigneeRepository;
import jakarta.annotation.PostConstruct;
import jakarta.validation.Valid;

@RestController
@ApiVersion1
public class ToDoController {

    @Autowired
    private ToDoRepository toDoRepository;

    @Autowired
    private AssigneeRepository assigneeRepository;

    @PostConstruct
    public void init() {
        long numberOfAssignees = assigneeRepository.count();
        var list1 = new ArrayList<Assignee>();
        var list2 = new ArrayList<Assignee>();
        if(numberOfAssignees == 0) {
            Assignee newAssignee = new Assignee("Max","Alex","st188886@stud.uni-stuttgart.de");
            list1.add(assigneeRepository.save(newAssignee));
            Assignee newAssignee2 = new Assignee("Fabian","Sam","st192938@stud.uni-stuttgart.de");
            list2.add(assigneeRepository.save(newAssignee2));
        }
     
        long numberOfToDos = toDoRepository.count();
        if(numberOfToDos == 0){
            ToDo newToDo = new ToDo("Washing Dishes", "You have to help the Prof washing his Dishes", false, Date.valueOf("2025-11-21"));
            newToDo.setAssigneeList(list1);
            toDoRepository.save(newToDo);
            ToDo newToDo2 = new ToDo("GroupMeeting","Group Meeting for PE2",true, Date.valueOf("2025-11-19"));
            newToDo2.setAssigneeList(list2);
            toDoRepository.save(newToDo2);
        }
    }

    // get all Todos
    @GetMapping("/todos")
    public List<ToDo> getToDos(){
        return (List<ToDo>) toDoRepository.findAll();
    }

    // get a Single Todo by Id
    @GetMapping("/todos/{id}")
    public ToDo getTodoById(@PathVariable("id") Long id) throws ResponseStatusException{
        return toDoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Todo with id %s not found", id)));
    }

    // create an Todo
    @PostMapping("/todos")
    @ResponseStatus(HttpStatus.CREATED)
    public ToDo createTodo(@Valid @RequestBody ToDo toDoRequest){
        
        // Validate dueDate
        if (toDoRequest.getDueDate() != null) {
            Long dueDateMillis = toDoRequest.getDueDate().getTime();
            try {
                new Date(dueDateMillis);
            } catch (Exception e) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid dueDate timestamp");
            }
            toDoRequest.setDueDate(new Date(dueDateMillis));
        }
        

        // Validate assigneeIdList
        List<Long> assigneeIdList = toDoRequest.getAssigneeIdList();
        if (assigneeIdList != null && !assigneeIdList.isEmpty()) {
            Set<Long> uniqueIds = new HashSet<>(assigneeIdList);
            if (uniqueIds.size() != assigneeIdList.size()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Assignee IDs must be unique");
            }

            List<Assignee> assignees = assigneeRepository.findAllById(assigneeIdList);
            if (assignees.size() != assigneeIdList.size()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "One or more assignee IDs do not exist");
            }

            toDoRequest.setAssigneeList(assignees);
        }

        // Save the ToDo entity
        toDoRepository.save(toDoRequest);

        return toDoRequest;
    }


    @PutMapping("/todos/{id}")
    public ToDo updateTodo(@PathVariable("id") long id, @Valid @RequestBody ToDo requestBody) {
        requestBody.setId(id);
        
        if(requestBody.isFinished()){
            requestBody.setFinishedDate(new Date(System.currentTimeMillis()));
        }

        toDoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Todo with id %s not found", id)));
        
         // Validate dueDate
         if (requestBody.getDueDate() != null) {
            Long dueDateMillis = requestBody.getDueDate().getTime();
            try {
                new Date(dueDateMillis);
            } catch (Exception e) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid dueDate timestamp");
            }
            requestBody.setDueDate(new Date(dueDateMillis));
        }
        

        // Validate assigneeIdList
        List<Long> assigneeIdList = requestBody.getAssigneeIdList();
        if (assigneeIdList != null && !assigneeIdList.isEmpty()) {
            Set<Long> uniqueIds = new HashSet<>(assigneeIdList);
            if (uniqueIds.size() != assigneeIdList.size()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Assignee IDs must be unique");
            }

            List<Assignee> assignees = assigneeRepository.findAllById(assigneeIdList);
            if (assignees.size() != assigneeIdList.size()) {
                throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "One or more assignee IDs do not exist");
            }

            requestBody.setAssigneeList(assignees);
        }

        // Save the ToDo entity
        return toDoRepository.save(requestBody);
    }

    @DeleteMapping("/todos/{id}")
    public ResponseEntity<Map<String, String>> deleteTodo(@PathVariable("id") long id){
        toDoRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Todo with id %s not found", id)));
        toDoRepository.deleteById(id);
        Map<String, String> response = new HashMap<>();
        response.put("message", String.format("Todo with id %s has been deleted", id));
        return ResponseEntity.ok(response);
    }
}
