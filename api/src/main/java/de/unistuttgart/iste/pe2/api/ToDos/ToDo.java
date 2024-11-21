package de.unistuttgart.iste.pe2.api.ToDos;


import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

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

@Entity
@Getter @Setter @NoArgsConstructor
public class ToDo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    
    @NotBlank(message = "Title must not be blank")
    private String title;

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

    public ToDo(String title, String description, boolean finished, Date dueDate){
        this.title = title;
        this.description = description;
        this.finished = finished;
        this.dueDate = dueDate;
    }
}
