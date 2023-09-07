package ru.practicum.compilation;

import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "compilations", schema = "public")
public class Compilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    //Закреплена ли подборка на главной странице сайта
    //default: false
    @NotNull
    private Boolean pinned = false;

    //Заголовок подборки*
    @NotBlank
    @Length(max = 50, min = 1)
    private String title;
}
