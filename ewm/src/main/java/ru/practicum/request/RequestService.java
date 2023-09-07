package ru.practicum.request;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto createRequest(Integer userId, Integer eventId);

    List<ParticipationRequestDto> findRequestById(Integer userId);

    ParticipationRequestDto updateRequest(Integer userId, Integer requestId);
}
