package ru.practicum.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import ru.practicum.location.LocationDto;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventAdminRequest {
    //Новая аннотация
    @Length(max = 2000, min = 20)
    private String annotation;

    //Новая категория
    private Integer category;

    //Новое описание
    @Length(max = 7000, min = 20)
    private String description;

    // Новые дата и время на которые намечено событие.
    // Дата и время указываются в формате "yyyy-MM-dd HH:mm:ss"
    private String eventDate;

    //Широта и долгота места проведения события
    private LocationDto location;

    //Новое значение флага о платности мероприятия
    private Boolean paid;

    //Новый лимит пользователей
    private Integer participantLimit;

    //Нужна ли пре-модерация заявок на участие
    private Boolean requestModeration;

    StateAction stateAction;

    //Новый заголовок
    @Length(max = 120, min = 3)
    private String title;
}
