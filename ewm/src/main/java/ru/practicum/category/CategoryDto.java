package ru.practicum.category;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private Integer id;
    @NotEmpty
    @Length(max = 250, min = 2)
    @Column(name = "name", nullable = false)
    private String name;
}
