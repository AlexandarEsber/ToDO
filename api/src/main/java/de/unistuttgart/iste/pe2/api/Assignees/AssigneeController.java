package de.unistuttgart.iste.pe2.api.Assignees;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import de.unistuttgart.iste.pe2.api.ToDos.ToDo;
import de.unistuttgart.iste.pe2.api.ToDos.ToDoRepository;
import jakarta.validation.Valid;

/**
 * REST Controller for managing Assignee operations.
 * Provides endpoints for CRUD operations on Assignee entities.
 */
@RestController
@ApiVersion1
public class AssigneeController {

    @Autowired
    private AssigneeRepository assigneeRepository;

    @Autowired
    private ToDoRepository toDoRepository;


    /**
     * Retrieves all Assignees.
     *
     * @return List of all Assignee objects
     */
    @GetMapping("/assignees")
    public List<Assignee> getAssignees(){
        return (List<Assignee>) assigneeRepository.findAll();
    }

    /**
     * Retrieves a single Assignee by their ID.
     *
     * @param id ID of the Assignee
     * @return Assignee object
     * @throws ResponseStatusException if Assignee not found
     */
    @GetMapping("/assignees/{id}")
    public Assignee getAssigneeById(@PathVariable("id") Long id) throws ResponseStatusException{
        return assigneeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Assignee with id %s not found", id)));
    }

    /**
     * Creates a new Assignee.
     *
     * @param requestAssignee Assignee object to be created
     * @return Created Assignee object
     */
    @PostMapping("/assignees")
    @ResponseStatus(HttpStatus.CREATED)
    public Assignee createAssignee(@Valid @RequestBody Assignee requestAssignee){
        Assignee newAssignee = new Assignee(requestAssignee.getPrename(), requestAssignee.getName(), requestAssignee.getEmail());
        return assigneeRepository.save(newAssignee);
    }

    /**
     * Updates an existing Assignee.
     *
     * @param id ID of the Assignee to update
     * @param requestBody Updated Assignee object
     * @return Updated Assignee object
     * @throws ResponseStatusException if Assignee not found
     */
    @PutMapping("/assignees/{id}")
    public Assignee updateAssignee(@PathVariable("id") long id, @Valid @RequestBody Assignee requestBody) {
        requestBody.setId(id);
        assigneeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Assignee with id %s not found", id)));
        return assigneeRepository.save(requestBody);
    }

    /**
     * Deletes an Assignee and removes them from all associated ToDos.
     *
     * @param id ID of the Assignee to delete
     * @return Deleted Assignee object
     * @throws ResponseStatusException if Assignee not found
     */
    @DeleteMapping("/assignees/{id}")
    public Assignee deleteAssignee(@PathVariable("id") long id){
        Assignee assigneeToDelete = assigneeRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, String.format("Assignee with id %s not found", id)));

        // Find all ToDos associated with the Assignee
        List<ToDo> associatedToDos = toDoRepository.findByAssigneeListContains(assigneeToDelete);

        // Remove the Assignee from each ToDo's assigneeList
        for (ToDo todo : associatedToDos) {
            todo.getAssigneeList().remove(assigneeToDelete);
            toDoRepository.save(todo); // Persist the updated ToDo
        }

        // Delete the Assignee
        assigneeRepository.deleteById(id);

        return assigneeToDelete;
    }
}
