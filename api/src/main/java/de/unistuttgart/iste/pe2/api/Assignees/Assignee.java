package de.unistuttgart.iste.pe2.api.Assignees;

import java.util.ArrayList;
import java.util.List;

import de.unistuttgart.iste.pe2.api.ToDos.ToDo;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter @NoArgsConstructor
public class Assignee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    @NotBlank(message = "Prename must not be blank")
    private String prename;

    @NotBlank(message = "Name must not be blank")
    private String name;

    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9._%+-]*uni-stuttgart\\.de$", 
             message = "Email must end with 'uni-stuttgart.de'")
    private String email;

    // @ManyToMany(mappedBy = "assigneeList")
    // private List<ToDo> toDoList = new ArrayList<>();

    public Assignee(String prename, String name, String email){
        this.prename = prename;
        this.name = name;
        this.email = email;
    }
}
