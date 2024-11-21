package de.unistuttgart.iste.pe2.api.ToDos;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.unistuttgart.iste.pe2.api.Assignees.Assignee;

public interface ToDoRepository extends JpaRepository<ToDo, Long> {
    List<ToDo> findByAssigneeListContains(Assignee assignee);
}
