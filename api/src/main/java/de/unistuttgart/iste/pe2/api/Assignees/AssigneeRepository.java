package de.unistuttgart.iste.pe2.api.Assignees;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for managing Assignee entities.
 * Extends JpaRepository to provide CRUD operations for Assignee objects.
 */
public interface AssigneeRepository extends JpaRepository<Assignee, Long> {
    
}
