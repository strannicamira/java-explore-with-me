package ru.practicum.compilation;

import lombok.*;

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
    @NotNull
    private Boolean pinned;

    //Заголовок подборки*
    @NotBlank
    private String title;
}
