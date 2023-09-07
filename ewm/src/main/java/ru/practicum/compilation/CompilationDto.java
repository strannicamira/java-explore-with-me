package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.event.Event;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CompilationDto {

    private List<Event> events;

    @NotNull
    private Integer id;

    //Закреплена ли подборка на главной странице сайта*
    @NotNull
    private Boolean pinned;

    //Заголовок подборки*
    @NotBlank
    private String title;
}
