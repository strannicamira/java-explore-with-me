package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;
import ru.practicum.category.CategoryDto;
import ru.practicum.location.LocationDto;
import ru.practicum.user.UserShortDto;

import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import static ru.practicum.util.Constants.TIME_PATTERN;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EventShortDto {
    //Краткое описание события*
    @NotBlank
    @Length(max = 2000, min = 20)
    private String annotation;

    //id категории к которой относится событие*
    @NotNull
    private CategoryDto category;

    //Количество одобренных заявок на участие в данном событии
    private Integer confirmedRequests;

    // Дата и время на которые намечено событие.*
    // Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    // Обратите внимание: дата и время на которые намечено событие не может быть раньше,
    // чем через два часа от текущего момента
    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = TIME_PATTERN)
    private String eventDate;

    //Идентификатор
    @NotNull
    private Integer id;

    //Пользователь (краткая информация)*
    @NotNull
    private UserShortDto initiator;

    //    Нужно ли оплачивать участие в событии*
    private Boolean paid = false;

    //Заголовок события*
    @NotBlank
    @Length(max = 120, min = 3)
    private String title;

    //Количество просмотрев события
    private Integer views;
}
