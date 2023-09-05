package ru.practicum.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users", schema = "public")
@SequenceGenerator(name="user_id_seq", initialValue=0, allocationSize=1)
public class User {
    @Id
//    @GeneratedValue
//    (strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="user_id_seq")
    private Integer id;
    @NotBlank
    @Length(max = 250, min = 2)
    @Column(name = "name", nullable = false)
    private String name;
    @NotBlank
    @Email
    @Length(max = 254, min = 6)
    @Column(name = "email", nullable = false, unique = true)
    private String email;
}
