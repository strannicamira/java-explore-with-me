package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.location.LocationDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {
    //Новая аннотация
//    @NotBlank
    @Length(max = 2000, min = 20)
    private String annotation;

    //Новая категория
//    @NotNull
    private Integer category;

    //Новое описание
//    @NotBlank
    @Length(max = 7000, min = 20)
    private String description;

    // Новые дата и время на которые намечено событие.
    // Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
//    @NotNull
//    @FutureOrPresent
//    @DateTimeFormat(pattern = TIME_PATTERN)
    private String eventDate;

    //Широта и долгота места проведения события
//    @NotNull
    private LocationDto location;

    //Новое значение флага о платности мероприятия
    private Boolean paid = false;

    //Новый лимит пользователей
    private Integer participantLimit;

    //Нужна ли пре-модерация заявок на участие
    private Boolean requestModeration = true;

    State stateAction;

    //Новый заголовок
//    @NotBlank
    @Length(max = 120, min = 3)
    private String title;
}
