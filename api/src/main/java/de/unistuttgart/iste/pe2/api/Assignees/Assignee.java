package de.unistuttgart.iste.pe2.api.Assignees;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity class representing an Assignee.
 * Contains personal information about a person who can be assigned to ToDo items.
 */
@Entity
@Getter @Setter @NoArgsConstructor
public class Assignee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;

    /**
     * First name of the assignee. Must not be blank.
     */
    @NotBlank(message = "Prename must not be blank")
    private String prename;

    /**
     * Last name of the assignee. Must not be blank.
     */
    @NotBlank(message = "Name must not be blank")
    private String name;

    /**
     * University email address of the assignee.
     * Must be a valid email address ending with 'uni-stuttgart.de'.
     */
    @Email(message = "Email should be valid")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9._%+-]*uni-stuttgart\\.de$", 
             message = "Email must end with 'uni-stuttgart.de'")
    private String email;

    /**
     * Creates a new Assignee with the specified properties.
     *
     * @param prename First name of the assignee
     * @param name Last name of the assignee
     * @param email University email address of the assignee
     */
    public Assignee(String prename, String name, String email){
        this.prename = prename;
        this.name = name;
        this.email = email;
    }
}
