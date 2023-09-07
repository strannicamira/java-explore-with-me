package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.Event;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewCompilationDto {

    private List<Integer> events;

    //Закреплена ли подборка на главной странице сайта*
    private Boolean pinned;

    //Заголовок подборки*
    @NotBlank
    @Length(max = 50, min = 1)
    private String title;
}
