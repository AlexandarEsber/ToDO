package de.unistuttgart.iste.pe2.api.ToDos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.unistuttgart.iste.pe2.api.Assignees.Assignee;

/**
 * Repository interface for managing ToDo entities.
 * Extends JpaRepository to provide CRUD operations for ToDo objects.
 */
public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    /**
     * Finds all ToDo items that are assigned to a specific assignee.
     *
     * @param assignee The assignee to search for
     * @return List of ToDo items assigned to the specified assignee
     */
    List<ToDo> findByAssigneeListContains(Assignee assignee);
}
