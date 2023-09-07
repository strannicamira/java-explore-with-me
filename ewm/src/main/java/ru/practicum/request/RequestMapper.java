package ru.practicum.request;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.util.Constants.TIME_PATTERN;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RequestMapper {
    public static ParticipationRequestDto mapToParticipationRequestDto(Request request) {
        ParticipationRequestDto dto = new ParticipationRequestDto();
        dto.setId(request.getId());
        dto.setRequesterId(request.getRequester().getId());
        dto.setEventId(request.getEvent().getId());
        dto.setCreated(request.getCreated().format(DateTimeFormatter.ofPattern(TIME_PATTERN)));
        return dto;
    }

    public static List<ParticipationRequestDto> mapToParticipationRequestDto(List<Request> requests) {
        List<ParticipationRequestDto> dtos = new ArrayList<>();
        for (Request request : requests) {
            dtos.add(mapToParticipationRequestDto(request));
        }
        return dtos;
    }
}
