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
    private Integer id;

    @FutureOrPresent
    @DateTimeFormat(pattern = TIME_PATTERN)
    private String created;

    private Integer event;

    private Integer requester;

    private String status;
}
