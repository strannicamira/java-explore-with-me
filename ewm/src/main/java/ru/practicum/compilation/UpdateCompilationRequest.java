package ru.practicum.compilation;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.event.Event;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCompilationRequest {

    //Список id событий подборки для полной замены текущего списка
    private List<Integer> events;

    //Закреплена ли подборка на главной странице сайта
    @NotNull
    private Boolean pinned;

    //Заголовок подборки
    @NotBlank
    @Length(max = 50, min = 1)
    private String title;
}
