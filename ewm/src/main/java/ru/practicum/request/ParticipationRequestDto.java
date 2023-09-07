package ru.practicum.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.FutureOrPresent;

import static ru.practicum.util.Constants.TIME_PATTERN;

@Data
@AllArgsConstructor
@NoArgsConstructor

public class ParticipationRequestDto {
    //Идентификатор заявки
//    @NotNull
    private Integer id;

    //Дата и время создания заявки
    //example: 2022-09-06T21:10:05.432
//    @NotNull
    @FutureOrPresent
    @DateTimeFormat(pattern = TIME_PATTERN)
    private String created;

    //Идентификатор события
//    @NotNull
    private Integer eventId;

    //Идентификатор пользователя, отправившего заявку
//    @NotNull
    private Integer requesterId;

    //Статус заявки
    //example: PENDING
//    @NotNull
    private String status;
}
