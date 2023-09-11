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
    private Integer id;

    //Дата и время создания заявки
    //example: 2022-09-06T21:10:05.432
    @FutureOrPresent
    @DateTimeFormat(pattern = TIME_PATTERN)
    private String created;

    //Идентификатор события
    private Integer event;

    //Идентификатор пользователя, отправившего заявку
    private Integer requester;

    //Статус заявки
    //example: PENDING
    private String status;
}
