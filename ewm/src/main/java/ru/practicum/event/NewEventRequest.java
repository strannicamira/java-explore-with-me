package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NewEventRequest {
    //Краткое описание события
    @NotBlank
    @Length(max = 2000, min = 20)
    private String annotation;

    //id категории к которой относится событие
    @NotNull
    private Integer category;

    //Полное описание события
    @NotBlank
    @Length(max = 7000, min = 20)
    private String description;

    // Дата и время на которые намечено событие.
    // Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    // Обратите внимание: дата и время на которые намечено событие не может быть раньше,
    // чем через два часа от текущего момента
    @NotNull
//    @FutureOrPresent
//    @DateTimeFormat(pattern = TIME_PATTERN)
    private String eventDate;

    //Широта и долгота места проведения события
    @NotNull
    private LocationDto location;

    //    Нужно ли оплачивать участие в событии
    private Boolean paid = false;

    //Ограничение на количество участников. Значение 0 - означает отсутствие ограничения
    private Integer participantLimit = 0;

    //Нужна ли пре-модерация заявок на участие.
    // Если true, то все заявки будут ожидать подтверждения инициатором события.
    // Если false - то будут подтверждаться автоматически.
    private Boolean requestModeration = true;

    //Заголовок события
    @NotBlank
    @Length(max = 120, min = 3)
    private String title;
}
