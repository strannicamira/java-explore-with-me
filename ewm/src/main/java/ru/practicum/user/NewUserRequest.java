package ru.practicum.user;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewUserRequest {
    @NotEmpty
    @Length(max = 250, min = 2)
    private String name;
    @NotEmpty
    @Email
    @Length(max = 254, min = 6)
    private String email;
}
