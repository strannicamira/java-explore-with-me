package ru.practicum.request;

import java.util.List;

public interface RequestService {
    ParticipationRequestDto createRequest(Integer userId, Integer eventId);

    List<ParticipationRequestDto> findRequestById(Integer userId);

    ParticipationRequestDto updateRequest(Integer userId, Integer requestId);

    List<ParticipationRequestDto> findEventRequests(Integer userId, Integer eventId);

    EventRequestStatusUpdateResult updateEventRequest(EventRequestStatusUpdateRequest request, Integer userId, Integer eventId);
}
