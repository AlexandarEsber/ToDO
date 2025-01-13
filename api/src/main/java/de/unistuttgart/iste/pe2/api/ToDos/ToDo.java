package de.unistuttgart.iste.pe2.api.ToDos;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import de.unistuttgart.iste.pe2.api.Assignees.Assignee;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Transient;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing a ToDo item.
 * Contains all information about a single ToDo including its status, assignments, and dates.
 */
@Entity
@Getter @Setter @NoArgsConstructor
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    
    /**
     * Title of the ToDo item. Cannot be blank.
     */
    @NotBlank(message = "Title must not be blank")
    private String title;

    /**
     * Detailed description of the ToDo item.
     */
    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private boolean finished = false;

    @ManyToMany
    @JoinTable(
            name = "todoAssignee",
            joinColumns = @JoinColumn(name = "todoId"),
            inverseJoinColumns = @JoinColumn(name = "assigneeId")
    )
    private List<Assignee> assigneeList = new ArrayList<>();

    @Transient
    private List<Long> assigneeIdList;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Column(nullable = false, updatable = false)
    private final Date createdDate = new Date(System.currentTimeMillis());

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Column(nullable = true)
    private Date dueDate;

    @JsonFormat(shape = JsonFormat.Shape.NUMBER)
    @Column(nullable = true)
    private Date finishedDate;

    @Column(nullable = true)
    private String category;

    /**
     * Creates a new ToDo with the specified properties.
     *
     * @param title Title of the ToDo
     * @param description Description of the ToDo
     * @param finished Completion status
     * @param dueDate Due date for the ToDo
     * @param category Category of the ToDo
     */
    public ToDo(String title, String description, boolean finished, Date dueDate, String category){
        this.title = title;
        this.description = description;
        this.finished = finished;
        this.dueDate = dueDate;
        this.category = category;
    }

    /**
     * Returns a formatted string of assignee names.
     *
     * @return String containing all assignee names joined with "+"
     */
    @JsonIgnore
    public String getFormatedAssignees(){
        return assigneeList.stream()
                .map(assignee -> assignee.getPrename() + " " + assignee.getName())
                .collect(Collectors.joining("+"));
    }
}
